package com.temenos.interaction.core.rim;

/*
 * #%L
 * interaction-core
 * %%
 * Copyright (C) 2012 - 2013 Temenos Holdings N.V.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.core.UriInfo;

import org.apache.wink.common.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.temenos.interaction.core.ExtendedMediaTypes;
import com.temenos.interaction.core.MultivaluedMapImpl;
import com.temenos.interaction.core.RESTResponse;
import com.temenos.interaction.core.command.CommandFailureException;
import com.temenos.interaction.core.command.HttpStatusTypes;
import com.temenos.interaction.core.command.InteractionCommand;
import com.temenos.interaction.core.command.InteractionCommand.Result;
import com.temenos.interaction.core.command.InteractionContext;
import com.temenos.interaction.core.command.InteractionException;
import com.temenos.interaction.core.command.NewCommandController;
import com.temenos.interaction.core.entity.Entity;
import com.temenos.interaction.core.entity.EntityProperties;
import com.temenos.interaction.core.entity.EntityProperty;
import com.temenos.interaction.core.entity.Metadata;
import com.temenos.interaction.core.hypermedia.Action;
import com.temenos.interaction.core.hypermedia.Event;
import com.temenos.interaction.core.hypermedia.Link;
import com.temenos.interaction.core.hypermedia.LinkHeader;
import com.temenos.interaction.core.hypermedia.ResourceState;
import com.temenos.interaction.core.hypermedia.ResourceStateMachine;
import com.temenos.interaction.core.hypermedia.Transition;
import com.temenos.interaction.core.hypermedia.validation.HypermediaValidator;
import com.temenos.interaction.core.hypermedia.validation.LogicalConfigurationListener;
import com.temenos.interaction.core.resource.EntityResource;
import com.temenos.interaction.core.resource.RESTResource;

/**
 * <P>
 * Implement HTTP interactions for resources using an hypermedia driven command 
 * controller. This model for resource interaction can be used for individual (item) 
 * or collection resources who conform to the HTTP generic uniform interface and the
 * Hypermedia As The Engine Of Application State (HATEOAS) constraints.
 * HTTP provides one operation to view the resource (GET), one operation to create
 * a new resource (POST) and two operations to change an individual resources
 * state (PUT and DELETE).  
 * </P>
 * @author aphethean
 *
 */
public class HTTPHypermediaRIM implements HTTPResourceInteractionModel {
	private final static Logger logger = LoggerFactory.getLogger(HTTPHypermediaRIM.class);

	private final HTTPHypermediaRIM parent;
	private final NewCommandController commandController;
	private final ResourceStateMachine hypermediaEngine;
	private final Metadata metadata;
	private final String resourcePath;
		
	/**
	 * <p>Create a new resource for HTTP interaction.</p>
	 * @param commandController
	 * 			All commands for all resources.
	 * @param hypermediaEngine
	 * 			All application states, responsible for creating links from one state to another.
	 * @param currentState	
	 * 			The current application state when accessing this resource.
	 */
	public HTTPHypermediaRIM(
			NewCommandController commandController, 
			ResourceStateMachine hypermediaEngine,
			Metadata metadata) {
		this(null, commandController, hypermediaEngine, metadata, hypermediaEngine.getInitial().getResourcePath(), true);
	}

	/*
	 * Create a child resource.  This constructor is used to create resources where there
	 * are sub states of the same entity.
	 * @param parent
	 * 			This resources parent interaction model.
	 * @param commandController
	 * 			All commands for all resources.
	 * @param hypermediaEngine
	 * 			All application states, responsible for creating links from one state to another.
	 * @param currentState	
	 * 			The current application state when accessing this resource.
	 */
	protected HTTPHypermediaRIM(
			HTTPHypermediaRIM parent, 
			NewCommandController commandController, 
			ResourceStateMachine hypermediaEngine,
			ResourceState currentState,
			Metadata metadata) {
		this(parent, commandController, hypermediaEngine, metadata, currentState.getResourcePath(), false);
	}
	
	private HTTPHypermediaRIM(
			HTTPHypermediaRIM parent, 
			NewCommandController commandController, 
			ResourceStateMachine hypermediaEngine,
			Metadata metadata,
			String currentPath,
			boolean printGraph) {
		this.parent = parent;
		this.commandController = commandController;
		this.hypermediaEngine = hypermediaEngine;
		this.metadata = metadata;
		this.resourcePath = currentPath;
		assert(commandController != null);
		assert(hypermediaEngine != null);
		assert(metadata != null);
		assert(resourcePath != null);
		hypermediaEngine.setCommandController(commandController);
		HypermediaValidator validator = HypermediaValidator.createValidator(hypermediaEngine, metadata);
		validator.setLogicalConfigurationListener(new LogicalConfigurationListener() {
			
			@Override
			public void noMetadataFound(ResourceStateMachine rsm,
					ResourceState state) {
				throw new RuntimeException("Invalid configuration of resource state [" + state + "] - no metadata for entity ["+state.getEntityName()+"]");
			}

			@Override
			public void noActionsConfigured(ResourceStateMachine rsm,
					ResourceState state) {
				throw new RuntimeException("Invalid configuration of resource state [" + state + "] - no actions configured");
			}

			@Override
			public void viewActionNotSeen(ResourceStateMachine rsm, ResourceState state) {
				if (!state.isPseudoState())
					logger.warn("Invalid configuration of resource state [" + state + "] - no view command");
				//				throw new RuntimeException("Invalid configuration of resource state [" + state + "] - no view command");
			}
			
			@Override
			public void actionNotAvailable(ResourceStateMachine rsm,
					ResourceState state, Action action) {
				throw new RuntimeException("Invalid configuration of resource state [" + state + "] - no command for action [" + action + "]");
			}
		});
		if (printGraph && hypermediaEngine.getInitial() != null) {
			logger.info("State graph for [" + this.toString() + "] [" + validator.graph() + "]");
		}
		validator.validate();
	}

	protected ResourceStateMachine getHypermediaEngine() {
		return hypermediaEngine;
	}
	
	/*
	 * Bootstrap the resource by attempting to fetch a command for all the required
	 * interactions with the resource state.
	private void bootstrap() {
		Set<String> interactions = new HashSet<String>();
		Set<String> configuredInteractions = hypermediaEngine.getInteractions(currentState);
		if (configuredInteractions != null)
			interactions.addAll(configuredInteractions);
		// every resource MUST have a GET command
		interactions.add("GET");
	
		if (interactions != null) {
			// interactions are a set of http methods
			for (String method : interactions) {
				logger.debug("Checking configuration for [" + method + "] " + getFQResourcePath());
				// check valid http method
				if (!(method.equals(HttpMethod.GET) || method.equals(HttpMethod.PUT) || method.equals(HttpMethod.DELETE) || method.equals(HttpMethod.POST)))
					throw new RuntimeException("Invalid configuration of state [" + hypermediaEngine.getInitial().getId() + "] - invalid http method [" + method + "]");
				// fetch command from command controller for this method
				InteractionCommand command = getCommandController().fetchCommand(method, getFQResourcePath());
				if (command == null)
					throw new RuntimeException("Invalid configuration of dynamic resource [" + this + "] - no state transition command for http method [" + method + "]");
			}
		}

		// TODO should be verified in constructor, but this class is currently mixed with dynamic resources that do not use links
		// assert(getResourceRegistry() != null);
		// resource created and valid, now register ourselves in the resource registry
//		if (getResourceRegistry() != null)
//			getResourceRegistry().add(this);
	}
	 */

	public String getResourcePath() {
		return resourcePath;
	}

	public String getFQResourcePath() {
		String result = "";
		if (getParent() != null)
			result = getParent().getResourcePath();
			
		return result + getResourcePath();
	}

	@Override
	public ResourceInteractionModel getParent() {
		return parent;
	}

	@Override
	public Collection<ResourceInteractionModel> getChildren() {
		List<ResourceInteractionModel> result = new ArrayList<ResourceInteractionModel>();
		
		for (ResourceState s : hypermediaEngine.getResourceStatesForPath(this.resourcePath)) {
			Map<String, Set<ResourceState>> resourceStates = hypermediaEngine.getResourceStatesByPath(s);
			for (String childPath : resourceStates.keySet()) {
				// get the sub states
//				Set<ResourceState> childStates = resourceStates.get(childPath);
				HTTPHypermediaRIM child = null;
				if (childPath.equals(s.getResourcePath())) {
					continue;
				}
				child = new HTTPHypermediaRIM(null, getCommandController(), hypermediaEngine, metadata, childPath, false);
				result.add(child);
			}
		}
		return result;
	}

    /*
     * The map of all commands for http methods, paths, and media types.
     */
    protected NewCommandController getCommandController() {
		return commandController;
	}

	/**
	 * GET a resource representation.
	 * @precondition a valid GET command for this resourcePath + id must be registered with the command controller
	 * @postcondition a Response with non null Status must be returned
	 * @invariant resourcePath not null
	 * @see com.temenos.interaction.core.rim.HTTPResourceInteractionModel#get(javax.ws.rs.core.HttpHeaders, java.lang.String)
	 */
	@Override
	@GET
	@Produces({
		MediaType.APPLICATION_ATOM_XML,
		MediaType.APPLICATION_XML,
		ExtendedMediaTypes.APPLICATION_ATOMSVC_XML,
		MediaType.APPLICATION_JSON,
		MediaType.APPLICATION_XHTML_XML,
		MediaType.TEXT_HTML,
		MediaType.WILDCARD})
    public Response get( @Context HttpHeaders headers, @PathParam("id") String id, @Context UriInfo uriInfo ) {
    	logger.info("GET " + getFQResourcePath());
    	assert(getResourcePath() != null);
    	Event event = new Event("GET", HttpMethod.GET);
    	// handle request
    	return handleRequest(headers, uriInfo, event, null);
	}
	
	private Response handleRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo, Event event, EntityResource<?> resource) {
    	// determine action
    	InteractionCommand action = hypermediaEngine.determineAction(event, getFQResourcePath());
    	// create the interaction context
    	InteractionContext ctx = buildInteractionContext(headers, uriInfo, event);
		return handleRequest(headers, ctx, event, action, resource, null);
	}

	private Response handleRequest(@Context HttpHeaders headers, InteractionContext ctx, Event event, InteractionCommand action, EntityResource<?> resource, Transition autoTransition) {
		assert(event != null);
		StatusType status = Status.NOT_FOUND;
    	if (action == null) {
    		if (event.isUnSafe()) {
    			status = HttpStatusTypes.METHOD_NOT_ALLOWED;
    		}
    		return buildResponse(headers, ctx.getPathParameters(), status, null, getInteractions(), null);
    	}
    	// determine current state, target state, and link used
    	initialiseInteractionContext(headers, event, ctx, resource);
    	// execute action
    	InteractionCommand.Result result = null;
    	try {
    		result = action.execute(ctx);
        	assert(result != null) : "InteractionCommand must return a result";
        	status = determineStatus(event, ctx, result, headers);
    	}
    	catch(InteractionException ie) {
    		logger.debug("Interaction command on state [" + ctx.getCurrentState().getId() + "] failed with error [" + ie.getHttpStatus() + " - " + ie.getHttpStatus().getReasonPhrase() + "]: " + ie.getMessage());
    		status = ie.getHttpStatus();
    		ctx.setException(ie);
    	}
    	// determine status
    	if (ctx.getResource() != null && status.getFamily() == Status.Family.SUCCESSFUL) {
    		/*
    		 * Add entity information to this resource
    		 */
    		ctx.getResource().setEntityName(ctx.getCurrentState().getEntityName());
    		/*
    		 * Add hypermedia information to this resource
    		 */
    		hypermediaEngine.injectLinks(ctx, ctx.getResource(), autoTransition);
    	}
    	// build response
    	return buildResponse(headers, ctx.getPathParameters(), status, ctx.getResource(), null, ctx);
    }
	
	private ResourceState initialiseInteractionContext(HttpHeaders headers, Event event, InteractionContext ctx, EntityResource<?> resource) {
		//Apply the etag on the If-Match header if available
		ctx.setPreconditionIfMatch(HeaderHelper.getFirstHeader(headers, HttpHeaders.IF_MATCH));
		
		// set the resource for the commands to access
		if(resource != null) {
        	ctx.setResource(resource);
		}
		
    	// work out the target state and link used
		ResourceState targetState = null;
		LinkHeader linkHeader = null;
		List<String> linkHeaders = headers.getRequestHeader("Link");
		if (linkHeaders != null && linkHeaders.size() > 0) {
    		// there must be only one Link header
    		assert(linkHeaders.size() == 1);
			linkHeader = LinkHeader.valueOf(linkHeaders.get(0));
		}
		Link linkUsed = hypermediaEngine.getLinkFromRelations(ctx.getPathParameters(), null, linkHeader);
		ctx.setLinkUsed(linkUsed);
		if (linkUsed != null)
			targetState = linkUsed.getTransition().getTarget();
		if (targetState == null)
			targetState = ctx.getCurrentState();
		ctx.setTargetState(targetState);
		return targetState;
	}
	
	private StatusType determineStatus(Event event, InteractionContext ctx, InteractionCommand.Result result, HttpHeaders headers) {
		assert(event != null);
		assert(ctx != null);
		
    	StatusType status = null;
    	switch(result) {
	    	case INVALID_REQUEST:					status = Status.BAD_REQUEST; break;
	    	case FAILURE:							status = Status.INTERNAL_SERVER_ERROR; break;
	    	case CONFLICT:							status = Status.PRECONDITION_FAILED; break;
	    	case SUCCESS: {

	    		status = Status.INTERNAL_SERVER_ERROR;
		    	if (event.getMethod().equals(HttpMethod.GET)) {
		    		String ifNoneMatch = HeaderHelper.getFirstHeader(headers, HttpHeaders.IF_NONE_MATCH);
		    		String etag = ctx.getResource() != null ? ctx.getResource().getEntityTag() : null;
		    		if (result == Result.SUCCESS && 
		    				etag != null && etag.equals(ifNoneMatch)) {
		    			//Response etag matches IfNoneMatch precondition
		    			status = Status.NOT_MODIFIED;
		    		} else if(result == Result.SUCCESS) {
		    			status = Status.OK;
		    		}
				} else if (event.getMethod().equals(HttpMethod.POST)) {
			    	// TODO need to add support for differed create (ACCEPTED) and actually created (CREATED)
					ResourceState currentState = ctx.getCurrentState();
					if (result == Result.SUCCESS) {
						if (currentState != null && currentState.getAllTargets() != null && currentState.getAllTargets().size() > 0
								&& ctx.getResource() != null) {
					   		status = Status.CREATED;
						} else if (ctx.getResource() == null) {
				   			status = Status.NO_CONTENT;
						} else {
							logger.warn("This pseudo state creates a new resource (the command implementing POST returns a resource), but no transitions have been configured");
					   		status = Status.OK;
						}
					}
				} else if (event.getMethod().equals(HttpMethod.PUT)) {
			    	/*
			    	 * The resource manager must return an error result code or have stored this 
			    	 * resource in a consistent state (conceptually a transaction)
			    	 */
			    	/*
			    	 * TODO add support for PUTs that create (CREATED) and PUTs that replace (OK)
			    	 */
			   		if (result == Result.SUCCESS && ctx.getResource() == null) {
			   			status = Status.NO_CONTENT;
			   		} else if (result == Result.SUCCESS) {
			   			status = Status.OK;
			   		}
				} else if (event.getMethod().equals(HttpMethod.DELETE)) {
			    	if (result == Result.SUCCESS) {
			        	// We do not support a delete command that returns a resource (HTTP does permit this)
			        	assert(ctx.getResource() == null);
			    		ResourceState targetState = ctx.getTargetState();
			    		Link linkUsed = ctx.getLinkUsed();
			        	if (targetState.isTransientState()) {
							Transition autoTransition = targetState.getAutoTransition();
							if (autoTransition.getTarget().getPath().equals(ctx.getCurrentState().getPath())
									|| (linkUsed != null && autoTransition.getTarget().equals(linkUsed.getTransition().getSource()))) {
			            		// this transition has been configured to reset content
			               		status = HttpStatusTypes.RESET_CONTENT;
			        		} else {
			        			status = Status.SEE_OTHER;
			        		}
						} else if (targetState.isPseudoState() || targetState.getPath().equals(ctx.getCurrentState().getPath())) {
			    			// did we delete ourselves or pseudo final state, both are transitions to No Content
			        		status = Response.Status.NO_CONTENT;
			    		} else {
			    			throw new IllegalArgumentException("Resource interaction exception, should not be " +
			    					"possible to use a link where target state is not our current state");
			    		}
			    	} else {
			    		assert(false) : "Unhandled result from Command";
			    	}
				}
	    	}
	    }

		return status;
	}
	
	private InteractionContext buildInteractionContext(HttpHeaders headers, UriInfo uriInfo, Event event) {
    	MultivaluedMap<String, String> queryParameters = uriInfo != null ? uriInfo.getQueryParameters(true) : null;
    	MultivaluedMap<String, String> pathParameters = uriInfo != null ? uriInfo.getPathParameters(true) : null;
    	// work around an issue in wink, wink does not decode query parameters in 1.1.3
    	decodeQueryParams(queryParameters);
    	// create the interaction context
    	ResourceState currentState = hypermediaEngine.determineState(event, getFQResourcePath());
    	InteractionContext ctx = new InteractionContext(pathParameters, queryParameters, currentState, metadata);
    	return ctx;
	}

    private Response buildResponse(HttpHeaders headers, MultivaluedMap<String, String> pathParameters, StatusType status, RESTResource resource, Set<String> interactions, InteractionContext ctx) {	
    	RESTResponse response = new RESTResponse(status, resource);

    	assert (response != null);
		assert (status != null);  // not a valid get command

		// Build the Response (representation will be created by the jax-rs Provider)
		ResponseBuilder responseBuilder = Response.status(status);
		if (status.equals(HttpStatusTypes.RESET_CONTENT)) {
			responseBuilder = HeaderHelper.allowHeader(responseBuilder, interactions);
		} else if (status.equals(HttpStatusTypes.METHOD_NOT_ALLOWED)) {
			assert(interactions != null);
			responseBuilder = HeaderHelper.allowHeader(responseBuilder, interactions);
		} else if (status.equals(Response.Status.NO_CONTENT)) {
			responseBuilder = HeaderHelper.allowHeader(responseBuilder, interactions);
		} else if (status.equals(Response.Status.SEE_OTHER)) {
			ResourceState targetState = ctx.getTargetState();
			Transition autoTransition = targetState.getAutoTransition();
    		Link target = hypermediaEngine.createLinkToTarget(autoTransition, resource, pathParameters);
			responseBuilder = HeaderHelper.locationHeader(responseBuilder, target.getHref());
		} else if (status.equals(Response.Status.CREATED)) {
			ResourceState currentState = ctx.getCurrentState();
			assert(currentState.getAllTargets() != null && currentState.getAllTargets().size() > 0) : "A pseudo state that creates a new resource MUST contain an auto transition to that new resource";
			Transition autoTransition = null;
			for(Link link : resource.getLinks()) {
				if(link.getRel() != null && !link.getRel().equals("self")) {		//Ignore self link
					if(autoTransition != null) {
						logger.warn("Resource state [" + currentState.getName() + "] has multiple auto-transition. Using [" + link.getId() + "].");
					}
					else {
						autoTransition = link.getTransition();
					}
				}
			}
			if (autoTransition != null && autoTransition.getCommand().isAutoTransition()) {
				assert(resource instanceof EntityResource) : "Must be an EntityResource as we have created a new resource";
	    		@SuppressWarnings("rawtypes")
				Link target = hypermediaEngine.createLink(autoTransition, ((EntityResource)resource).getEntity(), pathParameters);
				responseBuilder = HeaderHelper.locationHeader(responseBuilder, target.getHref());
				
				Action targetAction = autoTransition.getTarget().getViewAction();
				InteractionCommand action = getCommandController().fetchCommand(targetAction.getName());
				MultivaluedMap<String, String> autoPathParameters = new MultivaluedMapImpl<String>();
				autoPathParameters.putAll(pathParameters);
				Map<String,Object> transitionProperties = hypermediaEngine.getTransitionProperties(autoTransition, ((EntityResource<?>)resource).getEntity(), pathParameters);
				for (String key : transitionProperties.keySet()) {
					autoPathParameters.add(key, transitionProperties.get(key).toString());
				}
				InteractionContext autoCtx = new InteractionContext(autoPathParameters, ctx.getQueryParameters(), autoTransition.getTarget(), metadata);
	        	Response autoResponse = handleRequest(headers, autoCtx, new Event("GET", HttpMethod.GET), action, (EntityResource<?>) resource, autoTransition);
	        	if (autoResponse.getStatus() != HttpStatus.OK.getCode()) {
	        		logger.warn("Auto transition target did not return HttpStatus.OK status");
	        	}
	        	resource = (EntityResource<?>)((GenericEntity<?>)autoResponse.getEntity()).getEntity();
	        	resource.setEntityName(autoTransition.getTarget().getEntityName());
			}
			assert(response.getResource() != null);
    		responseBuilder.entity(resource.getGenericEntity());
   			responseBuilder = HeaderHelper.etagHeader(responseBuilder, resource.getEntityTag());
		} else if (status.equals(Response.Status.NOT_MODIFIED)) {
			responseBuilder = HeaderHelper.allowHeader(responseBuilder, interactions);
		} else if (status.getFamily() == Response.Status.Family.SUCCESSFUL) {
			assert(response.getResource() != null);
    		/*
    		 * Wrap response into a JAX-RS GenericEntity object to ensure we have the type 
    		 * information available to the Providers
    		 */
    		responseBuilder.entity(resource.getGenericEntity());
    		responseBuilder = HeaderHelper.allowHeader(responseBuilder, interactions);
   			responseBuilder = HeaderHelper.etagHeader(responseBuilder, resource.getEntityTag());
		} else if((status.getFamily() == Response.Status.Family.CLIENT_ERROR || status.getFamily() == Response.Status.Family.SERVER_ERROR) && ctx != null) {
			if(ctx.getCurrentState().getErrorState() != null) {
				//Resource has an onerror handler
				RESTResource errorResource = getResource(ctx.getCurrentState().getErrorState(), ctx);
				responseBuilder.entity(errorResource.getGenericEntity());
			}
			else if(hypermediaEngine.getException() != null && ctx.getException() != null) {
				//Resource state machine has an exception handler
				RESTResource exceptionResource = getResource(hypermediaEngine.getException(), ctx);
				responseBuilder.entity(exceptionResource.getGenericEntity());
			}
			else if(resource != null) {
				//Just return the resource entity
				responseBuilder.entity(resource.getGenericEntity());
			}
			responseBuilder = HeaderHelper.allowHeader(responseBuilder, interactions);
    	}

		logger.info("Building response " + status.getStatusCode() + " " + status.getReasonPhrase());
		return responseBuilder.build();
    }
    
    /*
     * Returns the resource on the specified resource state. 
     */
    private RESTResource getResource(ResourceState state, InteractionContext ctx) {
		try {
			RESTResource resource = hypermediaEngine.getResource(state, ctx);
			if(resource == null) {
				throw new InteractionException(Status.INTERNAL_SERVER_ERROR, "Resource state [" + state.getId() + "] did not return a resource.");
			}
			return resource;
		}
		catch(CommandFailureException cfe) {
			logger.debug("Command on resource state [" + state.getId() + "] failed with status [" + cfe.getResult() + "] " + cfe.getMessage());
			return cfe.getResource();
		}
		catch(InteractionException ie) {
			logger.error("Failed to access resource [" + state.getId() + "] with error [" + ie.getHttpStatus() + " - " + ie.getHttpStatus().getReasonPhrase() + "]: " + ie.getMessage());
			throw new RuntimeException(ie);
		}
    }
    
    @SuppressWarnings("static-access")
	private void decodeQueryParams(MultivaluedMap<String, String> queryParameters) {
    	try {
    		if (queryParameters == null)
    			return;
			URLDecoder ud = new URLDecoder();
			for (String key : queryParameters.keySet()) {
				List<String> values = queryParameters.get(key);
				if (values != null) {
					List<String> newValues = new ArrayList<String>();
				    for (String value : values) {
				    	if (value != null)
				    		newValues.add(ud.decode(value, "UTF-8"));
				    }
				    queryParameters.put(key, newValues);
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
    }
    
    /**
	 * Handle a POST from a regular html form.
	 */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response post( @Context HttpHeaders headers, @PathParam("id") String id, @Context UriInfo uriInfo, 
    		MultivaluedMap<String, String> formParams) {
    	assert(getResourcePath() != null);
    	Event event = new Event("POST", HttpMethod.POST);
    	// handle request
    	InteractionContext ctx = buildInteractionContext(headers, uriInfo, event);
    	initialiseInteractionContext(headers, event, ctx, null);
    	String entityName = metadata.getModelName() + Metadata.MODEL_SUFFIX + "." + ctx.getCurrentState().getEntityName();
		EntityResource<Entity> resource = new EntityResource<Entity>(entityName, createEntity(entityName, formParams));
    	return handleRequest(headers, uriInfo, event, resource);
    }
    
    private Entity createEntity(String entityName, MultivaluedMap<String, String> formParams) {
		EntityProperties fields = new EntityProperties();
		for (String key : formParams.keySet()) {
			fields.setProperty(new EntityProperty(key, formParams.getFirst(key)));
		}
		return new Entity(entityName, fields);
    }

    /**
	 * POST a document to a resource.
	 * @precondition a valid POST command for this resourcePath + id must be registered with the command controller
	 * @postcondition a Response with non null Status must be returned
	 * @invariant resourcePath not null
	 */
    @POST
	@Consumes({
		MediaType.APPLICATION_ATOM_XML,
    	MediaType.APPLICATION_XML, 
    	MediaType.APPLICATION_JSON, 
    	MediaType.WILDCARD})
	@Produces({
		MediaType.APPLICATION_ATOM_XML,
    	MediaType.APPLICATION_XML, 
    	MediaType.APPLICATION_JSON, 
    	MediaType.WILDCARD})
    public Response post( @Context HttpHeaders headers, @PathParam("id") String id, @Context UriInfo uriInfo, EntityResource<?> resource ) {
    	logger.info("POST " + getFQResourcePath());
    	assert(getResourcePath() != null);
    	Event event = new Event("POST", HttpMethod.POST);
    	// handle request
    	return handleRequest(headers, uriInfo, event, resource);
    }

    /**
	 * PUT a resource.
	 * @precondition a valid PUT command for this resourcePath + id must be registered with the command controller
	 * @postcondition a Response with non null Status must be returned
	 * @invariant resourcePath not null
	 * @see com.temenos.interaction.core.rim.HTTPResourceInteractionModel#put(javax.ws.rs.core.HttpHeaders, java.lang.String, com.temenos.interaction.core.EntityResource)
	 */
    @Override
	@PUT
	@Consumes({
		MediaType.APPLICATION_ATOM_XML,
    	MediaType.APPLICATION_XML, 
    	MediaType.APPLICATION_JSON, 
    	MediaType.WILDCARD})
    public Response put( @Context HttpHeaders headers, @PathParam("id") String id, @Context UriInfo uriInfo, EntityResource<?> resource ) {
    	logger.info("PUT " + getFQResourcePath());
    	assert(getResourcePath() != null);
    	Event event = new Event("PUT", HttpMethod.PUT);
    	// handle request
    	return handleRequest(headers, uriInfo, event, resource);
    }

	/**
	 * DELETE a resource.
	 * @precondition a valid DELETE command for this resourcePath + id must be registered with the command controller
	 * @postcondition a Response with non null Status must be returned
	 * @invariant resourcePath not null
	 * @see com.temenos.interaction.core.rim.HTTPResourceInteractionModel#delete(javax.ws.rs.core.HttpHeaders, java.lang.String)
	 */
    @Override
	@DELETE
	public Response delete( @Context HttpHeaders headers, @PathParam("id") String id, @Context UriInfo uriInfo) {
    	logger.info("DELETE " + getFQResourcePath());
    	assert(getResourcePath() != null);
    	Event event = new Event("DELETE", HttpMethod.DELETE);
    	// handle request
    	return handleRequest(headers, uriInfo, event, null);
    }
    
	/**
	 * OPTIONS for a resource.
	 * @precondition a valid GET command for this resourcePath must be registered with the command controller
	 * @postcondition a Response with non null Status must be returned
	 * @invariant resourcePath not null
	 */
    @Override
    public Response options( @Context HttpHeaders headers, @PathParam("id") String id, @Context UriInfo uriInfo ) {
    	logger.info("OPTIONS " + getFQResourcePath());
    	assert(getResourcePath() != null);
    	Event event = new Event("OPTIONS", HttpMethod.GET);
    	// create the interaction context
    	InteractionContext ctx = buildInteractionContext(headers, uriInfo, event);
    	// TODO add support for OPTIONS /resource/* which will provide information about valid interactions for any entity
		return buildResponse(headers, ctx.getPathParameters(), Status.NO_CONTENT, null, getInteractions(), null);
    }
    
    /**
     * Get the valid methods for interacting with this resource.
     * @return
     */
    public Set<String> getInteractions() {
    	Set<String> interactions = new HashSet<String>();
    	interactions.addAll(hypermediaEngine.getInteractionByPath().get(getFQResourcePath()));
    	interactions.add("HEAD");
    	interactions.add("OPTIONS");
    	return interactions;
    }

	@Override
	public ResourceState getCurrentState() {
		// TODO, need to figure out how to pass event in where required
    	return hypermediaEngine.determineState(new Event("GET", HttpMethod.GET), getFQResourcePath());
	}

	public boolean equals(Object other) {
		//check for self-comparison
	    if ( this == other ) return true;
	    if ( !(other instanceof HTTPHypermediaRIM) ) return false;
	    HTTPHypermediaRIM otherResource = (HTTPHypermediaRIM) other;
	    return getFQResourcePath().equals(otherResource.getFQResourcePath());
	}
	
	public int hashCode() {
		return getFQResourcePath().hashCode();
	}

	public String toString() {
		return ("HTTPHypermediaRIM [" + getFQResourcePath() + "]");
	}

}
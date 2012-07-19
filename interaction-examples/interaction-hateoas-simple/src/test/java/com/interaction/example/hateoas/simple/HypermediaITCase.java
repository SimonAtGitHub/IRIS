package com.interaction.example.hateoas.simple;

import static org.junit.Assert.*;

import java.io.InputStreamReader;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.test.framework.JerseyTest;
import com.temenos.interaction.core.media.hal.MediaType;
import com.theoryinpractise.halbuilder.ResourceFactory;
import com.theoryinpractise.halbuilder.spi.Link;
import com.theoryinpractise.halbuilder.spi.ReadableResource;
import com.theoryinpractise.halbuilder.spi.Resource;

/**
 * This test ensures that we can navigate from one application state
 * to another using hypermedia (links).
 * 
 * @author aphethean
 */
public class HypermediaITCase extends JerseyTest {

	public HypermediaITCase() throws Exception {
		super();
	}
	
	@Before
	public void initTest() {
		// TODO make this configurable
		// test with external server 
    	webResource = Client.create().resource(Configuration.TEST_ENDPOINT_URI); 
	}

	@After
	public void tearDown() {}


	@Test
	public void testGetEntryPointLinks() {
		ClientResponse response = webResource.path("/").accept(MediaType.APPLICATION_HAL_JSON).get(ClientResponse.class);
        assertEquals(Response.Status.Family.SUCCESSFUL, Response.Status.fromStatusCode(response.getStatus()).getFamily());

		ResourceFactory resourceFactory = new ResourceFactory();
		ReadableResource resource = resourceFactory.newResource(new InputStreamReader(response.getEntityInputStream()));

		List<Link> links = resource.getLinks();
		assertEquals(4, links.size());
		for (Link link : links) {
			if (link.getRel().equals("self") && link.getName().get().equals("home.initial>home.initial")) {
				assertEquals(Configuration.TEST_ENDPOINT_URI + "/", link.getHref());
			} else if (link.getName().get().equals("home.initial>Preferences.preferences")) {
				assertEquals(Configuration.TEST_ENDPOINT_URI + "/preferences", link.getHref());
			} else if (link.getName().get().equals("home.initial>Profile.profile")) {
				assertEquals(Configuration.TEST_ENDPOINT_URI + "/profile", link.getHref());
			} else if (link.getName().get().equals("home.initial>note.initial")) {
				assertEquals(Configuration.TEST_ENDPOINT_URI + "/notes", link.getHref());
			} else {
				fail("unexpected link");
			}
		}
	}
	
	@Test
	public void testCollectionLinks() {
		ClientResponse response = webResource.path("/notes").accept(MediaType.APPLICATION_HAL_JSON).get(ClientResponse.class);
        assertEquals(Response.Status.Family.SUCCESSFUL, Response.Status.fromStatusCode(response.getStatus()).getFamily());

		ResourceFactory resourceFactory = new ResourceFactory();
		ReadableResource resource = resourceFactory.newResource(new InputStreamReader(response.getEntityInputStream()));

		// the links from the collection
		List<Link> links = resource.getLinks();
		assertEquals(2, links.size());
		for (Link link : links) {
			if (link.getRel().equals("self")) {
				assertEquals(Configuration.TEST_ENDPOINT_URI + "/notes", link.getHref());
			} else if (link.getName().get().equals("note.initial>ID.new")) {
				assertEquals("POST " + Configuration.TEST_ENDPOINT_URI + "/notes/new", link.getHref());
			} else {
				fail("unexpected link [" + link.getName().get() + "]");
			}
		}
		
		// the items, and links on each item
		List<Resource> subresources = resource.getResources();
		assertNotNull(subresources);
		for (Resource item : subresources) {
			List<Link> itemLinks = item.getLinks();
			assertEquals(2, itemLinks.size());
			for (Link link : itemLinks) {
				if (link.getRel().contains("self")) {
					assertEquals(Configuration.TEST_ENDPOINT_URI + "/notes/" + item.getProperties().get("noteID"), link.getHref());
				} else if (link.getName().get().contains("note.end")) {
					assertEquals("DELETE " + Configuration.TEST_ENDPOINT_URI + "/notes/" + item.getProperties().get("noteID"), link.getHref());
				} else {
					fail("unexpected link [" + link.getName().get() + "]");
				}
			}
		}
	}

	/**
	 * Found a small issue where a GET to a non-existent resource still generated the links and this
	 * resulted in a server side error (500)
	 */
	@Test
	public void testGET404() {
		ClientResponse response = webResource.path("/notes/666").accept(MediaType.APPLICATION_HAL_JSON).get(ClientResponse.class);
        assertEquals(404, response.getStatus());
	}

	@Test
	public void testFollowDeleteItemLink() {
		ClientResponse response = webResource.path("/notes").accept(MediaType.APPLICATION_HAL_JSON).get(ClientResponse.class);
        assertEquals(Response.Status.Family.SUCCESSFUL, Response.Status.fromStatusCode(response.getStatus()).getFamily());

		ResourceFactory resourceFactory = new ResourceFactory();
		ReadableResource resource = resourceFactory.newResource(new InputStreamReader(response.getEntityInputStream()));
		
		// the items in the collection
		List<Resource> subresources = resource.getResources();
		assertNotNull(subresources);
		
		// follow the link to delete the first in the collection
		if (subresources.size() == 0) {
			// we might have run the integration tests more times than we have rows in our table
		} else {
			Resource item = subresources.get(0);
			List<Link> itemLinks = item.getLinks();
			assertEquals(2, itemLinks.size());
			Link deleteLink = null;
			for (Link link : itemLinks) {
				if (link.getName().get().contains("note.initial>note.end")) {
					deleteLink = link;
				}
			}
			assertNotNull(deleteLink);
			String[] hrefElements = deleteLink.getHref().split(" ");
			String method = hrefElements[0];
			assertEquals("DELETE", method);
			String uri = hrefElements[1];

			// create http client
			Client client = Client.create();
			// do not follow the Location redirect
			client.setFollowRedirects(false);
			// execute delete without custom link relation, will find the only DELETE transition from entity
			ClientResponse deleteResponse = client.resource(uri).accept(MediaType.APPLICATION_HAL_JSON)
					.delete(ClientResponse.class);
	        // 303 "See Other" instructs user agent to fetch another resource as specified by the 'Location' header
	        assertEquals(303, deleteResponse.getStatus());
	        assertEquals(Configuration.TEST_ENDPOINT_URI + "/notes", deleteResponse.getHeaders().getFirst("Location"));
		}
	}

	@Test
	public void testFollowDeleteItemLinkRelation() {
		ClientResponse response = webResource.path("/notes").accept(MediaType.APPLICATION_HAL_JSON).get(ClientResponse.class);
        assertEquals(Response.Status.Family.SUCCESSFUL, Response.Status.fromStatusCode(response.getStatus()).getFamily());

		ResourceFactory resourceFactory = new ResourceFactory();
		ReadableResource resource = resourceFactory.newResource(new InputStreamReader(response.getEntityInputStream()));
		
		// the items in the collection
		List<Resource> subresources = resource.getResources();
		assertNotNull(subresources);
		
		// follow the link to delete the first in the collection
		if (subresources.size() == 0) {
			// we might have run the integration tests more times than we have rows in our table
		} else {
			Resource item = subresources.get(0);
			List<Link> itemLinks = item.getLinks();
			assertEquals(2, itemLinks.size());
			Link deleteLink = null;
			for (Link link : itemLinks) {
				if (link.getName().get().contains("note.initial>note.end")) {
					deleteLink = link;
				}
			}
			assertNotNull(deleteLink);
			String[] hrefElements = deleteLink.getHref().split(" ");
			String method = hrefElements[0];
			assertEquals("DELETE", method);
			String uri = hrefElements[1];

			// execute delete with custom link relation (see rfc5988)
			ClientResponse deleteResponse = Client.create().resource(uri)
					.header("Link", "<" + uri + ">; rel=\"" + deleteLink.getName().get() + "\"")
					.accept(MediaType.APPLICATION_HAL_JSON)
					.delete(ClientResponse.class);
	        // 205 "Reset Content" instructs user agent to reload the resource that contained this link
	        assertEquals(205, deleteResponse.getStatus());
		}
	}
	
	@Test
	public void testFollowDeleteLinkWithLinkRel() {
		ClientResponse response = webResource.path("/notes").accept(MediaType.APPLICATION_HAL_JSON).get(ClientResponse.class);
        assertEquals(Response.Status.Family.SUCCESSFUL, Response.Status.fromStatusCode(response.getStatus()).getFamily());

		ResourceFactory resourceFactory = new ResourceFactory();
		ReadableResource resource = resourceFactory.newResource(new InputStreamReader(response.getEntityInputStream()));
		
		// the items in the collection
		List<Resource> subresources = resource.getResources();
		assertNotNull(subresources);
		
		// follow the link to delete the first in the collection
		if (subresources.size() == 0) {
			// we might have run the integration tests more times than we have rows in our table
		} else {
			Resource item = subresources.get(0);
			List<Link> itemLinks = item.getLinks();
			assertEquals(2, itemLinks.size());
			
			// GET item link (note.initial->note.exists)
			Link getLink = null;
			for (Link link : itemLinks) {
				if (link.getRel().contains("self")) {
					getLink = link;
				}
			}
			// follow GET item link
			assertNotNull(getLink);
			ClientResponse getResponse = Client.create().resource(getLink.getHref()).accept(MediaType.APPLICATION_HAL_JSON).get(ClientResponse.class);
	        assertEquals(Response.Status.Family.SUCCESSFUL, Response.Status.fromStatusCode(getResponse.getStatus()).getFamily());
			// the item
			ReadableResource itemResource = resourceFactory.newResource(new InputStreamReader(getResponse.getEntityInputStream()));
			List<Link> links = itemResource.getLinks();
			assertNotNull(links);
			assertEquals(3, links.size());
			
			// DELETE item link (note.exists->note.end, note.end is an auto transition to note.initial)
			Link deleteLink = null;
			for (Link link : links) {
				if (link.getName().get().contains("note.exists>note.end")) {
					deleteLink = link;
				}
			}
			assertNotNull(deleteLink);
			String[] hrefElements = deleteLink.getHref().split(" ");
			String method = hrefElements[0];
			assertEquals("DELETE", method);
			String uri = hrefElements[1];

			// create http client
			Client client = Client.create();
			// do not follow the Location redirect
			client.setFollowRedirects(false);
			// execute delete with custom link relation (see rfc5988)
			ClientResponse deleteResponse = client.resource(uri)
					.header("Link", "<" + uri + ">; rel=\"" + deleteLink.getName().get() + "\"")
					.accept(MediaType.APPLICATION_HAL_JSON)
					.delete(ClientResponse.class);
	        // 303 "See Other" instructs user agent to fetch another resource as specified by the 'Location' header
	        assertEquals(303, deleteResponse.getStatus());
	        assertEquals(Configuration.TEST_ENDPOINT_URI + "/notes", deleteResponse.getHeaders().getFirst("Location"));
		}
	}

	/**
	 * Attempt a DELETE to the notes resource (a collection resource)
	 */
	@Test
	public void deletePersonMethodNotAllowed() throws Exception {
		// attempt to delete the Person root, rather than an individual
		ClientResponse response = webResource.path("/notes").delete(ClientResponse.class);
        assertEquals(405, response.getStatus());

        assertEquals(3, response.getAllow().size());
        assertTrue(response.getAllow().contains("GET"));
        assertTrue(response.getAllow().contains("OPTIONS"));
        assertTrue(response.getAllow().contains("HEAD"));
	}

	/**
	 * Attempt a PUT to the notes resource (a collection resource)
	 */
	@Test
	public void putPersonMethodNotAllowed() throws Exception {
		String halRequest = "";
		// attempt to put to the notes collection, rather than an individual
		ClientResponse response = webResource.path("/notes").type(MediaType.APPLICATION_HAL_JSON).put(ClientResponse.class, halRequest);
        assertEquals(405, response.getStatus());

        assertEquals(3, response.getAllow().size());
        assertTrue(response.getAllow().contains("GET"));
        assertTrue(response.getAllow().contains("OPTIONS"));
        assertTrue(response.getAllow().contains("HEAD"));
	}

}

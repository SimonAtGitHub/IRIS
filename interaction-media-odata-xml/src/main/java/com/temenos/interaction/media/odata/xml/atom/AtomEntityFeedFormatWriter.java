package com.temenos.interaction.media.odata.xml.atom;

/*
 * #%L
 * interaction-media-odata-xml
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


import java.io.Writer;
import java.util.Collection;

import javax.ws.rs.core.UriInfo;

import org.apache.abdera.Abdera;
import org.apache.abdera.writer.StreamWriter;
import org.apache.commons.io.output.WriterOutputStream;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.odata4j.internal.InternalUtil;

import com.temenos.interaction.core.entity.Entity;
import com.temenos.interaction.core.entity.EntityMetadata;
import com.temenos.interaction.core.hypermedia.Link;
import com.temenos.interaction.core.resource.CollectionResource;
import com.temenos.interaction.core.resource.EntityResource;

/**
 * Writes a collection resource out as an Atom XML feed. 
 */
public class AtomEntityFeedFormatWriter {
	// Constants for OData
	public static final String d = "http://schemas.microsoft.com/ado/2007/08/dataservices";
	public static final String m = "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata";
	public static final String scheme = "http://schemas.microsoft.com/ado/2007/08/dataservices/scheme";
	public static final String atom_entry_content_type = "application/atom+xml;type=entry";
	public static final String href_lang = "en";

	private AtomEntityEntryFormatWriter entryWriter = new AtomEntityEntryFormatWriter();
	protected UriInfo uriInfo = null;
	protected String baseUri = "";
	
	/**
	 * Write a collection resource as an Atom XML feed
	 * @param uriInfo Current URI
	 * @param w Java writer to stream to atom+xml output
	 * @param collectionResource collection resource
	 * @param entityMetadata Metadata of entity
	 * @param inlineCount inline count
	 * @param skipToken skip token
	 * @param modelName Model name
	 */
	public void write(UriInfo uriInfo,
			Writer w, 
			CollectionResource<Entity> collectionResource,
			EntityMetadata entityMetadata, 
			Integer inlineCount,
			String skipToken,
			String modelName) 
	{
		this.uriInfo = uriInfo;
		String baseUri = uriInfo.getBaseUri().toString();
		String entitySetName = collectionResource.getEntitySetName();
		Collection<Link> links = collectionResource.getLinks();

		DateTime utc = new DateTime().withZone(DateTimeZone.UTC);
		String updated = InternalUtil.toString(utc);

		Abdera abdera = new Abdera();
		StreamWriter writer = abdera.newStreamWriter();
		writer.setOutputStream(new WriterOutputStream(w));
		writer.setAutoflush(false);
		writer.setAutoIndent(true);
		writer.startDocument();
	    writer.startFeed();
	    
	    //Write attributes
	    writer.writeNamespace("m", m);
	    writer.writeNamespace("d", d);
	    writer.writeAttribute("xml:base", baseUri);

	    //Write elements
	    writeElement(writer, "title", entitySetName, "type", "text");
	    writeElement(writer, "id", baseUri + uriInfo.getPath());
	    writeElement(writer, "updated", updated);

	    assert(links != null);
	    for (Link link : links) {
	    	String href = link.getHref();
	    	if (href.startsWith(baseUri)) {
	    		href = href.substring(baseUri.length());
	    	}
	    	String title = link.getTitle();
	    	String targetEntitySetName = null;
	    	if(link.getTransition().getTarget().getEntityName().equals(link.getTransition().getSource().getEntityName())) {
	    		targetEntitySetName = entitySetName;
	    	}
			String rel = AtomXMLProvider.getODataLinkRelation(link, targetEntitySetName);
	        writeElement(writer, "link", null, "rel", rel, "title", title, "href", href);
	    }

	    if (inlineCount != null) {
	      writeElement(writer, "m:count", inlineCount.toString());
	    }
	    
	    //Write entries
	    for (EntityResource<Entity> entityResource : collectionResource.getEntities()) {
	    	entryWriter.writeEntry(writer, entitySetName, entityResource.getEntity(), entityResource.getLinks(), uriInfo, updated, entityMetadata, modelName);
	    }

	    if (skipToken != null) {
	      String nextHref = uriInfo.getRequestUriBuilder().replaceQueryParam("$skiptoken", skipToken).build().toString();
	      writeElement(writer, "link", null, "rel", "next", "href", nextHref);
	    }
		
		writer.endFeed();
		writer.endDocument();
		writer.flush();
	}

	
	protected void writeElement(StreamWriter writer, String elementName, String elementText, String... attributes) {
		writer.startElement(elementName, "http://www.w3.org/2005/Atom");
		for (int i = 0; i < attributes.length; i += 2) {
			writer.writeAttribute(attributes[i], attributes[i + 1]);
		}
		if (elementText != null) {
			writer.writeElementText(elementText);
		}
		writer.endElement();
	}
}

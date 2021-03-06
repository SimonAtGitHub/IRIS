//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b26-ea3 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.06.20 at 02:21:10 PM IST 
//

package com.temenos.messagingLayer.pojo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * 
 * Denotes a business message, generated from T24 as a result of an update within T24. The source (originator) and type
 * of the event are
 * indicated by the associated attributes. A business event is a generic representation of the state and content of any
 * transaction. Note, at present
 * this definition is provided as guidance for localisations only. The TOCF does not provide this feature without
 * customisation in the R4 or R5 releases.
 * 
 * 
 * <p>
 * Java class for ofsml13BusinessEvent complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ofsml13BusinessEvent">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="eventPublished" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="eventTransmitted" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="eventExpiry" type="{http://www.w3.org/2001/XMLSchema}duration" minOccurs="0"/>
 *         &lt;element name="eventId" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *         &lt;element name="status" type="{http://www.temenos.com/T24/OFSML/130}ofsml13ProcessingStatus" minOccurs="0"/>
 *         &lt;element name="publishingUser" type="{http://www.w3.org/2001/XMLSchema}token" minOccurs="0"/>
 *         &lt;element name="transactionId" type="{http://www.w3.org/2001/XMLSchema}token" minOccurs="0"/>
 *         &lt;element name="field" type="{http://www.temenos.com/T24/OFSML/130}ofsml13MVField" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="override" type="{http://www.temenos.com/T24/OFSML/130}ofsml13OverrideField" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="auxField" type="{http://www.temenos.com/T24/OFSML/130}ofsml13MVField" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="auxOverride" type="{http://www.temenos.com/T24/OFSML/130}ofsml13OverrideField" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="source" use="required" type="{http://www.w3.org/2001/XMLSchema}token" />
 *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}token" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ofsml13BusinessEvent", propOrder = { "eventPublished", "eventTransmitted", "eventExpiry", "eventId",
		"status", "publishingUser", "transactionId", "field", "override", "auxField", "auxOverride" })
public class Ofsml13BusinessEvent {

	@XmlElement(namespace = "http://www.temenos.com/T24/OFSML/130")
	protected XMLGregorianCalendar eventPublished;
	@XmlElement(namespace = "http://www.temenos.com/T24/OFSML/130")
	protected XMLGregorianCalendar eventTransmitted;
	@XmlElement(namespace = "http://www.temenos.com/T24/OFSML/130")
	protected Duration eventExpiry;
	@XmlElement(namespace = "http://www.temenos.com/T24/OFSML/130")
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	protected String eventId;
	@XmlElement(namespace = "http://www.temenos.com/T24/OFSML/130")
	protected Ofsml13ProcessingStatus status;
	@XmlElement(namespace = "http://www.temenos.com/T24/OFSML/130")
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	protected String publishingUser;
	@XmlElement(namespace = "http://www.temenos.com/T24/OFSML/130")
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	protected String transactionId;
	@XmlElement(namespace = "http://www.temenos.com/T24/OFSML/130")
	protected List<Ofsml13MVField> field;
	@XmlElement(namespace = "http://www.temenos.com/T24/OFSML/130")
	protected List<Ofsml13OverrideField> override;
	@XmlElement(namespace = "http://www.temenos.com/T24/OFSML/130")
	protected List<Ofsml13MVField> auxField;
	@XmlElement(namespace = "http://www.temenos.com/T24/OFSML/130")
	protected List<Ofsml13OverrideField> auxOverride;
	@XmlAttribute(required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	protected String source;
	@XmlAttribute(required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	protected String type;

	/**
	 * Gets the value of the eventPublished property.
	 * 
	 * @return
	 *         possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getEventPublished() {
		return eventPublished;
	}

	/**
	 * Sets the value of the eventPublished property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setEventPublished(XMLGregorianCalendar value) {
		this.eventPublished = value;
	}

	/**
	 * Gets the value of the eventTransmitted property.
	 * 
	 * @return
	 *         possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getEventTransmitted() {
		return eventTransmitted;
	}

	/**
	 * Sets the value of the eventTransmitted property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setEventTransmitted(XMLGregorianCalendar value) {
		this.eventTransmitted = value;
	}

	/**
	 * Gets the value of the eventExpiry property.
	 * 
	 * @return
	 *         possible object is {@link Duration }
	 * 
	 */
	public Duration getEventExpiry() {
		return eventExpiry;
	}

	/**
	 * Sets the value of the eventExpiry property.
	 * 
	 * @param value
	 *            allowed object is {@link Duration }
	 * 
	 */
	public void setEventExpiry(Duration value) {
		this.eventExpiry = value;
	}

	/**
	 * Gets the value of the eventId property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getEventId() {
		return eventId;
	}

	/**
	 * Sets the value of the eventId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEventId(String value) {
		this.eventId = value;
	}

	/**
	 * Gets the value of the status property.
	 * 
	 * @return
	 *         possible object is {@link Ofsml13ProcessingStatus }
	 * 
	 */
	public Ofsml13ProcessingStatus getStatus() {
		return status;
	}

	/**
	 * Sets the value of the status property.
	 * 
	 * @param value
	 *            allowed object is {@link Ofsml13ProcessingStatus }
	 * 
	 */
	public void setStatus(Ofsml13ProcessingStatus value) {
		this.status = value;
	}

	/**
	 * Gets the value of the publishingUser property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getPublishingUser() {
		return publishingUser;
	}

	/**
	 * Sets the value of the publishingUser property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPublishingUser(String value) {
		this.publishingUser = value;
	}

	/**
	 * Gets the value of the transactionId property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * Sets the value of the transactionId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTransactionId(String value) {
		this.transactionId = value;
	}

	/**
	 * Gets the value of the field property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the field property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getField().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Ofsml13MVField }
	 * 
	 * 
	 */
	public List<Ofsml13MVField> getField() {
		if (field == null) {
			field = new ArrayList<Ofsml13MVField>();
		}
		return this.field;
	}

	/**
	 * Gets the value of the override property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the override property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getOverride().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Ofsml13OverrideField }
	 * 
	 * 
	 */
	public List<Ofsml13OverrideField> getOverride() {
		if (override == null) {
			override = new ArrayList<Ofsml13OverrideField>();
		}
		return this.override;
	}

	/**
	 * Gets the value of the auxField property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the auxField property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getAuxField().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Ofsml13MVField }
	 * 
	 * 
	 */
	public List<Ofsml13MVField> getAuxField() {
		if (auxField == null) {
			auxField = new ArrayList<Ofsml13MVField>();
		}
		return this.auxField;
	}

	/**
	 * Gets the value of the auxOverride property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the auxOverride property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getAuxOverride().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Ofsml13OverrideField }
	 * 
	 * 
	 */
	public List<Ofsml13OverrideField> getAuxOverride() {
		if (auxOverride == null) {
			auxOverride = new ArrayList<Ofsml13OverrideField>();
		}
		return this.auxOverride;
	}

	/**
	 * Gets the value of the source property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Sets the value of the source property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSource(String value) {
		this.source = value;
	}

	/**
	 * Gets the value of the type property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setType(String value) {
		this.type = value;
	}

}

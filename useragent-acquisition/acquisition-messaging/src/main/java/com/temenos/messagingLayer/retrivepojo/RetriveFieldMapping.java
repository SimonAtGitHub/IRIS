//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b26-ea3 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.07.21 at 05:05:49 AM GMT 
//

package com.temenos.messagingLayer.retrivepojo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for retriveFieldMapping element declaration.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;element name="retriveFieldMapping">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element ref="{}T24field" maxOccurs="unbounded"/>
 *           &lt;element ref="{}FrontEndObject"/>
 *         &lt;/sequence>
 *         &lt;attribute name="addPosition" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *         &lt;attribute name="conversionType" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *         &lt;attribute name="dataType" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "t24Field", "frontEndObject" })
@XmlRootElement(name = "retriveFieldMapping")
public class RetriveFieldMapping {

	@XmlElement(name = "T24field")
	protected List<String> t24Field;
	@XmlElement(name = "FrontEndObject")
	protected String frontEndObject;
	@XmlAttribute(required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	protected String addPosition;
	@XmlAttribute(required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	protected String conversionType;
	@XmlAttribute
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	protected String dataType;

	/**
	 * Gets the value of the t24Field property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the t24Field property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getT24Field().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link String }
	 * 
	 * 
	 */
	public List<String> getT24Field() {
		if (t24Field == null) {
			t24Field = new ArrayList<String>();
		}
		return this.t24Field;
	}

	/**
	 * Gets the value of the frontEndObject property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getFrontEndObject() {
		return frontEndObject;
	}

	/**
	 * Sets the value of the frontEndObject property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setFrontEndObject(String value) {
		this.frontEndObject = value;
	}

	/**
	 * Gets the value of the addPosition property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getAddPosition() {
		return addPosition;
	}

	/**
	 * Sets the value of the addPosition property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAddPosition(String value) {
		this.addPosition = value;
	}

	/**
	 * Gets the value of the conversionType property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getConversionType() {
		return conversionType;
	}

	/**
	 * Sets the value of the conversionType property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setConversionType(String value) {
		this.conversionType = value;
	}

	/**
	 * Gets the value of the dataType property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * Sets the value of the dataType property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDataType(String value) {
		this.dataType = value;
	}

}

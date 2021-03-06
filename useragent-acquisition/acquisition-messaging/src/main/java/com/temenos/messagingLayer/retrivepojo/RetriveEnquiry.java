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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for retriveEnquiry element declaration.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;element name="retriveEnquiry">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element ref="{}enquiryId"/>
 *           &lt;element ref="{}retriveFieldMapping" maxOccurs="unbounded"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "enquiryId", "retriveFieldMapping" })
@XmlRootElement(name = "retriveEnquiry")
public class RetriveEnquiry {

	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	protected String enquiryId;
	protected List<RetriveFieldMapping> retriveFieldMapping;

	/**
	 * Gets the value of the enquiryId property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getEnquiryId() {
		return enquiryId;
	}

	/**
	 * Sets the value of the enquiryId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEnquiryId(String value) {
		this.enquiryId = value;
	}

	/**
	 * Gets the value of the retriveFieldMapping property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
	 * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
	 * the retriveFieldMapping property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getRetriveFieldMapping().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link RetriveFieldMapping }
	 * 
	 * 
	 */
	public List<RetriveFieldMapping> getRetriveFieldMapping() {
		if (retriveFieldMapping == null) {
			retriveFieldMapping = new ArrayList<RetriveFieldMapping>();
		}
		return this.retriveFieldMapping;
	}

}

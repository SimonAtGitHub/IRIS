//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b26-ea3 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.07.21 at 05:05:49 AM GMT 
//

package com.temenos.messagingLayer.retrivepojo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the generated package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the Java representation for XML content.
 * The Java representation of XML content can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory methods for each of these are provided in
 * this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _T24Field_QNAME = new QName("", "T24field");
	private final static QName _FrontEndObject_QNAME = new QName("", "FrontEndObject");
	private final static QName _EnquiryId_QNAME = new QName("", "enquiryId");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package:
	 * generated
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link RetriveFieldMapping }
	 * 
	 */
	public RetriveFieldMapping createRetriveFieldMapping() {
		return new RetriveFieldMapping();
	}

	/**
	 * Create an instance of {@link RetriveEnquiry }
	 * 
	 */
	public RetriveEnquiry createRetriveEnquiry() {
		return new RetriveEnquiry();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "T24field")
	public JAXBElement<String> createT24Field(String value) {
		return new JAXBElement<String>(_T24Field_QNAME, String.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "FrontEndObject")
	public JAXBElement<String> createFrontEndObject(String value) {
		return new JAXBElement<String>(_FrontEndObject_QNAME, String.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "enquiryId")
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	public JAXBElement<String> createEnquiryId(String value) {
		return new JAXBElement<String>(_EnquiryId_QNAME, String.class, null, value);
	}

}

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b26-ea3 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.06.20 at 02:21:10 PM IST 
//

package com.temenos.messagingLayer.pojo;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 
 * A single repeatable field definition in a data set.
 * 
 * 
 * <p>
 * Java class for ofsml13DataSetFieldDefinition complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ofsml13DataSetFieldDefinition">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>token">
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}token" />
 *       &lt;attribute name="index" use="required" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *       &lt;attribute name="justification" type="{http://www.w3.org/2001/XMLSchema}token" />
 *       &lt;attribute name="label" use="required" type="{http://www.w3.org/2001/XMLSchema}token" />
 *       &lt;attribute name="length" use="required" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *       &lt;attribute name="repeatable" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="type" use="required" type="{http://www.temenos.com/T24/OFSML/130}t24DataType" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ofsml13DataSetFieldDefinition", propOrder = { "value" })
public class Ofsml13DataSetFieldDefinition {

	@XmlValue
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	protected String value;
	@XmlAttribute(required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	protected String id;
	@XmlAttribute(required = true)
	protected BigInteger index;
	@XmlAttribute
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	protected String justification;
	@XmlAttribute(required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	protected String label;
	@XmlAttribute(required = true)
	protected BigInteger length;
	@XmlAttribute(required = true)
	protected boolean repeatable;
	@XmlAttribute(required = true)
	protected T24DataType type;

	/**
	 * Gets the value of the value property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value of the value property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the value of the id property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setId(String value) {
		this.id = value;
	}

	/**
	 * Gets the value of the index property.
	 * 
	 * @return
	 *         possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getIndex() {
		return index;
	}

	/**
	 * Sets the value of the index property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setIndex(BigInteger value) {
		this.index = value;
	}

	/**
	 * Gets the value of the justification property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getJustification() {
		return justification;
	}

	/**
	 * Sets the value of the justification property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setJustification(String value) {
		this.justification = value;
	}

	/**
	 * Gets the value of the label property.
	 * 
	 * @return
	 *         possible object is {@link String }
	 * 
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the value of the label property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setLabel(String value) {
		this.label = value;
	}

	/**
	 * Gets the value of the length property.
	 * 
	 * @return
	 *         possible object is {@link BigInteger }
	 * 
	 */
	public BigInteger getLength() {
		return length;
	}

	/**
	 * Sets the value of the length property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setLength(BigInteger value) {
		this.length = value;
	}

	/**
	 * Gets the value of the repeatable property.
	 * 
	 */
	public boolean isRepeatable() {
		return repeatable;
	}

	/**
	 * Sets the value of the repeatable property.
	 * 
	 */
	public void setRepeatable(boolean value) {
		this.repeatable = value;
	}

	/**
	 * Gets the value of the type property.
	 * 
	 * @return
	 *         possible object is {@link T24DataType }
	 * 
	 */
	public T24DataType getType() {
		return type;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param value
	 *            allowed object is {@link T24DataType }
	 * 
	 */
	public void setType(T24DataType value) {
		this.type = value;
	}

}

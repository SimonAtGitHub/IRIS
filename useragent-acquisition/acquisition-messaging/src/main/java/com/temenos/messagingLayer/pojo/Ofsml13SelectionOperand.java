//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b26-ea3 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.06.20 at 02:21:10 PM IST 
//

package com.temenos.messagingLayer.pojo;

import javax.xml.bind.annotation.XmlEnum;

/**
 * <p>
 * Java class for ofsml13SelectionOperand.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="ofsml13SelectionOperand">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="EQ"/>
 *     &lt;enumeration value="NE"/>
 *     &lt;enumeration value="GE"/>
 *     &lt;enumeration value="GT"/>
 *     &lt;enumeration value="LE"/>
 *     &lt;enumeration value="LT"/>
 *     &lt;enumeration value="UL"/>
 *     &lt;enumeration value="LK"/>
 *     &lt;enumeration value="NR"/>
 *     &lt;enumeration value="RG"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlEnum
public enum Ofsml13SelectionOperand {

	EQ, GE, GT, LE, LK, LT, NE, NR, RG, UL;

	public String value() {
		return name();
	}

	public Ofsml13SelectionOperand fromValue(String v) {
		return valueOf(v);
	}

}

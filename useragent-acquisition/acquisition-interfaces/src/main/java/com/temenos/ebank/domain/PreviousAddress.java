package com.temenos.ebank.domain;

// Generated 6 janv. 2011 09:56:01 by Hibernate Tools 3.4.0.Beta1

/**
 * Address generated by hbm2java
 */
public class PreviousAddress extends Address implements java.io.Serializable {

	private static final long serialVersionUID = -8383547227585000671L;

	private Integer duration;
	private Customer customer;

	public PreviousAddress() {
	}

	public PreviousAddress(Integer duration, Customer customer) {
		this.duration = duration;
		this.customer = customer;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Customer getCustomer() {
		return customer;
	}

}

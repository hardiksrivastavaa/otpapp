package com.otpapp.otp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "enquiries")
public class Enquiry {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;

@Column(length=50, nullable=false) 
private String name;

@Column(length=6, nullable=false) 
private String gender;

@Column(length=15, nullable=false) 
private String contactNo;

@Column(length=50, nullable=false) 
private String emailAddress;

@Column(length=1000, nullable=false) 
private String enquiryText;

@Column(length=50, nullable=false) 
private String postedDate;

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getGender() {
	return gender;
}

public void setGender(String gender) {
	this.gender = gender;
}

public String getContactNo() {
	return contactNo;
}

public void setContactNo(String contactNo) {
	this.contactNo = contactNo;
}

public String getEmailAddress() {
	return emailAddress;
}

public void setEmailAddress(String emailAddress) {
	this.emailAddress = emailAddress;
}

public String getEnquiryText() {
	return enquiryText;
}

public void setEnquiryText(String enquiryText) {
	this.enquiryText = enquiryText;
}

public String getPostedDate() {
	return postedDate;
}

public void setPostedDate(String postedDate) {
	this.postedDate = postedDate;
}


}

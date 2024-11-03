package com.otpapp.otp.dto;

public class EnquiryDto {
	private String name;
	private String gender;
	private String contactNo;
	private String emailAddress;
	private String enquiryText;
	private String postedDate;

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

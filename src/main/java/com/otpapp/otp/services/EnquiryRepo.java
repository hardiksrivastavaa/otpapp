package com.otpapp.otp.services;

import org.springframework.data.jpa.repository.JpaRepository;
import com.otpapp.otp.model.Enquiry;

public interface EnquiryRepo extends JpaRepository<Enquiry, Integer> {

}

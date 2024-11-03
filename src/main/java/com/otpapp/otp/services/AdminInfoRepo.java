package com.otpapp.otp.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otpapp.otp.model.AdminInfo;

public interface AdminInfoRepo extends JpaRepository<AdminInfo, String> {

}

package com.otpapp.otp.services;

import org.springframework.data.jpa.repository.JpaRepository;
import com.otpapp.otp.model.StudentInfo;

public interface StudentInfoRepo extends JpaRepository<StudentInfo, String> {

}

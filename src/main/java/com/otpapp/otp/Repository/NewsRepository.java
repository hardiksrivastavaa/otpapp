package com.otpapp.otp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otpapp.otp.model.News;

public interface NewsRepository extends JpaRepository<News, Long> {
}

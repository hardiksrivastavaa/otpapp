package com.otpapp.otp.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.otpapp.otp.model.Response;

public interface ResponseRepo extends JpaRepository<Response, Integer> {

    @Query("SELECT r From Response r where r.responsetype = :responsetype")
    List<Response> FindResponseByResponesType(@Param("responsetype") String string);

}

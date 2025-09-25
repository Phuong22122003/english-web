package com.english.user_service.service;

import com.english.user_service.dto.request.*;
import com.english.user_service.dto.response.VerifyOtpResponse;
import org.springframework.stereotype.Service;

import com.english.user_service.dto.response.IntrospectResponse;
import com.english.user_service.dto.response.UserLoginResponse;

@Service
public interface AuthenticationService {
    public IntrospectResponse validateToken(IntrospectRequest request);
    public UserLoginResponse login(UserLogInRequest request);
    public void sendOtp(ForgotPasswordRequest request);
    public VerifyOtpResponse verifyOtp(VerifyOtpRequest request);
    public void resetPassword(ResetPasswordRequest request);
}

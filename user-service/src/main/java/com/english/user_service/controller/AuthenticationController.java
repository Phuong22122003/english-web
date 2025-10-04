package com.english.user_service.controller;

import com.english.user_service.dto.request.*;
import com.english.dto.ApiResponse;
import com.english.user_service.dto.response.VerifyOtpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.english.user_service.dto.response.IntrospectResponse;
import com.english.user_service.dto.response.UserLoginResponse;
import com.english.user_service.service.AuthenticationService;
import com.english.user_service.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/authenticate")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    AuthenticationService authenticationService;
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserCreationRequest request){
        userService.createUser(request);
        return ResponseEntity.ok().body("User created successfully");
    }

    @PostMapping("/introspect")
    public ResponseEntity<IntrospectResponse> validateToken(@RequestBody IntrospectRequest request){
        return ResponseEntity.ok().body(authenticationService.validateToken(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLogInRequest request){
        return ResponseEntity.ok().body(authenticationService.login(request));
    }

    @PostMapping("/password/otp")
    public ResponseEntity<ApiResponse<String>> sendOtp(@RequestBody  ForgotPasswordRequest forgotPasswordRequest){
        authenticationService.sendOtp(forgotPasswordRequest);
        return ResponseEntity.ok().body(
                ApiResponse.<String>builder()
                        .message("Send otp to email "+ forgotPasswordRequest.getEmail()+" successfully")
                        .build()
        );
    }
    @PostMapping("/password/otp/validation")
    public ResponseEntity<VerifyOtpResponse> verifyOtp(@RequestBody VerifyOtpRequest request){
        return ResponseEntity.ok().body(authenticationService.verifyOtp(request));
    }

    @PostMapping("/password/resets")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody ResetPasswordRequest request){
        authenticationService.resetPassword(request);
        return ResponseEntity.ok().body(
                ApiResponse.<String>builder()
                        .message("Password reset successfully")
                        .build()
        );
    }
}

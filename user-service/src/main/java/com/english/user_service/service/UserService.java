package com.english.user_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.english.user_service.dto.request.UserCreationRequest;
import com.english.user_service.dto.request.UserProfileUpdateRequest;
import com.english.user_service.dto.response.UserResponse;

@Service
public interface UserService {
    public void createUser(UserCreationRequest request);
    public void deleteUserAccount();
    public UserResponse updateUserProfile(UserProfileUpdateRequest request);
    public String updateAvatar(MultipartFile avatar);
    public UserResponse getUserById(String userId);
    public UserResponse getProfile();

}

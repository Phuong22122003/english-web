package com.english.user_service.service.implt;

import com.english.dto.FileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.english.service.FileService;
import com.english.user_service.dto.request.UserCreationRequest;
import com.english.user_service.dto.request.UserProfileUpdateRequest;
import com.english.user_service.dto.response.UserResponse;
import com.english.user_service.entity.User;
import com.english.user_service.enums.UserRole;
import com.english.user_service.mapper.UserMapper;
import com.english.user_service.repository.UserRepository;
import com.english.user_service.service.UserService;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
@Service
@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImplt implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    FileService fileService;


    @Override
    public void createUser(UserCreationRequest request) {
        boolean isExist = userRepository.existsByUsername(request.getUsername());
        if (isExist) {
            throw new RuntimeException("Username is already taken");
        }
        isExist = userRepository.existsByEmail(request.getEmail());
        if (isExist) {
            throw new RuntimeException("Email is already taken");
        }
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .fullname(request.getFullname())
                .role(UserRole.USER)
                .build();
        userRepository.save(user);
    }

    @Override
    public void deleteUserAccount() {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        userRepository.deleteById(userId);
    }

    @Override
    public UserResponse updateUserProfile(UserProfileUpdateRequest request) {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        User user = userRepository.findById(userId).orElseThrow(()->{
            return new RuntimeException("User not found");
        });
        userMapper.updateUserFromDto(request,user);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public String updateAvatar(MultipartFile avatar) {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new RuntimeException("User not found");
        });
        String publicId = user.getPublicId();
        FileResponse fileResponse;
        if(publicId==null){
            fileResponse = fileService.uploadImage(avatar);
        }else{
            fileResponse = fileService.uploadImage(avatar,publicId);
        }
        user.setAvartarUrl(fileResponse.getUrl());
        user.setPublicId(fileResponse.getPublicId());
        userRepository.save(user);
        return fileResponse.getUrl();
    }

    @Override
    public UserResponse getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new RuntimeException("User not found");
        });
        UserResponse response = userMapper.toUserResponse(user);
        return response;
    }

    @Override
    public UserResponse getProfile() {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new RuntimeException("User not found");
        });
        return userMapper.toUserResponse(user);
    }
}

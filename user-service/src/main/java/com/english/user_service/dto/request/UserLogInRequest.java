package com.english.user_service.dto.request;

import lombok.Data;

@Data
public class UserLogInRequest {
    String username;
    String password;
}

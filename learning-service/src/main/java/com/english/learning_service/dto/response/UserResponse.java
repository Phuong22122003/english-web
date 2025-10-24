package com.english.learning_service.dto.response;

import com.english.learning_service.enums.Level;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    Integer target; // target study time per day (minutes)
    Level level; // beginner, intermediate, advanced
    LocalDateTime createdAt; 
}

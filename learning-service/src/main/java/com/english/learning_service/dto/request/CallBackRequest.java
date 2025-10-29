package com.english.learning_service.dto.request;

import lombok.Data;

@Data
public class CallBackRequest extends PlanRequest{
    private String userId;
}

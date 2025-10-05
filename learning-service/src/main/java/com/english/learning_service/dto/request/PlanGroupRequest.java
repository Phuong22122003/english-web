package com.english.learning_service.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanGroupRequest {
    private String name;
    private String description;
    private List<PlanDetailRequest> details;
}

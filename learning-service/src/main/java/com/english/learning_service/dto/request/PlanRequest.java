package com.english.learning_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanRequest {
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer target;
    private List<PlanGroupRequest> planGroups;

}

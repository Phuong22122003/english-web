package com.english.learning_service.dto.response;

import com.english.learning_service.enums.ItemTypeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlanDetailResponse {
    private String id;
    private ItemTypeEnum topicType;
    private String topicId;
    private String topicName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}

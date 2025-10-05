package com.english.learning_service.dto.request;

import com.english.learning_service.enums.ItemTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanDetailRequest {
    private ItemTypeEnum topicType;
    private String topicId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}

package com.english.learning_service.dto.request;

import com.english.learning_service.enums.ItemTypeEnum;
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
public class ExamHistoryRequest {
    private ItemTypeEnum testType;
    private String testId;
    private int score;
    private List<UserAnswerRequest> answers;
    private LocalDateTime takenAt;
    private LocalDateTime submittedAt;
}

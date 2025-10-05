package com.english.learning_service.mapper;

import com.english.learning_service.dto.request.ExamHistoryRequest;
import com.english.learning_service.dto.response.ExamHistoryResponse;
import com.english.learning_service.entity.ExamHistory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExamHistoryMapper {
    public ExamHistory toExamHistory(ExamHistoryRequest request);
    public ExamHistoryResponse toExamHistoryResponse(ExamHistory examHistory);
    public List<ExamHistoryResponse> toExamHistoryResponses(List<ExamHistory> examHistories);
}

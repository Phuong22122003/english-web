package com.english.learning_service.service;

import com.english.learning_service.dto.request.ExamHistoryRequest;
import com.english.learning_service.dto.response.ExamHistoryResponse;
import com.english.learning_service.enums.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface ExamHistoryService {
    public ExamHistoryResponse addExamHistory(ExamHistoryRequest request);
    public Page<ExamHistoryResponse> getExamHistories(int page, int limit, FilterType filterType);
    public ExamHistoryResponse getExamHistoryDetail(String examHistoryId);
}

package com.english.learning_service.service;

import com.english.dto.response.StatisticResponse;
import com.english.enums.TimeRange;

public interface StatisticService {
    StatisticResponse getNumberOfTestIsTaken(TimeRange timeRange);
}

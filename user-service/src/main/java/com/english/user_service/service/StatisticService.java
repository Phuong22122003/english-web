package com.english.user_service.service;

import com.english.dto.response.StatisticResponse;
import com.english.enums.TimeRange;
import org.springframework.stereotype.Service;

@Service
public interface StatisticService {
    StatisticResponse getUsersStatistic(TimeRange  timeRange);
}

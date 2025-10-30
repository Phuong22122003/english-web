package com.english.user_service.controller;

import com.english.dto.response.StatisticResponse;
import com.english.enums.TimeRange;
import com.english.user_service.service.StatisticService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistic")
public class StatisticController {
    StatisticService statisticService;
    @GetMapping("/users")
    public ResponseEntity<StatisticResponse> getUsersStatistic(TimeRange timeRange){
        return  ResponseEntity.ok(statisticService.getUsersStatistic(timeRange));
    }
}

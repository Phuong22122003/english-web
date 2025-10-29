package com.english.dto.response;

import lombok.Data;

import java.util.Map;

@Data
public class StatisticResponse {
    Integer totalCount;
    Map<String, Integer> newElementsByPeriod;
}

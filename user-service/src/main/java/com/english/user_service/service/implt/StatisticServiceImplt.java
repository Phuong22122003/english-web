package com.english.user_service.service.implt;

import com.english.dto.response.StatisticResponse;
import com.english.enums.TimeRange;
import com.english.user_service.entity.User;
import com.english.user_service.repository.UserRepository;
import com.english.user_service.service.StatisticService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticServiceImplt implements StatisticService {

    UserRepository userRepository;

    @Override
    public StatisticResponse getUsersStatistic(TimeRange timeRange) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate;
        LocalDateTime endDate = now;

        // 🧭 1️⃣ Xác định khoảng thời gian bắt đầu
        switch (timeRange) {
            case TODAY -> startDate = now.toLocalDate().atStartOfDay();
            case ONE_WEEK -> startDate = now.minusWeeks(1).toLocalDate().atStartOfDay();
            case ONE_MONTH -> startDate = now.minusMonths(1).toLocalDate().atStartOfDay();
            case TWELVE_MONTHS, ALL -> startDate = now.minusYears(1).toLocalDate().atStartOfDay();
            default -> throw new IllegalArgumentException("Invalid time range: " + timeRange);
        }

        // 🧭 2️⃣ Lấy danh sách user trong khoảng thời gian đó
        List<User> users = userRepository.findByCreatedAtBetween(startDate, endDate);

        // 🧭 3️⃣ Xác định cách group theo timeRange
        Map<String, Integer> groupedData;
        DateTimeFormatter formatter;

        switch (timeRange) {
            case TODAY -> { // group theo giờ
                formatter = DateTimeFormatter.ofPattern("HH:00");
                groupedData = users.stream()
                        .collect(Collectors.groupingBy(
                                u -> u.getCreatedAt().format(formatter),
                                Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                        ));
            }
            case ONE_WEEK, ONE_MONTH -> { // group theo ngày
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                groupedData = users.stream()
                        .collect(Collectors.groupingBy(
                                u -> u.getCreatedAt().toLocalDate().format(formatter),
                                Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                        ));
            }
            case TWELVE_MONTHS, ALL -> { // group theo tháng
                formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                groupedData = users.stream()
                        .collect(Collectors.groupingBy(
                                u -> YearMonth.from(u.getCreatedAt()).format(formatter),
                                Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                        ));
            }
            default -> throw new IllegalArgumentException("Invalid time range: " + timeRange);
        }

        // 🧭 4️⃣ Tổng số người dùng (toàn hệ thống)
        int totalUsers = users.size();

        // 🧭 5️⃣ Trả về DTO
        StatisticResponse response = new StatisticResponse();
        response.setTotalCount(totalUsers);
        response.setNewElementsByPeriod(groupedData);

        return response;
    }
}

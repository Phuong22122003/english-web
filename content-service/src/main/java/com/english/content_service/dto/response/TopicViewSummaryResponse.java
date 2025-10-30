package com.english.content_service.dto.response;

import com.english.enums.TopicType;
import lombok.Data;

@Data
public class TopicViewSummaryResponse {
    String topicId;
    String topicName;
    TopicType topicType;
    int totalViews;
}

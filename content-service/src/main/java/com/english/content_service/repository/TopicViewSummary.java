package com.english.content_service.repository;

import com.english.enums.TopicType;

public interface TopicViewSummary {
    String getTopicId();
    TopicType getTopicType();
    Long getTotalViews();
}

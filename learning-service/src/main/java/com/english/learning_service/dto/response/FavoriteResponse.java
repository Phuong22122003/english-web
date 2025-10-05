package com.english.learning_service.dto.response;

import com.english.dto.response.GrammarTopicResponse;
import com.english.dto.response.ListeningTopicResponse;
import com.english.dto.response.VocabTopicResponse;
import com.english.learning_service.enums.ItemTypeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FavoriteResponse {
    private String id;
    private ItemTypeEnum itemType;
    private LocalDateTime addedAt;
    private GrammarTopicResponse grammarTopic;
    private ListeningTopicResponse listeningTopic;
    private VocabTopicResponse vocabTopic;
}

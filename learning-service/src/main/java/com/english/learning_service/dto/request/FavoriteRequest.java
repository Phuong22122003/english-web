package com.english.learning_service.dto.request;

import com.english.learning_service.enums.ItemTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteRequest {
    private String itemId;
    private ItemTypeEnum itemType;
}

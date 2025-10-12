package com.english.learning_service.mapper;

import com.english.learning_service.dto.request.FavoriteRequest;
import com.english.learning_service.dto.response.FavoriteResponse;
import com.english.learning_service.entity.Favorite;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {
    Favorite toFavorite(FavoriteRequest request);
    FavoriteResponse toFavoriteResponse(Favorite favorite);
    List<FavoriteResponse> toFavoriteResponses(List<Favorite> favorites);

}

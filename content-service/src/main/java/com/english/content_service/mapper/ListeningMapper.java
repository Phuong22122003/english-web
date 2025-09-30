package com.english.content_service.mapper;

import com.english.content_service.dto.request.ListeningRequest;
import com.english.content_service.dto.request.ListeningTopicRequest;
import com.english.content_service.dto.response.ListeningResponse;
import com.english.content_service.dto.response.ListeningTopicResponse;
import com.english.content_service.entity.Listening;
import com.english.content_service.entity.ListeningTopic;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ListeningMapper {

    //topic
    public ListeningTopicResponse toTopicResponse(ListeningTopic listeningTopic);
    public List<ListeningTopicResponse> toTopicResponses(List<ListeningTopic> listeningTopics);
    public ListeningTopic toTopicEntity(ListeningTopicRequest request);

    public List<ListeningResponse> toListeningResponse(List<Listening> listeningList);
    public List<Listening> toListeningEntities(List<ListeningRequest> requests);
}

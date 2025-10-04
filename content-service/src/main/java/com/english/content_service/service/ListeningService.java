package com.english.content_service.service;
import com.english.content_service.dto.request.ListeningRequest;
import com.english.content_service.dto.request.ListeningTestRequest;
import com.english.content_service.dto.request.ListeningTopicRequest;
import com.english.content_service.dto.response.ListeningResponse;
import com.english.content_service.dto.response.ListeningTestReponse;
import com.english.content_service.dto.response.ListeningTopicResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ListeningService {
    //topic
    public Page<ListeningTopicResponse> getTopics(int page, int size);
    public ListeningTopicResponse addTopic(ListeningTopicRequest request, MultipartFile imageFile);

    //listening
    public ListeningTopicResponse getListeningByTopic(String topicId);
    public List<ListeningResponse> addListeningList(String topicId, List<ListeningRequest> requests, List<MultipartFile> imageFiles, List<MultipartFile> audioFiles);

    // test
    public ListeningTopicResponse getTestsByTopic(String topic,int page, int size);
    public ListeningTestReponse addTest(String topicId, ListeningTestRequest request, List<MultipartFile> imageFiles,List<MultipartFile> audioFiles);
    public ListeningTestReponse getTestDetail(String testId);
}

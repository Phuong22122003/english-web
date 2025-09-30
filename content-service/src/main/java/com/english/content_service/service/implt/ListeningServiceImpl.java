package com.english.content_service.service.implt;

import com.english.content_service.dto.request.ListeningRequest;
import com.english.content_service.dto.request.ListeningTopicRequest;
import com.english.content_service.dto.response.GrammarTopicResponse;
import com.english.content_service.dto.response.ListeningResponse;
import com.english.content_service.dto.response.ListeningTopicResponse;
import com.english.content_service.entity.GrammarTopic;
import com.english.content_service.entity.Listening;
import com.english.content_service.entity.ListeningTopic;
import com.english.content_service.mapper.ListeningMapper;
import com.english.content_service.repository.ListeningRepository;
import com.english.content_service.repository.ListeningTopicRepository;
import com.english.service.FileService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.english.content_service.service.ListeningService;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ListeningServiceImpl implements ListeningService {
    ListeningMapper listeningMapper;
    FileService fileService;
    ListeningTopicRepository listeningTopicRepository;
    ListeningRepository listeningRepository;
    @Override
    public Page<ListeningTopicResponse> getTopics(int page, int size) {
        Page<ListeningTopic> topics = listeningTopicRepository.findAll(PageRequest.of(page, size));
        List<ListeningTopic> topicList = topics.getContent();
        List<ListeningTopicResponse> topicResponses = listeningMapper.toTopicResponses(topicList);
        return new PageImpl<>(topicResponses, PageRequest.of(page, size), topics.getTotalElements());
    }

    @Override
    @Transactional
    public ListeningTopicResponse addTopic(ListeningTopicRequest request, MultipartFile imageFile) {
        var fileRespose = fileService.uploadImage(imageFile);
        ListeningTopic topic = listeningMapper.toTopicEntity(request);
        topic.setCreatedAt(LocalDateTime.now());
        topic.setImageUrl(fileRespose.getUrl());
        topic.setPublicId(fileRespose.getPublicId());
        listeningTopicRepository.save(topic);
        return listeningMapper.toTopicResponse(topic);
    }

    @Override
    public List<ListeningResponse> getListeningByTopic(String topicId) {
        listeningTopicRepository.findById(topicId).orElseThrow(()->new RuntimeException("Topic Not found"));
        List<Listening> listeningList = listeningRepository.findByTopicId(topicId);
        return listeningMapper.toListeningResponse(listeningList);
    }

    @Override
    @Transactional
    public List<ListeningResponse> addListeningList(String topicId,
                                                    List<ListeningRequest> requests,
                                                    List<MultipartFile> imageFiles,
                                                    List<MultipartFile> audioFiles) {
        // tìm topic theo id
        ListeningTopic topic = listeningTopicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found"));

        // map request -> entity
        List<Listening> listenings = listeningMapper.toListeningEntities(requests);
        List<String> uploadedPublicIds = new ArrayList<>();

        try {
            for (int i = 0; i < listenings.size(); i++) {
                Listening listening = listenings.get(i);
                listening.setTopic(topic);
                listening.setCreatedAt(LocalDateTime.now());

                // upload image nếu có
                if (imageFiles != null && imageFiles.size() > i && imageFiles.get(i) != null && !imageFiles.get(i).isEmpty()) {
                    var imageResponse = fileService.uploadImage(imageFiles.get(i));
                    listening.setImageUrl(imageResponse.getUrl());
                    listening.setPublicImageId(imageResponse.getPublicId());
                    uploadedPublicIds.add(imageResponse.getPublicId());
                }

                // upload audio nếu có
                if (audioFiles != null && audioFiles.size() > i && audioFiles.get(i) != null && !audioFiles.get(i).isEmpty()) {
                    var audioResponse = fileService.uploadAudio(audioFiles.get(i));
                    listening.setAudioUrl(audioResponse.getUrl());
                    listening.setPublicAudioId(audioResponse.getPublicId());
                    uploadedPublicIds.add(audioResponse.getPublicId());
                }
            }

            // save hết vào db
            listeningRepository.saveAll(listenings);

        } catch (Exception e) {
            // rollback file đã upload
            for (String publicId : uploadedPublicIds) {
                fileService.deleteFile(publicId);
            }
            throw new RuntimeException("Failed to save listening list", e);
        }

        // trả về response
        return listeningMapper.toListeningResponse(listenings);
    }

}

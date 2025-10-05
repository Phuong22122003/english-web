package com.english.content_service.service.implt;

import com.english.content_service.dto.request.ListeningRequest;
import com.english.content_service.dto.request.ListeningTestRequest;
import com.english.content_service.dto.request.ListeningTopicRequest;
import com.english.dto.response.ListeningResponse;
import com.english.dto.response.ListeningTestReponse;
import com.english.dto.response.ListeningTopicResponse;
import com.english.content_service.entity.Listening;
import com.english.content_service.entity.ListeningTest;
import com.english.content_service.entity.ListeningTopic;
import com.english.content_service.mapper.ListeningMapper;
import com.english.content_service.repository.ListeningRepository;
import com.english.content_service.repository.ListeningTestQuestionRepository;
import com.english.content_service.repository.ListeningTestRepository;
import com.english.content_service.repository.ListeningTopicRepository;
import com.english.exception.NotFoundException;
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
    ListeningTestRepository listeningTestRepository;
    ListeningTestQuestionRepository listeningTestQuestionRepository;
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
    public ListeningTopicResponse getListeningByTopic(String topicId) {
        var topic = listeningTopicRepository.findById(topicId).orElseThrow(()->new RuntimeException("Topic Not found"));
        List<Listening> listeningList = listeningRepository.findByTopicId(topicId);
        var listeningListResponses = listeningMapper.toListeningResponse(listeningList);
        var topicReponse = listeningMapper.toTopicResponse(topic);
        topicReponse.setListenings(listeningListResponses);
        return topicReponse;
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

    @Override
    public ListeningTopicResponse getTestsByTopic(String topicId, int page, int size) {
        var topic = listeningTopicRepository.findById(topicId).orElseThrow(()->new RuntimeException("Topic Not found"));
        Page<ListeningTest> listeningTestPage = listeningTestRepository.findTestsByTopicId(topicId, PageRequest.of(page,size));

        var listeningTests = listeningMapper.toTestReponses(listeningTestPage.getContent());
        Page<ListeningTestReponse> testReponses = new PageImpl<>(listeningTests,PageRequest.of(page,size),listeningTestPage.getTotalElements());
        var topicReponse = listeningMapper.toTopicResponse(topic);
        topicReponse.setTests(testReponses);
        return topicReponse;
    }

    @Override
    @Transactional
    public ListeningTestReponse addTest(String topicId, ListeningTestRequest request, List<MultipartFile> imageFiles, List<MultipartFile> audioFiles) {
        // 1. tìm topic
        ListeningTopic topic = listeningTopicRepository.findById(topicId)
                .orElseThrow(() -> new NotFoundException("Topic not found"));

        // 2. tạo test
        ListeningTest test = ListeningTest.builder()
                .topic(topic)
                .name(request.getName())
                .duration(request.getDuration())
                .createdAt(LocalDateTime.now())
                .build();

        test = listeningTestRepository.save(test);

        // 3. map question request -> entity
        var questions = listeningMapper.toTestQuestions(request.getQuestions());
        List<String> uploadedPublicIds = new ArrayList<>();

        try {
            for (int i = 0; i < questions.size(); i++) {
                var q = questions.get(i);
                q.setTest(test);

                // upload image nếu có
                if (imageFiles != null && imageFiles.size() > i && imageFiles.get(i) != null && !imageFiles.get(i).isEmpty()) {
                    var imageResponse = fileService.uploadImage(imageFiles.get(i));
                    q.setImageUrl(imageResponse.getUrl());
                    q.setPublicImageId(imageResponse.getPublicId());
                    uploadedPublicIds.add(imageResponse.getPublicId());
                }

                // upload audio nếu có
                if (audioFiles != null && audioFiles.size() > i && audioFiles.get(i) != null && !audioFiles.get(i).isEmpty()) {
                    var audioResponse = fileService.uploadAudio(audioFiles.get(i));
                    q.setAudioUrl(audioResponse.getUrl());
                    q.setPublicAudioId(audioResponse.getPublicId());
                    uploadedPublicIds.add(audioResponse.getPublicId());
                }
            }

            // 4. lưu toàn bộ question
            // (bạn cần repository riêng cho ListeningTestQuestion, giống VocabularyTestQuestionRepository)
            // giả sử tên repo là listeningTestQuestionRepository
            questions = listeningTestQuestionRepository.saveAll(questions);

        } catch (Exception e) {
            // rollback các file đã upload
            for (String publicId : uploadedPublicIds) {
                fileService.deleteFile(publicId);
            }
            throw new RuntimeException("Failed to save listening test", e);
        }

        // 5. map về response
        ListeningTestReponse response = listeningMapper.toTestReponse(test);
        response.setQuestions(listeningMapper.toTestQuestionResponses(questions));

        return response;
    }

    @Override
    public ListeningTestReponse getTestDetail(String testId) {
        // 1. lấy test từ DB
        ListeningTest test = listeningTestRepository.findById(testId)
                .orElseThrow(() -> new NotFoundException("Test not found"));

        // 2. lấy các câu hỏi theo testId
        var questions = listeningTestQuestionRepository.findByTestId(testId);

        // 3. map sang response
        ListeningTestReponse response = listeningMapper.toTestReponse(test);
        response.setQuestions(listeningMapper.toTestQuestionResponses(questions));

        return response;
    }

    @Override
    public List<ListeningTestReponse> getTestByIds(List<String> ids) {
        List<ListeningTest> tests = listeningTestRepository.findAllById(ids);
        return listeningMapper.toTestReponses(tests);
    }


}

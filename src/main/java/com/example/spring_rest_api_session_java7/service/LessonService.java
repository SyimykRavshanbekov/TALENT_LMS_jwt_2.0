package com.example.spring_rest_api_session_java7.service;

import com.example.spring_rest_api_session_java7.dto.lesson.LessonRequest;
import com.example.spring_rest_api_session_java7.dto.lesson.LessonResponse;
import java.util.List;

public interface LessonService {
    List<LessonResponse> getAllLessons(Long id);

    LessonResponse addLesson(Long id, LessonRequest lessonRequest);

    LessonResponse getLessonById(Long id);

    LessonResponse updateLesson(LessonRequest lessonRequest, Long id);

    LessonResponse deleteLesson(Long id);
}

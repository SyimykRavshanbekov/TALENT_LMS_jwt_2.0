package com.example.spring_rest_api_session_java7.converter.lesson;

import com.example.spring_rest_api_session_java7.dto.lesson.LessonRequest;
import com.example.spring_rest_api_session_java7.entities.Lesson;
import org.springframework.stereotype.Component;


@Component
public class LessonRequestConverter {
    public Lesson createLesson(LessonRequest lessonRequest){
        if (lessonRequest == null){
            return null;
        }

        Lesson lesson = new Lesson();

        lesson.setLessonName(lessonRequest.getLessonName());

        return lesson;
    }


    public void updateLesson(Lesson lesson, LessonRequest lessonRequest){
        if (lessonRequest.getLessonName() != null){
            lesson.setLessonName(lessonRequest.getLessonName());
        }
    }
}

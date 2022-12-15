package com.example.spring_rest_api_session_java7.service;

import com.example.spring_rest_api_session_java7.dto.instructor.InstructorRequest;
import com.example.spring_rest_api_session_java7.dto.instructor.InstructorResponse;

import java.io.IOException;
import java.util.List;

public interface InstructorService {
    List<InstructorResponse> getAllList();

    List<InstructorResponse> getAllInstructor(Long courseId);

    InstructorResponse addInstructor(Long id, InstructorRequest instructor) throws IOException;

    InstructorResponse getInstructorById(Long id);

    InstructorResponse updateInstructor(InstructorRequest instructor, Long id) throws IOException;

    InstructorResponse deleteInstructor(Long id);

    void assignInstructor(Long courseId, Long instructorId) throws IOException;
}

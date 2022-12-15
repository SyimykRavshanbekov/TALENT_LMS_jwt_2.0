package com.example.spring_rest_api_session_java7.converter.instructor;

import com.example.spring_rest_api_session_java7.dto.instructor.InstructorRequest;
import com.example.spring_rest_api_session_java7.entities.Instructor;
import org.springframework.stereotype.Component;


@Component
public class InstructorRequestConverter {
    public Instructor createInstructor(InstructorRequest instructorRequest){
        if (instructorRequest == null){
            return null;
        }

        Instructor instructor = new Instructor();

        instructor.setFirstName(instructorRequest.getFirstName());
        instructor.setLastName(instructorRequest.getLastName());
        instructor.setEmail(instructorRequest.getEmail());
        instructor.setSpecialization(instructorRequest.getSpecialization());
        instructor.setPhoneNumber(instructorRequest.getPhoneNumber());


        return instructor;
    }


    public void updateInstructor(Instructor instructor, InstructorRequest instructorRequest){
        if (instructorRequest.getFirstName() != null){
            instructor.setFirstName(instructorRequest.getFirstName());
        }if (instructorRequest.getLastName() != null){
            instructor.setFirstName(instructorRequest.getFirstName());
        }if (instructorRequest.getEmail() != null){
            instructor.setEmail(instructorRequest.getEmail());
        }if (instructorRequest.getSpecialization() != null){
            instructor.setSpecialization(instructorRequest.getSpecialization());
        }if (instructorRequest.getPhoneNumber() != null){
            instructor.setPhoneNumber(instructorRequest.getPhoneNumber());
        }
    }
}

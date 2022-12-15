package com.example.spring_rest_api_session_java7.converter.student;

import com.example.spring_rest_api_session_java7.dto.student.StudentRequest;
import com.example.spring_rest_api_session_java7.entities.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentRequestConverter {
    public Student createStudent(StudentRequest studentRequest){
        if (studentRequest == null){
            return null;
        }

        Student student = new Student();

        student.setFirstName(studentRequest.getFirstName());
        student.setLastName(studentRequest.getLastName());
        student.setEmail(studentRequest.getEmail());
        student.setPhoneNumber(studentRequest.getPhoneNumber());
        student.setLastName(studentRequest.getLastName());
        student.setStudyFormat(studentRequest.getStudyFormat());

        return student;
    }


    public void updateStudent(Student student, StudentRequest studentRequest){
        if (studentRequest.getFirstName() != null){
            student.setFirstName(studentRequest.getFirstName());
        }if (studentRequest.getLastName() != null){
            student.setFirstName(studentRequest.getFirstName());
        }if (studentRequest.getEmail() != null){
            student.setEmail(studentRequest.getEmail());
        }if (studentRequest.getStudyFormat() != null){
            student.setStudyFormat(studentRequest.getStudyFormat());
        }if (studentRequest.getPhoneNumber() != null){
            student.setPhoneNumber(studentRequest.getPhoneNumber());
        }
    }
}

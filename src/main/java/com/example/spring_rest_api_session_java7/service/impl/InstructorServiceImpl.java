package com.example.spring_rest_api_session_java7.service.impl;

import com.example.spring_rest_api_session_java7.converter.instructor.InstructorRequestConverter;
import com.example.spring_rest_api_session_java7.converter.instructor.InstructorResponseConverter;
import com.example.spring_rest_api_session_java7.dto.instructor.InstructorRequest;
import com.example.spring_rest_api_session_java7.dto.instructor.InstructorResponse;
import com.example.spring_rest_api_session_java7.entities.*;
import com.example.spring_rest_api_session_java7.repository.CourseRepository;
import com.example.spring_rest_api_session_java7.repository.InstructorRepository;
import com.example.spring_rest_api_session_java7.repository.RoleRepository;
import com.example.spring_rest_api_session_java7.repository.UserRepository;
import com.example.spring_rest_api_session_java7.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService {
    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final InstructorRequestConverter instructorRequestConverter;
    private final InstructorResponseConverter instructorResponseConverter;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public List<InstructorResponse> getAllList() {
        return instructorResponseConverter.view(instructorRepository.findAll());
    }

    @Override
    public List<InstructorResponse> getAllInstructor(Long courseId) {
        return instructorResponseConverter.view(instructorRepository.getAllInstructor(courseId));
    }

    @Override
    public InstructorResponse addInstructor(Long id, InstructorRequest instructorRequest) throws IOException {
        for (User user: userRepository.findAll()) {
          if (user.getEmail().equals(instructorRequest.getEmail())){
              throw new IOException("This instructor already exists!");
          }
        }

        User user = new User();
        Role role = roleRepository.findById(2L).get();
        user.setPassword(passwordEncoder.encode(instructorRequest.getPassword()));
        user.setFirstName(instructorRequest.getFirstName());
        user.setEmail(instructorRequest.getEmail());
        user.setRole(role);
        role.getUsers().add(user);

        Instructor instructor = instructorRequestConverter.createInstructor(instructorRequest);
        Course course = courseRepository.getById(id);
        if (course.getGroups()!=null){
            for (Group group : course.getGroups()) {
                for (Student student: group.getStudents()) {
                    instructor.plus();
                }
            }
        }


        validator(instructorRequest.getPhoneNumber().replace(" ", ""), instructorRequest.getLastName().replace(" ", ""), instructorRequest.getFirstName().replace(" ", ""));
        course.addInstructors(instructor);
        instructor.setCourse(course);
        instructor.setUser(user);
        instructorRepository.save(instructor);
        userRepository.save(user);
        roleRepository.save(role);
        return instructorResponseConverter.viewInstructor(instructor);
    }

    @Override
    public InstructorResponse getInstructorById(Long id) {
        Instructor instructor = instructorRepository.getById(id);
        return instructorResponseConverter.viewInstructor(instructor);
    }

    @Override
    public InstructorResponse updateInstructor(InstructorRequest instructorRequest, Long id) throws IOException {
        validator(instructorRequest.getPhoneNumber().replace(" ", ""), instructorRequest.getLastName().replace(" ", ""), instructorRequest.getFirstName().replace(" ", ""));
        Instructor instructor = instructorRepository.getById(id);
        instructorRequestConverter.updateInstructor(instructor, instructorRequest);
        User user = instructor.getUser();
        if (instructorRequest.getEmail() != null)
            user.setEmail(instructorRequest.getEmail());
        if (instructorRequest.getPassword() != null)
            user.setFirstName(instructorRequest.getFirstName());
        if (instructorRequest.getFirstName() != null)
            user.setPassword(instructorRequest.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        instructorRepository.save(instructor);
        userRepository.save(user);
        return instructorResponseConverter.viewInstructor(instructor);
    }

    @Override
    public InstructorResponse deleteInstructor(Long id) {
        Instructor instructor = instructorRepository.findById(id).get();
        Role role = instructor.getUser().getRole();
        role.getUsers().remove(instructor.getUser());
        userRepository.delete(instructor.getUser());
        instructorRepository.delete(instructor);
        return instructorResponseConverter.viewInstructor(instructor);
    }

    @Override
    public void assignInstructor(Long courseId, Long instructorId) throws IOException {
        Instructor instructor = instructorRepository.findById(instructorId).get();
        Course course = courseRepository.findById(courseId).get();
        if (course.getInstructors()!=null){
            for (Instructor g : course.getInstructors()) {
                if (g.getId() == instructorId) {
                    throw new IOException("This instructor already exists!");
                }
            }
        }
        for (Group g:instructor.getCourse().getGroups()) {
            for (Student s:g.getStudents()) {
                instructor.minus();
            }
        }
        for (Group g: course.getGroups()) {
            for (Student s:g.getStudents()) {
                instructor.plus();
            }
        }
        instructor.getCourse().getInstructors().remove(instructor);
        instructor.setCourse(course);
        course.addInstructors(instructor);
        instructorRepository.save(instructor);
        courseRepository.save(course);
    }

    private void validator(String phone, String firstName, String lastName) throws IOException {
        if (firstName.length()>2 && lastName.length()>2) {
            for (Character i : firstName.toCharArray()) {
                if (!Character.isAlphabetic(i)) {
                    throw new IOException("В имени инструктора нельзя вставлять цифры");
                }
            }

            for (Character i : lastName.toCharArray()) {
                if (!Character.isAlphabetic(i)) {
                    throw new IOException("В фамилию инструктора нельзя вставлять цифры");
                }
            }
        } else {
            throw new IOException("В имени или фамилии инструктора должно быть как минимум 3 буквы");
        }

        if (phone.length()==13
                && phone.charAt(0) == '+'
                && phone.charAt(1) == '9'
                && phone.charAt(2) == '9'
                && phone.charAt(3) == '6'){
            int counter = 0;

            for (Character i : phone.toCharArray()) {
                if (counter!=0){
                    if (!Character.isDigit(i)) {
                        throw new IOException("Формат номера не правильный");
                    }
                }
                counter++;
            }
        }else {
            throw new IOException("Формат номера не правильный");
        }
    }
}

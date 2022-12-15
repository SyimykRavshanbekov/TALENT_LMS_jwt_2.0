package com.example.spring_rest_api_session_java7.service.impl;


import com.example.spring_rest_api_session_java7.converter.student.StudentRequestConverter;
import com.example.spring_rest_api_session_java7.converter.student.StudentResponseConverter;
import com.example.spring_rest_api_session_java7.dto.student.StudentRequest;
import com.example.spring_rest_api_session_java7.dto.student.StudentResponse;
import com.example.spring_rest_api_session_java7.entities.Course;
import com.example.spring_rest_api_session_java7.entities.Group;
import com.example.spring_rest_api_session_java7.entities.Instructor;
import com.example.spring_rest_api_session_java7.entities.Student;
import com.example.spring_rest_api_session_java7.repository.GroupRepository;
import com.example.spring_rest_api_session_java7.repository.StudentRepository;
import com.example.spring_rest_api_session_java7.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final StudentRequestConverter studentRequestConverter;
    private final StudentResponseConverter studentResponseConverter;

    @Override
    public List<StudentResponse> getAllListStudent() {
        return studentResponseConverter.view(studentRepository.findAll());
    }

    @Override
    public List<StudentResponse> getAllStudents(Long id) {
        return studentResponseConverter.view(studentRepository.getAllListStudent(id));
    }

    @Override
    public StudentResponse addStudent(Long id, StudentRequest studentRequest) throws IOException {
        List<Student> students = studentRepository.findAll();
        Student student = studentRequestConverter.createStudent(studentRequest);
        validator(student.getPhoneNumber().replace(" ", ""), student.getFirstName().replace(" ", ""), student.getLastName().replace(" ", ""));
        for (Student i : students) {
            if (i.getEmail().equals(studentRequest.getEmail())) {
                throw new IOException("Student with email already exists!");
            }
        }

        Group group = groupRepository.getById( id);
        group.addStudent(student);
        student.setGroups(group);
        for (Course c:student.getGroups().getCourses()) {
            for (Instructor i: c.getInstructors()) {
                i.plus();
            }
        }

        studentRepository.save(student);

        return studentResponseConverter.viewStudent(student);
    }

    @Override
    public StudentResponse getStudentById(Long id) {
        return studentResponseConverter.viewStudent(studentRepository.getById(id));
    }

    @Override
    public StudentResponse updateStudent(StudentRequest studentRequest, Long id) throws IOException {
        validator(studentRequest.getPhoneNumber().replace(" ", ""), studentRequest.getFirstName().replace(" ", ""), studentRequest.getLastName().replace(" ", ""));
        Student student = studentRepository.getById(id);
        studentRequestConverter.updateStudent(student, studentRequest);
        studentRepository.save(student);

        return studentResponseConverter.viewStudent(student);
    }

    @Override
    public StudentResponse deleteStudent(Long id) {
        Student student = studentRepository.getById(id);
        student.getGroups().getCompany().minusStudent();
        for (Course c:student.getGroups().getCourses()) {
            for (Instructor i:c.getInstructors()) {
                i.minus();
            }
        }
        student.setGroups(null);
        studentRepository.delete(student);
        return studentResponseConverter.viewStudent(student);
    }

    @Override
    public void assignStudent(Long groupId, Long studentId) throws IOException {
        Student student = studentRepository.getById(studentId);
        Group group = groupRepository.getById(groupId);

        if (group.getStudents()!=null){
            for (Student g : group.getStudents()) {
                if (g.getId() == studentId) {
                    throw new IOException("This student already exists!");
                }
            }
        }
        for (Course c: student.getGroups().getCourses()) {
            for (Instructor i: c.getInstructors()) {
                i.minus();
            }
        }
        for (Course c: group.getCourses()) {
            for (Instructor i: c.getInstructors()) {
                i.plus();
            }
        }
        student.getGroups().getStudents().remove(student);
        group.assignStudent(student);
        student.setGroups(group);
        studentRepository.save(student);
        groupRepository.save(group);
    }

    private void validator(String phone, String firstName, String lastName) throws IOException {
        if (firstName.length() > 2 && lastName.length() > 2) {
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

        if (phone.length() == 13
                && phone.charAt(0) == '+'
                && phone.charAt(1) == '9'
                && phone.charAt(2) == '9'
                && phone.charAt(3) == '6') {
            int counter = 0;

            for (Character i : phone.toCharArray()) {
                if (counter != 0) {
                    if (!Character.isDigit(i)) {
                        throw new IOException("Формат номера не правильный");
                    }
                }
                counter++;
            }
        } else {
            throw new IOException("Формат номера не правильный");
        }
    }
}

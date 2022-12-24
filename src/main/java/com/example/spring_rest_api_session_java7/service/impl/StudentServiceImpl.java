package com.example.spring_rest_api_session_java7.service.impl;


import com.example.spring_rest_api_session_java7.converter.student.StudentRequestConverter;
import com.example.spring_rest_api_session_java7.converter.student.StudentResponseConverter;
import com.example.spring_rest_api_session_java7.dto.student.StudentRequest;
import com.example.spring_rest_api_session_java7.dto.student.StudentResponse;
import com.example.spring_rest_api_session_java7.entities.*;
import com.example.spring_rest_api_session_java7.repository.GroupRepository;
import com.example.spring_rest_api_session_java7.repository.RoleRepository;
import com.example.spring_rest_api_session_java7.repository.StudentRepository;
import com.example.spring_rest_api_session_java7.repository.UserRepository;
import com.example.spring_rest_api_session_java7.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

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

        User user = new User();
        Role role = roleRepository.findById(3L).get();
        user.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
        user.setFirstName(studentRequest.getFirstName());
        user.setEmail(studentRequest.getEmail());
        user.setRole(role);
        role.getUsers().add(user);
        student.setUser(user);

        studentRepository.save(student);
        userRepository.save(user);
        roleRepository.save(role);
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

        User user = student.getUser();
        if (studentRequest.getEmail() != null)
            user.setEmail(studentRequest.getEmail());
        if (studentRequest.getPassword() != null)
            user.setFirstName(studentRequest.getFirstName());
        if (studentRequest.getFirstName() != null)
            user.setPassword(studentRequest.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        studentRequestConverter.updateStudent(student, studentRequest);
        studentRepository.save(student);
        userRepository.save(user);
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
        Role role = student.getUser().getRole();
        role.getUsers().remove(student.getUser());
        userRepository.delete(student.getUser());
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

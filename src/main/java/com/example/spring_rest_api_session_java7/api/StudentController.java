package com.example.spring_rest_api_session_java7.api;

import com.example.spring_rest_api_session_java7.dto.student.StudentRequest;
import com.example.spring_rest_api_session_java7.dto.student.StudentResponse;
import com.example.spring_rest_api_session_java7.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping("/getAll")
    @PreAuthorize("isAuthenticated()")
    public List<StudentResponse> getAllStudent(){
        return studentService.getAllListStudent();
    }

    @GetMapping("/getAllByGroupId/{groupId}")
    @PreAuthorize("isAuthenticated()")
    public List<StudentResponse> getAllStudent(@PathVariable Long groupId){
        return studentService.getAllStudents(groupId);
    }

    @PostMapping("/save/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Instructor')")
    public StudentResponse saveStudent(@PathVariable Long id, @RequestBody StudentRequest studentRequest) throws IOException {
        return studentService.addStudent(id, studentRequest);
    }

    @GetMapping("/findById/{id}")
    @PreAuthorize("isAuthenticated()")
    public StudentResponse findById(@PathVariable Long id){
        return studentService.getStudentById(id);
    }

    @DeleteMapping("/deleteById/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Instructor')")
    public StudentResponse deleteById(@PathVariable Long id){
        return studentService.deleteStudent(id);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Instructor')")
    public StudentResponse updateStudent(@RequestBody StudentRequest studentRequest, @PathVariable Long id) throws IOException {
        return studentService.updateStudent(studentRequest, id);
    }

    @PostMapping("/assignStudent/{studentId}/{groupId}")
    @PreAuthorize("hasAnyAuthority('Admin', 'Instructor')")
    public void assignStudent(@PathVariable("studentId")Long studentId, @PathVariable("groupId") Long groupId) throws IOException {
        studentService.assignStudent(groupId, studentId);
    }
}

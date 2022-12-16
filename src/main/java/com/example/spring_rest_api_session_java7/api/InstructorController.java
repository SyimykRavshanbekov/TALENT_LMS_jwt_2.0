package com.example.spring_rest_api_session_java7.api;

import com.example.spring_rest_api_session_java7.dto.instructor.InstructorRequest;
import com.example.spring_rest_api_session_java7.dto.instructor.InstructorResponse;
import com.example.spring_rest_api_session_java7.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/instructor")
@RequiredArgsConstructor
public class InstructorController {
    private final InstructorService instructorService;

    @GetMapping("/getAll")
    @PreAuthorize("isAuthenticated()")
    public List<InstructorResponse> getAllInstructor(){
        return instructorService.getAllList();
    }

    @GetMapping("/getAllByCourseId/{courseId}")
    @PreAuthorize("isAuthenticated()")
    public List<InstructorResponse> getAllInstructor(@PathVariable Long courseId){
        return instructorService.getAllInstructor(courseId);
    }

    @PostMapping("/save/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public InstructorResponse saveInstructor(@PathVariable Long id, @RequestBody InstructorRequest instructorRequest) throws IOException {
        return instructorService.addInstructor(id, instructorRequest);
    }

    @GetMapping("/findById/{id}")
    public InstructorResponse findById(@PathVariable Long id){
        return instructorService.getInstructorById(id);
    }

    @DeleteMapping("/deleteById/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public InstructorResponse deleteById(@PathVariable Long id){
        return instructorService.deleteInstructor(id);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public InstructorResponse updateInstructor(@RequestBody InstructorRequest instructorRequest, @PathVariable Long id) throws IOException {
        return instructorService.updateInstructor(instructorRequest, id);
    }

    @PostMapping("/assignInstructor/{instructorId}/{courseId}")
    @PreAuthorize("hasAuthority('Admin')")
    public void assignInstructor(@PathVariable("instructorId")Long instructorId, @PathVariable("courseId") Long courseId) throws IOException {
        instructorService.assignInstructor(courseId, instructorId);
    }
}

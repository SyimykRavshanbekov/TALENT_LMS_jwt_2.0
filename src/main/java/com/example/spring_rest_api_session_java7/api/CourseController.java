package com.example.spring_rest_api_session_java7.api;

import com.example.spring_rest_api_session_java7.dto.course.CourseRequest;
import com.example.spring_rest_api_session_java7.dto.course.CourseResponse;
import com.example.spring_rest_api_session_java7.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/getAll/{id}")
    @PreAuthorize("isAuthenticated()")
    public List<CourseResponse> getAllCourse(@PathVariable Long id){
        return courseService.getAllCourses(id);
    }

    @PostMapping("/save/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public CourseResponse saveCourse(@PathVariable Long id, @RequestBody CourseRequest courseRequest) throws IOException {
        return courseService.addCourse(id, courseRequest);
    }

    @GetMapping("/findById/{id}")
    @PreAuthorize("isAuthenticated()")
    public CourseResponse findById(@PathVariable Long id){
        return courseService.getCourseById(id);
    }

    @DeleteMapping("/deleteById/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public CourseResponse deleteById(@PathVariable Long id){
        return courseService.deleteCourse(id);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public CourseResponse updateCourse(@RequestBody CourseRequest courseRequest, @PathVariable Long id) throws IOException {
        return courseService.updateCourse(id, courseRequest);
    }
}

package com.example.spring_rest_api_session_java7.service.impl;


import com.example.spring_rest_api_session_java7.converter.course.CourseRequestConverter;
import com.example.spring_rest_api_session_java7.converter.course.CourseResponseConverter;
import com.example.spring_rest_api_session_java7.dto.course.CourseRequest;
import com.example.spring_rest_api_session_java7.dto.course.CourseResponse;
import com.example.spring_rest_api_session_java7.entities.Company;
import com.example.spring_rest_api_session_java7.entities.Course;
import com.example.spring_rest_api_session_java7.repository.CompanyRepository;
import com.example.spring_rest_api_session_java7.repository.CourseRepository;
import com.example.spring_rest_api_session_java7.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CompanyRepository companyRepository;
    private final CourseResponseConverter courseResponseConverter;
    private final CourseRequestConverter courseRequestConverter;

//    @Override
//    public List<Course> getAllCourses(Long id) {
//        return  courseRepository.getAllCourses(id);
//    }
//
//    @Override
//    public void addCourse(Long id, Course course) throws IOException {
//        validator(course.getCourseName().replace(" ", ""), course.getDescription().replace(" ", ""), course.getDuration());
//        Company company = companyRepository.getById(id);
//        company.addCourse(course);
//        course.setCompany(company);
//        courseRepository.save(course);
//    }
//
//    @Override
//    public Course getCourseById(Long id) {
//        return courseRepository.getById(id);
//    }
//
//    @Override
//    public void updateCourse(Course course, Long id) throws IOException {
//        validator(course.getCourseName(), course.getDescription(), course.getDuration());
//        Course course1 = courseRepository.getById(id);
//        course1.setCourseName(course.getCourseName());
//        course1.setDuration(course.getDuration());
//        course1.setDescription(course.getDescription());
//        courseRepository.save(course1);
//    }
//
//    @Override
//    public void deleteCourse(Long id) {
//        Course course = courseRepository.getById(id);
//        courseRepository.delete(course);
//    }

    @Override
    public List<CourseResponse> getAllCourses(Long id) {
        return courseResponseConverter.view(courseRepository.getAllCourses(id));
    }

    @Override
    public CourseResponse addCourse(Long id, CourseRequest courseRequest) throws IOException {
        validator(courseRequest.getCourseName().replace(" ", ""), courseRequest.getDescription().replace(" ", ""), courseRequest.getDuration());
        Course course = courseRequestConverter.createCourse(courseRequest);
        Company company = companyRepository.getById(id);
        company.addCourse(course);
        course.setCompany(company);
        courseRepository.save(course);
        return courseResponseConverter.viewCourse(course);
    }

    @Override
    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id).get();
        return courseResponseConverter.viewCourse(course);
    }

    @Override
    public CourseResponse updateCourse(Long id, CourseRequest courseRequest) throws IOException {
        Course course = courseRepository.findById(id).get();
        courseRequestConverter.updateCourse(course, courseRequest);
        courseRepository.save(course);

        return courseResponseConverter.viewCourse(course);
    }

    @Override
    public CourseResponse deleteCourse(Long id) {
        Course course = courseRepository.findById(id).get();
        courseRepository.delete(course);
        return courseResponseConverter.viewCourse(course);
    }

    private void validator(String courseName, String description, int duration) throws IOException {
        if (courseName.length() > 3 && description.length() > 5 && description.length() < 15 && duration > 0 && duration < 24) {
            for (Character i : courseName.toCharArray()) {
                if (!Character.isLetter(i)) {
                    throw new IOException("В название курса нельзя вставлять цифры");
                }
            }
        } else {
            throw new IOException("Form error course registration");
        }
    }
}
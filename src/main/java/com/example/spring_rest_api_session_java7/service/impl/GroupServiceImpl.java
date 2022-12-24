package com.example.spring_rest_api_session_java7.service.impl;


import com.example.spring_rest_api_session_java7.converter.group.GroupRequestConverter;
import com.example.spring_rest_api_session_java7.converter.group.GroupResponseConverter;
import com.example.spring_rest_api_session_java7.dto.group.GroupRequest;
import com.example.spring_rest_api_session_java7.dto.group.GroupResponse;
import com.example.spring_rest_api_session_java7.entities.*;
import com.example.spring_rest_api_session_java7.repository.CompanyRepository;
import com.example.spring_rest_api_session_java7.repository.CourseRepository;
import com.example.spring_rest_api_session_java7.repository.GroupRepository;
import com.example.spring_rest_api_session_java7.repository.StudentRepository;
import com.example.spring_rest_api_session_java7.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final CompanyRepository companyRepository;
    private final StudentRepository studentRepository;
    private final GroupResponseConverter groupResponseConverter;
    private final GroupRequestConverter groupRequestConverter;

    @Override
    public List<GroupResponse> getAllGroup(Long id) {
        return groupResponseConverter.view(groupRepository.getAllGroup(id)) ;
    }

    @Override
    public List<GroupResponse> getAllGroupsByCourseId(Long courseId) {
        return  groupResponseConverter.view(courseRepository.getById(courseId).getGroups());
    }

    @Override
    public GroupResponse addGroupByCourseId(Long id, Long courseId, GroupRequest groupRequest) {
        if (groupRequest.getImage().length()<15){
            groupRequest.setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ6ZKLU-ncmqfKAVjB03AcWFuB2mdVuYGlG85cD-xmF5vf9QwsmYJn8iHMKtzou756N854&usqp=CAU");
        }

        Group group = groupRequestConverter.createGroup(groupRequest);

        Company company = companyRepository.findById(id).get();
        Course course = courseRepository.findById(courseId).get();

        company.addGroup(group);
        group.setCompany(company);
        group.addCourse(course);
        course.addGroup(group);

        groupRepository.save(group);

        return groupResponseConverter.viewGroup(group);
    }

    @Override
    public GroupResponse addGroup(Long id, GroupRequest groupRequest) {
        if (groupRequest.getImage().length()<15){
            groupRequest.setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ6ZKLU-ncmqfKAVjB03AcWFuB2mdVuYGlG85cD-xmF5vf9QwsmYJn8iHMKtzou756N854&usqp=CAU");
        }
        Group group = groupRequestConverter.createGroup(groupRequest);
        Company company = companyRepository.getById(id);
        company.addGroup(group);
        group.setCompany(company);
        groupRepository.save(group);

        return groupResponseConverter.viewGroup(group);
    }

    @Override
    public GroupResponse getGroupById(Long id) {
        Group group =  groupRepository.findById(id).get();
        return groupResponseConverter.viewGroup(group);
    }

    @Override
    public GroupResponse updateGroup(GroupRequest groupRequest, Long id) {
        if (groupRequest.getImage().length()<15){
            groupRequest.setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ6ZKLU-ncmqfKAVjB03AcWFuB2mdVuYGlG85cD-xmF5vf9QwsmYJn8iHMKtzou756N854&usqp=CAU");
        }
        Group group = groupRepository.getById(id);
        groupRequestConverter.updateGroup(group, groupRequest);
        groupRepository.save(group);

        return groupResponseConverter.viewGroup(group);
    }

    @Override
    public GroupResponse deleteGroup(Long id) {
        Group group = groupRepository.getById(id);
        for (Student s: group.getStudents()) {
            group.getCompany().minusStudent();
        }

        for (Course c: group.getCourses()) {
            for (Student student: group.getStudents()) {
                for (Instructor i: c.getInstructors()) {
                    i.minus();
                }
            }
        }

        for (Course c : group.getCourses()) {
            c.getGroups().remove(group);
            group.minusCount();
        }
        studentRepository.deleteAll(group.getStudents());
        group.setCourses(null);
        groupRepository.delete(group);

        return groupResponseConverter.viewGroup(group);
    }

    @Override
    public void assignGroup(Long courseId, Long groupId) throws IOException {
        Group group = groupRepository.getById(groupId);
        Course course = courseRepository.getById(courseId);
        if (course.getGroups()!=null){
            for (Group g : course.getGroups()) {
                if (g.getId() == groupId) {
                    throw new IOException("This group already exists!");
                }
            }
        }

        if (course.getInstructors() != null) {
            for (Instructor i: course.getInstructors()) {
                for (Student s: group.getStudents()) {
                    i.plus();
                }
            }
        }

        group.addCourse(course);
        course.addGroup(group);
        courseRepository.save(course);
        groupRepository.save(group);
    }
}

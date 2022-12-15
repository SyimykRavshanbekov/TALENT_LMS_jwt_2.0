package com.example.spring_rest_api_session_java7.api;

import com.example.spring_rest_api_session_java7.dto.group.GroupRequest;
import com.example.spring_rest_api_session_java7.dto.group.GroupResponse;
import com.example.spring_rest_api_session_java7.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @GetMapping("/getAllByCourseId/{companyId}/{courseId}")
    public List<GroupResponse> getAllGroupsByCourseId(@PathVariable("companyId")Long companyId, @PathVariable("courseId") Long courseId){
        return groupService.getAllGroupsByCourseId(courseId);
    }

    @GetMapping("/getAllByCompanyId/{companyId}")
    public List<GroupResponse> getAllGroupsByCompanyId(@PathVariable("companyId") Long companyId){
        return groupService.getAllGroup(companyId);
    }

    @PostMapping("/addByCompanyId/{companyId}")
    public GroupResponse addGroupByCompanyId(@PathVariable("companyId") Long companyId, @RequestBody GroupRequest groupRequest){
        return groupService.addGroup(companyId, groupRequest);
    }

    @PostMapping("/addByCourseId/{companyId}/{courseId}")
    public GroupResponse addGroupByCourseId(@PathVariable("companyId") Long companyId, @PathVariable("courseId") Long courseId, @RequestBody GroupRequest groupRequest){
        return groupService.addGroupByCourseId(companyId, courseId, groupRequest);
    }

    @GetMapping("/findById/{companyId}")
    public GroupResponse findById(@PathVariable Long companyId){
        return groupService.getGroupById(companyId);
    }

    @DeleteMapping("/delete/{groupId}")
    public GroupResponse deleteGroup(@PathVariable Long groupId){
        return groupService.deleteGroup(groupId);
    }

    @PutMapping("/update/{groupId}")
    public GroupResponse updateGroupByCompanyId(@PathVariable Long groupId, @RequestBody GroupRequest groupRequest){
        return groupService.updateGroup(groupRequest, groupId);
    }
    
    @PostMapping("/assignGroup/{groupId}/{courseId}")
    public void assignGroup(@PathVariable("groupId")Long groupId, @PathVariable("courseId") Long courseId) throws IOException {
        groupService.assignGroup(courseId, groupId);
    }
}

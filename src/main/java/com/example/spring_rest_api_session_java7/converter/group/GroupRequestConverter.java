package com.example.spring_rest_api_session_java7.converter.group;

import com.example.spring_rest_api_session_java7.dto.group.GroupRequest;
import com.example.spring_rest_api_session_java7.entities.Group;
import org.springframework.stereotype.Component;

@Component
public class GroupRequestConverter {
    public Group createGroup(GroupRequest groupRequest){
        if (groupRequest == null){
            return null;
        }

        Group group = new Group();

        group.setGroupName(groupRequest.getGroupName());
        group.setImage(groupRequest.getImage());
        group.setDateOfStart(groupRequest.getDateOfStart());

        return group;
    }


    public void updateGroup(Group group, GroupRequest groupRequest){
        if (groupRequest.getGroupName() != null){
            group.setGroupName(groupRequest.getGroupName());
        }if (groupRequest.getDateOfStart() != null){
           group.setDateOfStart(groupRequest.getDateOfStart());
        }if (groupRequest.getImage() != null){
            group.setImage(groupRequest.getImage());
        }
    }
}

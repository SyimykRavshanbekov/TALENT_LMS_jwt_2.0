package com.example.spring_rest_api_session_java7.converter;

import com.example.spring_rest_api_session_java7.dto.response.LoginResponse;
import com.example.spring_rest_api_session_java7.entities.Role;
import com.example.spring_rest_api_session_java7.entities.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * author: Ulansky
 */
@Component
public class LoginConverter {

    public LoginResponse loginView(String token,
                                   String message,
                                   User user){
         var loginResponse = new LoginResponse();
         if(user != null){
             setAuthorite(loginResponse,user.getRoles());
         }
         loginResponse.setJwtToken(token);
         loginResponse.setMessage(message);
         return loginResponse;
    }
    private void setAuthorite(LoginResponse loginResponse, List<Role> roles){
        Set<String> authorities = new HashSet<>();
        for(Role role : roles){
            authorities.add(role.getRoleName());
        }
        loginResponse.setAuthorities(authorities);
    }
}

package com.example.spring_rest_api_session_java7.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * author: Ulansky
 */
@Getter
@Setter
public class RegisterResponse {
    private String id;
    private String email;
    private String firstName;
}

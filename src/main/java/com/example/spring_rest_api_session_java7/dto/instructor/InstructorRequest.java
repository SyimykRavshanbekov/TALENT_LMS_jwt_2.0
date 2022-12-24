package com.example.spring_rest_api_session_java7.dto.instructor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstructorRequest {
    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String password;

    private String email;

    private String specialization;
}

package com.example.spring_rest_api_session_java7.dto.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequest {
    private String companyName;

    private String locatedCountry;

//    private List<Course> courses;
//
//    private List<Group> groups;
}

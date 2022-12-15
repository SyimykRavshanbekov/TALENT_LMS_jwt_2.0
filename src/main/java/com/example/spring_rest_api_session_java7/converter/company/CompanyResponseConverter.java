package com.example.spring_rest_api_session_java7.converter.company;

import com.example.spring_rest_api_session_java7.dto.company.CompanyResponse;
import com.example.spring_rest_api_session_java7.entities.Company;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CompanyResponseConverter {
    public CompanyResponse viewCompany(Company company){
        if (company==null){
            return null;
        }

        CompanyResponse companyResponse = new CompanyResponse();

        companyResponse.setId(company.getId());
        companyResponse.setCompanyName(company.getCompanyName());
        companyResponse.setLocatedCountry(company.getLocatedCountry());
        companyResponse.setCount(company.getCount());

        return companyResponse;
    }

    public List<CompanyResponse> view(List<Company> companies){
        List<CompanyResponse> companyResponses = new ArrayList<>();

        for (Company company: companies) {
            companyResponses.add(viewCompany(company));
        }

        return  companyResponses;
    }
}

package com.example.spring_rest_api_session_java7.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * author: Ulansky
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    @SequenceGenerator(name = "role_seq", sequenceName = "role_seq", allocationSize = 1)
    private Long id;
    private String roleName;

    @OneToMany(mappedBy = "role", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<User> users = new ArrayList<>();

//    @OneToMany(targetEntity = User.class, mappedBy = "roles", cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH, CascadeType.PERSIST})
//    private List<User> users;
}

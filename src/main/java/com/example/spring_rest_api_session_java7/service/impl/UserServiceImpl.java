package com.example.spring_rest_api_session_java7.service.impl;

import com.example.spring_rest_api_session_java7.dto.request.RegisterRequest;
import com.example.spring_rest_api_session_java7.dto.response.RegisterResponse;
import com.example.spring_rest_api_session_java7.entities.Role;
import com.example.spring_rest_api_session_java7.entities.User;
import com.example.spring_rest_api_session_java7.repository.RoleRepository;
import com.example.spring_rest_api_session_java7.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private User mapToEntity(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setPassword(request.getPassword());
        return user;
    }

    private RegisterResponse mapToResponse(User user) {
        if (user == null) {
            return null;
        }
        RegisterResponse response = new RegisterResponse();
        if (user.getId() != null) {
            response.setId(String.valueOf(user.getId()));
        }
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        return response;
    }

    private List<RegisterResponse> mapToResponseList(List<User> users) {
        List<RegisterResponse> registerResponses = new ArrayList<>();
        for (User user : users) {
            registerResponses.add(mapToResponse(user));
        }

        return registerResponses;
    }


    @PostConstruct
    public void initMethod() {
        if (roleRepository.findAll().size() == 0 && roleRepository.findAll().size() == 0) {
            Role role1 = new Role();
            role1.setRoleName("Admin");

            Role role2 = new Role();
            role2.setRoleName("Instructor");

            Role role3 = new Role();
            role3.setRoleName("Student");

            RegisterRequest request = new RegisterRequest();
            request.setEmail("esen@gmail.com");
            request.setPassword(passwordEncoder.encode("1234"));
            request.setFirstName("Esen");

            User user2 = mapToEntity(request);

            user2.setRole(role1);
            role1.getUsers().add(user2);

            userRepository.save(user2);
            roleRepository.save(role1);
            roleRepository.save(role2);
            roleRepository.save(role3);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("not found email"));
    }

    public List<RegisterResponse> getAllUsers() {
        return mapToResponseList(userRepository.findAll());
    }

    public RegisterResponse getUserById(Long id) {
        return mapToResponse(userRepository.findById(id).get());
    }

    public RegisterResponse updateUser(Long id, RegisterRequest userRequest) throws IOException {
        User user = userRepository.findById(id).get();
        if (user.getRole().getRoleName().equals("Instructor") || user.getRole().getRoleName().equals("Student")){
            throw new IOException("You can update instructor/student in another collention");
        }

        if (userRequest.getEmail() != null)
            user.setEmail(userRequest.getEmail());
        if (userRequest.getPassword() != null)
            user.setFirstName(userRequest.getFirstName());
        if (userRequest.getFirstName() != null)
            user.setPassword(userRequest.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        return mapToResponse(user);
    }

    public RegisterResponse deleteUser(Long id) throws IOException {
        User user = userRepository.findById(id).get();

        if (user.getRole().getRoleName().equals("Admin")) {
            throw new IOException("You can't delete admin");
        }

        userRepository.delete(user);
        return mapToResponse(user);
    }

    public RegisterResponse changeRole(Long roleId, Long userId) throws IOException {
        User user = userRepository.findById(userId).get();
        Role role = roleRepository.findById(roleId).get();

        if (role.getRoleName().equals("Admin")) {
            throw new IOException("only 1 user can be admin");
        }

        if (user.getRole().getRoleName().equals(role.getRoleName())){
            throw new IOException("this user already have this role");
        }

        user.setRole(role);
        role.getUsers().add(user);

        userRepository.save(user);
        roleRepository.save(role);

        return mapToResponse(user);
    }
}

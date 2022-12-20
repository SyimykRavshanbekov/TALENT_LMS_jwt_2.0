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
import java.util.Arrays;
import java.util.List;

/**
 * author: Ulansky
 */

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterResponse create(RegisterRequest request) {
        User user = mapToEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role role = roleRepository.findById(3L).get();
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
        return mapToResponse(user);
    }

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

    private List<RegisterResponse> mapToResponseList(List<User> users){
        List<RegisterResponse> registerResponses = new ArrayList<>();
        for (User user: users) {
            registerResponses.add(mapToResponse(user));
        }

        return registerResponses;
    }


    @PostConstruct
    public void initMethod(){
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

        user2.setRoles(Arrays.asList(role1));
        role1.setUsers(Arrays.asList(user2));

        userRepository.save(user2);
        roleRepository.save(role1);
        roleRepository.save(role2);
        roleRepository.save(role3);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("not found email"));
    }

    public List<RegisterResponse> getAllUsers(){
        return mapToResponseList(userRepository.findAll());
    }

    public RegisterResponse getUserById(Long id){
        return mapToResponse(userRepository.findById(id).get());
    }

    public RegisterResponse updateUser(Long id, RegisterRequest userRequest){
        User user = userRepository.findById(id).get();
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

    public RegisterResponse deleteUser(Long id){
        User user = userRepository.findById(id).get();
        userRepository.delete(user);
        return mapToResponse(user);
    }

    public RegisterResponse changeRole(Long roleId, Long userId) throws IOException {
        User user = userRepository.findById(userId).get();
        Role role = roleRepository.findById(roleId).get();
        if (role.getRoleName().equals("Admin")){
            throw new IOException("only 1 user can be admin");
        }

        for (Role r: user.getRoles()) {
           if (r.getRoleName().equals(role.getRoleName())){
               throw new IOException("This user already have this role");
           }
        }

        user.getRoles().add(role);
        role.getUsers().add(user);

        userRepository.save(user);
        roleRepository.save(role);

        return mapToResponse(user);
    }
}

package com.chuya.userservice.service;

import com.chuya.common.exception.NotFoundException;
import com.chuya.common.model.Department;
import com.chuya.common.model.User;
import com.chuya.userservice.model.UserVO;
import com.chuya.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class UserService {
    public UserService(UserRepository userRepository, @Qualifier("templateWithAuthAndLB") RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @PreAuthorize("hasAuthority('users.write')")
    public User saveUser(User user) {
        log.info("Saving user {} {}", user.getFirstName(), user.getLastName());
        User saved = userRepository.save(user);
        log.info("User {} {} saved", user.getFirstName(), user.getLastName());

        return saved;
    }

    @PreAuthorize("hasAuthority('users.read')")
    public UserVO getUserById(Long id) {
        UserVO userVO = new UserVO();
        User user = userRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException(String.format("User (id: %s) does not exist", id));
        });

        Department department = null;

        try {
            department = restTemplate.getForObject("http://DEPARTMENT-SERVICE/departments/" + user.getDepartmentId(), Department.class);
        } catch (HttpStatusCodeException httpError) {
            log.error("Cannot find department (id: {}) associated with user (id: {})", user.getDepartmentId(), user.getId());
        }

        userVO.setUser(user);
        userVO.setDepartment(department);

        return userVO;
    }
}

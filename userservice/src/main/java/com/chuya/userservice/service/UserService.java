package com.chuya.userservice.service;

import com.chuya.common.exception.NotFoundException;
import com.chuya.common.model.Department;
import com.chuya.common.model.User;
import com.chuya.userservice.model.UserVO;
import com.chuya.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    public User saveUser(User user) {
        log.info("Saving user {} {}", user.getFirstName(), user.getLastName());
        User saved = userRepository.save(user);
        log.info("User {} {} saved", user.getFirstName(), user.getLastName());

        return saved;
    }

    public UserVO getUserById(Long id) {
        UserVO userVO = new UserVO();
        User user = userRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException(String.format("User (id: %s) does not exist", id));
        });

        Department department = null;

        try {
            department = restTemplate.getForObject("http://DEPARTMENT-SERVICE/departments/" + user.getDepartmentId(), Department.class);
        } catch (HttpStatusCodeException httpError) {
            log.error("User (id: {}) is associated with a non existing department (id: {})", user.getId(), user.getDepartmentId());
        }

        userVO.setUser(user);
        userVO.setDepartment(department);

        return userVO;
    }
}

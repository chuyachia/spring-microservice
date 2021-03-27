package com.chuya.userservice.model;

import com.chuya.common.model.Department;
import com.chuya.common.model.User;
import lombok.Data;

@Data
public class UserVO {
    private User user;
    private Department department;
}

package com.chuya.security.model;

import lombok.Data;

@Data
public class GetTokenRequest {
    private String username;
    private String password;
}

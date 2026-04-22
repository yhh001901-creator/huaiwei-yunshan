package com.restaurant.dto;

import lombok.Data;

@Data
public class LoginVO {
    private Long userId;
    private String username;
    private String phone;
    private Integer role;
    private String roleName;
    private String token;
}

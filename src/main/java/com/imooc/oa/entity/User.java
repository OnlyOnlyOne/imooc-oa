package com.imooc.oa.entity;

import lombok.Data;

@Data
public class User {
    private Long userId;
    private String username;
    private String password;
    private Long employeeId;
    private Integer salt;
}

package com.imooc.oa.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Notice {
    private Long noticeId;

    private Long receiverId;

    private String content;

    private Date createTime;
}
package com.poword.model;

import lombok.Data;

import java.util.Date;

@Data
public class UserWordInfoModel {
    private int id;               // 主键ID
    private boolean highlighted;  // 是否被划线
    private boolean favorited;    // 是否被收藏
    private String annotation;    // 用户批注
    private Date updateTime;      // 更新时间
    private Date createTime;      // 创建时间
}

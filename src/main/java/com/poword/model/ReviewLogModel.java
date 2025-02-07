package com.poword.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewLogModel {
    private Long reviewId;
    private Integer wordId;  // 外键，引用 dict 表的 id 列
    private Integer result;  // 记录答题结果

    // 可以根据需要定义更多字段，比如创建时间等
    private LocalDateTime createTime;  // 用于记录创建时间，假设你需要该字段
}

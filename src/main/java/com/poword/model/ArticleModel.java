package com.poword.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleModel {
    private Long id;  // 对应数据库中的 id 字段
    private String title;  // 对应数据库中的 title 字段
    private LocalDateTime generatedTime;  // 对应数据库中的 generatedtime 字段
    private String articleInChinese;  // 对应数据库中的 articleinchinese 字段
    private String articleInEnglish;  // 对应数据库中的 articleinenglish 字段
    private String insertedWord;  // 对应数据库中的 insertedword 字段
}

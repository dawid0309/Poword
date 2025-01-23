package com.poword.model;

import lombok.Data;
import java.util.List;

@Data
public class WordModel extends WordBaseModel{
    private String word; // 单词名称
    private List<String> definition; // 单词释义（英文），每行一个释义
    private List<String> translation; // 单词释义（中文），每行一个释义
    private String tag; // 标签，空格分割
}
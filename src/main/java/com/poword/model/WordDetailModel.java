package com.poword.model;

import lombok.Data;
import java.util.List;

@Data
public class WordDetailModel extends WordBaseModel {
    private String phonetic; // 音标，以英语英标为主
    private List<String> definition; // 单词释义（英文），每行一个释义
    private List<String> translation; // 单词释义（中文），每行一个释义
    private String pos; // 词语位置，用 "/" 分割不同位置
    private String tag; // 标签，空格分割
    private String exchange; // 时态复数等变换，使用 "/" 分割不同项目
    private String syno; // 同义词
    private String star; // 星标
}

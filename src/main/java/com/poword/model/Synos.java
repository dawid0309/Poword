package com.poword.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Synos {
    private int id;
    private String word;
    private String synos;
    private Date updatetime;
    private Date createdtime;
    private String source;
}

package com.poword.model.request;

import com.poword.model.UserBackground;
import lombok.Data;

@Data
public class NewsDTO {
    private String title;
    private String rawArticle;
    private String optimizedArticle;
    private String articleEnglish;
    private UserBackground userBackground;
    private String[] wordsToInsert;
    private String[] wordsInserted;


    //benchmark
    private boolean highQuality;
    private boolean needRetranslate;
    private boolean needReinsert;

    private String advice;
}

package com.poword.service;

import com.poword.dao.QueryLogDao;
import com.poword.dao.StarDictDao;
import com.poword.model.WordBaseModel;
import lombok.Data;
import lombok.Getter;

public class WordQueryService {

    @Getter
    private static WordQueryService instance;
    StarDictDao starDictDao = StarDictDao.getInstance();
    QueryLogDao queryLogDao = QueryLogDao.getInstance();

    public WordBaseModel queryWordDetailByWord(String word){
        WordBaseModel wordBaseModel = starDictDao.query(word, true);
        queryLogDao.insertQueryLog(Integer.getInteger(wordBaseModel.getId()));
        return wordBaseModel;
    }

    public WordBaseModel queryWordByWord(String word){
        WordBaseModel wordBaseModel = starDictDao.query(word, false);
        queryLogDao.insertQueryLog(Integer.getInteger(wordBaseModel.getId()));
        return wordBaseModel;
    }
}

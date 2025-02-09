package com.poword.service;

import com.poword.dao.QueryLogDao;
import com.poword.dao.DictDao;
import com.poword.model.WordBaseModel;
import com.poword.model.WordDetailModel;
import com.poword.model.WordModel;
import lombok.Data;
import lombok.Getter;

public class WordQueryService {

    @Getter
    private static WordQueryService instance = new WordQueryService();
    DictDao dictDao = DictDao.getInstance();
    QueryLogDao queryLogDao = QueryLogDao.getInstance();

    public WordDetailModel queryWordDetailByWord(String word){
        WordBaseModel wordBaseModel = dictDao.query(word, true);
        queryLogDao.insertQueryLog(Integer.parseInt(wordBaseModel.getId()));
        return (WordDetailModel) wordBaseModel;
    }

    public WordModel queryWordByWord(String word){
        WordBaseModel wordBaseModel = dictDao.query(word, false);
        queryLogDao.insertQueryLog(Integer.getInteger(wordBaseModel.getId()));
        return (WordModel) wordBaseModel;
    }
}

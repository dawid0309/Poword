package com.poword.service;

import com.poword.dao.QueryLogDao;
import com.poword.dao.DictDao;
import com.poword.dao.UserWordInfoDao;
import com.poword.model.UserWordInfoModel;
import com.poword.model.WordBaseModel;
import com.poword.model.WordDetailModel;
import com.poword.model.WordModel;
import lombok.Getter;
import java.sql.*;
import java.util.Date;

public class WordQueryService {

    @Getter
    private static WordQueryService instance = new WordQueryService();
    DictDao dictDao = DictDao.getInstance();
    QueryLogDao queryLogDao = QueryLogDao.getInstance();
    UserWordInfoDao userWordInfoDao = UserWordInfoDao.getInstance();

    public WordDetailModel queryDetailByWord(String word){
        WordBaseModel wordBaseModel = dictDao.query(word, true);
        if(wordBaseModel == null){
            System.out.printf("the word:%s is not found%n", word);
            return null;
        }

        SetUserWordInfo(wordBaseModel);

        queryLogDao.insertQueryLog(wordBaseModel.getId());

        return (WordDetailModel) wordBaseModel;
    }

    private void SetUserWordInfo(WordBaseModel wordBaseModel) {
        Timestamp currentTime = new Timestamp(new Date().getTime());
        UserWordInfoModel userWordInfoModel = new UserWordInfoModel();
        userWordInfoModel.setId(wordBaseModel.getId());
        userWordInfoModel.setHighlighted(true);
        userWordInfoModel.setCreateTime(currentTime);
        userWordInfoModel.setUpdateTime(currentTime);
        userWordInfoDao.insertUserWordInfo(userWordInfoModel);
    }

    public WordModel queryByWord(String word){
        WordBaseModel wordBaseModel = dictDao.query(word, false);
        if(wordBaseModel == null){
            System.out.printf("the word:%s is not found%n", word);
            return null;
        }
        queryLogDao.insertQueryLog(wordBaseModel.getId());
        return (WordModel) wordBaseModel;
    }
}

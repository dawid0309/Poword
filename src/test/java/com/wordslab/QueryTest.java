package com.wordslab;

import com.poword.dao.DictDao;
import com.poword.model.WordBaseModel;
import com.poword.service.WordQueryService;
import org.junit.jupiter.api.Test;

import com.poword.dao.DictDao;
import com.poword.model.WordDetailModel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

/**
 * Unit test for simple App.
 */
class QueryTest {
    /**
     * Rigorous Test.
     */
    DictDao starDict = DictDao.getInstance();
    WordQueryService wordQueryService = WordQueryService.getInstance();

    @Test
    void QueryTest() {
        WordDetailModel result = wordQueryService.queryWordDetailByWord("apple");
        System.out.println(result);
    }

    @Test
    void MatchTest() {
        List<WordBaseModel> matchedWord = starDict.match("pierce", 10, true,true);
        matchedWord.forEach(System.out::println);
    }
}

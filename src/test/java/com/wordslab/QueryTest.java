package com.wordslab;

import com.poword.dao.DictDao;
import com.poword.model.WordBaseModel;
import com.poword.model.WordModel;
import com.poword.service.WordQueryService;
import org.junit.jupiter.api.Test;

import com.poword.model.WordDetailModel;

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
    void QueryDetailTest() {
        WordDetailModel result = wordQueryService.queryDetailByWord("wolf");
        System.out.println(result);
    }

    @Test
    void QueryTest() {
        WordModel result = wordQueryService.queryByWord("wolf");
        System.out.println(result);
    }

    @Test
    void MatchTest() {
        List<WordBaseModel> matchedWord = starDict.match("pierce", 10, true,true);
        matchedWord.forEach(System.out::println);
    }
}

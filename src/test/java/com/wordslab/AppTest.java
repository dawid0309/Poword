package com.wordslab;

import com.poword.model.WordBaseModel;
import org.junit.jupiter.api.Test;

import com.poword.dao.StarDictDao;
import com.poword.model.WordDetailModel;
import com.poword.model.WordModel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

/**
 * Unit test for simple App.
 */
class AppTest {
    /**
     * Rigorous Test.
     */
    StarDictDao starDict = new StarDictDao();

    @Test
    void QueryTest() {
        int count = starDict.count();
        WordDetailModel result = (WordDetailModel) starDict.query(10,false);
        System.out.println(count);
        System.out.println(result);
    }

    @Test
    void MatchTest() {
        List<WordBaseModel> matchedWord = starDict.match("pierce", 10, true,true);
        matchedWord.forEach(System.out::println);
    }
}

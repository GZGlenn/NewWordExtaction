package com.glenn.driver;

import com.glenn.action.NewsActionImpl;
import com.glenn.action.WordActionImpl;
import com.glenn.entity.NewsEntity;
import com.glenn.entity.WordEntity;
import com.glenn.util.DateUtil;
import com.glenn.word.NewsWordExtractorImpl;
import com.glenn.word.Word;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class NewWordExtraction {

    public static void main(String[] args) {
        int ds = Integer.valueOf(DateUtil.format(DateUtil.parseDate(DateUtil.today()), "yyyyMMdd"));
        int newsNum = 100000;
        NewsActionImpl newsAction = new NewsActionImpl();
        List<NewsEntity> newsList = newsAction.getNewsWithLimit(newsNum);

        ArrayList<String> contents = new ArrayList<>();
        for (NewsEntity entity : newsList) {
            contents.add(entity.getContent());
        }

        NewsWordExtractorImpl wordExtractor = new NewsWordExtractorImpl();

        wordExtractor.setFreeDegreeThre(3);
        wordExtractor.setFrezzDegreeThre(1000);
        HashSet<Word> newWords = wordExtractor.coreExtraction(contents);
        ArrayList<WordEntity> newWordEntityList = new ArrayList<>();
        for (Word word : newWords) {
            WordEntity wordEntity = new WordEntity();
            wordEntity.setDs(ds);
            wordEntity.setFrequency(word.getNum());
            wordEntity.setWord(word.getWord());
            newWordEntityList.add(wordEntity);
        }

        WordActionImpl wordAction = new WordActionImpl();
        wordAction.saveOrUpdate(newWordEntityList);
    }
}

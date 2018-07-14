package com.glenn.test;

import com.glenn.action.NewsActionImpl;
import com.glenn.entity.NewsEntity;
import com.glenn.word.NewsWordExtractorImpl;
import com.glenn.word.Word;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TestNewWordExtractor {

    public static void main(String[] args) {
        int newsNum = 100000;
        NewsActionImpl newsAction = new NewsActionImpl();
        List<NewsEntity> newsList = newsAction.getNewsWithLimit(newsNum);

        ArrayList<String> contents = new ArrayList<>();
        for (NewsEntity entity : newsList) {
            contents.add(entity.getContent());
        }

        NewsWordExtractorImpl wordExtractor = new NewsWordExtractorImpl();

        wordExtractor.setFreeDegreeThre(3);
//        wordExtractor.setFrezzDegreeThre(1000);
        HashSet<Word> newWords = wordExtractor.coreExtraction(contents);

        for (Word word : newWords) {
            System.out.println(word.showDetail());
        }
        System.out.println(newWords.size());
    }

}

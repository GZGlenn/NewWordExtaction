package com.glenn.test;

import com.glenn.util.WikiPediaUtil;
import com.glenn.word.NewsWordExtractorImpl;
import com.glenn.word.Word;

import java.util.ArrayList;
import java.util.HashSet;

public class TestNewWordExtractorOnWikiPedia {

    public static void main(String[] args) {
        String root = "/home/public/dataset/nlp/GNLP/words/source/extracted/AA";
        ArrayList<String> contents = WikiPediaUtil.read(root, 100000);

        NewsWordExtractorImpl wordExtractor = new NewsWordExtractorImpl();

        wordExtractor.setFreeDegreeThre(3);
        wordExtractor.setFrezzDegreeThre(1000);
        HashSet<Word> newWords = wordExtractor.coreExtraction(contents);

        for (Word word : newWords) {
            System.out.println(word.showDetail());
        }
        System.out.println(newWords.size());
    }

}

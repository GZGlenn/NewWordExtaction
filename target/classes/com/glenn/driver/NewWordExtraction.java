package com.glenn.driver;

import com.glenn.action.NewsActionImpl;
import com.glenn.action.WordActionImpl;
import com.glenn.entity.NewsEntity;
import com.glenn.entity.WordEntity;
import com.glenn.util.DateUtil;
import com.glenn.util.FileUtil;
import com.glenn.word.NewsWordExtractorImpl;
import com.glenn.word.Word;
import sun.reflect.misc.FieldUtil;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class NewWordExtraction {

    public static void main(String[] args) {
        if (args.length <= 0) {
            throw new IllegalArgumentException("should input new word save path");
        }

        int ds = Integer.valueOf(DateUtil.format(DateUtil.parseDate(DateUtil.today()), "yyyyMMdd"));
        int newsNum = 150000;
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

        List<WordEntity> latestWordList = wordAction.getWordListFromLastDs();
        String path = args[0];
        FileUtil.deleteFile(path);
        FileWriter fw = FileUtil.createFileWriter(path);
        for (WordEntity word : latestWordList) {
            String string = word.getWord() + '\t' + word.getFrequency() + '\n';
            FileUtil.append(fw, string);
        }
        FileUtil.close(fw);
    }
}

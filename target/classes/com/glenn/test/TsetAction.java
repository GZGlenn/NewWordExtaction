package com.glenn.test;

import com.glenn.action.WordActionImpl;
import com.glenn.entity.WordEntity;
import com.glenn.word.Word;

import java.util.ArrayList;

public class TsetAction {

    public static void main(String[] args) {
        testAccumulateSave();
        testAccumulateSaveList();
        testDeleteDue();
    }

    public static void testAccumulateSave() {
        WordEntity wordEntity = new WordEntity();
        wordEntity.setWord("aaa");
        wordEntity.setDs(20180501);
        wordEntity.setFrequency(10);

        WordActionImpl action = new WordActionImpl();
        action.accumulateSaveOrUpdate(wordEntity);

        WordEntity wordEntity1 = new WordEntity();
        wordEntity1.setWord("aaa");
        wordEntity1.setFrequency(123);
        wordEntity1.setDs(20180502);

        action.accumulateSaveOrUpdate(wordEntity1);
    }

    public static void testAccumulateSaveList() {
        WordActionImpl action = new WordActionImpl();
        ArrayList<WordEntity> wordList = new ArrayList<>();
        for (int i = 0 ; i < 10 ; i++) {
            WordEntity wordEntity = new WordEntity();
            wordEntity.setWord("i_" + i);
            wordEntity.setDs(20180501);
            wordEntity.setFrequency(i);
            wordList.add(wordEntity);
        }
        action.accumulateSaveOrUpdate(wordList);

        ArrayList<WordEntity> wordEntityArrayList = new ArrayList<>();
        for (int i = 0 ; i < 10 ; i++) {
            WordEntity wordEntity = new WordEntity();
            wordEntity.setWord("i_" + i);
            wordEntity.setDs(20180502);
            wordEntity.setFrequency(i);
            wordList.add(wordEntity);
            wordEntityArrayList.add(wordEntity);
        }

        action.accumulateSaveOrUpdate(wordEntityArrayList);
    }

    public static void testDeleteDue() {
        WordEntity wordEntity = new WordEntity();
        wordEntity.setWord("xx");
        wordEntity.setDs(20170101);
        wordEntity.setFrequency(1);

        WordActionImpl action = new WordActionImpl();
        action.accumulateSaveOrUpdate(wordEntity);

        wordEntity.setDs(20170103);
        action.accumulateSaveOrUpdate(wordEntity);
        wordEntity.setDs(20170105);
        action.accumulateSaveOrUpdate(wordEntity);

        action.deleteDue();
    }
}

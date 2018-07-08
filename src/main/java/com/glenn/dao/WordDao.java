package com.glenn.dao;

import com.glenn.entity.WordEntity;

import java.util.List;

public interface WordDao {

    public boolean addOrUpdate(WordEntity word);

    public boolean isExists(WordEntity word);

    public boolean delete(WordEntity word);

    public boolean delete(int ds);

    public double getTotalNum();

    public double getTotalNum(int ds);

    public WordEntity getWord(String word);

    public List<WordEntity> getWordListByDs(int ds);

    public List<WordEntity> getLastDayWords();

    public List<Integer> getDsList();
}

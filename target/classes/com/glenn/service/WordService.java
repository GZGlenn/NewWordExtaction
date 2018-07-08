package com.glenn.service;

import com.glenn.entity.WordEntity;
import com.glenn.word.Word;

import java.util.ArrayList;
import java.util.List;

interface WordService {

    public boolean isExists(WordEntity word);

    public boolean saveOrUpdate(WordEntity word);

    public boolean saveOrUpdate(ArrayList<WordEntity> wordList);

    public boolean accumulateSaveOrUpdate(WordEntity word);

    public boolean accumulateSaveOrUpdate(ArrayList<WordEntity> wordList);

    public boolean delete(WordEntity word);

    public boolean delete(int ds);

    public boolean delete(ArrayList<WordEntity> wordList);

    public double getNum(int ds);

    public double getNum();

    public List<Integer> getDsList();
}

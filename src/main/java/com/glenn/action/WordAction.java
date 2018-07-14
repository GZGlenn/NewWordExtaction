package com.glenn.action;

import com.glenn.entity.WordEntity;

import java.util.ArrayList;
import java.util.List;

public interface WordAction {

    public boolean saveOrUpdate(WordEntity word);

    public boolean saveOrUpdate(ArrayList<WordEntity> wordList);

    public boolean accumulateSaveOrUpdate(WordEntity wordEntity);

    public boolean accumulateSaveOrUpdate(ArrayList<WordEntity> wordList);

    public boolean delete(WordEntity word);

    public boolean deleteDue();

    public List<WordEntity> getWordListFromLastDs();

}

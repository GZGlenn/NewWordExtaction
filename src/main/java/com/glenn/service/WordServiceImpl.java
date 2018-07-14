package com.glenn.service;

import com.glenn.dao.WordDao;
import com.glenn.entity.WordEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("wordService")
public class WordServiceImpl implements WordService {

    //自动注入userDao，也可以使用@Autowired
    @Resource
    private WordDao wordDao;

    @Override
    public boolean isExists(WordEntity word) {
        return this.wordDao.isExists(word);
    }

    @Override
    public boolean saveOrUpdate(WordEntity word) {
        return this.wordDao.addOrUpdate(word);
    }

    @Override
    public boolean saveOrUpdate(ArrayList<WordEntity> wordList) {
        for (WordEntity word : wordList) {
            this.wordDao.addOrUpdate(word);
        }
        return true;
    }

    @Override
    public boolean accumulateSaveOrUpdate(WordEntity word) {
        List<Integer> dsList = this.wordDao.getDsList();
        int ds = -1;
        for (int i = 0 ; i < dsList.size(); i++) {
            if (dsList.get(i) < word.getDs()) {
                ds = dsList.get(i);
                break;
            }
        }

        if (ds == -1) {
            saveOrUpdate(word);
        }
        else {
            List<WordEntity> wordList = this.wordDao.getWordListByDs(ds);
            for (WordEntity wordEntity : wordList) {
                if (wordEntity.getWord().equals(word.getWord())) {
                    word.setFrequency(word.getFrequency() + wordEntity.getFrequency());
                    break;
                }
            }
            saveOrUpdate(word);
        }

        return false;
    }

    @Override
    public boolean accumulateSaveOrUpdate(ArrayList<WordEntity> wordList) {
        List<Integer> dsList = this.wordDao.getDsList();
        int ds = -1;
        for (int i = 0 ; i < dsList.size(); i++) {
            if (dsList.get(i) < wordList.get(0).getDs()) {
                ds = dsList.get(i);
                break;
            }
        }

        if (ds == -1) {
            for (WordEntity word : wordList) {
                saveOrUpdate(word);
            }
        }
        else {
            List<WordEntity> oldWordList = this.wordDao.getWordListByDs(ds);
            HashMap<String, WordEntity> oldWordMap = new HashMap<>();
            for (WordEntity word: oldWordList) {
                oldWordMap.put(word.getWord(), word);
            }
            for (WordEntity word : wordList) {
                if (oldWordMap.containsKey(word.getWord())) {
                    word.setFrequency(word.getFrequency() + oldWordMap.get(word.getWord()).getFrequency());
                }
                saveOrUpdate(word);
            }
        }

        return true;
    }

    @Override
    public boolean delete(WordEntity word) {
        return this.wordDao.delete(word);
    }

    @Override
    public boolean delete(int ds) {
        return this.wordDao.delete(ds);
    }

    @Override
    public boolean delete(ArrayList<WordEntity> wordList) {
        for (WordEntity word : wordList) {
            this.wordDao.delete(word);
        }
        return true;
    }

    @Override
    public double getNum(int ds) {
        return this.wordDao.getTotalNum(ds);
    }

    @Override
    public double getNum() {
        return this.wordDao.getTotalNum();
    }

    @Override
    public List<Integer> getDsList() {
        return this.wordDao.getDsList();
    }

    @Override
    public List<WordEntity> getWordListFromLastDs() {
        return this.wordDao.getLastDayWords();
    }
}

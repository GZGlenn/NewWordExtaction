package com.glenn.action;

import com.glenn.entity.WordEntity;
import com.glenn.service.WordServiceImpl;
import com.glenn.util.DateUtil;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WordActionImpl implements WordAction {

    public final int DUEMONTH = 2;
    public final double MAXWORDNUM = 2E8;

    public WordServiceImpl wordService;

    public WordActionImpl() {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"./applicationContext.xml"});
        BeanFactory factory = (BeanFactory) context;
        wordService = (WordServiceImpl) factory.getBean("wordService");
    }

    @Override
    public boolean saveOrUpdate(WordEntity word) {
        return this.wordService.saveOrUpdate(word);
    }

    @Override
    public boolean saveOrUpdate(ArrayList<WordEntity> wordList) {
        return this.wordService.saveOrUpdate(wordList);
    }

    @Override
    public boolean accumulateSaveOrUpdate(WordEntity wordEntity) {
        return this.wordService.accumulateSaveOrUpdate(wordEntity);
    }

    @Override
    public boolean accumulateSaveOrUpdate(ArrayList<WordEntity> wordList) {
        return this.wordService.accumulateSaveOrUpdate(wordList);
    }

    @Override
    public boolean delete(WordEntity word) {
        return this.wordService.delete(word);
    }

    @Override
    public boolean deleteDue() {

        double totalNum = this.wordService.getNum();
        List<Integer> dsList = this.wordService.getDsList();
        if (dsList.isEmpty()) return true;

        int index;
        for (index = dsList.size() - 1; totalNum > MAXWORDNUM && index > 0; index--) {
            this.wordService.delete(dsList.get(index));
        }

        Date today = DateUtil.parseDate(DateUtil.today());
        Date dueDay = DateUtil.offsiteDate(today, Calendar.MONTH, -DUEMONTH);
        int dueDs = Integer.valueOf(DateUtil.format(dueDay, "yyyyMMdd"));

        for (int i = index ; i > 0 && dsList.get(i) < dueDs; i--) {
            this.wordService.delete(dsList.get(i));
        }

        return true;
    }

    @Override
    public List<WordEntity> getWordListFromLastDs() {
        return wordService.getWordListFromLastDs();
    }

}

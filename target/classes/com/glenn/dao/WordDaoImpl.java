package com.glenn.dao;

import com.glenn.entity.WordEntity;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Transactional(rollbackFor = Exception.class)
//出现Exception异常回滚
@Repository("wordDao") //进行注入
public class WordDaoImpl implements WordDao {

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public boolean addOrUpdate(WordEntity word) {
        sessionFactory.getCurrentSession().saveOrUpdate(word);
        return true;
    }

    @Override
    public boolean isExists(WordEntity word) {
        String hsql = "FROM WordEntity w where w.ds=? and w.word=?";
        Query query = sessionFactory.getCurrentSession().createQuery(hsql);
        query.setInteger(0, word.getDs());
        query.setString(1, word.getWord());
        return query.uniqueResult() != null;
    }

    @Override
    public boolean delete(WordEntity word) {
        String hsql = "DELETE FROM WordEntity w where w.ds=? and w.word=?";
        Query query = sessionFactory.getCurrentSession().createQuery(hsql);
        query.setInteger(0, word.getDs());
        query.setString(1, word.getWord());
        query.executeUpdate();
        return true;
    }

    @Override
    public boolean delete(int ds) {
        String hsql = "DELETE FROM WordEntity w where w.ds=?";
        Query query = sessionFactory.getCurrentSession().createQuery(hsql);
        query.setInteger(0, ds);
        query.executeUpdate();
        return true;
    }

    @Override
    public double getTotalNum() {
        String hsql = "SELECT COUNT(*) FROM WordEntity w";
        Query query = sessionFactory.getCurrentSession().createQuery(hsql);
        double totalNum = Double.valueOf(query.uniqueResult().toString());
        return totalNum;
    }

    @Override
    public double getTotalNum(int ds) {
        String hsql = "SELECT COUNT(1) FROM WordEntity w where w.ds=?";
        Query query = sessionFactory.getCurrentSession().createQuery(hsql);
        query.setInteger(0, ds);
        double totalNum = Double.valueOf(query.uniqueResult().toString());
        return totalNum;
    }

    @Override
    public WordEntity getWord(String word) {
        String hsql = "FROM WordEntity w where w.word=?";
        Query query = sessionFactory.getCurrentSession().createQuery(hsql);
        query.setString(0, word);
        List<WordEntity> result = query.list();
        if (result.isEmpty()) return null;
        int maxDs = result.get(0).getDs();
        int maxIdx = 0;
        for (int i = 1; i < result.size(); i++) {
            WordEntity wordEntity = result.get(i);
            if (wordEntity.getDs() >= maxDs) {
                maxDs = wordEntity.getDs();
                maxIdx = i;
            }
        }
        return result.get(maxIdx);
    }

    @Override
    public List<WordEntity> getWordListByDs(int ds) {
        String hsql = "FROM WordEntity w where w.ds=?";
        Query query = sessionFactory.getCurrentSession().createQuery(hsql);
        query.setInteger(0, ds);
        return query.list();
    }

    @Override
    public List<WordEntity> getLastDayWords() {
        List<Integer> dsList = getDsList();
        if (dsList.isEmpty()) return new ArrayList<>(0);
        String hsql = "FROM WordEntity w where w.ds=?";
        Query query = sessionFactory.getCurrentSession().createQuery(hsql);
        query.setInteger(0, dsList.get(0));
        List<WordEntity> result = query.list();
        return result;
    }

    @Override
    public List<Integer> getDsList() {
        String hsql = "SELECT ds from WordEntity w group by w.ds";
        Query query = sessionFactory.getCurrentSession().createQuery(hsql);
        List<Integer> dsObjList = query.list();

        ArrayList<Integer> result = new ArrayList<>();
        for (Integer dsInfo : dsObjList) {
            result.add(dsInfo);
        }

        result.sort(new Comparator <Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if(o1 < o2){
                    return 1;
                }else if(o1 > o2){
                    return -1;
                }
                return 0;
            }
        });
        return result;
    }
}

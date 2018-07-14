package com.glenn.dao;

import com.glenn.entity.NewsEntity;
import com.glenn.entity.NewsEntityPK;
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
@Repository("newsDao") //进行注入
public class NewsDaoImpl implements NewsDao {
    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public void addOrUpdate(NewsEntity news) {
        sessionFactory.getCurrentSession().saveOrUpdate(news);
    }

    @Override
    public void delete(NewsEntity news) {
        sessionFactory.getCurrentSession().delete(news);
    }

    @Override
    public void delete(int ds) {
        String hsql = "DELETE FROM NewsEntity n where n.ds=?";
        Query query = sessionFactory.getCurrentSession().createQuery(hsql);
        query.setInteger(0, ds);
        query.executeUpdate();
    }

    @Override
    public NewsEntity getNews(NewsEntityPK pk) {
        String hsql = "FROM NewsEntity n where n.url=? and n.publicTime=?";
        Query query = sessionFactory.getCurrentSession().createQuery(hsql);
        query.setString(0, pk.getUrl());
        query.setString(1, pk.getPublicTime());
        return (NewsEntity) query.uniqueResult();
    }

    @Override
    public List<NewsEntity> getNewsList(int ds) {
        String hsql = "FROM NewsEntity n where n.ds=?";
        Query query = sessionFactory.getCurrentSession().createQuery(hsql);
        query.setInteger(0, ds);
        return query.list();
    }

    @Override
    public List<NewsEntity> getNewsList() {
        String hsql = "FROM NewsEntity n";
        Query query = sessionFactory.getCurrentSession().createQuery(hsql);
        return query.list();
    }

    @Override
    public List<NewsEntity> getNewsListWithLimit(int num) {
        String hsql = "FROM NewsEntity n";
        Query query = sessionFactory.getCurrentSession().createQuery(hsql);
        query.setMaxResults(num);
        return query.list();
    }
    @Override
    public double getTotalNum() {
        String hsql = "SELECT COUNT(*) FROM NewsEntity n";
        Query query = sessionFactory.getCurrentSession().createQuery(hsql);
        double totalNum = Double.valueOf(query.uniqueResult().toString());
        return totalNum;
    }

    @Override
    public double getTotalNum(int ds) {
        String hsql = "SELECT COUNT(1) FROM NewsEntity n where n.ds=?";
        Query query = sessionFactory.getCurrentSession().createQuery(hsql);
        query.setInteger(0, ds);
        double totalNum = Double.valueOf(query.uniqueResult().toString());
        return totalNum;
    }

    @Override
    public List<Integer> getDsList() {
        String hsql = "SELECT ds from NewsEntity n group by n.ds";
        Query query = sessionFactory.getCurrentSession().createQuery(hsql);
        List<Integer> dsObjList = query.list();

        ArrayList<Integer> result = new ArrayList<>();
        for (Integer dsInfo : dsObjList) {
            result.add(dsInfo);
        }

        result.sort(new Comparator<Integer>() {
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

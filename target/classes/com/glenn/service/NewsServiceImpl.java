package com.glenn.service;

import com.glenn.dao.NewsDao;
import com.glenn.entity.NewsEntity;
import com.glenn.entity.NewsEntityPK;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("newsService")
public class NewsServiceImpl implements NewsService {

    //自动注入userDao，也可以使用@Autowired
    @Resource
    private NewsDao newsDao;

    @Override
    public boolean isExists(NewsEntity news) {
        NewsEntityPK pk = new NewsEntityPK();
        pk.setUrl(news.getUrl());
        pk.setPublicTime(news.getPublicTime());
        return this.isExists(pk);
    }

    @Override
    public boolean isExists(NewsEntityPK pk) {
        NewsEntity result = this.newsDao.getNews(pk);
        if (result == null) return false;
        else return true;
    }

    @Override
    public long getNumByDs(int ds) {
        return this.newsDao.getNewsList(ds).size();
    }

    @Override
    public List<NewsEntity> getNews(int ds) {
        return this.newsDao.getNewsList(ds);
    }

    @Override
    public List<NewsEntity> getNewsWithLimit(int num) {
        return this.newsDao.getNewsListWithLimit(num);
    }

    @Override
    public List<NewsEntity> getLastNewsWithLimit(long num) {
        if (this.newsDao.getTotalNum() <= num) return this.newsDao.getNewsList();

        List<Integer> dsList = this.newsDao.getDsList();
        ArrayList<NewsEntity> newsList = new ArrayList<>();

        for (Integer ds : dsList) {
            newsList.addAll(this.newsDao.getNewsList(ds));
            if (newsList.size() >= num) break;
        }

        return newsList;
    }

    @Override
    public List<NewsEntity> getNews() {
        return this.newsDao.getNewsList();
    }
}

package com.glenn.service;

import com.glenn.entity.NewsEntity;
import com.glenn.entity.NewsEntityPK;

import java.util.List;

public interface NewsService {

    public boolean isExists(NewsEntity news);

    public boolean isExists(NewsEntityPK pk);

    public long getNumByDs(int ds);

    public List<NewsEntity> getNews(int ds);

    public List<NewsEntity> getNewsWithLimit(long num);

    public List<NewsEntity> getNews();
}

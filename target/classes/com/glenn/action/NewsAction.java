package com.glenn.action;

import com.glenn.entity.NewsEntity;

import java.util.List;

public interface NewsAction {

    public List<NewsEntity> getNews();

    public List<NewsEntity> getNews(int ds);

    public List<NewsEntity> getNewsWithLimit(long num);
}

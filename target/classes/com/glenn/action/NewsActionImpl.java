package com.glenn.action;

import com.glenn.entity.NewsEntity;
import com.glenn.service.NewsServiceImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class NewsActionImpl implements NewsAction {

    public NewsServiceImpl newsService;

    public NewsActionImpl() {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"./applicationContext.xml"});
        BeanFactory factory = (BeanFactory) context;
        newsService = (NewsServiceImpl) factory.getBean("newsService");
    }

    @Override
    public List<NewsEntity> getNews() {
        return newsService.getNews();
    }

    @Override
    public List<NewsEntity> getNews(int ds) {
        return newsService.getNews(ds);
    }

    @Override
    public List<NewsEntity> getNewsWithLimit(long num) {
        return newsService.getNewsWithLimit(num);
    }
}

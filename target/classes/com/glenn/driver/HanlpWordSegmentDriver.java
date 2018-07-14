package com.glenn.driver;

import com.glenn.action.NewsActionImpl;
import com.glenn.entity.NewsEntity;
import com.glenn.util.FileUtil;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

import java.io.FileWriter;
import java.util.List;

public class HanlpWordSegmentDriver {

    public static boolean isCleanWord(Term term) {
        if (term.nature.startsWith("nx") || 
                term.word.matches(".*\\d+.*") ||
                term.word.matches("([0-9]\\d*\\.?\\d*)|(0\\.\\d*[0-9])") ||
                term.nature.startsWith('w')) return false;
        else return true;
    }

    public static void main(String[] args) {

        if (args.length <= 0) {
            throw new IllegalArgumentException("should input save path");
        }


        int maxNum = 15000;
        NewsActionImpl newsAction = new NewsActionImpl();
        List<NewsEntity> newsList = newsAction.getNewsWithLimit(maxNum);

        String path = args[0];
        FileUtil.deleteFile(path);

        FileWriter fw = FileUtil.createFileWriter(path);
        for (NewsEntity news : newsList) {
            List<Term> termList = HanLP.segment(news.getContent());
            String string = "";
            for (Term term : termList) {
                if (isCleanWord(term)) {
                    string += term.toString() + " ";
                }
            }

            FileUtil.append(fw, string);
        }

//        System.out.println(HanLP.segment("-").get(0).nature);
    }

}

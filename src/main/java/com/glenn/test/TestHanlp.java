package com.glenn.test;

import com.glenn.word.Word;
import com.hankcs.hanlp.HanLP;

import java.util.HashSet;

public class TestHanlp {

    public static void main(String[] args) {

        String content = "你好，欢迎使用HanLP！";

        System.out.println(HanLP.segment(content));

        System.out.println(content.substring(content.length()-1,content.length()));

        HashSet<Word> wordSet = new HashSet<>();
        Word wordObj = new Word("123");
        wordSet.add(wordObj);

        Word wordObj2 = new Word("123");
        wordObj2.setNum(22);

        System.out.println(wordSet.contains(wordObj2));

        wordSet.add(wordObj2);

        System.out.println(wordSet.size());
        System.out.println(wordSet.iterator().next().getNum());
    }
}

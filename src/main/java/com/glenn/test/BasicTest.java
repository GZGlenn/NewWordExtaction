package com.glenn.test;

import java.util.ArrayList;

public class BasicTest {

    public static void main(String[] args) {

        String targetWordStr = "平易近人";
        ArrayList<Double> dynamicFrezzScore = new ArrayList<>(5);
        System.out.println(targetWordStr.length());
        System.out.println(dynamicFrezzScore.size());
        System.out.println(targetWordStr.substring(1, 2));
    }

}

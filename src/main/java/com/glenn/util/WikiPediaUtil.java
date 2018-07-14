package com.glenn.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class WikiPediaUtil {

    public static ArrayList<String> read(String path, long maxParagraphNum) {
        File rootFile = new File(path);
        File[] files = rootFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.endsWith("chs");
            }
        });


        ArrayList<String> content = new ArrayList<>();
        long needReadParagraphNum = maxParagraphNum;
        for (File file : files) {
            ArrayList<String> info = FileUtil.readFileByLine(file.getAbsolutePath(), needReadParagraphNum);
            for (String string : info) {
                if (string.equals("") || string.startsWith("<")) {
                    continue;
                }
                content.add(string);
            }

            needReadParagraphNum -= content.size();
            if (needReadParagraphNum <= 0) break;
        }

//        for (String string : content) {
//            if (string.equals("")) {
//                System.out.println("123");
//            }
//            else {
//                System.out.println(string);
//            }
//        }
        return content;
    }
}

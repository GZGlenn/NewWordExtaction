package com.glenn.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class FileUtil {


    public static ArrayList<String> readFileByLine(String sFile, long maxLineNum) {
        ArrayList<String> result = new ArrayList<String>();

        if (sFile == null) return result;
        File file = new File(sFile);
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            int hadReadLineNum = 0;
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            String line = null;
            while((line = bufferedReader.readLine()) != null && (maxLineNum > 0 && hadReadLineNum < maxLineNum)) {
                result.add(line.trim());
                hadReadLineNum++;
            }
        } catch (Exception e) {
            LogUtil.getInstance().printLog(e.getMessage(), LogUtil.LEVEL.ERROR);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    LogUtil.getInstance().printLog(e.getMessage(), LogUtil.LEVEL.ERROR);
                }
            }
            if (fileReader != null)  {
                try {
                    fileReader.close();
                } catch (Exception e) {
                    LogUtil.getInstance().printLog(e.getMessage(), LogUtil.LEVEL.ERROR);
                }
            }

            return result;
        }

    }
}

package com.glenn.word;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

import javax.persistence.Tuple;
import java.util.*;

/**
 * https://blog.csdn.net/smartcat2010/article/details/77883735
 */
public class NewsWordExtractorImpl implements NewsWordExtractor {

    public static int MAXNGRAM = 4;

    public double frezzDegreeThre;
    public double freeDegreeThre;
    public double frequencyDegreeThre;
    public int nGram;

    public double getFrezzDegreeThre() {
        return frezzDegreeThre;
    }

    public double getFreeDegreeThre() {
        return freeDegreeThre;
    }

    public double getFrequencyDegreeThre() {
        return frequencyDegreeThre;
    }

    public int getnGram() {
        return nGram;
    }

    public void setFrezzDegreeThre(double frezzDegreeThre) {
        this.frezzDegreeThre = frezzDegreeThre;
    }

    public void setFreeDegreeThre(double freeDegreeThre) {
        this.freeDegreeThre = freeDegreeThre;
    }

    public void setFrequencyDegreeThre(double frequencyDegreeThre) {
        this.frequencyDegreeThre = frequencyDegreeThre;
    }

    public void setnGram(int nGram) {
        this.nGram = Math.min(MAXNGRAM, nGram);
    }

    public NewsWordExtractorImpl() {
        this.freeDegreeThre = -1;
        this.frezzDegreeThre = -1;
        this.frequencyDegreeThre = -1;
        this.nGram = MAXNGRAM;
    }

    public NewsWordExtractorImpl(double frezzDegreeThre, double freeDegreeThre, double frequencyDegreeThre, int nGram) {
        this.frezzDegreeThre = frezzDegreeThre;
        this.freeDegreeThre = freeDegreeThre;
        this.frequencyDegreeThre = frequencyDegreeThre;
        this.nGram = Math.min(MAXNGRAM, nGram);
    }

    public HashSet<Word> coreExtraction(ArrayList<String> content) {
        HashMap<String, Word> wordList = getCandidateWord(content);
        HashSet<Word> newWordList = getNewWords(wordList, content);
        return newWordList;
    }

    @Override
    public HashMap<String, Word> getCandidateWord(ArrayList<String> content) {
        HashMap<String, Word> result = new HashMap<>();

        double totalNum = 0;
        ArrayList<String> setences = divideStringByPunc(content);
        for (int i = 1; i <= this.nGram; i++) {
            for (String sentence : setences) {
                for (int start = 0 ; start <= sentence.length() - i; start++) {
                    String word = sentence.substring(start, start + i);
                    if (!result.containsKey(word)) {
                        result.put(word, new Word(word));
                        totalNum++;
                    }
                    else {
                        Word wordObj = result.get(word);
                        wordObj.setNum(wordObj.getNum() + 1);
                        result.put(word, wordObj);
                    }
                }
            }
        }

        for (HashMap.Entry<String, Word> entry : result.entrySet()) {
            Word wordObj = entry.getValue();
            wordObj.setFrequency(wordObj.getNum() / totalNum);
            result.put(entry.getKey(), wordObj);
        }

        return result;
    }


    private ArrayList<String> divideStringByPunc(ArrayList<String> content) {
        ArrayList<String> result = new ArrayList<>();
        for (String info : content) {
            result.addAll(divideStringByPunc(info));
        }

        return result;
    }

    private ArrayList<String> divideStringByPunc(String content) {
        ArrayList<String> result = new ArrayList<>();
        List<Term> termList = HanLP.segment(content);
        for (int oldIndex = 0, index = 0 ; index < termList.size(); index++) {
            if (termList.get(index).nature.startsWith('w')) {
                if (index == 0) {
                    oldIndex++;
                    continue;
                }

                String info = "";
                for (int idx = oldIndex; idx< index; idx++) {
                    info += termList.get(idx).word;
                }

                if (!info.isEmpty()) {
                    result.add(info);
                }
                oldIndex = index+1;
            }
        }

        return result;
    }

    @Override
    public HashSet<Word> getNewWords(HashMap<String, Word> wordSet, ArrayList<String> content) {
        HashMap<String, Word> cleanWordSet = calStatisticInfo(wordSet, content);
        HashMap<String, Word> cleanWordMap = cleanWord(cleanWordSet);

        HashSet<Word> result = new HashSet<>();
        for (HashMap.Entry<String, Word> entry : cleanWordMap.entrySet()) {
            result.add(entry.getValue());
        }
        return result;
    }

    private HashMap<String, Word> calStatisticInfo(HashMap<String, Word> wordSet, ArrayList<String> content) {
        calFrezzDegree(wordSet);
        HashMap<String, Word> cleanWordSetByFrezz = new HashMap<>();

        if (frezzDegreeThre <= 0) {
            ArrayList<Double> frezzList = new ArrayList<>();
            for (Iterator<HashMap.Entry<String, Word>> it = wordSet.entrySet().iterator(); it.hasNext();){
                HashMap.Entry<String, Word> item = it.next();
                frezzList.add(item.getValue().getFrequency() / item.getValue().getFrezzDegree());
            }
            frezzList.sort(new Comparator <Double>() {
                @Override
                public int compare(Double o1, Double o2) {
                    if(o1 < o2){
                        return 1;
                    }else if(o1 > o2){
                        return -1;
                    }
                    return 0;
                }
            });
            double total = frezzList.size();
            frezzDegreeThre = Math.max(1000, frezzList.get((int)Math.round(0.1 * total)));
        }

        for (Iterator<HashMap.Entry<String, Word>> it = wordSet.entrySet().iterator(); it.hasNext();){
            HashMap.Entry<String, Word> item = it.next();

            double ratio = item.getValue().getFrequency() / item.getValue().getFrezzDegree();
            if (ratio <= this.frezzDegreeThre) {
                continue;
            }
            cleanWordSetByFrezz.put(item.getKey(), item.getValue());
        }

        calFreeDegree(cleanWordSetByFrezz, content);
        HashMap<String, Word> cleanWordSetByFree = new HashMap<>();
        for (Iterator<HashMap.Entry<String, Word>> it = cleanWordSetByFrezz.entrySet().iterator(); it.hasNext();){
            HashMap.Entry<String, Word> item = it.next();
            if (item.getValue().getFreeDegree() <= this.freeDegreeThre) {
                continue;
            }
            cleanWordSetByFree.put(item.getKey(), item.getValue());
        }
        return cleanWordSetByFree;
    }

    private void calFrezzDegree(HashMap<String, Word> wordSet) {

        for (HashMap.Entry<String, Word> entry : wordSet.entrySet()) {
            if (entry.getKey().length() == 1) continue;

            String targetWordStr = entry.getKey();
            // dynamic rule
            ArrayList<Double> dynamicFrezzScore = new ArrayList<>();
            for (int i = 0 ; i < targetWordStr.length(); i++) {
                dynamicFrezzScore.add(i, 0.0);
            }

            double frezz = -1;
            String tmpWord0 = targetWordStr.charAt(0) + "";
            String tmpWord1 = targetWordStr.charAt(1) + "";
            if (targetWordStr.length() == 2) {
                frezz = wordSet.get(tmpWord0).getFrequency() * wordSet.get(tmpWord1).getFrequency();
            }
            else if (targetWordStr.length() == 3) {
                String tmpWord2 = targetWordStr.charAt(2) + "";
                frezz = Math.max(wordSet.get(tmpWord0 + tmpWord1).getFrequency() * wordSet.get(tmpWord2).getFrequency(),
                        wordSet.get(tmpWord1 + tmpWord2).getFrequency() * wordSet.get(tmpWord0).getFrequency());
            }
            else if (targetWordStr.length() == 4) {
                String tmpWord2 = targetWordStr.charAt(2) + "";
                String tmpWord3 = targetWordStr.charAt(3) + "";
                frezz = Math.max(wordSet.get(tmpWord0 + tmpWord1 + tmpWord2).getFrequency() * wordSet.get(tmpWord3).getFrequency(),
                        Math.max(wordSet.get(tmpWord0).getFrequency() * wordSet.get(tmpWord1 + tmpWord2 + tmpWord3).getFrequency(),
                        wordSet.get(tmpWord0 + tmpWord1).getFrequency() * wordSet.get(tmpWord2 + tmpWord3).getFrequency()));
            }
            else if (targetWordStr.length() == 5) {
                String tmpWord2 = targetWordStr.charAt(2) + "";
                String tmpWord3 = targetWordStr.charAt(3) + "";
                String tmpWord4 = targetWordStr.charAt(4) + "";
                frezz = Math.max(wordSet.get(tmpWord0 + tmpWord1 + tmpWord2 + tmpWord3).getFrequency() * wordSet.get(tmpWord4).getFrequency(),
                        Math.max(wordSet.get(tmpWord1+tmpWord2+tmpWord3+tmpWord4).getFrequency() * wordSet.get(tmpWord0).getFrequency(),
                        Math.max(wordSet.get(tmpWord0+tmpWord1+tmpWord2).getFrequency() * wordSet.get(tmpWord3+tmpWord4).getFrequency(),
                                 wordSet.get(tmpWord2+tmpWord3+tmpWord4).getFrequency() * wordSet.get(tmpWord0 + tmpWord1).getFrequency())));
            }

            wordSet.get(entry.getKey()).setFrezzDegree(frezz);
        }


    }

    private void calFreeDegree(HashMap<String, Word> wordSet, ArrayList<String> content) {
        for (HashMap.Entry<String, Word> entry : wordSet.entrySet()) {
            if (entry.getKey().length() == 1) continue;

            String targetWord = entry.getKey();
            HashMap<String, Double> leftWords = new HashMap<>();
            HashMap<String, Double> rightWords = new HashMap<>();

            // consider 2-3gram
            for (String sentence : content) {
                if (targetWord.equals(sentence)) continue;

                String tmpSentence = sentence;
                do {
                    int index = tmpSentence.indexOf(targetWord);
                    if (index < 0) break;
                    if (index == 0) {
                        statisticRightWords(tmpSentence.substring(targetWord.length()), rightWords);
                    } else if (index + targetWord.length() == tmpSentence.length()) {
                        statisticLeftWords(tmpSentence.substring(0, index), leftWords);
                        break;
                    } else {
                        statisticLeftWords(tmpSentence.substring(0, index), leftWords);
                        statisticRightWords(tmpSentence.substring(index + targetWord.length()), rightWords);
                    }
                    tmpSentence = tmpSentence.substring(index + targetWord.length());
                } while (true);
            }

            double leftEntropy = calEntropy(leftWords);
            double rightEntropy = calEntropy(rightWords);

            wordSet.get(targetWord).setFreeDegree(Math.min(leftEntropy, rightEntropy));
        }
    }

    private void statisticRightWords(String sentence, HashMap<String, Double> wordMap) {
        if (sentence.length() >= 1) {
            String word = sentence.charAt(0) + "";
            wordMap.put(word, wordMap.getOrDefault(word, 0.0) + 1);
        }
        if (sentence.length() >= 2) {
            String word = sentence.charAt(0) + "" + sentence.charAt(1);
            wordMap.put(word, wordMap.getOrDefault(word, 0.0) + 1);
        }
        if (sentence.length() >= 3) {
            String word = sentence.charAt(0) + "" + sentence.charAt(1) + sentence.charAt(2);
            wordMap.put(word, wordMap.getOrDefault(word, 0.0) + 1);
        }
    }

    private void statisticLeftWords(String sentence, HashMap<String, Double> wordMap) {
        int len = sentence.length();
        if (sentence.length() >= 1) {
            String word = sentence.charAt(len - 1) + "";
            wordMap.put(word, wordMap.getOrDefault(word, 0.0) + 1);
        }
        if (sentence.length() >= 2) {
            String word = sentence.charAt(len - 2) + "" + sentence.charAt(len - 1);
            wordMap.put(word, wordMap.getOrDefault(word, 0.0) + 1);
        }
        if (sentence.length() >= 3) {
            String word = sentence.charAt(len - 3) + "" + sentence.charAt(len - 2) + sentence.charAt(len - 1) + "";
            wordMap.put(word, wordMap.getOrDefault(word, 0.0) + 1);
        }
    }

    private double calEntropy(HashMap<String, Double> wordMap) {

        double totalNum = 0;

        for (HashMap.Entry<String, Double> entry : wordMap.entrySet()) {
            totalNum += entry.getValue();
        }

        double entropy = 0;
        for (HashMap.Entry<String, Double> entry : wordMap.entrySet()) {
            double frequency = entry.getValue() / totalNum;
            entropy += -1 * frequency * Math.log(frequency);
        }

        return entropy;
    }

    // clean too short word, too little fre word
    private HashMap<String, Word> cleanWord(HashMap<String, Word> wordSet) {
        if (frequencyDegreeThre == 0) {
            double totalNum = wordSet.size();
            ArrayList<Double> frequencies = new ArrayList<Double>();
            for (HashMap.Entry<String, Word> entry : wordSet.entrySet()) {
                frequencies.add(0, entry.getValue().getFrequency());
            }

            frequencies.sort(new Comparator <Double>() {
                @Override
                public int compare(Double o1, Double o2) {
                    if(o1 < o2){
                        return 1;
                    }else if(o1 > o2){
                        return -1;
                    }
                    return 0;
                }
            });
            frequencyDegreeThre = frequencies.get((int)Math.round(0.1 * totalNum));
        }

        HashMap<String, Word> cleanMap = new HashMap<>();
        for (HashMap.Entry<String, Word> entry : wordSet.entrySet()) {
            if (entry.getValue().getFrequency() > frequencyDegreeThre) {
                cleanMap.put(entry.getKey(), entry.getValue());
            }
        }

        return cleanMap;
    }
}


package com.glenn.word;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * https://blog.csdn.net/smartcat2010/article/details/77883735
 */
public class NewWordExtractorImpl implements NewsWordExtractor {

    public static int MAXNGRAM = 5;

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

    public NewWordExtractorImpl() {
        this.freeDegreeThre = -1;
        this.frezzDegreeThre = -1;
        this.frequencyDegreeThre = -1;
        this.nGram = MAXNGRAM;
    }

    public NewWordExtractorImpl(double frezzDegreeThre, double freeDegreeThre, double frequencyDegreeThre, int nGram) {
        this.frezzDegreeThre = frezzDegreeThre;
        this.freeDegreeThre = freeDegreeThre;
        this.frequencyDegreeThre = frequencyDegreeThre;
        this.nGram = Math.min(MAXNGRAM, nGram);
    }

    public HashSet<Word> coreExtraction(ArrayList<String> content) {
        HashSet<Word> wordList = getCandidateWord(content);
        HashSet<Word> newWordList = getNewWords(wordList, content);
        return newWordList;
    }

    @Override
    public HashSet<Word> getCandidateWord(ArrayList<String> content) {
        HashSet<Word> result = new HashSet<>();

        ArrayList<String> setences = divideStringByPunc(content);
        for (int i = 1; i <= this.nGram; i++) {
            for (String sentence : setences) {
                for (int start = 0 ; start <= sentence.length() - i; start++) {
                    Word wordObj = new Word(sentence.substring(start, start + 1));
//                    if (result.contains(wordObj))
                }
            }
        }

        return null;
    }

    @Override
    public HashSet<Word> getNewWords(HashSet<Word> wordSet, ArrayList<String> content) {

        return null;
    }

    public ArrayList<String> divideStringByPunc(ArrayList<String> content) {
        ArrayList<String> result = new ArrayList<>();
        for (String info : content) {
            result.addAll(divideStringByPunc(info));
        }

        return result;
    }

    public ArrayList<String> divideStringByPunc(String content) {
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

}

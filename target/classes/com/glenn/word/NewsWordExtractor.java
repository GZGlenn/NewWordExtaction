package com.glenn.word;

import java.util.ArrayList;
import java.util.HashSet;

public interface NewsWordExtractor {

    public HashSet<Word> getCandidateWord(ArrayList<String> content);

    public HashSet<Word> getNewWords(HashSet<Word> wordSet, ArrayList<String> content);
}

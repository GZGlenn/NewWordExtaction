package com.glenn.word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public interface NewsWordExtractor {

    public HashMap<String, Word> getCandidateWord(ArrayList<String> content);

    public HashSet<Word> getNewWords(HashMap<String, Word> wordMap, ArrayList<String> content);
}

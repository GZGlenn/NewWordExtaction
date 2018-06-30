package com.glenn.word;

import java.io.Serializable;
import java.util.Objects;

public class Word extends Object implements Serializable {

    private String word;
    private String pos;
    private int num;
    private double frequency;

    private double frezzDegree;
    private double freeDegree;

    public String getWord() {
        return word;
    }

    public String getPos() {
        return pos;
    }

    public int getNum() {
        return num;
    }

    public double getFrequency() {
        return frequency;
    }

    public double getFrezzDegree() {
        return frezzDegree;
    }

    public double getFreeDegree() {
        return freeDegree;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public void setFrezzDegree(double frezzDegree) {
        this.frezzDegree = frezzDegree;
    }

    public void setFreeDegree(double freeDegree) {
        this.freeDegree = freeDegree;
    }

    public Word(String word, String pos, int num, double frequency, double frezzDegree, double freeDegree) {
        this.word = word;
        this.pos = pos;
        this.num = num;
        this.frequency = frequency;
        this.frezzDegree = frezzDegree;
        this.freeDegree = freeDegree;
    }

    public Word(String word) {
        this.word = word;
        this.pos = "";
        this.num = 1;
        this.frequency = -1;
        this.freeDegree = -1;
        this.frezzDegree = -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word)) return false;
        Word word1 = (Word) o;
        return Objects.equals(word, word1.word) && Objects.equals(pos, word1.pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, pos);
    }
}

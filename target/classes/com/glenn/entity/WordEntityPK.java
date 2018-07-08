package com.glenn.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class WordEntityPK implements Serializable {
    private int ds;
    private String word;

    @Column(name = "ds")
    @Id
    public int getDs() {
        return ds;
    }

    public void setDs(int ds) {
        this.ds = ds;
    }

    @Column(name = "word")
    @Id
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordEntityPK that = (WordEntityPK) o;
        return ds == that.ds &&
                Objects.equals(word, that.word);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ds, word);
    }
}

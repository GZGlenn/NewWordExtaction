package com.glenn.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "word", schema = "netease_news", catalog = "")
@IdClass(WordEntityPK.class)
public class WordEntity {
    private int ds;
    private String word;
    private String pos;
    private int frequency;

    @Id
    @Column(name = "ds")
    public int getDs() {
        return ds;
    }

    public void setDs(int ds) {
        this.ds = ds;
    }

    @Id
    @Column(name = "word")
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Basic
    @Column(name = "pos")
    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    @Basic
    @Column(name = "frequency")
    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordEntity that = (WordEntity) o;
        return ds == that.ds &&
                frequency == that.frequency &&
                Objects.equals(word, that.word) &&
                Objects.equals(pos, that.pos);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ds, word, pos, frequency);
    }
}

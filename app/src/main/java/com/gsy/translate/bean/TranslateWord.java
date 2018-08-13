package com.gsy.translate.bean;

import java.util.ArrayList;

/**
 * Created by Think on 2018/2/25.
 */

public class TranslateWord {
    private String word;
    private ArrayList<String> definition;
    private ArrayList<String> note;
    private String sourceName;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public ArrayList<String> getDefinition() {
        return definition;
    }

    public void setDefinition(ArrayList<String> definition) {
        this.definition = definition;
    }

    public ArrayList<String> getNote() {
        return note;
    }

    public void setNote(ArrayList<String> note) {
        this.note = note;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}

package com.gsy.translate.bean;

/**
 * Created by Think on 2018/2/4.
 */

public class DBWord {
    private String word;
    private String definition;

    public DBWord(){

    }
    public DBWord(String word,String definition){
        this.word = word;
        this.definition = definition;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DBWord dbWord = (DBWord) o;

        if (word != null ? !word.equals(dbWord.word) : dbWord.word != null) return false;
        return definition != null ? definition.equals(dbWord.definition) : dbWord.definition == null;
    }

    @Override
    public int hashCode() {
        int result = word != null ? word.hashCode() : 0;
        result = 31 * result + (definition != null ? definition.hashCode() : 0);
        return result;
    }
}

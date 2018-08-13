package com.gsy.translate.bean;

/**
 * Created by Think on 2017/12/9.
 */

public class Word {

    private int id;
    private String word;
    private String imagePath;
    private String usPhonemes;
    private String ukPhonemes;
    private String usPhonemesAudio;
    private String ukPhonemesAudio;
    private String translate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getUsPhonemes() {
        return usPhonemes;
    }

    public void setUsPhonemes(String usPhonemes) {
        this.usPhonemes = usPhonemes;
    }

    public String getUkPhonemes() {
        return ukPhonemes;
    }

    public void setUkPhonemes(String ukPhonemes) {
        this.ukPhonemes = ukPhonemes;
    }

    public String getUsPhonemesAudio() {
        return usPhonemesAudio;
    }

    public void setUsPhonemesAudio(String usPhonemesAudio) {
        this.usPhonemesAudio = usPhonemesAudio;
    }

    public String getUkPhonemesAudio() {
        return ukPhonemesAudio;
    }

    public void setUkPhonemesAudio(String ukPhonemesAudio) {
        this.ukPhonemesAudio = ukPhonemesAudio;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }
}

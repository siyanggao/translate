package com.gsy.translate.home.bean;

import com.gsy.translate.bean.BaseResponse;
import com.gsy.translate.bean.Sentence;
import com.gsy.translate.bean.Word;

import java.util.ArrayList;

/**
 * Created by Think on 2017/12/9.
 */

public class HomeBean extends BaseResponse {

    HomeBeanData data;

    public HomeBeanData getData() {
        return data;
    }

    public void setData(HomeBeanData data) {
        this.data = data;
    }

    public class HomeBeanData{
        private ArrayList<Sentence> sentence;
        private ArrayList<Word> word;


        public ArrayList<Sentence> getSentence() {
            return sentence;
        }

        public void setSentence(ArrayList<Sentence> sentence) {
            this.sentence = sentence;
        }

        public ArrayList<Word> getWord() {
            return word;
        }

        public void setWord(ArrayList<Word> word) {
            this.word = word;
        }
    }
}

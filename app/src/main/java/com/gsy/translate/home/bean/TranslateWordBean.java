package com.gsy.translate.home.bean;

import com.gsy.translate.bean.BaseResponse;
import com.gsy.translate.bean.TranslateWord;

import java.util.ArrayList;

/**
 * Created by Think on 2018/2/25.
 */

public class TranslateWordBean extends BaseResponse {
    private TranslateData  data;

    public TranslateData getData() {
        return data;
    }

    public void setData(TranslateData data) {
        this.data = data;
    }

    public class TranslateData{
        private ArrayList<TranslateWord> basic;

        public ArrayList<TranslateWord> getBasic() {
            return basic;
        }

        public void setBasic(ArrayList<TranslateWord> basic) {
            this.basic = basic;
        }
    }
}

package com.gsy.translate.home.bean;

import com.gsy.translate.bean.Article;
import com.gsy.translate.bean.BaseResponse;

/**
 * Created by Think on 2018/1/28.
 */

public class ArticleDetailBean extends BaseResponse {
    private Article data;

    public Article getData() {
        return data;
    }

    public void setData(Article data) {
        this.data = data;
    }
}

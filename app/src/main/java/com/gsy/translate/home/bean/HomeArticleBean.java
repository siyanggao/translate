package com.gsy.translate.home.bean;

import com.gsy.translate.bean.Article;
import com.gsy.translate.bean.BaseResponse;

import java.util.List;

/**
 * Created by Think on 2017/12/21.
 */

public class HomeArticleBean extends BaseResponse {
    private List<Article> data;

    public List<Article> getData() {
        return data;
    }

    public void setData(List<Article> data) {
        this.data = data;
    }
}

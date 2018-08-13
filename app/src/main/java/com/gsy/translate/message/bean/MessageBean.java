package com.gsy.translate.message.bean;

import com.gsy.translate.bean.BaseResponse;
import com.gsy.translate.bean.Message;

import java.util.List;

/**
 * Created by Think on 2018/2/1.
 */

public class MessageBean extends BaseResponse {

    private List<Message> data;

    public List<Message> getData() {
        return data;
    }

    public void setData(List<Message> data) {
        this.data = data;
    }
}

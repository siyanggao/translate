package com.gsy.translate.my.bean;

import com.gsy.translate.bean.BaseResponse;
import com.gsy.translate.bean.User;

import java.util.ArrayList;

/**
 * Created by Think on 2017/12/24.
 */

public class UserBean extends BaseResponse{
    private User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}

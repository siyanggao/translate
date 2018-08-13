package com.gsy.translate.my.bean;

import com.gsy.translate.bean.BaseResponse;
import com.gsy.translate.bean.Version;

/**
 * Created by Think on 2018/1/21.
 */

public class VersionBean extends BaseResponse {
    private Version data;

    public Version getVersion() {
        return data;
    }

    public void setVersion(Version version) {
        this.data = version;
    }
}

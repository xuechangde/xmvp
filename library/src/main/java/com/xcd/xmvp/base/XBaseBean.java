package com.xcd.xmvp.base;

import java.io.Serializable;

/**
 * @author xcd
 */
public class XBaseBean<T> implements Serializable {


    /**
     * data :
     * errorCode : 0
     * errorMsg :
     */

    public int errorCode;
    public String errorMsg;
    public T data;

    public XBaseBean(int code, String data) {
        this.errorCode = code;
        this.data = (T) data;
    }
}

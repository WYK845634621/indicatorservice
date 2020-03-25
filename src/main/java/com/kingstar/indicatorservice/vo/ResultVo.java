package com.kingstar.indicatorservice.vo;

import java.io.Serializable;

/**
 * @Desc: 响应状态
 * @Auth: xingzhi.yang
 * @Date: 2018年1月19日 上午11:29:39
 */
public class ResultVo<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String CODE_WAITING = "2";
    public static final String CODE_SUCCESS = "20000";
    public static final String CODE_FAIL = "0";

    public static final String MSG_WAITING = "等待";
    public static final String MSG_SUCCESS = "成功";
    public static final String MSG_FAIL = "失败";


    /**
     * 日志相关报文
     */
    private Object log;


    private String code = CODE_FAIL;
    private String msg = MSG_FAIL;

    private T result;

    public ResultVo() {
        super();
    }

    public ResultVo(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultVo(String code, String msg, T t) {
        this.code = code;
        this.msg = msg;
        this.result = t;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Object getLog() {
        return log;
    }

    public void setLog(Object log) {
        this.log = log;
    }
}

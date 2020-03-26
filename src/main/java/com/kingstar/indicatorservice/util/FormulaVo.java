package com.kingstar.indicatorservice.util;

import java.util.List;

/**
 * 单条公式计算对象
 */
public class FormulaVo {


    private String resultKey;   //新增的key

    private String resultValue;

    private List<String> conditionKeys;     //条件的key

    private List<String> chooseNum;     //选择的位号

    //运算符
    private String operator;
    //计算方式
    private String method;


    //计算方法
//    public void calculate() {
//        FormulaUtil.calculate(this);
//    }

    public String getResultKey() {
        return resultKey;
    }

    public void setResultKey(String resultKey) {
        this.resultKey = resultKey;
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    public List<String> getConditionKeys() {
        return conditionKeys;
    }

    public void setConditionKeys(List<String> conditionKeys) {
        this.conditionKeys = conditionKeys;
    }

    public List<String> getChooseNum() {
        return chooseNum;
    }

    public void setChooseNum(List<String> chooseNum) {
        this.chooseNum = chooseNum;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }


}

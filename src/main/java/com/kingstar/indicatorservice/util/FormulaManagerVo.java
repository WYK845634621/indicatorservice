package com.kingstar.indicatorservice.util;


import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公式计算总对象
 */
public class FormulaManagerVo {

    private static final String FORMULA_OPERATOR_CHOOSE = "C";

    private String s;   //formula

    private List<FormulaVo> fvlist;

    private List<String> conditionList;     //传入的需要操作的key

    private Map<String, String> conditionValueMap;

    public FormulaManagerVo(String s) {
        this.s = s;
        this.fvlist = new ArrayList<FormulaVo>();
        this.conditionList = new ArrayList<String>();
        this.conditionValueMap = new HashMap<String, String>();
        this.init(s);
    }


    //初始化方法
    public void init(String s) {

        if (!StringUtil.isEmpty(s)) {
            String[] formulaVoArr = s.split(",");
            //拆解数组里的每个数据 resultkey chooseNum operator mehtod ->fv conditionKey -> conditionList
            for (int i = 0; i < formulaVoArr.length; i++) {
                //key3@b_middledbsyncabnormal@b_middledbsync@A0
                String[] tempArr = formulaVoArr[i].split("@");
                FormulaVo formulaVo = new FormulaVo();
                List<String> conditionKeys = new ArrayList<>();
                formulaVo.setResultKey(tempArr[0]);
                //倒数第二位
                formulaVo.setOperator(tempArr[tempArr.length - 1].substring(0, 1));
                //最后一位
                formulaVo.setMethod(tempArr[tempArr.length - 1].substring(1, 2));
                if (formulaVo.getOperator().equals(FORMULA_OPERATOR_CHOOSE)) {
                    //选择运算
                    conditionList.add(tempArr[1]);
                    conditionKeys.add(tempArr[1]);
                    formulaVo.setConditionKeys(conditionKeys);
                    List<String> numList = new ArrayList<>();
                    for (int j = 2; j < tempArr.length - 1; j++) {
                        numList.add(tempArr[j]);
                    }
                    formulaVo.setChooseNum(numList);
                    fvlist.add(formulaVo);
                } else {
                    for (int j = 1; j < tempArr.length - 1; j++) {
                        conditionList.add(tempArr[j]);
                        conditionKeys.add(tempArr[j]);
                    }
                    formulaVo.setConditionKeys(conditionKeys);
                    fvlist.add(formulaVo);
                }
            }
        }
    }

    //将原有的值设置进来
    public void setValue(List<String> ls) {
        for (int i = 0; i < ls.size(); i++) {
            conditionValueMap.put(conditionList.get(i), ls.get(i));
        }
    }

    //将计算好的结果返回
    public Map<String, String> getResultValue(List<String> ls, FormulaManagerVo fmv) {

        ////将值设置进来
        this.setValue(ls);

        //执行计算
        return calculate(fmv);

    }

    public Map<String, String> calculate(FormulaManagerVo fmv) {

        Map<String, String> resultValueMap = new HashMap<>();

        if (CollectionUtils.isEmpty(fmv.getFvlist())) {
            for (FormulaVo fv : fmv.getFvlist()) {
                resultValueMap.putAll(FormulaUtil.calculateOperator(fv, fmv.getConditionValueMap()));
            }
        }

        return resultValueMap;
    }


    public List<FormulaVo> getFvlist() {
        return fvlist;
    }

    public void setFvlist(List<FormulaVo> fvlist) {
        this.fvlist = fvlist;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public List<String> getConditionList() {
        return conditionList;
    }

    public void setConditionList(List<String> conditionList) {
        this.conditionList = conditionList;
    }

    public Map<String, String> getConditionValueMap() {
        return conditionValueMap;
    }

    public void setConditionValueMap(Map<String, String> conditionValueMap) {
        this.conditionValueMap = conditionValueMap;
    }

}

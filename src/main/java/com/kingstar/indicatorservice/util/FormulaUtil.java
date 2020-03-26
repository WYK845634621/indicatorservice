package com.kingstar.indicatorservice.util;

import java.util.HashMap;
import java.util.Map;

public class FormulaUtil {

    //加
    private static final String FORMULA_OPERATOR_ADD = "A";
    //减
    private static final String FORMULA_OPERATOR_SUBSTRACT = "S";
    //乘
    private static final String FORMULA_OPERATOR_MULTIPLY = "M";
    //除
    private static final String FORMULA_OPERATOR_DIVIDED = "D";
    //选择
    private static final String FORMULA_OPERATOR_CHOOSE = "C";

    //连续
    private static final String FORMULA_METHOD_CONTINUE = "C";
    //跳跃
    private static final String FORMULA_METHOD_JUMP = "J";

    /**
     * 公式对象的计算处理：操作方法
     * @param fv
     * @param conditionValueMap
     * @return
     */
    public static Map<String, String> calculateMethod(FormulaVo fv, Map<String, String> conditionValueMap) {
        Map<String, String> resultValueMap = new HashMap<>();
        StringBuffer sb = new StringBuffer();

        if (fv.getMethod().equals(FORMULA_METHOD_JUMP)) {
            String valueTemp = conditionValueMap.get(fv.getConditionKeys().get(0));
            String[] temparr = valueTemp.split(",");
            for (int j = 0; j < fv.getChooseNum().size(); j++) {
                int num = Integer.parseInt(fv.getChooseNum().get(j));
                sb.append(temparr[num]);
                sb.append(",");
            }
            fv.setResultValue(trans(sb));
            resultValueMap.put(fv.getResultKey(), fv.getResultValue());
        }
        if (fv.getMethod().equals(FORMULA_METHOD_CONTINUE)) {
            String valueTemp = conditionValueMap.get(fv.getConditionKeys().get(0));
            String[] temparr = valueTemp.split(",");
            int k = Integer.parseInt(fv.getChooseNum().get(0));
            int size = Integer.parseInt(fv.getChooseNum().get(1));
            for (; k <= size; k++) {
                sb.append(temparr[k]);
                sb.append(",");
            }
            fv.setResultValue(trans(sb));
            resultValueMap.put(fv.getResultKey(), fv.getResultValue());
        }
        return resultValueMap;
    }

    /**
     * 公式对象的计算处理：操作类型
     * @param fv
     * @param conditionValueMap
     * @return
     */
    public static Map<String, String> calculateOperator(FormulaVo fv, Map<String, String> conditionValueMap) {

        Map<String, String> resultValueMap = new HashMap<>();
        int resultValue = 0;
        StringBuffer sb = new StringBuffer();

        if (fv.getOperator().equals(FORMULA_OPERATOR_ADD)) {
            for (int m = 0; m < fv.getConditionKeys().size(); m++) {
                resultValue += Integer.parseInt(conditionValueMap.get(fv.getConditionKeys().get(m)));
            }
            fv.setResultValue(String.valueOf(resultValue));
            resultValueMap.put(fv.getResultKey(), fv.getResultValue());

        }
        if (fv.getOperator().equals(FORMULA_OPERATOR_SUBSTRACT)) {

        }
        if (fv.getOperator().equals(FORMULA_OPERATOR_MULTIPLY)) {

        }
        if (fv.getOperator().equals(FORMULA_OPERATOR_DIVIDED)) {

        }
        if (fv.getOperator().equals(FORMULA_OPERATOR_CHOOSE)) {
            resultValueMap.putAll(calculateMethod(fv, conditionValueMap));

        }
        return resultValueMap;
    }

    /**
     * StringBuffer 转化 String
     * @param sb
     * @return
     */
    public static String trans(StringBuffer sb) {
        String s = new String(sb);
        s = s.substring(0, s.length() - 1);
        return s;
    }

}

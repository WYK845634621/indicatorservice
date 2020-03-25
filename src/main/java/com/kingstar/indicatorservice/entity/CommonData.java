package com.kingstar.indicatorservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author yikai.wang
 * @since 2019-09-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private Date createTime;
    private Date modifyTime;
    private Integer dataStatus;


    private String amount;
    private String statusCode;  //图形编码号 默认是朝下的

    private String nodesJson;     //曲线图十三个数字的json   只有涉及到曲线图,这个字段才不会是空的

    private String abnormalAmount;  //异常数量
    private String parentId;        //上层节点的id
    private String upStatusCode;    //跟上一层的状态
    private String depth;           //深度


    public CommonData(String amount, String statusCode) {
        this.amount = amount;
        this.statusCode = statusCode;
    }

    public CommonData(String name, String amount, String statusCode) {
        this.name = name;
        this.amount = amount;
        this.statusCode = statusCode;
    }

    public CommonData(String name) {
        this.name = name;
    }


    public CommonData(String id, String amount, String statusCode, String abnormalAmount) {
        this.id = id;
        this.amount = amount;
        this.statusCode = statusCode;
        this.abnormalAmount = abnormalAmount;
    }

    public CommonData(String id, String name, String statusCode, String parentId, String upStatusCode, String depth) {
        this.id = id;
        this.name = name;
        this.statusCode = statusCode;
        this.parentId = parentId;
        this.upStatusCode = upStatusCode;
        this.depth = depth;
    }
}

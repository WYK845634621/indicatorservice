package com.kingstar.indicatorservice.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description 前端存储key的表
 * @Tips
 * @Author yikai.wang
 * @Date 2020/1/3 9:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("indicatorservice.instruction")
public class Instruction {
    private String id;
    private Integer dataStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;
    private String pre;
    private String remark;


}

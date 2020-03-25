package com.kingstar.indicatorservice.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

/**
 * @Description
 * @Tips
 * @Author yikai.wang
 * @Date 2020/1/2 10:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("indicatorservice.wordbook")
public class Wordbook {
    private String id;
    private Integer dataStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;
    private String pre;
    private String suf;
    /**
     * @Note: 会议决定使用配置文件决定执行插入或更新操作  所以这个字段暂时没用
     * @Date:2020/1/3 16:55 @Auth:yikai.wang @Desc(V):
     */
    private Long timeprt;


    public Wordbook(String pre, String suf) {
        this.pre = pre;
        this.suf = suf;
        this.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        this.setCreateTime(new Date());
        this.setModifyTime(new Date());
        this.setDataStatus(1);
    }

}

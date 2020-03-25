package com.kingstar.indicatorservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description
 * @Tips
 * @Author yikai.wang
 * @Date 2020/3/24 17:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScreenSetting {
    private String id;
    private Integer dataStatus;
    private Date createTime;
    private Date modifyTime;
    private String name;
    private String param;
    private Integer drag;
    private Integer style;
    private Integer data;
}

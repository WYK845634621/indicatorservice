package com.kingstar.indicatorservice.pojo;

import com.kingstar.indicatorservice.entity.CommonData;

/**
 * @Description
 * @Tips
 * @Author yikai.wang
 * @Date 2019/9/7 17:05
 */
public class CommonDataPojo extends CommonData {
    private String[] nodes; //曲线图十三个数字

    public CommonDataPojo() {
    }

    public String[] getNodes() {
        return nodes;
    }

    public void setNodes(String[] nodes) {
        this.nodes = nodes;
    }

    public CommonDataPojo(String name,String[] nodes) {
        super.setName(name);
        this.nodes = nodes;
    }
}

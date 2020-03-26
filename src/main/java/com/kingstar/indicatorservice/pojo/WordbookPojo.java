package com.kingstar.indicatorservice.pojo;

import com.kingstar.indicatorservice.entity.Wordbook;

/**
 * @Description
 * @Tips
 * @Author yikai.wang
 * @Date 2020/1/2 15:50
 */
public class WordbookPojo extends Wordbook {
    private Long after;
    private Long front;

    public Long getAfter() {
        return after;
    }

    public void setAfter(Long after) {
        this.after = after;
    }

    public Long getFront() {
        return front;
    }

    public void setFront(Long front) {
        this.front = front;
    }

    public WordbookPojo(String pre,Long after, Long front) {
        super.setPre(pre);
        this.after = after;
        this.front = front;
    }

    public WordbookPojo() {
    }
}

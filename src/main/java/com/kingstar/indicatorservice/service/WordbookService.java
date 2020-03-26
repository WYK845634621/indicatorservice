package com.kingstar.indicatorservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kingstar.indicatorservice.entity.Wordbook;
import com.kingstar.indicatorservice.pojo.WordbookPojo;

/**
 * @Description
 * @Tips
 * @Author yikai.wang
 * @Date 2020/1/2 10:46
 */
public interface WordbookService  extends IService<Wordbook> {
    Wordbook queryOne(WordbookPojo pojo);

    Wordbook queryOneByPre(String key);
}

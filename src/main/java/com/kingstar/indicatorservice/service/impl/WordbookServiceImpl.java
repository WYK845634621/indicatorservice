package com.kingstar.indicatorservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kingstar.indicatorservice.entity.Wordbook;
import com.kingstar.indicatorservice.mapper.WordbookMapper;
import com.kingstar.indicatorservice.pojo.WordbookPojo;
import com.kingstar.indicatorservice.service.WordbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Tips
 * @Author yikai.wang
 * @Date 2020/1/2 10:47
 */
@Service
public class WordbookServiceImpl  extends ServiceImpl<WordbookMapper, Wordbook> implements WordbookService {

    @Autowired
    private WordbookMapper mapper;
    @Override
    public Wordbook queryOne(WordbookPojo pojo) {
        return mapper.queryOne(pojo);
    }

    @Override
    public Wordbook queryOneByPre(String key) {

        return mapper.queryOneByPre(key);
    }
}

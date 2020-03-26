package com.kingstar.indicatorservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kingstar.indicatorservice.entity.Wordbook;
import com.kingstar.indicatorservice.pojo.WordbookPojo;
import org.springframework.stereotype.Repository;

/**
 * @Description
 * @Tips
 * @Author yikai.wang
 * @Date 2020/1/2 10:45
 */
@Repository
public interface WordbookMapper extends BaseMapper<Wordbook> {

    Wordbook queryOne(WordbookPojo pojo);

    Wordbook queryOneByPre(String key);
}

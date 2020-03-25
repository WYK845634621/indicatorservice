package com.kingstar.indicatorservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kingstar.indicatorservice.entity.ScreenSetting;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description
 * @Tips
 * @Author yikai.wang
 * @Date 2020/3/24 17:14
 */
@Repository
public interface ScreenSettingMapper extends BaseMapper<ScreenSetting> {
    List<ScreenSetting> queryAll();

    ScreenSetting queryOne(ScreenSetting screenSetting);
}

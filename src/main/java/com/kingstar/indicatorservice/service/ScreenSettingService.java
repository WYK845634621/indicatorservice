package com.kingstar.indicatorservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kingstar.indicatorservice.entity.ScreenSetting;
import com.kingstar.indicatorservice.vo.ResultVo;

import java.util.List;

/**
 * @Description
 * @Tips
 * @Author yikai.wang
 * @Date 2020/2/17 9:22
 */
public interface ScreenSettingService extends IService<ScreenSetting> {

    ResultVo<String> addOne(ScreenSetting screenSetting);

    ResultVo<List<ScreenSetting>> queryAll();

    ResultVo<String> updateOne(ScreenSetting screenSetting);

    ResultVo<ScreenSetting> queryOne(ScreenSetting screenSetting);
}

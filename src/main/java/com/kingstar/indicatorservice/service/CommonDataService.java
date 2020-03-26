package com.kingstar.indicatorservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kingstar.indicatorservice.entity.CommonData;
import com.kingstar.indicatorservice.vo.ResultVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yikai.wang
 * @since 2019-09-06
 */
public interface CommonDataService extends IService<CommonData> {

    ResultVo<Object> commonQuery(String condition, String formula);


}

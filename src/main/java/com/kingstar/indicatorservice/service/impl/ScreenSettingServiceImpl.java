package com.kingstar.indicatorservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kingstar.indicatorservice.entity.ScreenSetting;
import com.kingstar.indicatorservice.mapper.ScreenSettingMapper;
import com.kingstar.indicatorservice.service.ScreenSettingService;
import com.kingstar.indicatorservice.vo.ResultVo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @Description
 * @Tips
 * @Author yikai.wang
 * @Date 2020/2/17 9:22
 */
@Service
public class ScreenSettingServiceImpl extends ServiceImpl<ScreenSettingMapper, ScreenSetting> implements ScreenSettingService {


    private static Log logger = LogFactory.getLog(ScreenSettingServiceImpl.class);

    @Autowired
    private ScreenSettingMapper screenSettingMapper;

    @Override
    public ResultVo<String> addOne(ScreenSetting screenSetting) {
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            screenSetting.setId(UUID.randomUUID().toString().replaceAll("-",""));
            screenSetting.setCreateTime(new Date());
            screenSetting.setModifyTime(new Date());
            screenSetting.setDataStatus(1);
            logger.info("serviceimpl add...");
            screenSettingMapper.insert(screenSetting);
            resultVo.setCode(ResultVo.CODE_SUCCESS);
            resultVo.setMsg(ResultVo.MSG_SUCCESS);
        }catch (Exception e){
            logger.error("error in ScreenSettingServiceImpl.addOne",e);
            resultVo.setCode(ResultVo.CODE_FAIL);
            resultVo.setMsg(ResultVo.MSG_FAIL);
        }

        return resultVo;
    }

    @Override
    public ResultVo<List<ScreenSetting>> queryAll() {
        ResultVo<List<ScreenSetting>> resultVo = new ResultVo<>();
        try {
            List<ScreenSetting> list = screenSettingMapper.queryAll();
            if (CollectionUtils.isEmpty(list)){
                resultVo.setCode(ResultVo.CODE_SUCCESS);
                resultVo.setMsg("暂无记录");
                return resultVo;
            }
            resultVo.setResult(list);
            resultVo.setCode(ResultVo.CODE_SUCCESS);
            resultVo.setMsg(ResultVo.MSG_SUCCESS);
        }catch (Exception e){
            logger.error("error in ScreenSettingServiceImpl.queryAll",e);
            resultVo.setCode(ResultVo.CODE_FAIL);
            resultVo.setMsg(ResultVo.MSG_FAIL);
        }
        return resultVo;

    }

    @Override
    public ResultVo<String> updateOne(ScreenSetting screenSetting) {
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            ScreenSetting exist = screenSettingMapper.queryOne(screenSetting);
            if (Objects.isNull(exist)){
                resultVo.setCode(ResultVo.CODE_FAIL);
                resultVo.setMsg("此ID记录不存在");
                return resultVo;
            }
            screenSetting.setModifyTime(new Date());
            screenSettingMapper.updateById(screenSetting);
            resultVo.setCode(ResultVo.CODE_SUCCESS);
            resultVo.setMsg(ResultVo.MSG_SUCCESS);
        }catch (Exception e){
            logger.error("error in ScreenSettingServiceImpl.updateOne",e);
            resultVo.setCode(ResultVo.CODE_FAIL);
            resultVo.setMsg(ResultVo.MSG_FAIL);
        }
        return resultVo;
    }

    @Override
    public ResultVo<ScreenSetting> queryOne(ScreenSetting screenSetting) {
        ResultVo<ScreenSetting> resultVo = new ResultVo<>();
        try {
            ScreenSetting exist = screenSettingMapper.queryOne(screenSetting);
            if (Objects.isNull(exist)){
                resultVo.setCode(ResultVo.CODE_FAIL);
                resultVo.setMsg("此ID记录不存在");
                return resultVo;
            }
            resultVo.setResult(exist);
            resultVo.setCode(ResultVo.CODE_SUCCESS);
            resultVo.setMsg(ResultVo.MSG_SUCCESS);
        }catch (Exception e){
            logger.error("error in ScreenSettingServiceImpl.queryOne",e);
            resultVo.setCode(ResultVo.CODE_FAIL);
            resultVo.setMsg(ResultVo.MSG_FAIL);
        }
        return resultVo;
    }
}

package com.kingstar.indicatorservice.web;

import com.kingstar.indicatorservice.entity.ScreenSetting;
import com.kingstar.indicatorservice.service.ScreenSettingService;
import com.kingstar.indicatorservice.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @Description
 * @Tips
 * @Author yikai.wang
 * @Date 2020/2/17 9:20
 */
@RestController
@RequestMapping("/indicatorservice/screensetting")
@Slf4j
public class ScreenSettingController {

    @Autowired
    private ScreenSettingService screenSettingService;

    @GetMapping("/hello")
    public String hello(){
        log.info("controller");
        return "hello";
    }

    @PostMapping("/addOne")
    public ResultVo<String> addOne(@RequestBody ScreenSetting screenSetting){
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            if (Objects.isNull(screenSetting)){
                resultVo.setCode(ResultVo.CODE_FAIL);
                resultVo.setMsg("入参为空");
                return resultVo;
            }
            if (StringUtils.isEmpty(screenSetting.getName())){
                resultVo.setCode(ResultVo.CODE_FAIL);
                resultVo.setMsg("大屏名称为空");
                return resultVo;
            }

            resultVo = screenSettingService.addOne(screenSetting);
            log.info("controller.add");
        }catch (Exception e){
            log.error("error in ScreenSettingController.addOne",e);
            resultVo.setCode(ResultVo.CODE_FAIL);
            resultVo.setMsg(ResultVo.MSG_FAIL);
        }

        return resultVo;
    }

    //查所有
    @PostMapping("/queryAll")
    public ResultVo<List<ScreenSetting>> queryAll(){
        ResultVo<List<ScreenSetting>> resultVo = new ResultVo<>();
        try {
            resultVo = screenSettingService.queryAll();
        }catch (Exception e){
            log.error("error in ScreenSettingController.queryAll",e);
            resultVo.setCode(ResultVo.CODE_FAIL);
            resultVo.setMsg(ResultVo.MSG_FAIL);
        }
        return resultVo;
    }


    /**
     * @Note:
     * @Date:2020/2/17 10:01 @Auth:yikai.wang @Desc(V): 根据ID更新,删除的话传datasttatus为0
     */
    @PostMapping("/updateOne")
    public ResultVo<String> updateOne(@RequestBody ScreenSetting screenSetting){
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            if (Objects.isNull(screenSetting)){
                resultVo.setCode(ResultVo.CODE_FAIL);
                resultVo.setMsg("入参为空");
                return resultVo;
            }
            if (StringUtils.isEmpty(screenSetting.getId())){
                resultVo.setCode(ResultVo.CODE_FAIL);
                resultVo.setMsg("ID为空");
                return resultVo;
            }
            if (StringUtils.isEmpty(screenSetting.getName())){
                resultVo.setCode(ResultVo.CODE_FAIL);
                resultVo.setMsg("大屏名称为空");
                return resultVo;
            }
            resultVo = screenSettingService.updateOne(screenSetting);
        }catch (Exception e){
            log.error("error in ScreenSettingController.updateOne",e);
            resultVo.setCode(ResultVo.CODE_FAIL);
            resultVo.setMsg(ResultVo.MSG_FAIL);
        }
        return resultVo;
    }


    /**
     * @Date:2020/2/17 10:36 @Auth:yikai.wang @Desc(V): 根据ID查询单个
     */
    @PostMapping("/queryOne")
    public ResultVo<ScreenSetting> queryOne(@RequestBody ScreenSetting screenSetting){
        ResultVo<ScreenSetting> resultVo = new ResultVo<>();
        try {
            if (Objects.isNull(screenSetting)){
                resultVo.setCode(ResultVo.CODE_FAIL);
                resultVo.setMsg("入参为空");
                return resultVo;
            }
            if (StringUtils.isEmpty(screenSetting.getId())){
                resultVo.setCode(ResultVo.CODE_FAIL);
                resultVo.setMsg("ID为空");
                return resultVo;
            }
            resultVo = screenSettingService.queryOne(screenSetting);
        }catch (Exception e){
            log.error("error in ScreenSettingController.queryOne",e);
            resultVo.setCode(ResultVo.CODE_FAIL);
            resultVo.setMsg(ResultVo.MSG_FAIL);
        }
        return resultVo;
    }


}

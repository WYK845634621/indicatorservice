package com.kingstar.indicatorservice.web;


import com.kingstar.indicatorservice.entity.CommonData;
import com.kingstar.indicatorservice.pojo.ParamPojo;
import com.kingstar.indicatorservice.service.CommonDataService;
import com.kingstar.indicatorservice.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yikai.wang
 * @since 2019-09-06
 */
@RestController
@RequestMapping("/indicatorservice/commonData")
@Slf4j
public class CommonDataController {



    @Autowired
    private CommonDataService commonDataService;




    /**
     * @Note: 目前用的
     * @Date:2019/12/4 15:59 @Auth:yikai.wang @Desc(V): 1.0.7
     */
    @PostMapping(value = {"/commonQuery"})
    public ResultVo<Object> commonQuery(HttpServletRequest request, HttpServletResponse response, @RequestBody ParamPojo paramPojo) {
        ResultVo<Object> resultVo = new ResultVo<>();
        try {
            if (StringUtils.isEmpty(paramPojo.getCondition())) {
                resultVo.setMsg("入参不能为空");
                resultVo.setCode(ResultVo.CODE_FAIL);
                return resultVo;
            }

            resultVo = commonDataService.commonQuery(paramPojo.getCondition(),paramPojo.getFormula());
        } catch (Exception e) {
            log.error("error in AnytestController.queryAnyOne", e);
            resultVo.setCode(ResultVo.CODE_FAIL);
            resultVo.setMsg("接口异常,无法正常返回");
        }
        return resultVo;
    }


    @PostMapping(value = {"/test"})
    public ResultVo<Object> test(HttpServletRequest request, HttpServletResponse response) {
        ResultVo<Object> resultVo = new ResultVo<>();
        try {
            String[] nodes = new String[]{Math.random() * 500 + "", Math.random() * 500 + "", Math.random() * 500 + "", Math.random() * 500 + "",
                    Math.random() * 500 + "", Math.random() * 500 + "", Math.random() * 500 + "", Math.random() * 500 + "", Math.random() * 500 + "",
                    Math.random() * 500 + "", Math.random() * 500 + "", Math.random() * 500 + "", Math.random() * 500 + ""};
            //io曲线图颜色由前端定义
            CommonData commonData = new CommonData();
            commonData.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            commonData.setNodesJson("hello json");
            commonDataService.save(commonData);
            resultVo.setCode(ResultVo.CODE_SUCCESS);
            resultVo.setMsg(ResultVo.MSG_SUCCESS);
        } catch (Exception e) {
            log.error("error in AnytestController.queryAnyOne", e);
            resultVo.setCode(ResultVo.CODE_FAIL);
            resultVo.setMsg("接口异常,无法正常返回");
        }
        return resultVo;
    }

    @PostMapping("/tttt")
    public String t(){
        CommonData commonData = new CommonData();
        commonData.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        log.info("正在插入");
        commonDataService.save(commonData);
        return "成功";
    }


    @PostMapping("/all")
    public List<CommonData> all(){
        return commonDataService.list(null);
    }

}


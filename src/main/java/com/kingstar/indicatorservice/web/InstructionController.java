package com.kingstar.indicatorservice.web;

import com.kingstar.indicatorservice.entity.Instruction;
import com.kingstar.indicatorservice.pojo.InstructionPojo;
import com.kingstar.indicatorservice.service.InstructionService;
import com.kingstar.indicatorservice.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @Description
 * @Tips
 * @Author yikai.wang
 * @Date 2020/1/3 9:55
 */
@RestController
@RequestMapping("/indicatorservice/instruction")
@Slf4j
public class InstructionController {


    @Autowired
    private InstructionService instructionService;

    @PostMapping("/addOne")
    public ResultVo<String> addOne(@RequestBody Instruction instruction){
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            if (Objects.isNull(instruction)){
                resultVo.setMsg("入参为空");
                resultVo.setCode(ResultVo.CODE_FAIL);
                return resultVo;
            }
            if (StringUtils.isEmpty(instruction.getPre())){
                resultVo.setMsg("pre为空");
                resultVo.setCode(ResultVo.CODE_FAIL);
                return resultVo;
            }
            instruction.setId(UUID.randomUUID().toString().replaceAll("-",""));
            instruction.setCreateTime(new Date());
            instruction.setModifyTime(new Date());
            instruction.setDataStatus(1);
            instructionService.save(instruction);
            resultVo.setMsg(ResultVo.MSG_SUCCESS);
            resultVo.setCode(ResultVo.CODE_SUCCESS);
        }catch (Exception e){
            log.error("error in InstructionController.addOne",e);
        }

        return resultVo;
    }


    /**
     * id或者pre必填一个
     * @param instruction
     * @return
     */
    @PostMapping("/queryOne")
    public ResultVo<Instruction> queryOne(@RequestBody Instruction instruction){
        ResultVo<Instruction> resultVo = new ResultVo<>();
        try {
            if (Objects.isNull(instruction)){
                resultVo.setMsg("入参为空");
                resultVo.setCode(ResultVo.CODE_FAIL);
                return resultVo;
            }
            if (StringUtils.isEmpty(instruction.getId()) && StringUtils.isEmpty(instruction.getPre())){
                resultVo.setMsg("id和pre都为空");
                resultVo.setCode(ResultVo.CODE_FAIL);
                return resultVo;
            }
            resultVo = instructionService.queryOne(instruction);
        }catch (Exception e){
            log.error("error in InstructionController.queryOne",e);
        }

        return resultVo;
    }


    /**
     * 根据id修改 删除也用这个
     * @param instruction
     * @return
     */
    @PostMapping("/updateOne")
    public ResultVo<String> updateOne(@RequestBody Instruction instruction){
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            if (Objects.isNull(instruction)){
                resultVo.setMsg("入参为空");
                resultVo.setCode(ResultVo.CODE_FAIL);
                return resultVo;
            }
            if (StringUtils.isEmpty(instruction.getId())){
                resultVo.setMsg("id为空");
                resultVo.setCode(ResultVo.CODE_FAIL);
                return resultVo;
            }

            resultVo = instructionService.updateOne(instruction);
        }catch (Exception e){
            log.error("error in InstructionController.updateOne",e);
        }

        return resultVo;
    }


    //这里是查数据库获取的所有
    @PostMapping("/queryAll")
    public ResultVo<List<InstructionPojo>> queryAll(){
        ResultVo<List<InstructionPojo>> resultVo = new ResultVo<>();
        try {
            resultVo = instructionService.queryAll();
        }catch (Exception e){
            log.error("error in InstructionController.queryAll",e);
        }

        return resultVo;
    }


    @PostMapping("/test")
    public ResultVo<String> test(){
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            resultVo = instructionService.test();
        }catch (Exception e){
            log.error("error in InstructionController.test",e);
        }

        return resultVo;
    }


    //这里是redis获取的所有
    @Deprecated
    @PostMapping("/all")
    public ResultVo<Map<String,String>> all(){
        ResultVo<Map<String,String>> resultVo = new ResultVo<>();
        try {
            resultVo = instructionService.all();
        }catch (Exception e){
            log.error("error in InstructionController.all",e);
        }

        return resultVo;
    }


}

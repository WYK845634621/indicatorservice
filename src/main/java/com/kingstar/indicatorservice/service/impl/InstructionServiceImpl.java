package com.kingstar.indicatorservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kingstar.indicatorservice.entity.Instruction;
import com.kingstar.indicatorservice.mapper.InstructionMapper;
import com.kingstar.indicatorservice.pojo.InstructionPojo;
import com.kingstar.indicatorservice.service.InstructionService;
import com.kingstar.indicatorservice.util.RedisUtil;
import com.kingstar.indicatorservice.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @Description
 * @Tips
 * @Author yikai.wang
 * @Date 2020/1/3 9:54
 */
@Service
@Slf4j
public class InstructionServiceImpl extends ServiceImpl<InstructionMapper, Instruction> implements InstructionService {


    @Autowired
    private InstructionMapper instructionMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Value("${custom.prefix.Oriental_Securities}")
    private String Oriental_Securities;


    @Override
    public ResultVo<Instruction> queryOne(Instruction instruction) {
        ResultVo<Instruction> resultVo = new ResultVo<>();
        try {
            Instruction result = instructionMapper.queryOne(instruction);
            if (Objects.isNull(result)){
                resultVo.setMsg("数据库无记录");
                resultVo.setCode(ResultVo.CODE_FAIL);
                return resultVo;
            }
            resultVo.setMsg(ResultVo.MSG_SUCCESS);
            resultVo.setCode(ResultVo.CODE_SUCCESS);
            resultVo.setResult(result);
        }catch (Exception e){
            log.error("error in InstructionServiceImpl.queryOne",e);
            resultVo.setMsg(e.getLocalizedMessage());
            resultVo.setCode(ResultVo.CODE_FAIL);
        }
        return resultVo;
    }

    @Override
    public ResultVo<String> updateOne(Instruction instruction) {
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            Instruction result = instructionMapper.queryOneById(instruction);
            if (Objects.isNull(result)){
                resultVo.setMsg("数据库无记录,检查入参");
                resultVo.setCode(ResultVo.CODE_FAIL);
                return resultVo;
            }
            instruction.setModifyTime(new Date());
            instructionMapper.updateById(instruction);
            resultVo.setMsg(ResultVo.MSG_SUCCESS);
            resultVo.setCode(ResultVo.CODE_SUCCESS);
        }catch (Exception e){
            log.error("error in InstructionServiceImpl.updateOne",e);
            resultVo.setMsg(e.getLocalizedMessage());
            resultVo.setCode(ResultVo.CODE_FAIL);
        }
        return resultVo;
    }

    @Override
    public ResultVo<List<InstructionPojo>> queryAll() {
        ResultVo<List<InstructionPojo>> resultVo = new ResultVo<>();
        try {
            List<InstructionPojo> instructions = instructionMapper.queryAll();
            log.info("--------查询所有,数量为: " + instructions.size());
            if (CollectionUtils.isEmpty(instructions)){
                resultVo.setMsg("数据库无记录");
                resultVo.setCode(ResultVo.CODE_FAIL);
                return resultVo;
            }
            resultVo.setMsg(ResultVo.MSG_SUCCESS);
            resultVo.setCode(ResultVo.CODE_SUCCESS);
            resultVo.setResult(instructions);
        }catch (Exception e){
            log.error("error in InstructionServiceImpl.queryAll",e);
            resultVo.setMsg(e.getLocalizedMessage());
            resultVo.setCode(ResultVo.CODE_FAIL);
        }
        return resultVo;
    }




    @Override
    public ResultVo<String> test() {
        ResultVo<String> resultVo = new ResultVo<>();
        try {
            List<Instruction> list = new ArrayList<>();
            Set<String> keys = redisUtil.hkeys(Oriental_Securities);
            for (String key : keys){
                Instruction instruction = new Instruction(UUID.randomUUID().toString().replaceAll("-",""),key);
                list.add(instruction);
            }
            this.saveBatch(list);
            resultVo.setMsg(ResultVo.MSG_SUCCESS);
            resultVo.setCode(ResultVo.CODE_SUCCESS);
        }catch (Exception e){
            log.error("error in InstructionServiceImpl.test",e);
            resultVo.setMsg(e.getLocalizedMessage());
            resultVo.setCode(ResultVo.CODE_FAIL);
        }

        return resultVo;
    }

    @Override
    public ResultVo<Map<String, String>> all() {
        ResultVo<Map<String, String>> resultVo = new ResultVo<>();
        try{
            Map<String, String> map = redisUtil.hgetAll(Oriental_Securities);
            if (CollectionUtils.isEmpty(map)){
                resultVo.setMsg("redis为空");
                resultVo.setCode(ResultVo.CODE_FAIL);
            }
            resultVo.setResult(map);
            log.info(map.size() + "//////////////////////");
            resultVo.setMsg(ResultVo.MSG_SUCCESS);
            resultVo.setCode(ResultVo.CODE_SUCCESS);
        }catch (Exception e){
            log.error("error in InstructionServiceImpl.all",e);
        }
        return resultVo;
    }
}

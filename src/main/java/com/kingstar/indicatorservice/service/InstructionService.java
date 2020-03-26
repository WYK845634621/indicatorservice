package com.kingstar.indicatorservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kingstar.indicatorservice.entity.Instruction;
import com.kingstar.indicatorservice.pojo.InstructionPojo;
import com.kingstar.indicatorservice.vo.ResultVo;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Tips
 * @Author yikai.wang
 * @Date 2020/1/3 9:53
 */
public interface InstructionService extends IService<Instruction> {

    ResultVo<Instruction> queryOne(Instruction instruction);

    ResultVo<String> updateOne(Instruction instruction);

    ResultVo<List<InstructionPojo>> queryAll();

    ResultVo<String> test();

    ResultVo<Map<String, String>> all();
}

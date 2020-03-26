package com.kingstar.indicatorservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kingstar.indicatorservice.entity.Instruction;
import com.kingstar.indicatorservice.pojo.InstructionPojo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description
 * @Tips
 * @Author yikai.wang
 * @Date 2020/1/3 9:52
 */
@Repository
public interface InstructionMapper extends BaseMapper<Instruction> {
    Instruction queryOne(Instruction instruction);


    Instruction queryOneById(Instruction instruction);

    List<InstructionPojo> queryAll();

}

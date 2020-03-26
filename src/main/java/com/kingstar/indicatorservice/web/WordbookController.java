package com.kingstar.indicatorservice.web;

import com.kingstar.indicatorservice.entity.Wordbook;
import com.kingstar.indicatorservice.service.WordbookService;
import com.kingstar.indicatorservice.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Description
 * @Tips
 * @Author yikai.wang
 * @Date 2020/1/2 10:48
 */
@RestController
@RequestMapping("/indicatorservice/wordbook")
@Slf4j
public class WordbookController {



    @Autowired
    private WordbookService service;

    @PostMapping("/test")
    public String test(){
        return "hello";
    }

    @PostMapping("/add")
    public ResultVo<String> add(@RequestBody Wordbook wordbook){
        ResultVo<String> resultVo = new ResultVo<>();
        wordbook.setId(UUID.randomUUID().toString().replaceAll("-",""));
        wordbook.setCreateTime(new Date());
        wordbook.setModifyTime(new Date());
        wordbook.setDataStatus(1);
        wordbook.setTimeprt(System.currentTimeMillis());
        service.save(wordbook);
        resultVo.setCode(ResultVo.CODE_SUCCESS);
        resultVo.setMsg(ResultVo.MSG_SUCCESS);
        return resultVo;
    }


    @PostMapping("/all")
    public List<Wordbook> all(){
        return service.list(null);
    }
}

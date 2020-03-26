package com.kingstar.indicatorservice.task;

import com.kingstar.indicatorservice.entity.Wordbook;
import com.kingstar.indicatorservice.service.WordbookService;
import com.kingstar.indicatorservice.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Description
 * @Tips
 * @Author yikai.wang
 * @Date 2020/1/2 15:04
 */
@EnableScheduling
@Component
@Slf4j
public class SaveScheduledTask {


    @Autowired
    private RedisUtil redisUtil;
    @Value("${custom.prefix.Oriental_Securities}")
    private String Oriental_Securities;

    @Value("${custom.prefix.operate_is_insert}")
    private Integer operate_is_insert;

    @Autowired
    private WordbookService wordbookService;

    @Scheduled(cron = "0 0/5 * * * ?")
    @Transactional
    public void operate() {
        try {
            Set<String> keys = redisUtil.hkeys(Oriental_Securities);
            if (1 == operate_is_insert){
                log.info("开始时间:  " + System.currentTimeMillis());
                List<Wordbook> list = new ArrayList<>();
                for (String key : keys) {
                    Wordbook wordbook = new Wordbook(key,redisUtil.hget(Oriental_Securities,key));
                    list.add(wordbook);
                }
                wordbookService.saveBatch(list);
                operate_is_insert = 0;
                log.info("批量插入成功....." + "结束时间:  " + System.currentTimeMillis());
            }else if (2 == operate_is_insert){
                Thread.currentThread().sleep(3000);
                //更新操作
                List<Wordbook> list = new ArrayList<>();
                for (String key : keys) {
                    Wordbook exist = wordbookService.queryOneByPre(key);
                    if (!Objects.isNull(exist)) {
                        exist.setModifyTime(new Date());
                        String v = redisUtil.hget(Oriental_Securities,key);
                        exist.setSuf(v == null ? " " : v);
                        list.add(exist);
                    }else {
                        log.warn("记录不存在,key为=====" +key);
                    }
                }
                wordbookService.updateBatchById(list);
                log.info("批量更新完成......" + new Date());
            }else {
                log.info("进入待机状态.....");
            }
//            for (String key : keys) {
//                Long now = System.currentTimeMillis();
//                //前后延迟30秒
//                Long after = now + 61 * 1000;
//                Long front = now - 61 * 1000;
//                WordbookPojo pojo = new WordbookPojo(key, after, front);
//                Wordbook exist = wordbookService.queryOne(pojo);
//                if (!Objects.isNull(exist)) {
//                    exist.setModifyTime(new Date());
//                    exist.setSuf("update");
//                    exist.setTimeprt(now);
//                    wordbookService.updateById(exist);
//                    log.info("定时更新完成......" + new Date());
//                } else {
//                    Wordbook wordbook = new Wordbook();
//                    wordbook.setId(UUID.randomUUID().toString().replaceAll("-", ""));
//                    //这里的date格式没有问题
//                    wordbook.setCreateTime(new Date());
//                    wordbook.setModifyTime(new Date());
//                    wordbook.setPre(key);
//                    wordbook.setSuf(redisUtil.get(key));
//                    wordbook.setDataStatus(1);
//                    wordbook.setTimeprt(now);
//                    wordbookService.insert(wordbook);
//                    log.info("定时插入完成......" + new Date());
//                }
//
//            }
        } catch (Exception e) {
            log.error("error in SaveScheduledTask.operate", e);
        }

    }

}

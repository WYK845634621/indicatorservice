package com.kingstar.indicatorservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kingstar.indicatorservice.entity.CommonData;
import com.kingstar.indicatorservice.entity.Wordbook;
import com.kingstar.indicatorservice.enums.Constant;
import com.kingstar.indicatorservice.mapper.CommonDataMapper;
import com.kingstar.indicatorservice.pojo.CommonDataPojo;
import com.kingstar.indicatorservice.service.CommonDataService;
import com.kingstar.indicatorservice.service.WordbookService;
import com.kingstar.indicatorservice.util.RedisUtil;
import com.kingstar.indicatorservice.util.StringUtil;
import com.kingstar.indicatorservice.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yikai.wang
 * @since 2019-09-06
 */
@Service
@EnableScheduling
@Slf4j
public class CommonDataServiceImpl extends ServiceImpl<CommonDataMapper, CommonData> implements CommonDataService {


    @Autowired
    private RedisUtil redisUtil;
    @Value("${custom.prefix.Oriental_Securities}")
    private String Oriental_Securities;
    @Autowired
    private WordbookService wordbookService;


    /**
     * @param condition
     * @param formula
     * @return
     */
    @Override
    public ResultVo<Object> commonQuery(String condition, String formula) {
        ResultVo<Object> resultVo = new ResultVo<>();
        Map<String, String> result = new HashMap<>();

        /**
         * 加减乘只用到第一位   选择用到第二位   除法用到三四位
         */
        // 解析 formula 获取新key    多个公式可以用逗号隔开
        try {
            if (!StringUtil.isEmpty(formula)) {
                String[] fors = formula.split(",");
                for (String f : fors){
                    String[] split = f.split("@");
                    String finalKey = split[0];
                    int len = split.length;
                    char operate = split[len - 1].charAt(0);
                    char method = split[len - 1].charAt(1);
                    int[] vs = null;
                    StringBuffer finalValue = new StringBuffer();
                    if (Constant.OP_ADD == operate) {

                        add(split, len, vs, finalValue);

                    } else if (Constant.OP_SUBSTRACT == operate) {

                        subtract(split, len, vs, finalValue);

                    } else if (Constant.OP_MULTIPLY == operate) {

                        multiply(split, len, vs, finalValue);

                    } else if (Constant.OP_DIVIDED == operate) {

                        divide(split, len, finalValue);

                    } else if (Constant.OP_CHOOSE == operate) {

                        choose(split, len, method, finalValue);

                    } else {
                        resultVo.setCode(ResultVo.CODE_FAIL);
                        resultVo.setMsg("操作符有误");
                        return resultVo;
                    }
                    result.put(finalKey, finalValue.toString());
                    redisUtil.hset(Oriental_Securities, finalKey, finalValue.toString());
                }


            }

            String[] keys = condition.split(",");
            for (String key: keys){
                String v = redisUtil.hget(Oriental_Securities, key);
                if (StringUtils.isEmpty(v)){
                    log.error("redis no value, the key is : ---" + key);
                    Wordbook exist = wordbookService.queryOneByPre(key);
                    if (!Objects.isNull(exist)) {
                        String vv = exist.getSuf();
                        log.info("the data from db is :    " + vv);
                        result.put(key,vv);
                    }
                }else {
                    result.put(key,v);
                }
            }
            resultVo.setResult(result);
            resultVo.setCode(ResultVo.CODE_SUCCESS);
            resultVo.setMsg(ResultVo.MSG_SUCCESS);
        } catch (Exception e) {
            log.error("error in CommonDataServiceImpl.commonQuery", e);
        }

        return resultVo;
    }

    //选择操作
    private void choose(String[] split, int len, char method, StringBuffer finalValue) throws Exception {
        String v = redisUtil.hget(Oriental_Securities, split[1]);
        if (StringUtils.isEmpty(v)){
            log.error(split[1] + " 在redis无值");
        }
        String[] original = v.split(",");
        if (Constant.M_CONTINUE == method){
            int index1 = Integer.valueOf(split[2]);
            int index2 = Integer.valueOf(split[3]);
            for (int i = index1-1; i < index2 ; i++) {
                finalValue.append(original[i] + ",");
            }
        }else if (Constant.M_JUMP == method){
            for (int i = 2; i < len - 1; i++) {
                int index = Integer.valueOf(split[i]) -1 ;
                finalValue.append(original[index] + ",");
            }
        }
        finalValue.deleteCharAt(finalValue.length()-1);
    }


    //A*B A位数多放前面  可连续乘
    private void multiply(String[] split, int len, int[] vs, StringBuffer finalValue) throws Exception {
        for (int j = 1; j < len - 1; j++) {
            String v = redisUtil.hget(Oriental_Securities, split[j]);
            if (StringUtils.isEmpty(v)){
                log.error(split[j] + " 在redis无值");
            }
            String[] temp = v.split(",");
            int l = temp.length;
            //初始化一次 值取第一个key的
            if (j == 1) {
                vs = new int[l];
                for (int i = 0; i < l; i++) {
                    vs[i] = Integer.valueOf(temp[i]);
                }
            }
            //从第2个开始以后的相乘操作
            if (j > 1) {
                for (int i = 0; i < l; i++) {
                    vs[i] *= Integer.valueOf(temp[i]);
                }
            }
        }
        for (int i = 0; i < vs.length; i++) {
            finalValue.append(vs[i] + ",");
        }
        finalValue.deleteCharAt(finalValue.length()-1);
    }

    //减法操作
    //A-B A放在前面  可以连续减  位数多的放前面
    private void subtract(String[] split, int len, int[] vs, StringBuffer finalValue) throws Exception {
        for (int j = 1; j < len - 1; j++) {
            String v = redisUtil.hget(Oriental_Securities, split[j]);
            if (StringUtils.isEmpty(v)){
                log.error(split[j] + " 在redis无值");
            }
            String[] temp = v.split(",");
            int l = temp.length;
            //初始化一次 值取第一个key的
            if (j == 1) {
                vs = new int[l];
                for (int i = 0; i < l; i++) {
                    vs[i] = Integer.valueOf(temp[i]);
                }
            }
            //从第2个开始以后的相减操作
            if (j > 1) {
                for (int i = 0; i < l; i++) {
                    vs[i] -= Integer.valueOf(temp[i]);
                }
            }
        }
        for (int i = 0; i < vs.length; i++) {
            finalValue.append(vs[i] + ",");
        }
        finalValue.deleteCharAt(finalValue.length()-1);
    }

    //整数操作
    //在执行加操作的时候,如果两个key的位数不想等,则位数多的那一个放在前面  可连续加
    private void add(String[] split, int len, int[] vs, StringBuffer finalValue) throws Exception {
        for (int j = 1; j < len - 1; j++) {
            String v = redisUtil.hget(Oriental_Securities, split[j]);
            if (StringUtils.isEmpty(v)){
                log.error(split[j] + " 在redis无值");
            }
            String[] temp = v.split(",");
            int l = temp.length;
            //初始化一次
            if (j == 1) {
                vs = new int[l];
            }
            for (int i = 0; i < l ; i++){
                vs[i] += Integer.valueOf(temp[i]);
            }
        }
        for (int i = 0; i < vs.length; i++) {
            finalValue.append(vs[i] + ",");
        }
        finalValue.deleteCharAt(finalValue.length()-1);
    }


    //除法运算操作
    //支持连续除 用@分隔开就行 第一位以外不出现0: "test@b_leftcpu@b_rightactive@b_righttableuse@D011"
    private void divide(String[] split, int len, StringBuffer finalValue) throws Exception {
        Double[] dvs = null;
        for (int j = 1; j < len - 1; j++) {
            String v = redisUtil.hget(Oriental_Securities, split[j]);
            if (StringUtils.isEmpty(v)){
                log.error(split[j] + " 在redis无值");
            }
            String[] temp = v.split(",");
            int l = temp.length;
            //初始化一次 值取第一个key的
            if (j == 1) {
                dvs = new Double[l];
                for (int i = 0; i < l; i++) {
                    dvs[i] = Double.valueOf(temp[i]);
                }
            }
            //从第2个开始以后的相除操作
            if (j > 1) {
                for (int i = 0; i < l; i++) {
                    dvs[i] /= Double.valueOf(temp[i]);
                }
            }
        }
        DecimalFormat df = null;
        char hold = split[len - 1].charAt(2);
        char symbol = split[len - 1].charAt(3);
        if (Constant.H_NONE == hold) {
            df = new DecimalFormat("0");
        } else if (Constant.H_ONE == hold) {
            df = new DecimalFormat("0.0");
        } else if (Constant.H_TWO == hold) {
            df = new DecimalFormat("0.00");
        }
        if (Constant.S_NONE == symbol) {
            for (int i = 0; i < dvs.length; i++) {
                finalValue.append(df.format(dvs[i]) + ",");
            }
        } else if (Constant.S_ONE == symbol) {
            for (int i = 0; i < dvs.length; i++) {
                finalValue.append(df.format(dvs[i] * 100) + "%,");
            }
        }

        finalValue.deleteCharAt(finalValue.length()-1);
    }



            /*FormulaManagerVo fmv = new FormulaManagerVo(formula);

            //取出需要获取的值列表
            List<String> ls = this.getNeedValueFromRedis(fmv.getConditionList());

            //获取新的key_value 并存入redis
            formulaMap = fmv.getResultValue(ls, fmv);
            for (Map.Entry<String, String> entry : formulaMap.entrySet()) {
                try {
                    log.info("formula中的key存入redis");
                    log.info("formula中的key为" + entry.getKey());
                    redisUtil.hset(Oriental_Securities, entry.getKey(), entry.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //// 从redis取出condition中的key的值
        Map<String, String> conditionMap = new HashMap<>();

        try {
            String[] keys = condition.split(",");
            for (int i = 0; i < keys.length; i++) {
                String key = keys[i];
                log.info("查询的key为：" + key);
                if (!StringUtils.isEmpty(key)) {
                    String data = redisUtil.hget(Oriental_Securities, key);
                    if (!StringUtils.isEmpty(data)) {
                        log.info(key + "查redis的数据为：" + data);
                        conditionMap.put(key, data);
                    }
                }
            }
            conditionMap.putAll(formulaMap);

            resultVo.setResult(conditionMap);
            resultVo.setCode(ResultVo.CODE_SUCCESS);
            resultVo.setMsg(ResultVo.MSG_SUCCESS);*/
        /*} catch (Exception e) {
            log.error("error in CommonDataServiceImpl.commonQuery", e);
            resultVo.setCode(ResultVo.CODE_FAIL);
            resultVo.setMsg("接口异常,无法正常返回");
        }*/


    //取出需要获取的key的值列表
    public List<String> getNeedValueFromRedis(List<String> conditionList) {
        List<String> ls = new ArrayList<>();
        if (CollectionUtils.isEmpty(conditionList)) {
            for (String s : conditionList) {
                try {
                    String value = redisUtil.hget(Oriental_Securities, s);
                    ls.add(value.replace("[", "").replace("]", "").replace("\"\"", ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ls;
    }

    //    @Scheduled(fixedRate = 360000)
    public void generateDataD() {
        String[] statusCodes_out = new String[]{"d_internetout_list_1_pic", "d_internetout_list_2_pic", "d_internetout_list_3_pic"};
        String[] statusCodes_site = new String[]{"d_internetsite_list_1_pic", "d_internetsite_list_2_pic", "d_internetsite_list_3_pic"};
        String[] statusCodes_cpuuse = new String[]{"d_cpuuse_list_1_pic", "d_cpuuse_list_2_pic", "d_cpuuse_list_3_pic"};
        String[] statusCodes_internetstatuses = new String[]{"d_internetstatus_list_0.5_pic", "d_internetstatus_list_50_pic", "d_internetstatus_list_80_pic", "d_internetstatus_list_cut_pic"};

        List<CommonData> outs = new ArrayList<>(32);
        List<CommonData> sites = new ArrayList<>(32);
        List<CommonData> cpuuses = new ArrayList<>(8);
        List<CommonData> internetstatuses = new ArrayList<>(16);
        try {
            for (int i = 0; i < 20; i++) {
                outs.add(new CommonData("机器" + i, (int) (Math.random() * 100) + "", statusCodes_out[(int) (Math.random() * 3)]));
                sites.add(new CommonData("机器" + i, (int) (Math.random() * 100) + "", statusCodes_site[(int) (Math.random() * 3)]));
            }
            redisUtil.hset(Oriental_Securities, "d_internetout_list", JSON.toJSONString(outs));
            redisUtil.hset(Oriental_Securities, "d_internetsite_list", JSON.toJSONString(sites));

            for (int i = 0; i < 8; i++) {
                cpuuses.add(new CommonData("设备" + i, (int) (Math.random() * 100) + "", statusCodes_cpuuse[(int) (Math.random() * 3)]));
            }
            redisUtil.hset(Oriental_Securities, "d_cpuuse_list", JSON.toJSONString(cpuuses));


            //depth=1的时候就是四个大的机房
            //1的话就定1和2的    2的就是2和3的, 3的就是3和4的,  4的就是4和2的
            internetstatuses.add(new CommonData("00001", "上海电信高桥机房", statusCodes_internetstatuses[(int) (Math.random() * 4)], null, statusCodes_internetstatuses[(int) (Math.random() * 4)], "1"));
            internetstatuses.add(new CommonData("00002", "上海上证通机房", statusCodes_internetstatuses[(int) (Math.random() * 4)], "00001", statusCodes_internetstatuses[(int) (Math.random() * 4)], "1"));
            internetstatuses.add(new CommonData("00003", "上海中山南路机房", statusCodes_internetstatuses[(int) (Math.random() * 4)], "00002", statusCodes_internetstatuses[(int) (Math.random() * 4)], "1"));
            internetstatuses.add(new CommonData("00004", "上海东莞南方中心", statusCodes_internetstatuses[(int) (Math.random() * 4)], "00002", statusCodes_internetstatuses[(int) (Math.random() * 4)], "2"));

            for (int i = 0; i < 11; i++) {
                internetstatuses.add(new CommonData(00005 + i + "", "云" + i, statusCodes_internetstatuses[(int) (Math.random() * 4)], "00003", statusCodes_internetstatuses[(int) (Math.random() * 4)], "3"));
            }
            redisUtil.hset(Oriental_Securities, "d_internetstatus_list", JSON.toJSONString(internetstatuses));

            log.info("D页面模拟数据刷进redis成功");
        } catch (Exception e) {
            log.error("error in CommonDataServiceImpl.generateDataD", e);
        }
    }

    //    @Scheduled(fixedRate = 300000)
    public void generateDataB() {
        try {
            List<CommonData> dbstatuses = Arrays.asList(new CommonData("entirety", (int) (Math.random() * 40) + "", "b_dbstatus_entirety_pic"),
                    new CommonData("total", (int) (Math.random() * 40) + "", "b_dbstatus_total_pic"),
                    new CommonData("normal", (int) (Math.random() * 40) + "", "b_dbstatus_normal_pic"),
                    new CommonData("abnormal", (int) (Math.random() * 40) + "", "b_dbstatus_abnormal_pic")
            );
            redisUtil.hset(Oriental_Securities, "b_dbstatus", JSON.toJSONString(dbstatuses));
//            log.info("将b_dbstatus刷进redis成功......");


            //状态码
            String[] statusCodes_cpuuse = new String[]{"b_cpuuse_list_1_pic", "b_cpuuse_list_2_pic", "b_cpuuse_list_3_pic", "b_cpuuse_list_4_pic", "b_cpuuse_list_5_pic"};
            String[] statusCodes_tableuse = new String[]{"b_tableuse_1_list_pic", "b_tableuse_2_list_pic", "b_tableuse_3_list_pic", "b_tableuse_4_list_pic", "b_tableuse_5_list_pic"};
            String[] statusCodes_active = new String[]{"b_active_list_1_pic", "b_active_list_2_pic", "b_active_list_3_pic", "b_active_list_4_pic", "b_active_list_5_pic"};

            List<CommonData> cpuuse = new ArrayList<>(8);   //CPU利用率
            List<CommonDataPojo> dbio = new ArrayList<>(8);     //数据库高I/O
            List<CommonData> tableuse = new ArrayList<>(8); //表空间利用率
            List<CommonData> active = new ArrayList<>(8);   //活动会话
            for (int i = 0; i < 5; i++) {
                String systemName = "系统" + Math.random() * 60;
                cpuuse.add(new CommonData(systemName + "_cpuuse", (int) (Math.random() * 100) + "", statusCodes_cpuuse[(int) (Math.random() * 5)]));
                String[] nodes = new String[]{Math.random() * 500 + "", Math.random() * 500 + "", Math.random() * 500 + "", Math.random() * 500 + "",
                        Math.random() * 500 + "", Math.random() * 500 + "", Math.random() * 500 + "", Math.random() * 500 + "", Math.random() * 500 + "",
                        Math.random() * 500 + "", Math.random() * 500 + "", Math.random() * 500 + "", Math.random() * 500 + ""};
                //io曲线图颜色由前端定义
                dbio.add(new CommonDataPojo(systemName + "_dbio", nodes));
                tableuse.add(new CommonData(systemName + "_tableuse", (int) (Math.random() * 100) + "", statusCodes_tableuse[(int) (Math.random() * 5)]));
                active.add(new CommonData(systemName + "_active", (int) (Math.random() * 100) + "", statusCodes_active[(int) (Math.random() * 5)]));

            }
            redisUtil.hset(Oriental_Securities, "b_cpuuse_list", JSON.toJSONString(cpuuse));
//            log.info("将b_cpuuse_list刷进redis成功......");
            redisUtil.hset(Oriental_Securities, "b_dbio_list", JSON.toJSONString(dbio));
//            log.info("将b_dbio_list刷进redis成功......");
            redisUtil.hset(Oriental_Securities, "b_tableuse_list", JSON.toJSONString(tableuse));
//            log.info("将b_tableuse_list刷进redis成功......");
            redisUtil.hset(Oriental_Securities, "b_active_list", JSON.toJSONString(active));
//            log.info("将b_active_list刷进redis成功......");

            String[] statusCodes_dbsync = new String[]{"b_dbsync_1_pic", "b_dbsync_2_pic"};

            List<CommonData> dbsyncs = Arrays.asList(
                    new CommonData(null, (int) (Math.random() * 500) + "", statusCodes_dbsync[(int) (Math.random() * 2)], (int) (Math.random() * 40) + ""),
                    new CommonData(null, (int) (Math.random() * 500) + "", statusCodes_dbsync[(int) (Math.random() * 2)], (int) (Math.random() * 40) + "")
            );
            redisUtil.hset(Oriental_Securities, "b_dbsync", JSON.toJSONString(dbsyncs));
//            log.info("将b_dbsync刷进redis成功......");

        } catch (Exception e) {
            log.error("error in CommonDataServiceImpl.generateDataB", e);
        }

        log.info("B页面模拟数据刷进redis成功");
    }


    /**
     * @Note: 模拟a的大屏数据
     * @Date:2019/9/7 9:39 @Auth:yikai.wang @Desc(V): 1.0.5
     */
//    @Scheduled(fixedRate = 180000)
    public void generateDataA() {
        String[] statusCodes = new String[]{"a_warning_circle_pic", "a_generally_circle_pic", "a_serious_circle_pic", "a_disaster_circle_pic"};
        try {
            redisUtil.hset(Oriental_Securities, "a_warning", JSON.toJSONString(Arrays.asList(new CommonData((int) (Math.random() * 40) + "", "a_warning_pic"))));
            redisUtil.hset(Oriental_Securities, "a_generally_serious", JSON.toJSONString(Arrays.asList(new CommonData((int) (Math.random() * 30) + "", "a_generally_serious_pic"))));
            redisUtil.hset(Oriental_Securities, "a_serious", JSON.toJSONString(Arrays.asList(new CommonData((int) (Math.random() * 60) + "", "a_serious_pic"))));
            redisUtil.hset(Oriental_Securities, "a_disaster", JSON.toJSONString(Arrays.asList(new CommonData((int) (Math.random() * 10) + "", "a_disaster_pic"))));

            List<CommonData> systems = new ArrayList<>(20);
            for (int i = 0; i < 20; i++) {
                systems.add(new CommonData("系统" + i, null, statusCodes[(int) (Math.random() * 4)]));
            }
            redisUtil.hset(Oriental_Securities, "a_system_list", JSON.toJSONString(systems));


            List<CommonData> machines = new ArrayList<>();
            List<CommonData> cpus = new ArrayList<>(10);
            List<CommonData> rams = new ArrayList<>(10);
            List<CommonData> roms = new ArrayList<>(10);
            List<CommonDataPojo> flow = new ArrayList<>(10);
            List<CommonDataPojo> io = new ArrayList<>(10);

            for (int i = 0; i < 10; i++) {
                String name = "机器" + i;
                machines.add(new CommonData(name));
                cpus.add(new CommonData(name + "_cpu", (int) (Math.random() * 100) + "", null));
                rams.add(new CommonData(name + "_ram", (int) (Math.random() * 100) + "", null));
                roms.add(new CommonData(name + "_rom", (int) (Math.random() * 100) + "", null));
                /**
                 * @Note: 线的颜色是前端定义的
                 * @Date:2019/9/7 10:28 @Auth:yikai.wang @Desc(V): 1.0.5
                 */
                String[] flows = new String[]{String.valueOf(Math.random() * 900),
                        String.valueOf(Math.random() * 900), String.valueOf(Math.random() * 900),
                        String.valueOf(Math.random() * 900), String.valueOf(Math.random() * 900),
                        String.valueOf(Math.random() * 900), String.valueOf(Math.random() * 900),
                        String.valueOf(Math.random() * 900), String.valueOf(Math.random() * 900),
                        String.valueOf(Math.random() * 900), String.valueOf(Math.random() * 900),
                        String.valueOf(Math.random() * 900), String.valueOf(Math.random() * 900)};
                flow.add(new CommonDataPojo(name + "_flow", flows));
                String[] ios = new String[]{String.valueOf(Math.random() * 1100),
                        String.valueOf(Math.random() * 1100), String.valueOf(Math.random() * 1100),
                        String.valueOf(Math.random() * 1100), String.valueOf(Math.random() * 1100),
                        String.valueOf(Math.random() * 1100), String.valueOf(Math.random() * 1100),
                        String.valueOf(Math.random() * 1100), String.valueOf(Math.random() * 1100),
                        String.valueOf(Math.random() * 1100), String.valueOf(Math.random() * 1100),
                        String.valueOf(Math.random() * 1100), String.valueOf(Math.random() * 1100)};
                io.add(new CommonDataPojo(name + "_io", ios));

            }
            redisUtil.hset(Oriental_Securities, "a_cpu_list", JSON.toJSONString(cpus));
            redisUtil.hset(Oriental_Securities, "a_ram_list", JSON.toJSONString(rams));
            redisUtil.hset(Oriental_Securities, "a_rom_list", JSON.toJSONString(roms));
            redisUtil.hset(Oriental_Securities, "a_machine_list", JSON.toJSONString(machines));
            redisUtil.hset(Oriental_Securities, "a_flow_list", JSON.toJSONString(flow));
            redisUtil.hset(Oriental_Securities, "a_io_list", JSON.toJSONString(io));
        } catch (Exception e) {
            log.error("error in CommonDataServiceImpl.generateDataA", e);
        }

        log.info("A页面模拟数据刷进redis成功");
    }


    //    @Scheduled(fixedRate = 60000)
    public void generateDataH() {
        List<String> dataOtc = new ArrayList<>();
        List<String> dataFund = new ArrayList<>();
        List<String> dataCash = new ArrayList<>();

        dataOtc.add("1");
        dataOtc.add("2");
        dataOtc.add("3");
        dataOtc.add("7");
        dataOtc.add("0");

        dataFund.add("3");
        dataFund.add("5");
        dataFund.add("2");
        dataFund.add("1");

        dataCash.add("3");
        dataCash.add("5");

        try {

            log.info("HOtc 模块存入数据到redis" + JSON.toJSONString(dataOtc));
            redisUtil.hset(Oriental_Securities, "h_otc", JSON.toJSONString(dataOtc));

            log.info("HFund 模块存入数据到redis" + JSON.toJSONString(dataFund));
            redisUtil.hset(Oriental_Securities, "h_fund", JSON.toJSONString(dataFund));

            log.info("HCash 模块存入数据到redis" + JSON.toJSONString(dataCash));
            redisUtil.hset(Oriental_Securities, "h_cash", JSON.toJSONString(dataCash));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    @Scheduled(fixedRate = 60000)
    public void generateDataH2() {
        String otc = "40";
        String fund = "54";

        try {
            log.info("HOtc 模块存入数据到redis" + JSON.toJSONString(otc));
//            redisUtil.hset(Oriental_Securities, "h_otc", JSON.toJSONString(otc));
            redisUtil.hset(Oriental_Securities, "h_otc", otc);

            log.info("HFund 模块存入数据到redis" + JSON.toJSONString(fund));
            redisUtil.hset(Oriental_Securities, "h_fund", fund);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //    @Scheduled(fixedRate = 60000)
    public void generateDataH1() {
        List<String> ips = new ArrayList<>();
        List<String> cpus = new ArrayList<>();
        List<String> rams = new ArrayList<>();
        List<String> roms = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            ips.add("10.253.46." + String.valueOf(20 - i));
            cpus.add(String.valueOf(30 - i));
            rams.add(String.valueOf(50 - i));
            roms.add(String.valueOf(100 - i));
        }

        try {

            log.info("HIp 模块存入数据到redis" + JSON.toJSONString(ips));
            redisUtil.hset(Oriental_Securities, "h_ip", JSON.toJSONString(ips));

            log.info("HCpu 模块存入数据到redis" + JSON.toJSONString(cpus));
            redisUtil.hset(Oriental_Securities, "h_cpu", JSON.toJSONString(cpus));

            log.info("HRam 模块存入数据到redis" + JSON.toJSONString(rams));
            redisUtil.hset(Oriental_Securities, "h_ram", JSON.toJSONString(rams));

            log.info("HRom 模块存入数据到redis" + JSON.toJSONString(roms));
            redisUtil.hset(Oriental_Securities, "h_rom", JSON.toJSONString(roms));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

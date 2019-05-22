package com.potevio.analog.service;

import com.alibaba.fastjson.JSON;
import com.potevio.analog.dao.ExcelConfigDao;
import com.potevio.analog.exception.ConditionIsNullException;
import com.potevio.analog.exception.DuplicateIpException;
import com.potevio.analog.exception.RightTerminalIsNull;
import com.potevio.analog.pojo.ConfigData;
import com.potevio.analog.pojo.ConfigWrapperData;
import com.potevio.analog.pojo.ExcelData;
import com.potevio.analog.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ConfigService {
    private List<ConfigData> configs = new ArrayList<>();

    private String port;
    private String interval;

    public long stopTime = 0;//上次停止测试时间戳
    public static final long INTERRUPTTIME = 120;//停止测试后到下一次可以测试的时间间隔

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AnalogService analogService;

    @Autowired
    private ExcelConfigDao excelConfigDao;

    @Autowired
    private IdWorker idWorker;


    public String[] getGlobalPortAndrInterval() {
        if (this.port != null && this.interval != null) {
            String[] portinterval = new String[2];
            portinterval[0] = port;
            portinterval[1] = interval;
            return portinterval;
        }
        return null;
    }

    public List<ConfigData> addConfig(ConfigData data) {
        if (data == null) {
            throw new ConditionIsNullException("条件为空不操作");
        }
        data.setIsfilter("1");
        data.setId(idWorker.nextId() + "");
        data.setExpireTime("0");
        List<String> ips = data.getUeList();
        if (ips != null && !ips.isEmpty()) {
            //使用勾选方式，直接加到配置列表集合
            for (ConfigData oldconfig : configs) {
                Iterator<String> it = ips.iterator();
                while (it.hasNext()) {
                    String newip = it.next();
                    if (oldconfig.getUeList().contains(newip)) {
                        if ("1".equals(data.getIsfilter())) {
                            it.remove();
                        } else {
                            throw new DuplicateIpException("出现ip覆盖返回操作失败异常");
                        }
                    }
                }
            }
            if (!ips.isEmpty()) {
                Collections.sort(data.getUeList(), new IpComparator());
                configs.add(data);
                return configs;
            } else {
                throw new RightTerminalIsNull("没有符合条件的终端");
            }
        } else {
            //使用ip段的方式
            String startip = data.getStartip();
            String endip = data.getEndip();
            long start = IpUtil.getIp2long(startip);
            long end = IpUtil.getIp2long2(endip);
            List<String> newips = getValidIps2(start, end);
            if (!newips.isEmpty()) {
                //判断新集合和已有集合是否有重合
                for (ConfigData oldconfig : configs) {
                    Iterator<String> it = newips.iterator();
                    while (it.hasNext()) {
                        String newip = it.next();
                        if (oldconfig.getUeList().contains(newip)) {
                            if ("1".equals(data.getIsfilter())) {
                                it.remove();
                            } else {
                                throw new DuplicateIpException("出现ip覆盖返回操作失败异常");
                            }
                        }
                    }
                }
                if (!newips.isEmpty()) {
                    Collections.sort(newips, new IpComparator());
                    data.getUeList().addAll(newips);
                    configs.add(data);
                    return configs;
                } else {
                    throw new RightTerminalIsNull("没有符合条件的终端");
                }
            } else {
                throw new RightTerminalIsNull("没有符合条件的终端");
            }
        }
    }

    private List<String> getValidIps(Set<String> allIp, String startIp, String endIp) {
        String ipSection = startIp + "-" + endIp;
        List<String> newips = new ArrayList<>();
        allIp.forEach(ip -> {
            if (IpUtil.ipIsValid(ipSection, ip)) {
                newips.add(ip);
            }
        });
        return newips;
    }

    private List<String> getValidIps2(long ip1, long ip2) {
        Long dbSize = stringRedisTemplate.getConnectionFactory().getConnection().dbSize();
        long start = ip1 < ip2 ? ip1 : ip2;
        long end = ip1 >= ip2 ? ip1 : ip2;
        List<String> newips = new ArrayList<>();
        if (dbSize < (end - start) * 10) {
            long finalStart = start;
            Set<String> allIp = stringRedisTemplate.keys("*");
            allIp.forEach(ip -> {
                long now = IpUtil.ipToLong(ip);
                if (now >= finalStart && now <= end) {
                    newips.add(ip);
                }
            });
        } else {
            for (long i = start; i <= end; i++) {
                String ip = IpUtil.longToIp(i);
                if (stringRedisTemplate.hasKey(ip)) {
                    newips.add(ip);
                }
            }
        }
        return newips;
    }


    public List<ConfigData> deleteConfig(String id) {
        int selectIndex = -1;
        for (int i = 0; i < configs.size(); i++) {
            if (id.equals(configs.get(i).getId())) {
                selectIndex = i;
                break;
            }
        }
        if (selectIndex != -1) {
            configs.remove(selectIndex);
            return configs;
        }
        return null;
    }

    public List<ConfigData> getAllConfig() {
        return configs;
    }

    /**
     * 向测试引擎发送配置信息并发出开始命令
     *
     * @return
     */
    public boolean starttest(String port, String interval) {
        this.port = port;
        this.interval = interval;
        //把对象转为JSON字符串
        ConfigWrapperData configWrapper = new ConfigWrapperData(port, interval, configs);
        String json = JSON.toJSONString(configWrapper);
        //使用redis消息队列发送开始命令和配置数据
        System.out.println("开始测试：" + json);
        stringRedisTemplate.convertAndSend("start_and_stop", json);
        return true;
    }

    /**
     * 向测试引擎发送停止指令
     *
     * @return
     */
    public boolean stoptest() {
        //开始倒计时
        stopTime = System.currentTimeMillis();
        stringRedisTemplate.convertAndSend("start_and_stop", "stop");
        System.out.println("停止测试");
        return true;
    }

    public void deleteAll() {
        configs.clear();
    }

    public void clean() {
        Map map = new HashMap<String, String>();
        map.put("testTime", "0");
        map.put("pktReceivable", "0");
        map.put("pktReceived", "0");
        map.put("avgDelay", "0");
        map.put("maxDelay", "0");
        map.put("lastDelay", "0");
        HashOperations ops = stringRedisTemplate.opsForHash();
        configs.forEach(config -> {
            config.getUeList().forEach(ip -> {
                ops.putAll(ip, map);
            });
        });
    }

    public List<ExcelData> getExcelConfig() {
        return excelConfigDao.getExcelConfig();
    }

    public List<ExcelData> addExcelConfig(ExcelData data){
        return excelConfigDao.addExcelConfig(data);
    }

    public List<ExcelData> deleteExcelConfig(String id){
        return excelConfigDao.deleteExcelConfig(id);
    }

//    public List<ExcelData> getExcelConfig() {
//        if (ExcelFileUtil.INSTANCE.isEmpty()) {//文件为空不读取
//            return null;
//        }
//        XSSFWorkbook workbook = null;
//        try {//文件格式错误不读取
//            workbook = new XSSFWorkbook(ExcelFileUtil.INSTANCE.getExcelFileInputStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//        Sheet sheet = workbook.getSheet("configlist");
//        if (sheet == null) {//不存在configlist的sheet表不读取
//            return null;
//        }
//        //数据不完整的行过滤掉
//        if (sheet.getLastRowNum() == 0) {//sheet中无行数据
//            if (sheet.getRow(0).getLastCellNum() != 9) {
//                return null;
//            }
//        }
//        List<ExcelData> dataList = new ArrayList<>();
//        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
//            Row row = sheet.getRow(i);
//            short cellNum = row.getLastCellNum();
//            if (cellNum == 9) {
//                ExcelData excelData = new ExcelData();
//                for (int j = 0; j < cellNum; j++) {
//                    String value = null;
//                    if (row.getCell(j).getCellType() == CellType.NUMERIC) {
//                        value = String.valueOf((long) row.getCell(j).getNumericCellValue());
//                    } else {
//                        value = row.getCell(j).getStringCellValue();
//                    }
//                    switch (j) {
//                        case 0:
//                            excelData.setId(value);
//                            break;
//                        case 1:
//                            excelData.setNumOfPktsPerTime(value);
//                            break;
//                        case 2:
//                            excelData.setIntervalOfPerTime(value);
//                            break;
//                        case 3:
//                            excelData.setIntervalOfPerPkt(value);
//                            break;
//                        case 4:
//                            excelData.setPortNum(value);
//                            break;
//                        case 5:
//                            excelData.setTestType(value);
//                            break;
//                        case 6:
//                            excelData.setDataLength(value);
//                            break;
//                        case 7:
//                            excelData.setIp(value);
//                            break;
//                        case 8:
//                            excelData.setCallLength(value);
//                            break;
//                    }
//                }
//                dataList.add(excelData);
//            }
//        }
//        if (dataList.isEmpty()) {
//            return null;
//        }
//        return dataList;
//    }

//    public List<ExcelData> addExcelConfig(ExcelData data) {
//        data.setId(idWorker.nextId() + "");
//        List<ExcelData> excelDataList = getExcelConfig();
//        if (excelDataList == null) {
//            excelDataList = new ArrayList<>();
//        }
//        excelDataList.add(data);
//        addExcelConfigs(excelDataList);
//        return excelDataList;
//    }

//    public List<ExcelData> deleteExcelConfig(String id) {
//        List<ExcelData> excelConfig = getExcelConfig();
//        Iterator<ExcelData> it = excelConfig.iterator();
//        boolean hasRemoved = false;
//        while (it.hasNext()) {
//            ExcelData next = it.next();
//            if (id.equals(next.getId())) {
//                it.remove();
//                hasRemoved = true;
//            }
//        }
//        if (hasRemoved) {
//            addExcelConfigs(excelConfig);
//        }
//        return excelConfig;
//    }

//    public void addExcelConfigs(List<ExcelData> datas) {
//        XSSFWorkbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("configlist");
//        for (int i = 0; i < datas.size(); i++) {
//            ExcelData data = datas.get(i);
//            Row row = sheet.createRow(i);
//            for (int j = 0; j < 9; j++) {
//                Cell cell = row.createCell(j);
//                switch (j) {
//                    case 0:
//                        cell.setCellValue(data.getId());
//                        break;
//                    case 1:
//                        cell.setCellValue(data.getNumOfPktsPerTime());
//                        break;
//                    case 2:
//                        cell.setCellValue(data.getIntervalOfPerTime());
//                        break;
//                    case 3:
//                        cell.setCellValue(data.getIntervalOfPerPkt());
//                        break;
//                    case 4:
//                        cell.setCellValue(data.getPortNum());
//                        break;
//                    case 5:
//                        cell.setCellValue(data.getTestType());
//                        break;
//                    case 6:
//                        cell.setCellValue(data.getDataLength());
//                        break;
//                    case 7:
//                        cell.setCellValue(data.getIp());
//                        break;
//                    case 8:
//                        cell.setCellValue(data.getCallLength());
//                        break;
//                }
//            }
//        }
//        try {
//            workbook.write(ExcelFileUtil.INSTANCE.getExcelFileOutputStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}

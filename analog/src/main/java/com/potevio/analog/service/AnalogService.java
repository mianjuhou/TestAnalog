package com.potevio.analog.service;

import com.potevio.analog.pojo.AnalogData;
import com.potevio.analog.pojo.ConfigData;
import com.potevio.analog.pojo.TerminalData;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class AnalogService {

    private static String[] titles = {"imsi", "终端IP", "端口号", "在线状态", "测试状态", "测试次数", "应收包数", "实收包数", "开始时间", "结束时间", "平均时延", "最大时延", "最新时延"};

    @Autowired
    private ConfigService configService;

    private RedisTemplate redisTemplate;

    private List<String> mlkeys;

    private List<String> mkeys;

    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }

    public AnalogService() {
        mlkeys = new ArrayList<>();
        mlkeys.add("imsi");
        mlkeys.add("ip");
        mlkeys.add("port");
        mlkeys.add("onlinestate");
        mlkeys.add("testStatus");
        mlkeys.add("testTime");
        mlkeys.add("pktReceivable");
        mlkeys.add("pktReceived");
        mlkeys.add("startTime");
        mlkeys.add("endTime");
        mlkeys.add("avgDelay");
        mlkeys.add("maxDelay");
        mlkeys.add("lastDelay");
        mkeys = new ArrayList<>();
        mkeys.add("imsi");
        mkeys.add("ip");
        mkeys.add("port");
        mkeys.add("onlinestate");
    }

    public List<AnalogData> findRealData() {
        HashOperations ops = redisTemplate.opsForHash();
        Set<String> keys = redisTemplate.keys("*");
        List<AnalogData> analogs = new ArrayList<>();
        List<ConfigData> configs = configService.getAllConfig();
        List<String> configIpList = new ArrayList<>();
        configs.forEach(config -> {
            configIpList.addAll(config.getUeList());
        });
        keys.forEach(key -> {
            List<String> list = ops.multiGet(key, mlkeys);
            if (configIpList.contains(list.get(1))) {
                AnalogData analogData = new AnalogData();
                analogData.setImsi(list.get(0));
                analogData.setIp(list.get(1));
                analogData.setPort(list.get(2));
                analogData.setOnlinestate(list.get(3));
                analogData.setTestStatus(list.get(4));
                analogData.setTestTime(list.get(5));
                analogData.setPktReceivable(list.get(6));
                analogData.setPktReceived(list.get(7));
                analogData.setStartTime(list.get(8));
                analogData.setEndTime(list.get(9));
                analogData.setAvgDelay(list.get(10));
                analogData.setMaxDelay(list.get(11));
                analogData.setLastDelay(list.get(12));
                analogs.add(analogData);
            }
        });
        return analogs;
    }

//    public List<List<AnalogData>> findRealData() {
//        HashOperations ops = redisTemplate.opsForHash();
//        Set<String> keys = redisTemplate.keys("*");
//        List<List<AnalogData>> analogsList = new ArrayList<>();
//        List<ConfigData> configs = configService.getAllConfig();
//        configs.forEach(config -> {
//            analogsList.add(new ArrayList<AnalogData>());
//        });
//        keys.forEach(key -> {
//            List<String> list = ops.multiGet(key, mlkeys);
//            for (int i = 0; i < configs.size(); i++) {
//                if (configs.get(i).getUeList().contains(list.get(1))) {
//                    AnalogData analogData = new AnalogData();
//                    analogData.setImsi(list.get(0));
//                    analogData.setIp(list.get(1));
//                    analogData.setPort(list.get(2));
//                    analogData.setOnlinestate(list.get(3));
//                    analogData.setTestStatus(list.get(4));
//                    analogData.setTestTime(list.get(5));
//                    analogData.setPktReceivable(list.get(6));
//                    analogData.setPktReceived(list.get(7));
//                    analogData.setStartTime(list.get(8));
//                    analogData.setEndTime(list.get(9));
//                    analogData.setAvgDelay(list.get(10));
//                    analogData.setMaxDelay(list.get(11));
//                    analogData.setLastDelay(list.get(12));
//                    analogsList.get(i).add(analogData);
//                }
//            }
//        });
//        return analogsList;
//    }

    /**
     * 获取设备选择列表所需数据
     */
    public List<TerminalData> getTerminalList() {
        HashOperations ops = redisTemplate.opsForHash();
        Set<String> keys = redisTemplate.keys("*");
        List<TerminalData> terminals = new ArrayList<>();
        keys.forEach(key -> {
            List<String> list = ops.multiGet(key, mkeys);
            TerminalData terminalData = new TerminalData();
            terminalData.setImsi(list.get(0));
            terminalData.setIp(list.get(1));
            terminalData.setPort(list.get(2));
            terminalData.setOnlinestate(list.get(3));
            terminals.add(terminalData);
        });
        return terminals;
    }

    public TerminalData getTerminalData(String ip) {
        HashOperations ops = redisTemplate.opsForHash();
        List<String> list = ops.multiGet(ip, mkeys);
        if (list == null || list.isEmpty()) {
            return null;
        }
        TerminalData terminalData = new TerminalData();
        terminalData.setImsi(list.get(0));
        terminalData.setIp(list.get(1));
        terminalData.setPort(list.get(2));
        terminalData.setOnlinestate(list.get(3));
        return terminalData;
    }

    public static File getExportFile(String dir) {
        File dirFile = new File(dir);
        File file = new File(dirFile, System.currentTimeMillis() + ".xls");
        if (file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public boolean export(String filepath) {
        try {
            File file = getExportFile("/D/excel/");
            HSSFWorkbook workbook = null;
            try {
                workbook = new HSSFWorkbook(new FileInputStream(file));
            } catch (Exception e) {
                workbook = new HSSFWorkbook();
            }
            HSSFSheet sheet = workbook.createSheet();
            //创建标题栏
            HSSFRow titleRow = sheet.createRow(0);
            for (int i = 0; i < titles.length; i++) {
                titleRow.createCell(i).setCellValue(titles[i]);
            }
            //创建数据栏
            HashOperations ops = redisTemplate.opsForHash();
            Set<String> keys = redisTemplate.keys("*");
            keys.forEach(key -> {
                List<String> values = ops.multiGet(key, mlkeys);
                HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
                for (int i = 0; i < values.size(); i++) {
                    dataRow.createCell(i).setCellValue(values.get(i));
                }
            });
            workbook.write(new FileOutputStream(file));
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

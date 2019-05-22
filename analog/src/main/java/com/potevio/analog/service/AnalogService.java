package com.potevio.analog.service;

import com.potevio.analog.entity.PageResult;
import com.potevio.analog.pojo.*;
import com.potevio.analog.util.IpUtil;
import com.potevio.analog.util.RequestComparetor;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class AnalogService {

    private static String[] titles = {"imsi", "终端IP", "端口号", "在线状态", "测试状态", "测试次数", "应收包数", "实收包数", "开始时间", "结束时间", "平均时延", "最大时延", "最新时延"};

    @Autowired
    private ConfigService configService;

    private RedisTemplate redisTemplate;

    private List<String> mlkeys;

    private List<String> mkeys;

    private Map<String, Map<String, TerminalData>> stationInfos = new HashMap<>();

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
        mlkeys.add("eNodeBId");
        mkeys = new ArrayList<>();
        mkeys.add("imsi");
        mkeys.add("ip");
        mkeys.add("port");
        mkeys.add("onlinestate");
        mkeys.add("eNodeBId");
    }

    public List<AnalogData> findRealData() {
        HashOperations ops = redisTemplate.opsForHash();
//        Set<String> keys = redisTemplate.keys("*");
        List<AnalogData> analogs = new ArrayList<>();
        List<ConfigData> configs = configService.getAllConfig();
        List<String> configIpList = new ArrayList<>();
        configs.forEach(config -> {
            configIpList.addAll(config.getUeList());
        });
        configIpList.forEach(ip -> {
            if (redisTemplate.hasKey(ip)) {
                List<String> list = ops.multiGet(ip, mlkeys);
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
//        keys.forEach(key -> {
//            List<String> list = ops.multiGet(key, mlkeys);
//            if (configIpList.contains(list.get(1))) {
//                AnalogData analogData = new AnalogData();
//                analogData.setImsi(list.get(0));
//                analogData.setIp(list.get(1));
//                analogData.setPort(list.get(2));
//                analogData.setOnlinestate(list.get(3));
//                analogData.setTestStatus(list.get(4));
//                analogData.setTestTime(list.get(5));
//                analogData.setPktReceivable(list.get(6));
//                analogData.setPktReceived(list.get(7));
//                analogData.setStartTime(list.get(8));
//                analogData.setEndTime(list.get(9));
//                analogData.setAvgDelay(list.get(10));
//                analogData.setMaxDelay(list.get(11));
//                analogData.setLastDelay(list.get(12));
//                analogs.add(analogData);
//            }
//        });
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
        try {
            keys.stream().parallel().forEach(key -> {
                List<String> list = ops.multiGet(key, mkeys);
                TerminalData terminalData = new TerminalData();
                terminalData.setImsi(list.get(0));
                terminalData.setIp(list.get(1));
                terminalData.setPort(list.get(2));
                terminalData.setOnlinestate(list.get(3));
                terminals.add(terminalData);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return terminals;
    }


    public List<TerminalData> getTerminalScanList() {
        HashOperations ops = redisTemplate.opsForHash();
        List<TerminalData> terminals = new ArrayList<>();
        try {
            redisTemplate.execute((RedisCallback<List<TerminalData>>) connection -> {
                Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().count(5).build());
                while (cursor.hasNext()) {
                    String key = new String(cursor.next());
                    List<String> list = ops.multiGet(key, mkeys);
                    TerminalData terminalData = new TerminalData();
                    terminalData.setImsi(list.get(0));
                    terminalData.setIp(list.get(1));
                    terminalData.setPort(list.get(2));
                    terminalData.setOnlinestate(list.get(3));
                    terminals.add(terminalData);
                    System.out.println("数量" + terminals.size() + " 位置:" + cursor.getPosition());
                    if (terminals.size() >= 1000) {
                        return terminals;
                    }
                }
                return terminals;
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return terminals;
        }
    }

    /**
     * 获取基站ID列表
     *
     * @return
     */
    public List<String> getStationDatas() {
        if (!stationInfos.isEmpty()) {
            return new ArrayList<>(stationInfos.keySet());
        }
        Set<String> keys = redisTemplate.keys("*");
        HashOperations ops = redisTemplate.opsForHash();
        keys.forEach(key -> {
            List<String> list = ops.multiGet(key, mkeys);
            String eNodeBId = list.get(4);
            String ip = list.get(1);
            TerminalData terminalData = new TerminalData();
            terminalData.setImsi(list.get(0));
            terminalData.setIp(ip);
            terminalData.setPort(list.get(2));
            terminalData.setOnlinestate(list.get(3));
            terminalData.setENodeBId(eNodeBId);
            if (stationInfos.keySet().contains(eNodeBId)) {
                stationInfos.get(eNodeBId).put(ip, terminalData);
            } else {
                HashMap<String, TerminalData> terminalMap = new HashMap<>();
                terminalMap.put(ip, terminalData);
                stationInfos.put(eNodeBId, terminalMap);
            }
        });
        return new ArrayList<>(stationInfos.keySet());
    }

    /**
     * 获取基站对应的终端列表
     *
     * @param stationId
     * @return
     */
    public List<TerminalData> getTerminalDatas(String stationId) {
        if (stationInfos.keySet().contains(stationId)) {
            return new ArrayList<>(stationInfos.get(stationId).values());
        }
        Set<String> keys = redisTemplate.keys("*");
        HashOperations ops = redisTemplate.opsForHash();
        Map<String, TerminalData> terminalMap = new HashMap<>();
        keys.forEach(key -> {
            List<String> list = ops.multiGet(key, mkeys);
            if (stationId.equals(list.get(4))) {
                TerminalData terminalData = new TerminalData();
                terminalData.setImsi(list.get(0));
                terminalData.setIp(list.get(1));
                terminalData.setPort(list.get(2));
                terminalData.setOnlinestate(list.get(3));
                terminalData.setENodeBId(list.get(4));
                terminalMap.put(list.get(1), terminalData);
            }
        });
        stationInfos.put(stationId, terminalMap);
        return new ArrayList<>(terminalMap.values());
    }

    public void updateTerminalData(TerminalData data) {
        if (Strings.isEmpty(data.getENodeBId())) {
            return;
        }
        if (stationInfos.containsKey(data.getENodeBId())) {
            Map<String, TerminalData> terminalMap = stationInfos.get(data.getENodeBId());
            terminalMap.put(data.getIp(), data);
        } else {
            Map<String, TerminalData> terminalMap = new HashMap<>();
            terminalMap.put(data.getIp(), data);
            stationInfos.put(data.getENodeBId(), terminalMap);
        }
    }

    /**
     * 使用原始方式获取分页终端
     *
     * @param page
     * @param size
     * @return
     */
    public PageResult<TerminalData> getTerminalPageData3(int page, int size) {
        Set<String> keys = redisTemplate.keys("*");
        HashOperations ops = redisTemplate.opsForHash();
        Iterator<String> it = keys.iterator();
        long startPosition = page * size;
        long endPosition = startPosition + size;
        List<TerminalData> terminals = new ArrayList<>();
        String next = null;
        long position = -1;
        while (it.hasNext()) {
            position++;
            next = it.next();
            if (position < startPosition) {
                continue;
            } else if (position >= startPosition && position < endPosition) {
                List<String> list = ops.multiGet(next, mkeys);
                TerminalData terminalData = new TerminalData();
                terminalData.setImsi(list.get(0));
                terminalData.setIp(list.get(1));
                terminalData.setPort(list.get(2));
                terminalData.setOnlinestate(list.get(3));
                terminals.add(terminalData);
            } else if (position >= endPosition) {
                break;
            }
        }
        return new PageResult<>(keys.size(), terminals);
    }


    public PageResult<TerminalData> getTerminalPageData2(int page, int size) {
        HashOperations ops = redisTemplate.opsForHash();
        List<TerminalData> terminals = new ArrayList<>();
        long startPosition = page * size;
        long endPosition = startPosition + size;
        return (PageResult<TerminalData>) redisTemplate.execute((RedisCallback<PageResult<TerminalData>>) connection -> {
            Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().count(1000).build());
            byte[] next = null;
            long position = 0;
            while (cursor.hasNext()) {
                next = cursor.next();
                position = cursor.getPosition();
                if (position <= startPosition) {
                    continue;
                } else if (position > startPosition && position <= endPosition) {
                    String key = new String(next);
                    List<String> list = ops.multiGet(key, mkeys);
                    TerminalData terminalData = new TerminalData();
                    terminalData.setImsi(list.get(0));
                    terminalData.setIp(list.get(1));
                    terminalData.setPort(list.get(2));
                    terminalData.setOnlinestate(list.get(3));
                    terminals.add(terminalData);
                    System.out.println("数量" + terminals.size() + " 位置:" + cursor.getPosition());
                } else if (position > endPosition) {
                    break;
                }
            }
            PageResult<TerminalData> pageResult = new PageResult<>(connection.dbSize(), terminals);
            return pageResult;
        });
    }

    public PageResult<TerminalData> getTerminalPageData(int page, int size) {
        Set<String> keys = redisTemplate.keys("*");
        ZSetOperations zops = redisTemplate.opsForZSet();
        //使用SortedSet进行排序，然后取范围
        if (!redisTemplate.hasKey("keyorder")) {
            Set<ZSetOperations.TypedTuple> tupleSet = new HashSet<>();
            keys.forEach(ip -> {
                tupleSet.add(new DefaultTypedTuple<>(ip, (double) IpUtil.ipToLong(ip)));
                if (tupleSet.size() >= 1000) {
                    zops.add("keyorder", tupleSet);
                    tupleSet.clear();
                    System.out.println("插入一批数据");
                }
            });
            if (!tupleSet.isEmpty()) {
                zops.add("keyorder", tupleSet);
                tupleSet.clear();
                System.out.println("插入一批数据");
            }
        }
        Set<String> rangeKeys = zops.range("keyorder", page * size, (page + 1) * size);
        HashOperations ops = redisTemplate.opsForHash();
        List<TerminalData> terminals = new ArrayList<>();
        rangeKeys.forEach(key -> {
            List<String> list = ops.multiGet(key, mkeys);
            TerminalData terminalData = new TerminalData();
            terminalData.setImsi(list.get(0));
            terminalData.setIp(list.get(1));
            terminalData.setPort(list.get(2));
            terminalData.setOnlinestate(list.get(3));
            terminals.add(terminalData);
        });
        zops.size("keyorder");
        return new PageResult<TerminalData>(zops.size("keyorder"), terminals);
    }

    public TerminalData getTerminalData(String ip) {
        if (!redisTemplate.hasKey(ip)) {
            return null;
        }
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
        terminalData.setENodeBId(list.get(4));
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

    public List<RealResponseData> findOrderRealData(RequestOrderData orderData) {
        HashOperations ops = redisTemplate.opsForHash();
        List<ConfigData> configs = configService.getAllConfig();
        List<RealResponseData> responses = new ArrayList<>();
        RequestComparetor comparetor = new RequestComparetor(orderData);
        configs.forEach(config -> {
            RealResponseData realResponseData = config.obtainRealResponseData();
            List<AnalogData> analogs = new ArrayList<>();
            config.getUeList().forEach(ip -> {
                if (redisTemplate.hasKey(ip)) {
                    List<String> list = ops.multiGet(ip, mlkeys);
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
            //排序
            if (comparetor.isNeedOrder()) {
                Collections.sort(analogs, comparetor);
            }
            realResponseData.setAnalogList(analogs);
            //添加
            responses.add(realResponseData);
        });
        return responses;
    }
}

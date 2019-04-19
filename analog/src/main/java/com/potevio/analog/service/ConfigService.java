package com.potevio.analog.service;

import com.alibaba.fastjson.JSON;
import com.potevio.analog.exception.ConditionIsNullException;
import com.potevio.analog.exception.DuplicateIpException;
import com.potevio.analog.exception.RightTerminalIsNull;
import com.potevio.analog.pojo.ConfigData;
import com.potevio.analog.pojo.ConfigWrapperData;
import com.potevio.analog.pojo.TerminalData;
import com.potevio.analog.util.IdWorker;
import com.potevio.analog.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConfigService {
    private List<ConfigData> configs = new ArrayList<>();

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AnalogService analogService;

    @Autowired
    private IdWorker idWorker;

    public List<ConfigData> addConfig(ConfigData data) {
        if (data == null) {
            throw new ConditionIsNullException("条件为空不操作");
        }
        data.setId(idWorker.nextId() + "");
        data.setExpireTime("0");
        List<String> ips = data.getUeList();
        if (ips != null && !ips.isEmpty()) {
            data.getUeList().add("180.0.0.177");
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
                configs.add(data);
                return configs;
            } else {
                throw new RightTerminalIsNull("没有符合条件的终端");
            }
//            configs.add(data);
//            return configs;
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
        stringRedisTemplate.convertAndSend("start_and_stop", "stop");
        System.out.println("停止测试");
        return true;
    }

    public void deleteAll() {
        configs.clear();
    }

    public void clean() {
        Map map = new HashMap<String, String>();
        map.put("testTime","0");
        map.put("pktReceivable","0");
        map.put("pktReceived","0");
        map.put("avgDelay","0");
        map.put("maxDelay","0");
        map.put("lastDelay","0");
        HashOperations ops = stringRedisTemplate.opsForHash();
        configs.forEach(config -> {
            config.getUeList().forEach(ip -> {
                ops.putAll(ip,map);
            });
        });
    }
}

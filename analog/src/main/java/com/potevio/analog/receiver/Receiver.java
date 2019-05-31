
package com.potevio.analog.receiver;

import com.alibaba.fastjson.JSON;
import com.potevio.analog.pojo.TerminalData;
import com.potevio.analog.service.AnalogService;
import com.potevio.analog.ws.MyWebSocket;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class Receiver {

    @Autowired
    private AnalogService analogService;

    public void receiveMessage(String message) {
        TerminalData terminalData = analogService.getTerminalData(message);
        String json = null;
        if (terminalData == null) {
            analogService.updateTerminalData(null,message);
            json = "{}";
            System.out.println("此数据不存在或已被删除");
        } else {
            analogService.updateTerminalData(terminalData,null);
            json = JSON.toJSONString(terminalData);
            System.out.println("推送给前端数据:" + json);
        }
        //整合websocket后推送到客户端
        try {
            MyWebSocket.sendInfo(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
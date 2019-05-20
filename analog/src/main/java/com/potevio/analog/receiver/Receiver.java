
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
        if (terminalData == null) {
            System.out.println("此数据不存在");
            return;
        }
        analogService.updateTerminalData(terminalData);
        System.out.println("推送数据:" + terminalData);
        String json = JSON.toJSONString(terminalData);
        //整合websocket后推送到客户端
        try {
            MyWebSocket.sendInfo(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
package com.fs.websocketdemo.controller;

import com.fs.websocketdemo.ws.MyWebSocket;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

//http://localhost:9002/sso/socket/test
@RestController
@RequestMapping("/socket")
@CrossOrigin
public class WebSocketController {

    @RequestMapping("/test")
    public String test() {
        try {
            MyWebSocket.sendInfo("这是服务端测试");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ok";
    }

}

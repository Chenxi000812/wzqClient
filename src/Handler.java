import NetWork.Entity.Msg;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.Socket;

public class Handler {
    public static Socket socket;
    public static MainFrame mainFrame;
    public static final int ACTION_MATCHING = 0;
    public static final int ACTION_CANCLE_MATCHING = 1;
    public static final int ACTION_BATTLE_PlAY = 2;
    public static final int ACTION_BATTLE_EXIT= 3;

    public static final int MATCHING_SUCCESS = 1;
    public static final int BATTLE_FINISHED = 2;
    public static final int ACCESS_DENIED = -1;
    public static final int ACCESS_SUCCESS = 6;
    public static final int URBLACK = 3;
    public static final int URWHITE = 4;
    public static final int UROUND = 5;
    public static void ReceiveMsg(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                        String line;
                        while ((line = reader.readLine())!=null){
                            line = line.trim();
                            if (!line.contains("44332255")){
                                System.out.println(line);
                                handlerMsg(JSON.parseObject(line,Msg.class));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }).start();
    }

    public static void handlerMsg(Msg msg){
        if (msg.getAction()==Handler.MATCHING_SUCCESS){
            mainFrame.battleStart();
            mainFrame.showText(msg.getData());
        }else if (msg.getAction()==Handler.ACCESS_SUCCESS){
            mainFrame.drawCircle(mainFrame.isBlack);
            mainFrame.UROUND = false;
            mainFrame.showText("对手的回合");
        }else if (msg.getAction()==Handler.UROUND){
            mainFrame.UROUND = true;
            mainFrame.showText("你的回合");
            if (msg.getData()!=null){
                JSONObject jsonObject = JSON.parseObject(msg.getData());
                mainFrame.drawCircle(!mainFrame.isBlack,jsonObject.getInteger("a"),jsonObject.getInteger("b"));
            }
        }else if (msg.getAction()==Handler.URBLACK){
            mainFrame.isBlack = true;
        }else if (msg.getAction()==Handler.URWHITE){
            mainFrame.isBlack = false;
        }else if (msg.getAction()==Handler.BATTLE_FINISHED){
            mainFrame.battleExit();
            mainFrame.showText(msg.getData());
        }
    }
    public static void sendMsgToS(int action,String data){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action",action);
        jsonObject.put("data",data);
        try {
            OutputStream out = socket.getOutputStream();
            out.write((jsonObject.toJSONString()+"\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

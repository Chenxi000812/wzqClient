import com.alibaba.fastjson.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.TimerTask;

public class MainFrame {

    public JFrame jFrame;

    public JPanel jPanel;

    public JButton jButton;

    public JButton exitBT;

    public JLabel jLabel;

    boolean UROUND = false;

    boolean isBlack;
    boolean isMatching = false;

    int x;
    int y;

    public MainFrame(){
        jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setTitle("五子棋");
        jFrame.setBounds(100,100,900,700);
        jFrame.setVisible(true);
        jPanel = new JPanel();
        jPanel.setBounds(0,0,600,600);
        jPanel.setBackground(new Color(255, 177, 18));
        jPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (UROUND){
                    x = (e.getX()-10+15)/30*30;
                    y = (e.getY()-10+15)/30*30;
                    System.out.println("单击了：" + x + " " + y);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("a",x/30);
                    jsonObject.put("b",y/30);
                    Handler.sendMsgToS(Handler.ACTION_BATTLE_PlAY,jsonObject.toJSONString());
                }
            }
        });

        jButton = new JButton("匹配");
        jButton.setBounds(630,0,100,40);
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isMatching = !isMatching;
                if (isMatching){
                    jButton.setText("取消匹配");
                    showText("匹配中。。。");
                    Handler.sendMsgToS(Handler.ACTION_MATCHING,null);
                }else {
                    jButton.setText("匹配");
                    showText("");
                    Handler.sendMsgToS(Handler.ACTION_CANCLE_MATCHING,null);
                }
            }
        });
        exitBT = new JButton("投降");
        exitBT.setBounds(630,0,100,40);
        exitBT.setVisible(false);
        exitBT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Handler.sendMsgToS(Handler.ACTION_BATTLE_EXIT,null);
            }
        });

        jLabel = new JLabel();
        jLabel.setBounds(620,60,200,20);
        jLabel.setText("");

        jFrame.add(jLabel);
        jFrame.add(jButton);
        jFrame.add(exitBT);
        jFrame.add(jPanel);
        refreshUi();
    }

    public void refreshUi(){
        jPanel.repaint();
        java.util.Timer timer = new java.util.Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //1.获取画笔(2D绘图对象，显示对象)
                Graphics pen = jPanel.getGraphics();
                pen.setColor(Color.black);
                for (int i = 0; i <= 20; i = i + 1) {
                    pen.drawLine(10, 30 * i+10, 600+10, 30 * i+10);
                    pen.drawLine(30 * i+10, 10, 30 * i+10, 600+10);
                }
            }
        }, new Date(), 100);
    }

    public void battleStart(){
        jButton.setText("匹配");
        isMatching = false;
        jButton.setVisible(false);
        exitBT.setVisible(true);
        refreshUi();
    }
    public void battleExit(){
        jButton.setVisible(true);
        exitBT.setVisible(false);
        UROUND = false;
    }

    public void drawCircle(boolean isblack){
        Graphics pen = jPanel.getGraphics();
        if (isblack){
            pen.setColor(Color.black);
        }else {
            pen.setColor(Color.white);
        }
        pen.fillOval(x,y,20,20);
    }

    public void drawCircle(boolean isblack,int a,int b){
        Graphics pen = jPanel.getGraphics();
        if (isblack){
            pen.setColor(Color.black);
        }else {
            pen.setColor(Color.white);
        }
        pen.fillOval(a*30,b*30,20,20);
    }

    public void showText(String s){
        jLabel.setText(s);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("192.144.212.104", 8686);
        Handler.socket = socket;
        Handler.ReceiveMsg();
        Handler.mainFrame = new MainFrame();
    }
}

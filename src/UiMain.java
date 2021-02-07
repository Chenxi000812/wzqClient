import java.io.IOException;
import java.net.Socket;

public class UiMain {
    //主方法
    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("192.144.212.104", 8686);
        Handler.socket = socket;
        Handler.ReceiveMsg();
        Handler.mainFrame = new MainFrame();
    }
}

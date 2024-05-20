import Program.ServerWindow;
import Program.TestFirst;
import Program.TestSecond;

public class Main {
    public static void main(String[] args) {
        ServerWindow serverWindow = new ServerWindow();
//        new ClientGUI(serverWindow);
//        new ClientGUI(serverWindow);
        new TestSecond(serverWindow);
        new TestFirst(serverWindow);
    }
}
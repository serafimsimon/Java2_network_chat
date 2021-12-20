import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MyServer extends JFrame {

    private final Integer SERVER_PORT = 8880;
    Socket socket = null;


    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public MyServer() {
        System.out.println(ANSI_GREEN + "Server start" + ANSI_RESET);
        startServer();
    }

    private void startServer() {

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {

            System.out.println(ANSI_GREEN + "Server wait connection..." + ANSI_RESET);
            socket = serverSocket.accept();
            System.out.println(ANSI_GREEN + "Client connected" + ANSI_RESET);

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());


            while (true) {
                String str = dis.readUTF();
                if (str.equalsIgnoreCase("/finish")) {
                    dos.writeUTF(str);
                    break;
                }
                dos.writeUTF(str);
                System.out.println(str);
                Scanner scanner = new Scanner(System.in);
                System.out.print(ANSI_GREEN + "Ответ сервера: " + ANSI_RESET);
                String str1 = scanner.nextLine();
                dos.writeUTF(str1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


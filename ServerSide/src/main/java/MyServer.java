import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {
    private final Integer SERVER_PORT = 8880;
    Socket socket = null;

    public MyServer() {
        System.out.println("Server start");
        startServer();

    }

    private void startServer(){

    try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)){

        System.out.println("Server wait connection...");
        socket = serverSocket.accept();
        System.out.println("Client connected");

        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        while (true){
            String str = dis.readUTF();
            if(str.equalsIgnoreCase("/finish")){
                dos.writeUTF(str);
                break;
            }
            dos.writeUTF("You send me: " + str);
            System.out.println(str);
        }

    }

    catch (Exception e){
        e.printStackTrace();
    }}


}


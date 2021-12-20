import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class EchoClient extends JFrame {
    private final String SERVER_ADDRESS = "127.0.0.1";
    private final int SERVER_PORT = 8880;
    private DataInputStream dis;
    private DataOutputStream dos;

    private JTextField msgInputField;
    private JTextArea chatArea;

    private Socket socket;


    public EchoClient() throws IOException {
        connectionToServer();
        prepareGUI();
    }

    private void connectionToServer() throws IOException {
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());


        new Thread(() -> {
            try {
                while (true) {
                    String fromServer = dis.readUTF();
                    if (fromServer.equalsIgnoreCase("/finish")){
                        break;}
                    chatArea.append(fromServer + "\n");
                    }
                }
            catch(Exception e) {
                e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Wrong connection to server");
            }

        }).start();
    }

    private void sendMessageToServer(){
    String msg = msgInputField.getText();
    if (msg != null && !msg.trim().isEmpty()){
       try{
           dos.writeUTF(msg);
           msgInputField.setText("");
           msgInputField.grabFocus();
    } catch (IOException e){
       e.printStackTrace();
       JOptionPane.showMessageDialog(null, "You send incorrect message");

       }
    }
    }

    private void closeConnection(){
    try {
        dis.close();
    }
    catch (IOException e) {
        e.printStackTrace();
    }

    try {
        dos.close();
    }
    catch (IOException e) {
        e.printStackTrace();
    }

    try {
        socket.close();
    }
    catch (IOException e){
        e.printStackTrace();
    }
    }


    public void prepareGUI() {
        // Параметры окна
        setBounds(600, 300, 500, 500);
        setTitle("Клиент");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Текстовое поле для вывода сообщений
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        // Нижняя панель с полем для ввода сообщений и кнопкой отправки сообщений
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JButton btnSendMsg = new JButton("Отправить");
        bottomPanel.add(btnSendMsg, BorderLayout.EAST);
        msgInputField = new JTextField();
        add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.add(msgInputField, BorderLayout.CENTER);
        btnSendMsg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessageToServer();
            }
        });
        msgInputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessageToServer();
            }
        });

        // Настраиваем действие на закрытие окна
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    dos.writeUTF("/finish");
                    closeConnection();
                }catch (IOException ioException){
                    ioException.printStackTrace();
                }
            }
        });

        setVisible(true);
    }
}








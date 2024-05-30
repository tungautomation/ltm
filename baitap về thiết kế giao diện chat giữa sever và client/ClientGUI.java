import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ClientGUI extends JFrame {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private JTextField messageField;
    private JTextArea chatArea;

    public ClientGUI() {
        setTitle("Chat Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel chứa các thành phần của giao diện
        JPanel panel = new JPanel(new BorderLayout());

        // TextArea để hiển thị tin nhắn chat
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        
        panel.add(scrollPane, BorderLayout.CENTER);

        // TextField để nhập tin nhắn mới
        messageField = new JTextField();
        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        panel.add(messageField, BorderLayout.SOUTH);

        add(panel, BorderLayout.CENTER);

        // Kết nối đến server
        connectToServer("localhost", 12345);

        setVisible(true);
    }

    private void connectToServer(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Luồng nhận tin nhắn từ server
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String message;
                        while ((message = in.readLine()) != null) {
                            chatArea.append("Server: " + message + "\n");
                        }
                    } catch (IOException e) {
                    }
                }
            }).start();
        } catch (IOException e) {

        }
    }

    private void sendMessage() {
        String message = messageField.getText();
        out.println(message);
        chatArea.append("Client: " + message + "\n");
        messageField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientGUI();
            }
        });
    }
}

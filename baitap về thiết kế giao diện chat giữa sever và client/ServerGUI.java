import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ServerGUI extends JFrame {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    private JTextField messageField;
    private JTextArea chatArea;

    public ServerGUI() {
        setTitle("Chat Server");
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

        // Bắt đầu server
        startServer(12345);

        setVisible(true);
    }

    private void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started. Waiting for clients...");

            // Chấp nhận kết nối từ client
            clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket);

            // Tạo luồng đầu vào và đầu ra cho client
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Luồng nhận tin nhắn từ client
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String message;
                        while ((message = in.readLine()) != null) {
                            chatArea.append("Client: " + message + "\n");
                        }
                    } catch (IOException e) {
                    }
                }
            }).start();
        } catch (IOException e) {
            messageField.setText("Không có kết nối tới client:");
        }
    }

    private void sendMessage() {
        String message = messageField.getText();
        out.println(message);
        chatArea.append("Server: " + message + "\n");
        messageField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ServerGUI();
            }
        });
    }
}

package Program;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

import static Program.ServerWindow.isServerWorking;

public class TestFirst extends JFrame {

    private JFrame frame;
    private JTextArea chatHistoryArea;
    private JTextField messageField;
    private JTextField userField;
    private JButton sendButton;
    private JButton clearButton;
    private JButton loginButton;
    private JButton exitButton;
    private ServerWindow serverWindow;
    private final File logFile = new File("src/File/log.txt");
    public boolean isChatWorking;



    public TestFirst(ServerWindow serverWindow) {
        this.serverWindow = serverWindow;

        frame = new JFrame("Client Chat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 500);
        frame.setBounds(620, 0, 300, 500);

        chatHistoryArea = new JTextArea();
        chatHistoryArea.setEditable(false);
        chatHistoryArea.setBackground(Color.MAGENTA);
        JScrollPane scrollPane = new JScrollPane(chatHistoryArea);
        frame.add(scrollPane, BorderLayout.CENTER);
        chatHistoryArea.setForeground(Color.WHITE);

        messageField = new JTextField();
        sendButton = new JButton("Send");
        clearButton = new JButton("Clear");
        loginButton = new JButton("Login");
        exitButton = new JButton("Mute");

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());
        messagePanel.add(messageField, BorderLayout.CENTER);
        messagePanel.add(sendButton, BorderLayout.EAST);
        messagePanel.add(clearButton, BorderLayout.SOUTH);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new FlowLayout());
        loginPanel.add(loginButton);
        loginPanel.add(exitButton);
        userField = new JTextField("first_user", 0);
        messagePanel.add(userField, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(messagePanel, BorderLayout.SOUTH);
        frame.add(loginPanel, BorderLayout.NORTH);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Обработчик нажатия клавиши Enter в текстовом поле
        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearChatHistory();
            }
            private void clearChatHistory() {
                messageField.setText("");
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageField.setText("");
                login();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isChatWorking = false;
                JOptionPane.showMessageDialog(frame, "Сервер отключён.\n");
            }
        });



        frame.setVisible(true);
    }

    private void sendMessage() {
        if (isServerWorking && isChatWorking) {
            String user = userField.getText();
            String message = messageField.getText();
            if (!message.isEmpty()) {
                serverWindow.logMessage(user + " : " + message);
                serverWindow.logMessage("\n");
                messageField.setText("");
            }
        }
        else {
            JOptionPane.showMessageDialog(frame, "Сервер отключён.\n");
        }
    }

    private void login() {
        if (isServerWorking) {
            isChatWorking = true;
            JOptionPane.showMessageDialog(frame, "Сервер запущен.\n");
            chatTimer();
//            requestChatHistoryFromServer();
        }
        else {
            JOptionPane.showMessageDialog(frame, "Сервер отключён.\n");
        }
    }

//    private void chatTimer(){
//        // Вызов метода loadChatHistory каждую 1 секунду с использованием Timer
//        if (isServerWorking && isChatWorking) {
//            Timer timer = new Timer(1000, new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    loadChatHistory();
//                }
//            });
//            timer.start(); // Запуск таймера, не безопасная процедура
//        }
//    }

    private void chatTimer(){
        Timer timer = new Timer(1000, e -> updateChatHistory());
        timer.start();
    }

//    private void loadChatHistory() {
//        if (logFile.exists()) {
//            StringBuilder history = new StringBuilder();
//            try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    history.append(line).append("\n");
//                }
//                chatHistoryArea.setText(history.toString());
//                chatHistoryArea.setCaretPosition(chatHistoryArea.getDocument().getLength());
//            } catch (IOException e) {
//                JOptionPane.showMessageDialog(frame, "Не удается прочитать историю чата.");
//            }
//        }
//    }



    private void updateChatHistory() {
        if (serverWindow.hasUpdates()) {
            String history = serverWindow.getChatHistory();
            chatHistoryArea.setText(history);
        }
    }





}

package Program;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static Program.ServerWindow.isServerWorking;

public class TestSecond extends JFrame {

    private String userName = "second_user";
    private JFrame frame;
    private JTextArea chatHistoryArea;
    private JTextField messageField;
    private JTextField userField;
    private JButton sendButton;
    private JButton clearButton;
    private JButton loginButton;
    private ServerWindow serverWindow;
    private JButton exitButton;
    public boolean isChatWorking;
    private final File logFile = new File("src/File/log.txt");


    public TestSecond(ServerWindow serverWindow) {
        this.serverWindow = serverWindow;

        frame = new JFrame("Client Chat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 500);

        chatHistoryArea = new JTextArea();
        chatHistoryArea.setEditable(false);
        chatHistoryArea.setBackground(Color.GREEN);
        JScrollPane scrollPane = new JScrollPane(chatHistoryArea);
        frame.add(scrollPane, BorderLayout.CENTER);
        chatHistoryArea.setForeground(Color.BLACK);

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

        userField = new JTextField(userName, 0);
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
        }
        else {
            JOptionPane.showMessageDialog(frame, "Сервер отключён.\n");
        }
    }

    private void chatTimer(){
        // Вызов метода loadChatHistory каждую 1 секунду с использованием Timer
        if (isServerWorking && isChatWorking) {
            Timer timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    loadChatHistory();
                }
            });
            timer.start(); // Запуск таймера, не безопасная процедура
        }
    }


    private void loadChatHistory() {
        if (logFile.exists()) {
            StringBuilder history = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    history.append(line).append("\n");
                }
                chatHistoryArea.setText(history.toString());
                chatHistoryArea.setCaretPosition(chatHistoryArea.getDocument().getLength());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Не удается прочитать историю чата.");
            }
        }
    }



}

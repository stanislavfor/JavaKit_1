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

public class ClientGUI extends JFrame {
    private JFrame frame;
    private JTextArea chatHistoryArea;
    private JTextField userField;
    private JTextField messageField;
    private ServerWindow serverWindow;
    private JButton sendButton;
    private JButton clearButton;
    private JButton loginButton;
    private final File logFile = new File("src/File/log.txt");


    public ClientGUI(ServerWindow serverWindow) {
        this.serverWindow = serverWindow;

        frame = new JFrame("Client Chat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 500);

        chatHistoryArea = new JTextArea();
        chatHistoryArea.setEditable(false);
        chatHistoryArea.setBackground(Color.GREEN);

        JScrollPane scrollPane = new JScrollPane(chatHistoryArea);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setForeground(Color.BLACK);

        // Панель для ввода сообщений
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());
        userField = new JTextField("user", 0);
        messagePanel.add(userField, BorderLayout.NORTH);

        // Текстовое поле для ввода сообщений
        messageField = new JTextField();
        messagePanel.add(messageField, BorderLayout.CENTER);

        // Кнопка отправки сообщений
        sendButton = new JButton("Send");
        messagePanel.add(sendButton, BorderLayout.EAST);

        clearButton = new JButton("Clear");
        messagePanel.add(clearButton, BorderLayout.SOUTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(messagePanel, BorderLayout.SOUTH);
        loginButton = new JButton("Login");
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new FlowLayout());
        loginPanel.add(loginButton);
        frame.add(loginPanel, BorderLayout.NORTH);

        // Обработчик нажатия кнопки очистки истории чата
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearChatHistory();
            }
            private void clearChatHistory() {
                messageField.setText("");
            }
        });


        // Обработчик нажатия кнопки отправки сообщения
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
                login();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        // Отображение окна
        frame.setVisible(true);
    }


    private void sendMessage() {
        if (isServerWorking) {
            String user = userField.getText();
            String message = messageField.getText();
            if (!message.isEmpty()) {
                serverWindow.logMessage(user + " : " + message);
                serverWindow.logMessage("\n");
                messageField.setText("");
            }
        }
        else {
            JOptionPane.showMessageDialog(frame, "Сервер не запущен.\n");
        }
    }


    private void login() {
        if (isServerWorking) {
            JOptionPane.showMessageDialog(frame, "Сервер запущен.\n");
        }
        else {
            JOptionPane.showMessageDialog(frame, "Сервер не запущен.\n");
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

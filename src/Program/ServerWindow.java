package Program;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class ServerWindow {
    public static JTextArea chatHistoryArea;
    private final File logFile = new File("src/File/log.txt");
    public static boolean isServerWorking;

    public ServerWindow() {

            JFrame frame = new JFrame("Server Window");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 600);


            chatHistoryArea = new JTextArea();
            chatHistoryArea.setForeground(Color.BLUE);
            chatHistoryArea.setEditable(false);
            chatHistoryArea.setBackground(Color.CYAN);
            JScrollPane scrollPane = new JScrollPane(chatHistoryArea);
            frame.add(scrollPane, BorderLayout.CENTER);

            // Панель для кнопок управления сервером
            JPanel buttonPanel = new JPanel();
            JButton startButton = new JButton("Start");
            startButton.setBackground(Color.GREEN);
            JButton stopButton = new JButton("Stop");
            stopButton.setBackground(Color.RED);
            buttonPanel.add(startButton);
            buttonPanel.add(stopButton);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            // Обработчик нажатия кнопки запуска сервера
            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    startServer();
                }
            });

            // Обработчик нажатия кнопки остановки сервера
            stopButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    stopServer();
                }
            });

            frame.setVisible(true);
    }


    protected void startServer() {
        if (!isServerWorking) {
            isServerWorking = true;
        }
        if (logFile.exists()) {
            getChatHistory();
        }
        chatHistoryArea.append(":сервер стартовал\n");
    }

    private void stopServer() {
        isServerWorking = false;
        chatHistoryArea.append(":сервер остановлен\n");
    }

    public void logMessage(String message) {
        // Запись сообщения в историю чата и файл log.txt
        chatHistoryArea.append(message);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isServerRunning() {
        return false;
    }


    public String getChatHistory() {
        StringBuilder history = new StringBuilder();
        if (logFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    chatHistoryArea.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return history.toString();
    }


}

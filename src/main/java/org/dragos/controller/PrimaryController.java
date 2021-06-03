package org.dragos.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;


public class PrimaryController implements Initializable {

    private static final String INPUT_FILE = "fisier.txt";
    @FXML
    public Label time;
    @FXML
    public Button stopButton;
    @FXML
    public Label temperature;
    @FXML
    public Label pciTemp;

    private static final String FILE_NAME = "input.txt";
    private static FileWriter myWriter;
    private static Timeline clock;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            myWriter = new FileWriter(FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initClock();
        initHoverState();
    }

    void initClock() {

        clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            time.setText(currentTime.getHour() + ":" + currentTime.getMinute() + ":" + currentTime.getSecond());

                String chunk = generateTemp();
                String[] chunks = chunk.split(" ", 2);
                pciTemp.setText(chunks[1]);
                temperature.setText(chunks[0]);


        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    void initHoverState() {
        stopButton.setOnMouseEntered(event -> stopButton.setStyle("-fx-background-color:#ffa500;"));
        stopButton.setOnMouseExited(event -> stopButton.setStyle("-fx-background-color:coral;"));
    }

    public void stopUi() {
        clock.stop();
        System.exit(0);
    }

    public String generateTemp() {
        String[] cpu = new String[100];
        String[] pci = new String[100];
        StringBuilder secondBuilder = new StringBuilder();
        try {
            String command = "sensors";
            Process child = Runtime.getRuntime().exec(command);
            while (true) {

                InputStream in = child.getInputStream();
                int c;
                StringBuilder builder = new StringBuilder();
                while ((c = in.read()) != -1) {
                    builder.append((char) c);
                }
                in.close();
                String[] parsedLines = builder.toString().split("\n", 20);

                cpu = parsedLines[3].split(" ", 11);
                pci = parsedLines[12].split(" ", 6);

                secondBuilder.append(cpu[9]);
                secondBuilder.append(" ");
                secondBuilder.append(pci[4]);

            }
        } catch (IOException e) {
            System.err.println(" ");
        }

        return secondBuilder.toString();

    }
}
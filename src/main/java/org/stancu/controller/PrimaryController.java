package org.stancu.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;


public class PrimaryController implements Initializable {

    @FXML
    public Label time;
    @FXML
    public Button stopButton;
    @FXML
    public Label temperature;
    @FXML
    public Label pciTemp;

    private static Timeline clock;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
        String[] cpu;
        String[] pci;
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
            System.out.println(" ");
        }

        return secondBuilder.toString();

    }
}
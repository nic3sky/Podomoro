package com.sang.controllers;


import com.sang.model.Attempt;
import com.sang.model.AttemptKind;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;


public class Home {
    private final AudioClip mySound;
    @FXML
    private VBox container;
    @FXML
    private TextArea message;

    @FXML
    private Label title;
    private Attempt mCurrentAttempt;
    private StringProperty mTimerText;
    private Timeline mTimeLine;

    public Home() {
        mTimerText = new SimpleStringProperty();
        setTimerText(0);
        mySound = new AudioClip(getClass().getResource("/sounds/defaultsound.mp3").toExternalForm());
    }

    public String getTimerText() {
        return mTimerText.get();
    }

    public StringProperty timerTextProperty() {
        return mTimerText;
    }

    public void setTimerText(String mTimerText) {
        this.mTimerText.set(mTimerText);
    }

    public void setTimerText(int remainingSeconds) {
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        setTimerText(String.format("%02d:%02d", minutes, seconds));
    }


    private void prepareAttempt(AttemptKind kind) {
        clearAttempStyles();
        //Method has succincted by Refactor -Extract
        resetClock();


        mCurrentAttempt = new Attempt("", kind);
        addAttemptStyle(kind);
        title.setText(kind.getDisplayName());
        setTimerText(mCurrentAttempt.getmRemainingSeconds());
        //Creating multiple timelines
        mTimeLine = new Timeline();
        //Timeline animation for every single second
        mTimeLine.setCycleCount(kind.getmTotalSeconds());
        mTimeLine.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e -> {
            mCurrentAttempt.tick();
            setTimerText(mCurrentAttempt.getmRemainingSeconds());
        }));
        //setonfinished : Animation in JavaFX
        //if currentA is Focus -> switch to Break , otherwise -> focus.
        mTimeLine.setOnFinished(e -> {
            saveCurrentAttempt();
            mySound.play();
            prepareAttempt(mCurrentAttempt.getmKind() == AttemptKind.FOCUS ? AttemptKind.BREAK : AttemptKind.FOCUS);
        });

    }

    private void saveCurrentAttempt() {
        mCurrentAttempt.setmMessage(message.getText());
        mCurrentAttempt.save();

    }

    private void resetClock() {
        if (mTimeLine != null && mTimeLine.getStatus() == Animation.Status.RUNNING) {
            mTimeLine.stop();
        }
    }

    public void playTimer() {
        container.getStyleClass().add("playing");
        mTimeLine.play();
    }

    public void pauseTimer() {
        container.getStyleClass().remove("playing");
        mTimeLine.pause();
    }

    private void addAttemptStyle(AttemptKind kind) {

        container.getStyleClass().add(kind.toString().toLowerCase());
    }

    private void clearAttempStyles() {
        container.getStyleClass().remove("playing");
        for (AttemptKind kind : AttemptKind.values()) {
            container.getStyleClass().remove(kind.toString().toLowerCase());

        }
    }


    public void handleRestart(ActionEvent actionEvent) {
        mySound.stop();
        prepareAttempt(AttemptKind.FOCUS);
        playTimer();
    }

    public void handlePlay(ActionEvent actionEvent) {
        if (mCurrentAttempt == null) {
            handleRestart(actionEvent);
        } else {
            playTimer();
        }
    }

    public void handlePause(ActionEvent actionEvent) {
        pauseTimer();
    }
}

package com.jclarity.region.fx;


import com.jclarity.region.FrameCounter;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

import java.util.Timer;
import java.util.TimerTask;

public class VideoControls extends HBox {

    private ObjectProperty<ObservableList<ToggleButton>> buttons;
    private boolean playing = false;
    private Timer timer = null;
    private Button playPause;
    private FrameCounter frameCounter;
    final TimeSlider slider;

    public VideoControls() {
        getStyleClass().add("video-controls");
        slider = new TimeSlider();
        setButtons();
    }

    public void setFrameCounter(FrameCounter frameCounter) {
        this.frameCounter = frameCounter;
        slider.setFrameCounter( frameCounter);
    }

    private void setButtons() {

        ToggleButton openFileDialog = new ToggleButton("open");

        openFileDialog.setOnAction(event -> {
            pause();
            System.out.println("open file" + event.toString());
        });


        Image image;
        image = new Image(getClass().getResourceAsStream("step_to_beginning.png"));
        ToggleButton skipToBeginning = new ToggleButton("|<");  //rewind
        skipToBeginning.setOnAction(event -> {
            pause();
            frameCounter.set(0);
            System.out.println(event.toString() + ", counter: " + frameCounter.getFrameIndex());
        });

        image = new Image(getClass().getResourceAsStream("step_backwards.png"));
        ToggleButton rewind = new ToggleButton("<<");
        rewind.setOnAction(event -> {
            pause();
            timer.cancel();
            frameCounter.stepBackwards();
        });


        image = new Image(getClass().getResourceAsStream("play.png"));
        playPause = new Button(">");
        playPause.setOnAction(event -> {
            if ( ! playing) {
                playPause.setText("||");
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                if ( ! frameCounter.atEnd())
                                    frameCounter.stepForward();
                                else {
                                    this.cancel();
                                    playPause.setText(">");
                                    playing = false;
                                }
                            }
                        }, 0, 500);
                playing = true;
            } else
                pause();
        });


        image = new Image(getClass().getResourceAsStream("step_forward.png"));
        ToggleButton fastForward = new ToggleButton(">>"); //
        fastForward.setOnAction(event -> {
            pause();
            frameCounter.stepForward();
            System.out.println(event.toString() + ", counter: " + frameCounter.getFrameIndex());
        });


        image = new Image(getClass().getResourceAsStream("step_to_end.png"));
        ToggleButton skipToEnd = new ToggleButton(">|"); //end
        skipToEnd.setOnAction(event -> {
            pause();
            frameCounter.stepToEnd();
        });
        getChildren().addAll(openFileDialog, skipToBeginning, rewind, playPause, slider, fastForward, skipToEnd);
    }

    private void pause() {
        if ( playing) {
            playPause.setText(">");
            timer.cancel();
            playing = false;
        }
    }
}

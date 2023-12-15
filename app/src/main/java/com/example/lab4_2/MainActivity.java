package com.example.lab4_2;

import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private TextView songName, duration;
    private double timeElapsed = 0, finalTime = 0;
    private int forwardTime = 2000, backwardTime = 2000;
    private Handler durationHandler = new Handler();
    private SeekBar seekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
    }

    public void initializeViews() {
        songName = findViewById(R.id.songName);
        mediaPlayer = MediaPlayer.create(this, R.raw.hyukoh);
        finalTime = mediaPlayer.getDuration();
        duration = findViewById(R.id.songDuration);
        seekbar = findViewById(R.id.seekBar);
        songName.setText("[Official Audio] HYUKOH(혁오) - Silverhair Express\n");
        seekbar.setMax((int) finalTime);
        seekbar.setClickable(false);
    }

    public void play(View view) {
        mediaPlayer.start();
        timeElapsed = mediaPlayer.getCurrentPosition();
        seekbar.setProgress((int) timeElapsed);
        durationHandler.postDelayed(updateSeekBarTime, 100);
    }

    private final Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            timeElapsed = mediaPlayer.getCurrentPosition();
            seekbar.setProgress((int) timeElapsed);
            double timeRemaining = finalTime - timeElapsed;
            duration.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining),
                    TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))
            ));
            durationHandler.postDelayed(this, 100);
        }
    };

    public void pause(View view) {
        mediaPlayer.pause();
    }

    public void forward(View view) {
        if ((timeElapsed + forwardTime) <= finalTime) {
            timeElapsed += forwardTime;
            mediaPlayer.seekTo((int) timeElapsed);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        durationHandler.removeCallbacks(updateSeekBarTime);
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
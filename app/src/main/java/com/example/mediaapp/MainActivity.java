package com.example.mediaapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private MediaPlayer mp;
    private SeekBar sk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button play = findViewById(R.id.button3);
        Button pause = findViewById(R.id.button2);
        Button next=findViewById(R.id.button);
        sk = findViewById(R.id.seekBar);

        // Create media player object
        mp = MediaPlayer.create(this, R.raw.dugga_elo);

        // Set seek bar max duration
        sk.setMax(mp.getDuration());

        // Start media file
        play.setOnClickListener(v -> mp.start());

        // Pause media file
        pause.setOnClickListener(v -> mp.pause());

        // Update seek bar while playing
        new Thread(() -> {
            while (mp != null) {
                try {
                    if (mp.isPlaying()) {
                        handler.post(() -> sk.setProgress(mp.getCurrentPosition()));
                    }
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // Seek bar change listener
        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mp.seekTo(progress);
                }
            }
            // Pause playback while user is dragging
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            // Resume playback after user stops dragging
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
       Intent intent = new Intent(this, video.class);
        next.setOnClickListener(v -> startActivity(intent));
        // Release media player resources when activity is destroyed
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }
}

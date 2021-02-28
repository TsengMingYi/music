package com.example.myapplication4;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import static com.example.myapplication4.MusicManager.*;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private String URI_PREFIX;
    private ImageView playOrPauseBtn;
    private ImageView stopBtn;
    private SeekBar seek_Bar;
    private ImageView nextBtn;

    private MusicManager musicManager;

    private final int CHOOSE_SONG_REQUEST_CODE = 123;

    private Handler handler = new Handler();
    private Runnable seekbarAnimation = new Runnable() {
        @Override
        public void run() {
            Log.e("test", " " + musicManager.getNowPlayTime() + "  " + musicManager.getMaxPlayTime());
            updateProgressBar();
            handler.postDelayed(seekbarAnimation, 1000);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate()");


       musicManager = MusicManager.getInstance();


        musicManager.setContext(this);
        musicManager.init();

        URI_PREFIX = "android.resource://" + getPackageName() + "/";

//
        musicManager.setDataSource(Uri.parse(URI_PREFIX + R.raw.music));
        musicManager.prepareAsync();

        findViews();
        playOrPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Press playOrPauseBtn: curState: " + musicManager.getCurState());
                if (musicManager.getCurState() == MusicManager.PREPARED || musicManager.getCurState() == MusicManager.PAUSED) {
                    musicManager.start();
                    handler.postDelayed(seekbarAnimation, 1000);
                } else if (musicManager.getCurState() == MusicManager.SARTED) {
                    musicManager.pause();
                    handler.removeCallbacks(seekbarAnimation);
                }

                updateUI();
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "Press stopBtn: curState: " + musicManager.getCurState());
                if (musicManager.getCurState() == MusicManager.SARTED || musicManager.getCurState() == MusicManager.PAUSED) {
                    musicManager.stop();
                    handler.removeCallbacks(seekbarAnimation);
                }
                if (musicManager.getCurState() == MusicManager.STOPPED) {
                    musicManager.prepareAsync();
                }

                //updateUI();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicManager.getCurState() != PREPARED) {
                    return;
                }
                chooseSong();
            }
        });

        seek_Bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicManager.setMusicPlayTime(seekBar.getProgress());
            }
        });
    }

    private void chooseSong() {
        Intent ii = new Intent();
        ii.setAction(Intent.ACTION_GET_CONTENT);
        ii.setType("*/*");
        startActivityForResult(ii, CHOOSE_SONG_REQUEST_CODE);
    }

    public void updateProgressBar() {
        seek_Bar.setProgress(musicManager.getNowPlayTime());
        seek_Bar.setMax(musicManager.getMaxPlayTime());
    }

    public void onPreparedListener() {
        updateUI();
        seek_Bar.setProgress(0);
        seek_Bar.setMax(musicManager.getMaxPlayTime());
    }

    public void updateUI() {


        int curState = musicManager.getCurState();
        switch (curState) {
            case IDLE:

                break;
            case INITIALIZED:
                break;
            case PREPARING:
                break;
            case PREPARED:
                playOrPauseBtn.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                stopBtn.setEnabled(false);
                break;
            case SARTED:
                playOrPauseBtn.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
                stopBtn.setEnabled(true);
                break;
            case STOPPED:

                break;
            case PAUSED:
                playOrPauseBtn.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                stopBtn.setEnabled(true);
                break;
        }


    }

    private void findViews() {
        playOrPauseBtn = findViewById(R.id.play_or_pause_btn);
        stopBtn = findViewById(R.id.stop_btn);
        seek_Bar = findViewById(R.id.seek_bar);
        nextBtn = findViewById(R.id.next_btn);
        musicManager.stop();

    }

    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == CHOOSE_SONG_REQUEST_CODE) {
            Uri uri = data.getData();
            if (uri != null) {
                musicManager.reset();
                musicManager.setDataSource(uri);
                musicManager.prepareAsync();
            }
        }

    }
}

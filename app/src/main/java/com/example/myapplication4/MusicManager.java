package com.example.myapplication4;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

public class MusicManager {

    private int curState = 0;

    public static final int IDLE = 0;
    public static final int INITIALIZED =1;
    public static final int PREPARING =2;
    public static final int PREPARED = 3;
    public static final int SARTED = 4;
    public static final int STOPPED = 5;
    public static final int PAUSED = 6;

    private MediaPlayer mediaPlayer = new MediaPlayer();

    private static MusicManager instance;

    private Context context;

    public void setContext(Context context){
        this.context = context;
    }

    public static MusicManager getInstance(){
        if (instance==null){
            instance = new MusicManager();
        }
        return instance;
    }

    public void setMusicPlayTime(int newPlayTime){
        mediaPlayer.seekTo(newPlayTime);
    }

    public int getMaxPlayTime(){
        return mediaPlayer.getDuration();
    }

    public int getNowPlayTime(){
        return mediaPlayer.getCurrentPosition();
    }

    public void init(){
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (canChangeState(curState , PREPARED)){
                    curState = PREPARED;
                    ( (MainActivity) context) .onPreparedListener();
                }
            }
        });
        reset();
    }

    public void reset(){
        if(canChangeState(curState , IDLE)){
            mediaPlayer.reset();
            curState = IDLE;
        }
    }

    private MusicManager() {
    }


    private boolean canChangeState(int oldState, int newState){
        switch (oldState){
            case IDLE:
                return newState== INITIALIZED || newState==IDLE;
            case INITIALIZED:
                return newState== PREPARING || newState==IDLE;
            case PREPARING:
                return newState==PREPARED || newState==IDLE;
            case PREPARED:
                return newState==SARTED || newState==STOPPED || newState==IDLE;
            case SARTED:
                return newState==STOPPED || newState==PAUSED || newState==IDLE;
            case STOPPED:
                return newState==PREPARING || newState==IDLE;
            case PAUSED:
                return newState==SARTED || newState==STOPPED || newState==IDLE;
        }
        return false;
    }

    public boolean start(){
        if ( canChangeState( curState ,  SARTED) ){
            mediaPlayer.start();
            curState =  SARTED;
            return true;
        }else{
            return false;
        }

    }

    public int getCurState(){
        return curState;
    }

    public boolean prepareAsync(){
        if (canChangeState(curState , PREPARING)){
            mediaPlayer.prepareAsync();
            curState = PREPARING;
            return true;
        }else{
            return false;
        }

    }

    public boolean stop(){
        if (canChangeState(curState , STOPPED)){
            mediaPlayer.stop();
            curState = STOPPED;
            return true;
        }else{
            return false;
        }
    }

    public boolean pause(){
        if (canChangeState(curState , PAUSED)){
            mediaPlayer.pause();
            curState = PAUSED;
            return true;
        }else{
            return false;
        }
    }

    public boolean setDataSource(Uri uri){
        if (canChangeState(curState , INITIALIZED)){
            try {
                mediaPlayer.setDataSource(context,uri);
                curState = INITIALIZED;
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }else {
            return false;
        }
    }


}

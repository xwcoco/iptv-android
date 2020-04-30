package com.dfsoft.myiptvplayer.player;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.ArrayList;

public class PlaybackService extends Service {

    private String TAG = "PlaybackService";


    private class LocalBinder extends Binder {
        PlaybackService getService() {
            return PlaybackService.this;
        }
    }
    public static PlaybackService getService(IBinder iBinder) {
        LocalBinder binder = (LocalBinder) iBinder;
        return binder.getService();
    }

    private final IBinder mBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
        if (mLibVLC != null) {
            mLibVLC.release();
        }
        super.onDestroy();
    }

    private LibVLC mLibVLC = null;
    public MediaPlayer mMediaPlayer;

    public void initVLC(Context context) {

        if (mLibVLC != null) return;

        ArrayList<String> options= new ArrayList<>();
        options.add("-vvv");
//        options.add("--h264-fps=2");
//        options.add("--hevc-fps=20");
        mLibVLC = new LibVLC(context, options);
        mMediaPlayer = new MediaPlayer(mLibVLC);
//        mMediaPlayer.setEventListener(this);
    }

    public void play(String url) {
        Media media = new Media(mLibVLC, Uri.parse(url));
        media.addOption(":network-caching=1000");
        mMediaPlayer.setMedia(media);
        media.release();

        mMediaPlayer.play();

    }

}

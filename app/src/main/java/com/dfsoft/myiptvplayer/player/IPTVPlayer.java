package com.dfsoft.myiptvplayer.player;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.MainThread;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.util.ArrayList;

public class IPTVPlayer implements MediaPlayer.EventListener {
    private String TAG = "IPTVPlayer";
    private static final boolean USE_TEXTURE_VIEW = false;
    private static final boolean ENABLE_SUBTITLES = true;

    private VLCVideoLayout mVideoLayout;


    private Context mContext;

    private boolean mBound = false;


    public IPTVPlayer(Context context) {
        mContext = context;
        Intent it = new Intent(context, PlaybackService.class);
        mBound = mContext.bindService(it, mServiceConnection, Service.BIND_AUTO_CREATE);
    }

    @MainThread
    public interface Callback {
        void onConnected(PlaybackService service);

        void onDisconnected();

        void onBuffering(float percent);
    }

    private Callback mCallback;

    public void setCallBack(Callback callBack) {
        this.mCallback = callBack;
    }

    private PlaybackService mService = null;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            Log.d(TAG, "Service Connected");
            if (!mBound)
                return;

            mService = PlaybackService.getService(iBinder);

            if (mService != null) {
                mService.initVLC(mContext);

                mService.mMediaPlayer.setEventListener(IPTVPlayer.this);

                if (mCallback != null)
                    mCallback.onConnected(mService);
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "Service Disconnected");
            mService = null;
            if (mCallback != null)
                mCallback.onDisconnected();
        }
    };


    public void setVideoLayout(VLCVideoLayout layout) {
        mVideoLayout = layout;
        if (mService != null) {
            Log.d(TAG, "setVideoLayout: ---");
            if (mService.mMediaPlayer.isPlaying()) {
                Log.d(TAG, "setVideoLayout: +++");
                mService.mMediaPlayer.attachViews(mVideoLayout, null, ENABLE_SUBTITLES, USE_TEXTURE_VIEW);
                mService.mMediaPlayer.updateVideoSurfaces();
            }
        }
    }

    public void play(String url) {
        if (mService != null) {
            mService.play(url);

            if (mVideoLayout != null) {
                mService.mMediaPlayer.attachViews(mVideoLayout, null, ENABLE_SUBTITLES, USE_TEXTURE_VIEW);
            }
        }
//        mMediaPlayer.attachViews(mVideoLayout, null, ENABLE_SUBTITLES, USE_TEXTURE_VIEW);
//
//        Media media = new Media(mLibVLC, Uri.parse(url));
//        media.addOption(":network-caching=300");
//        mMediaPlayer.setMedia(media);
//        media.release();
//
//        mMediaPlayer.play();
    }

    public void onStop() {
//        if (mMediaPlayer != null) {
//            mMediaPlayer.stop();
//            mMediaPlayer.detachViews();
//        }
    }

    @Override
    public void onEvent(MediaPlayer.Event event) {
        switch (event.type) {
            case MediaPlayer.Event.Buffering:
                Log.d(TAG, "buffer.... " + event.getBuffering());
                if (mCallback != null)
                    mCallback.onBuffering(event.getBuffering());
                break;
            case MediaPlayer.Event.Opening:
//                Log.d(TAG,"open....");
                break;
            case MediaPlayer.Event.LengthChanged:
//                Log.d(TAG,"length Changed :" + event.getLengthChanged());
                break;
            case MediaPlayer.Event.EncounteredError:
                Log.d(TAG, "onEvent: EncounteredError");
            default:
//                Log.d(TAG, "onEvent: 0x"+Integer.toHexString(event.type));
        }
    }

    public void onDestroy() {
        Intent it = new Intent(mContext, PlaybackService.class);
        mContext.stopService(it);

    }


}

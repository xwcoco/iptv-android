package com.dfsoft.myiptvplayer.player;

import android.content.Context;
import android.net.Uri;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.util.ArrayList;

public class IPTVPlayer {
    private static final boolean USE_TEXTURE_VIEW = false;
    private static final boolean ENABLE_SUBTITLES = true;

    private VLCVideoLayout mVideoLayout;

    private LibVLC mLibVLC;
    private MediaPlayer mMediaPlayer;

    public IPTVPlayer(Context context, VLCVideoLayout videoLayout) {
        ArrayList<String> options= new ArrayList<>();
        options.add("-vvv");
//        options.add("--h264-fps=2");
//        options.add("--hevc-fps=20");
        mLibVLC = new LibVLC(context, options);
        mMediaPlayer = new MediaPlayer(mLibVLC);

        mVideoLayout = videoLayout;
    }

    public void play(String url) {
        mMediaPlayer.attachViews(mVideoLayout, null, ENABLE_SUBTITLES, USE_TEXTURE_VIEW);

        Media media = new Media(mLibVLC, Uri.parse(url));
        media.addOption(":network-caching=300");
        mMediaPlayer.setMedia(media);
        media.release();

        mMediaPlayer.play();
    }

    public void onStop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.detachViews();
        }
    }

    public void onDestroy() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
        if (mLibVLC != null) {
            mLibVLC.release();
        }
    }
}

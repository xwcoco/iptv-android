package com.dfsoft.myiptvplayer;

import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ConsoleControl {
    private final  String TAG = "ConsoleControl";
    private AppCompatActivity mMainView;
    private IPTVConfig config = IPTVConfig.getInstance();

    private TextView tv_channelnum;
    private TextView tv_channelname;
    private TextView tv_srcinfo;
    private TextView tv_epgcurrent;
    private TextView tv_epgnext;

    public ConsoleControl(AppCompatActivity mMainView) {
        this.mMainView = mMainView;
        tv_channelnum = mMainView.findViewById(R.id.tv_channelnum);
        tv_channelname = mMainView.findViewById(R.id.tv_channelname);
        tv_srcinfo = mMainView.findViewById(R.id.tv_srcinfo);

        tv_epgcurrent = mMainView.findViewById(R.id.tv_epgcurrent);
        tv_epgnext = mMainView.findViewById(R.id.tv_epgnext);
    }

    public void showPlayingChannelConsole() {
        IPTVChannel channel = config.getPlayingChannal();
        if (channel == null) return;

        tv_channelnum.setText(String.valueOf(channel.num));
        tv_channelname.setText(channel.name);
        String tmp = String.valueOf(channel.playIndex) + " / " + String.valueOf(channel.source.size());
        tv_srcinfo.setText(tmp);


        String curEPG = "";
        String nextEPG = "";

        if (channel.epg.isEmpty()) {
            Log.d(TAG, "showPlayingChannelConsole: empty epg");
            channel.loadEPGData();
        } else {
            channel.epg.getCurrentTimer();
            if (channel.epg.curTime != -1) {
                Log.d(TAG, "showPlayingChannelConsole: curTime = "+channel.epg.curTime);
                IPTVEpgData data = channel.epg.data.get(channel.epg.curTime);
                curEPG = data.starttime + " " + data.name;
                if (channel.epg.curTime + 1 < channel.epg.data.size()) {
                    data = channel.epg.data.get(channel.epg.curTime + 1);
                    nextEPG = data.starttime + " " + data.name;
                }
            }
        }

        tv_epgcurrent.setText(curEPG);
        tv_epgnext.setText(nextEPG);


    }
}

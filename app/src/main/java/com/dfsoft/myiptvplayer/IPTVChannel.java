package com.dfsoft.myiptvplayer;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dfsoft.myiptvplayer.views.EPGAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class IPTVChannel {
    private final String TAG = "IPTVChannel";
    public IPTVChannel() {
    }
    private OkHttpClient client = new OkHttpClient();

    private IPTVConfig config = IPTVConfig.getInstance();

    public Integer num = -1;

    public String name = "";

    public List<String> source = new ArrayList<String>();

    public IPTVEPG epg = new IPTVEPG();

    public int playIndex = 0;

    public EPGAdapter epgAdapter = null;

    public void loadEPGData() {
        String url = config.host + "/epg/live_proxy_epg.php/out_epg?id="+name;

        // 创建请求实例
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("IPTVConfig", "接口调用失败");
                epg.code = 0;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("IPTVConfig", "接口调用成功");
                final String strByNet = response.body().string();
                load(strByNet);
            }
        });
    }

    public void load(String dataString) {
        Gson json = new Gson();
        epg = json.fromJson(dataString, IPTVEPG.class);
        if (config.dataEventLister != null)
            config.dataEventLister.onEPGLoaded(this);
    }

}

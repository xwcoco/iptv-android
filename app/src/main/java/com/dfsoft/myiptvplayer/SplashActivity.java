package com.dfsoft.myiptvplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.dfsoft.myiptvplayer.FullscreenActivity;

public class SplashActivity extends AppCompatActivity implements IPTVConfig.DataEventLister {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_splash);

        IPTVConfig config = IPTVConfig.getInstance();
        config.setDataEventLister(this);
        config.initConfig();
    }

    /**
     * 跳转至MainActivity
     */
    private void toMainActivity() {
        Intent intent = new Intent(this, FullscreenActivity.class);

        //singTask
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //屏蔽back键
    }


    @Override
    public void onInitData(Boolean isOk) {
        if (isOk) {
            this.toMainActivity();
        } else {
            this.finish();
        }
    }
}

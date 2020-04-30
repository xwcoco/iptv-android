package com.dfsoft.myiptvplayer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dfsoft.myiptvplayer.player.IPTVPlayer;
import com.dfsoft.myiptvplayer.player.PlaybackService;
import com.dfsoft.myiptvplayer.views.CategoryAdapter;
import com.dfsoft.myiptvplayer.views.CategoryView;

import org.videolan.libvlc.util.VLCVideoLayout;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity implements IPTVPlayer.Callback {
    private String TAG = "FullscreenActivity";
    private TextView mInfoView;

//    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            if (AUTO_HIDE) {
//                delayedHide(AUTO_HIDE_DELAY_MILLIS);
//            }
//            return false;
//        }
//    };

    private CategoryView mCategoryView = null;
    private View mVideoView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }


        mVideoView = findViewById(R.id.fullscreen_content);

//        mCategoryView=PopupLayout.init(FullscreenActivity.this, R.layout.layout_category);
        mCategoryView = findViewById(R.id.category_view);
//        mCategoryHide = new HideContent(this,findViewById(R.id.fullscreen_category),mCategoryView);
//
//        mInfoView = findViewById(R.id.info);
//
//        mInfoHide = new HideContent(this,findViewById(R.id.fullscreen_info_panel),mInfoView);
//
//        mPanelHide = new HideContent(this,findViewById(R.id.fullscreen_panel_controls),findViewById(R.id.dummy_button));
//
//        mCategoryAdapter = new CategoryAdapter(this.getApplicationContext(),this.config.category);
//        mCategoryView.setAdapter(mCategoryAdapter);


        // Set up the user interaction to manually show or hide the system UI.
        mVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        this.initCategory();
        this.initPlayer();
    }

    private IPTVPlayer mPlayer = null;

    private CategoryAdapter mCategoryAdapter = null;

    private IPTVConfig config = IPTVConfig.getInstance();

    private void initCategory() {
//        this.config.initConfig();
//        application.android.on('activityCreated', function activityCreated(args) {
//            android.os.StrictMode.setThreadPolicy(android.os.StrictMode.ThreadPolicy.LAX)
//        })

//        try {
//            String ret =  this.config.post(this.config.host+"/data.php","{myiptv:1}");
//            Log.d(TAG, "initCategory: "+ret);
//        } catch (IOException e) {
//            Log.d(TAG, "initCategory: Error!");
//            e.printStackTrace();
//        }
//         this.config.run("")
//        final List<String> adapterData = new ArrayList<String>();
//        //存放要显示的数据
//        for (int i = 0; i < 20; i++) {
//            adapterData.add("ListItem" + i);
//        }
//        //创建ArrayAdapter对象adapter并设置适配器
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, adapterData);
//        //将LsitView绑定到ArrayAdapter上
//        mCategoryView.setAdapter(adapter);

    }

    private void initPlayer() {

//        mPlayer = new IPTVPlayer(this,(VLCVideoLayout)mContentView);
        mPlayer = new IPTVPlayer(this);

        mPlayer.setCallBack(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
//        delayedHide(100);
    }

    private void toggle() {
        this.mCategoryView.toggle();
    }


    private void showInfo(String text) {
        mInfoView.setText(text);
        mInfoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    }

//    /**
//     * Schedules a call to hide() in delay milliseconds, canceling any
//     * previously scheduled calls.
//     */
//    private void delayedHide(int delayMillis) {
////        mCategoryHide.hide();
////        mPanelHide.hide();
////        mInfoHide.hide();
//    }

    @Override
    protected void onDestroy() {
        mPlayer.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onConnected(PlaybackService service) {
        mPlayer.setVideoLayout((VLCVideoLayout)mVideoView);

        if (config.playingChannal == null) {
            config.playingChannal = config.getFirstCanPlayChannel();
        }
        if (config.playingChannal != null) {
            mPlayer.play(config.playingChannal.source.get(0));
        }

//        mPlayer.play("http://101.71.255.229:6610/zjhs/2/10105/index.m3u8?virtualDomain=zjhs.live_hls.zte.com&IASHttpSessionId=OTT3833320200222145900056637");
//        mPlayer.play("http://live.hcs.cmvideo.cn:8088/migu/kailu/cctv1hd265/57/20191230/index.m3u8?&encrypt=");

//        try {
//            String ret =  this.config.post(this.config.host+"/data.php","{data:{myiptv:1}}");
//            Log.d(TAG, "initCategory: "+ret);
//        } catch (IOException e) {
//            Log.d(TAG, "initCategory: Error!");
//            e.printStackTrace();
//        }

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onBuffering(float percent) {
//        showInfo("buffer: "+ percent + "%");
    }
}

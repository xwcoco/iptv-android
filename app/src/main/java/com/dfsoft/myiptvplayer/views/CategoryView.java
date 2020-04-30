package com.dfsoft.myiptvplayer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dfsoft.myiptvplayer.IPTVCategory;
import com.dfsoft.myiptvplayer.IPTVChannel;
import com.dfsoft.myiptvplayer.IPTVConfig;
import com.dfsoft.myiptvplayer.R;

public class CategoryView extends FrameLayout {

    private final String TAG = "CategoryView";

    private Boolean mVisible = false;

    private CategoryAdapter mCategoryAdapter = null;

    public IPTVConfig config = IPTVConfig.getInstance();

    private ListView mCateList = null;

    private ListView mChannelList = null;

    private ListView mEpgList = null;

    private Context mContext;

    public CategoryView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.mContext = context;

        LayoutInflater.from(context).inflate(R.layout.layout_category, this);

        mCateList = findViewById(R.id.categorylistView);

        mChannelList = findViewById(R.id.category_channel_list);

        mEpgList = findViewById(R.id.category_epg_list);

        mCategoryAdapter = new CategoryAdapter(context,this.config.category);

        mCateList.setAdapter(mCategoryAdapter);
        mCateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activeCategory(position);
            }
        });
        mCateList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                activeCategory(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mChannelList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activeChannel(position);
            }
        });

        mChannelList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                activeChannel(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void activeCategory(int index) {
        mCategoryAdapter.setCurrentItem(index);
        IPTVCategory cate = this.config.category.get(index);
        if (cate.channelAdapter == null) {
            cate.channelAdapter = new ChannelAdapter(this.mContext,this.config.category,index);
        }
        mChannelList.setAdapter(cate.channelAdapter);
        mCategoryAdapter.notifyDataSetChanged();

    }

    public void activeChannel(int index) {
        ChannelAdapter adapter = (ChannelAdapter) mChannelList.getAdapter();

        adapter.setCurrentItem(index);

        IPTVChannel channel = adapter.getChannel();
        if (channel != null) {
            if (channel.epg.isEmpty()) {
                channel.loadEPGData();
            }
            if (channel.epgAdapter == null) {
                channel.epgAdapter = new EPGAdapter(this.mContext, channel);
            }
            channel.epgAdapter.getCurrentTimer();
//            mEpgList.smoothScrollToPosition(channel.epgAdapter);
        }

        adapter.notifyDataSetChanged();
        int curtime = channel.epgAdapter.getCurTime();
        mEpgList.setAdapter(channel.epgAdapter);
        if (curtime != -1) {
            int h = mEpgList.getMeasuredHeight() / 2;
            mEpgList.setSelectionFromTop(curtime,h);
//            mEpgList.setSelection(curtime);
//            mEpgList.smoothScrollToPositionFromTop(curtime,2);
//            mEpgList.smoothScrollToPosition(curtime);
        }
//        int cateindex = mCategoryAdapter.getCurrentItem();
//        mChannelList.getAdapter()
    }

    public void show() {
        this.setVisibility(View.VISIBLE);
        this.mVisible = true;
    }

    public void hide() {
        this.setVisibility(View.GONE);
        this.mVisible = false;
    }

    public void toggle() {
        if (this.mVisible) {
            this.hide();
        } else
            this.show();
    }
}

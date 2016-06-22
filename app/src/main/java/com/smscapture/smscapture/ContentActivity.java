package com.smscapture.smscapture;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.smscapture.smscapture.adapter.ContentAdapter;
import com.smscapture.smscapture.entity.MMSInfo;


/**
 * Created by ${dennis} on 6/22/16.
 */
public class ContentActivity extends Activity {
    private RecyclerView rcContentView;
    private ContentAdapter contentAdapter;
    private LinearLayoutManager mlinearLayoutManager;
    private MMSInfo mmsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        rcContentView = (RecyclerView) findViewById(R.id.rc_content_view);
        mmsInfo = MainActivity.mmsInfoCLick;
        contentAdapter = new ContentAdapter(this,mmsInfo.getContent());
        mlinearLayoutManager = new LinearLayoutManager(this);
        mlinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcContentView.setAdapter(contentAdapter);
        rcContentView.setLayoutManager(mlinearLayoutManager);


    }
}

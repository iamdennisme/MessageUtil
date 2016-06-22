package com.smscapture.smscapture;

import android.Manifest;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.smscapture.smscapture.Util.CheckPermissionsUtil;
import com.smscapture.smscapture.Util.MessageUtil;
import com.smscapture.smscapture.Util.RequestPermissionsUtil;
import com.smscapture.smscapture.Util.ToastUtil;
import com.smscapture.smscapture.adapter.MMSAdapter;
import com.smscapture.smscapture.entity.MMSInfo;
import com.smscapture.smscapture.entity.MessageInfo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final public static int REQUEST_CODE = 1606;


    private RecyclerView rcSmsList;
    private LinearLayoutManager mManager;
    public static MMSInfo mmsInfoCLick = null;
    private MessageUtil messageUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        messageUtil = MessageUtil.getInstance(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermission();
        } else {
            getSmsInfo();
        }

    }

    private void getSmsInfo() {
        List<MessageInfo> allMessage = messageUtil.getAllMessage();
        List<MessageInfo> messageIds = messageUtil.getAllMms(allMessage);
        List<MMSInfo> mmsInfos = messageUtil.getMMSInfos(messageIds);
        initData(mmsInfos);
    }

    private void initData(List<MMSInfo> mmsInfos) {
        MMSAdapter mmsAdapter = new MMSAdapter(this, mmsInfos);
        rcSmsList.setAdapter(mmsAdapter);
    }

    private void initView() {
        mManager = new LinearLayoutManager(this);
        mManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcSmsList = (RecyclerView) findViewById(R.id.rc_sms_list);
        rcSmsList.setLayoutManager(mManager);
    }

    private void getPermission() {
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.READ_SMS);
        List<String> unGranted = CheckPermissionsUtil.getInstance(this).checkPermissions(permissions);
        if (unGranted.size() != 0) {
            RequestPermissionsUtil.getInstance(this).requestPermissions(unGranted, REQUEST_CODE);
        } else {
            getSmsInfo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                List<String> unGranted = RequestPermissionsUtil.getInstance(this).checkRequest(permissions, grantResults);
                if (unGranted.size() == 0) {
                    getSmsInfo();
                } else {
                    ToastUtil.show(this, "获取短信权限失败,请设置");
                }
                break;
            }
        }
    }
}

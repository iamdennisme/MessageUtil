package com.smscapture.smscapture.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smscapture.smscapture.ContentActivity;
import com.smscapture.smscapture.MainActivity;
import com.smscapture.smscapture.R;
import com.smscapture.smscapture.entity.MMSContentInfo;
import com.smscapture.smscapture.entity.MMSInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${dennis} on 6/21/16.
 */
public class MMSAdapter extends RecyclerView.Adapter {
    private List<MMSInfo> SMSInfoList = new ArrayList<>();
    private Context mContext;

    public MMSAdapter(Context context, List<MMSInfo> SMSInfoList) {
        this.SMSInfoList = SMSInfoList;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new SMSHolder(layoutInflater.inflate(R.layout.item_sms, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        SMSHolder smsHolder = (SMSHolder) holder;
        final MMSInfo smsInfo;
        if (SMSInfoList == null) {
            return;
        }
        smsInfo = SMSInfoList.get(position);
        if (smsInfo == null) {
            return;
        }
        if (smsInfo.getAddress() != null) {
            smsHolder.tvTitle.setText(smsInfo.getAddress());
        }
        if (smsInfo.getDate() != null) {
            smsHolder.tvData.setText(smsInfo.getDate());
        }
        if (smsInfo.getContent() != null) {
            List<MMSContentInfo> list = smsInfo.getContent();
            List<String> list1 = new ArrayList<>();
            for (MMSContentInfo contentInfo : list) {
                if (contentInfo.getType() == 1) {
                    String a = contentInfo.getMessage();
                    list1.add(a);
                }
            }
            if (list1 != null && list1.get(0) != null) {
                smsHolder.tvContent.setText(list1.get(0));
            }
        }
        smsHolder.llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mmsInfoCLick = smsInfo;
                Intent intent = new Intent(mContext, ContentActivity.class);
                mContext.startActivity(intent);
            }
        });
        smsHolder.llContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDialog(smsInfo.getId());
                return view.isLongClickable();
            }
        });


    }

    private void showDialog(final long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("确认删除吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                deleteMessage(id);
            }
        });
        builder.setNegativeButton("不要", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void deleteMessage(long id) {
      mContext.getContentResolver().delete(Uri.parse("content://mms-sms/conversations/"+id), null, null);
    }

    @Override
    public int getItemCount() {
        return SMSInfoList.size();
    }

    public class SMSHolder extends RecyclerView.ViewHolder {
        private LinearLayout llContent;
        private TextView tvTitle;
        private TextView tvData;
        private TextView tvContent;

        public SMSHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvData = (TextView) itemView.findViewById(R.id.tv_data);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            llContent = (LinearLayout) itemView.findViewById(R.id.ll_content);
        }
    }
}

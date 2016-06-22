package com.smscapture.smscapture.Util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.smscapture.smscapture.entity.MMSContentInfo;
import com.smscapture.smscapture.entity.MMSInfo;
import com.smscapture.smscapture.entity.MessageInfo;
import com.smscapture.smscapture.entity.SNSInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ${dennis} on 6/22/16.
 */
public class MessageUtil {
    private ContentResolver contentResolver;

    public MessageUtil(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public static MessageUtil getInstance(Context context) {
        return new MessageUtil(context.getContentResolver());
    }

    public List<SNSInfo> getAllSms() {
        List<SNSInfo> SNSInfos = new ArrayList<>();
        Uri uri = Uri.parse("content://sms/");
        Cursor cursor = contentResolver.query(uri, null, null, null, "date desc");
        int address_index = cursor.getColumnIndex("address");
        int date_index = cursor.getColumnIndex("date");
        int body_index = cursor.getColumnIndex("body");
        if (cursor.moveToNext()) {
            String[] arr = cursor.getColumnNames();
            String number = cursor.getString(address_index);
            long date = cursor.getLong(date_index);
            String body = cursor.getString(body_index);
            SNSInfo snsInfo = new SNSInfo();
            snsInfo.setBody(body);
            snsInfo.setNumber(number);
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date_ss = formatter.format(new Date(date));
            snsInfo.setDate(date_ss);
            SNSInfos.add(snsInfo);
        }
        return SNSInfos;
    }

    public List<MessageInfo> getAllMessage() {
        List<MessageInfo> snsids = new ArrayList<>();
        Uri THREADS_URI = Uri.parse("content://mms-sms/conversations?simple=true");
        Cursor cursor = contentResolver.query(THREADS_URI, null, null, null, null);
        int id = cursor.getColumnIndex("_id");
        int recipientIds = cursor.getColumnIndex("recipient_ids");
        while (cursor.moveToNext()) {
            long idn = cursor.getLong(id);
            String recipientIds1 = cursor.getString(recipientIds);
            MessageInfo snsid = new MessageInfo();
            snsid.setId(idn);
            snsid.setRecipientIds(recipientIds1);
            snsids.add(snsid);
        }
        if (cursor != null) {
            cursor.close();
        }
        return snsids;
    }

    public List<MessageInfo> getAllMms(List<MessageInfo> allMessage) {
        List<MessageInfo> messageIds = new ArrayList<>();
        for (MessageInfo messageId : allMessage) {
            long conId = messageId.getId();
            Uri uri = Uri.parse("content://mms-sms/conversations/" + String.valueOf(conId));
            String[] projection = {"ct_t", "_id", "address", "body", "date", "date_sent", "error_code", "read", "type", "msg_box"};
            Cursor cursor = contentResolver.query(uri, projection, null, null, null);
            while (cursor.moveToNext()) {
                String ct_t = cursor.getString(cursor.getColumnIndex("ct_t"));
                if ("application/vnd.wap.multipart.related".equals(ct_t)) {
                    long id = cursor.getLong(cursor.getColumnIndex("_id"));
                    long date = cursor.getLong(cursor.getColumnIndex("date"));
                    MessageInfo messageId1 = new MessageInfo();
                    messageId1.setId(id);
                    messageId1.setRecipientIds(messageId.getRecipientIds());
                    messageId1.setDate(date * 1000);
                    messageIds.add(messageId1);
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return messageIds;
    }

    private List<MMSContentInfo> getContent(long id) {
        List<MMSContentInfo> list = new ArrayList<>();
        String selectionPart = "mid=" + id;
        Uri uri = Uri.parse("content://mms/part");
        Cursor cursor = contentResolver.query(uri, null,
                selectionPart, null, null);
        if (cursor.moveToFirst()) {
            do {
                String partId = cursor.getString(cursor.getColumnIndex("_id"));
                String type = cursor.getString(cursor.getColumnIndex("ct"));
                if ("text/plain".equals(type)) {
                    String data = cursor.getString(cursor.getColumnIndex("_data"));
                    String body;
                    if (data != null) {
                        // implementation of this method below
                        body = getMmsText(partId);
                    } else {
                        body = cursor.getString(cursor.getColumnIndex("text"));
                    }
                    MMSContentInfo contentInfo = new MMSContentInfo(null, 1, body);
                    list.add(contentInfo);
                }
                if ("image/jpeg".equals(type) || "image/bmp".equals(type) ||
                        "image/gif".equals(type) || "image/jpg".equals(type) ||
                        "image/png".equals(type)) {
                    Bitmap bitmap = getMmsImage(partId);
                    MMSContentInfo contentInfo = new MMSContentInfo(bitmap, 0, null);
                    list.add(contentInfo);
                }
            } while (cursor.moveToNext());

        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    public List<MMSInfo> getMMSInfos(List<MessageInfo> messageIds) {
        List<MMSInfo> mmsInfos = new ArrayList<>();
        for (MessageInfo messageId : messageIds) {
            String address = getAddress(messageId.getRecipientIds());
            List<MMSContentInfo> list = getContent(messageId.getId());
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = formatter.format(new Date(messageId.getDate()));
            MMSInfo mmsInfo = new MMSInfo();
            mmsInfo.setAddress(address);
            mmsInfo.setContent(list);
            mmsInfo.setDate(date);
            mmsInfo.setId(messageId.getId());
            mmsInfos.add(mmsInfo);
        }
        return mmsInfos;
    }

    private String getAddress(String recipientIds) {
        String adress = null;
        Uri uri = Uri.parse("content://mms-sms/canonical-addresses/");
        Cursor cursor = contentResolver.query(uri, null, "_id = " + recipientIds, null, null);

        if (cursor.moveToNext()) {
            adress = cursor.getString(cursor.getColumnIndex("address"));
        }
        cursor.close();
        return adress;
    }

    private Bitmap getMmsImage(String _id) {
        Uri partURI = Uri.parse("content://mms/part/" + _id);
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = contentResolver.openInputStream(partURI);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return bitmap;
    }

    private String getMmsText(String id) {
        Uri partURI = Uri.parse("content://mms/part/" + id);
        InputStream is = null;
        StringBuilder sb = new StringBuilder();
        try {
            is = contentResolver.openInputStream(partURI);
            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader reader = new BufferedReader(isr);
                String temp = reader.readLine();
                while (temp != null) {
                    sb.append(temp);
                    temp = reader.readLine();
                }
            }
        } catch (IOException e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return sb.toString();
    }


}

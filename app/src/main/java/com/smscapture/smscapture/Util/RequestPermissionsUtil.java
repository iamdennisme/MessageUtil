package com.smscapture.smscapture.Util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${dennis} on 5/31/16.
 */
public class RequestPermissionsUtil {
    private Activity mActivity;

    public RequestPermissionsUtil(Activity activity) {
        this.mActivity = activity;
    }

    public static RequestPermissionsUtil getInstance(Activity activity) {
        return new RequestPermissionsUtil(activity);
    }
    /*
    * request permissions
    * */
    public void requestPermissions(List<String> permissions, int requestCode) {

        String[] permissionsArr = permissions.toArray((new String[permissions.size()]));
        request(permissionsArr, requestCode);
    }
    /*
    * request permission
    * */
    public void requsetPermisssion(String permission, int requestCode) {
        String[] permissionsArr = new String[]{permission};
        request(permissionsArr, requestCode);
    }

    private void request(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(mActivity, permissions, requestCode);
    }

    /*
    * check permissions
    * Returns a List of unauthorized
    * */
    public List<String> checkRequest(String[] permissions, int[] grantResult) {
        List<String> unGranted = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (grantResult[i] != PackageManager.PERMISSION_GRANTED) {
                unGranted.add(permissions[i]);
            }
        }
        return unGranted;
    }
}

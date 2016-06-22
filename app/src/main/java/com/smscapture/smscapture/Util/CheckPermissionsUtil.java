package com.smscapture.smscapture.Util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${dennis} on 5/31/16.
 */
public class CheckPermissionsUtil {
    private final Context mContext;

    public CheckPermissionsUtil(Context context) {
        this.mContext = context;
    }

    public static CheckPermissionsUtil getInstance(Context context) {
        return new CheckPermissionsUtil(context);
    }

    /*
     * check permissions
     * Returns a List of unauthorized
     * */
    public List<String> checkPermissions(List<String> permissions) {
        List<String> unGranted = new ArrayList<>();
        for (String permission : permissions) {
            if (!checkPermission(permission)) {
                unGranted.add(permission);
            }
        }
        return unGranted;
    }

    /*
     * check single permission
     * */
    public boolean checkPermission(String permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return mContext.checkSelfPermission(permissions) == PackageManager.PERMISSION_GRANTED;
        } else {
            //if sdk <23,always return true,Do not carry out dynamic authorization
            return true;
        }
    }
}

package com.example.gurudevkwt.permissionscheck;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

/**
 * Created by guru.dev.KWT on 2017-02-22.
 */

public class Permissions {
    public static String[] PERMISSIONS_LIST = {
            "android.permission.CAMERA",
            "android.permission.RECORD_AUDIO",
            "android.permission.READ_PHONE_STATE",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
    };

    public static void checkPermissionsEnabled(final Context context) {
        final Activity activity = (Activity) context;
        Dialog dialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.permissions_negative))
                .setMessage(context.getString(R.string.permissions_text))
                .setCancelable(false)
                .setNegativeButton(context.getString(R.string.permissions_ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (Build.VERSION.SDK_INT >= 9) {
                                    activity.startActivityForResult(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.example.gurudevkwt.permissionscheck")), 1);
                                } else {
                                    activity.startActivityForResult(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS), 1);
                                }
                                Toast.makeText(context, context.getResources().getString(R.string.permissions), Toast.LENGTH_SHORT).show();
                                activity.finish();
                            }
                        })
                .setPositiveButton(context.getString(R.string.negative),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, context.getResources().getString(R.string.permissions_negative_text), Toast.LENGTH_SHORT).show();
                                activity.finish();
                            }
                        })
                .create();
        dialog.show();
    }
}

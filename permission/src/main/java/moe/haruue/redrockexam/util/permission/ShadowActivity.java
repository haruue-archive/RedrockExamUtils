package moe.haruue.redrockexam.util.permission;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * 用来获取权限的空白 Activity
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class ShadowActivity extends AppCompatActivity {

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            String[] permissions = getIntent().getStringArrayExtra("permissions");
            requestPermissions(permissions, 1532);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ArrayList<String> grantedPermissions = new ArrayList<>(0);
        ArrayList<String> deniedPermissions = new ArrayList<>(0);
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                grantedPermissions.add(permissions[i]);
            } else {
                deniedPermissions.add(permissions[i]);
            }
        }
        if (!grantedPermissions.isEmpty()) {
            RequestPermission.getInstance(this).listener.onPermissionGranted((String[]) grantedPermissions.toArray());
        }
        if (!deniedPermissions.isEmpty()) {
            RequestPermission.getInstance(this).listener.onPermissionDenied((String[]) deniedPermissions.toArray());
        }
        //在此重置权限获取工具
        RequestPermission.reset();
        finish();
    }
}

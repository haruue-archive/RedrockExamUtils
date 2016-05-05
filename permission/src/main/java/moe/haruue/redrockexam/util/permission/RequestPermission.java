package moe.haruue.redrockexam.util.permission;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.Arrays;

/**
 * 更方便的 Android M 运行时权限请求工具类<br>
 *     获取实例时需要传入当前 Activity 作为参数（Activity 中直接传入 this 即可），每个实例只能进行一次权限申请，完成之后会自动调用 {@link RequestPermission#reset()} 进行重置 ，每次申请可以申请多个权限
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class RequestPermission {

    private static RequestPermission instance;

    private Context context;
    RequestPermissionListener listener;

    private RequestPermission(Context context) {
        this.context = context;
    }

    /**
     * 获取此工具的一个实例以进行操作
     * @param context Activity 的 content ，在 Activity 中传入 this 即可
     * @return 本类的一个实例
     */
    public static RequestPermission getInstance(Context context) {
        if (instance == null) {
            instance = new RequestPermission(context);
        }
        return instance;
    }

    /**
     * 申请权限
     * @param listener 申请回调监听器
     * @param permissions 权限数组
     */
    public void requestPermission(RequestPermissionListener listener, String...permissions) {
        if (Build.VERSION.SDK_INT >= 23) {
            this.listener = listener;
            Intent intent = new Intent(context, ShadowActivity.class);
            intent.putExtra("permissions", permissions);
            context.startActivity(intent);
        } else {
            listener.onPermissionGranted(permissions);
            reset();
        }
    }

    /**
     * 权限失败时的回调监听器，仅用于 View 绑定时的回调
     */
    public interface OnPermissionDeniedListener {

        /**
         * 被用户拒绝的权限
         * @param permissions 被用户拒绝的权限数组
         */
        void onPermissionDenied(String[] permissions);
    }

    /**
     * 将一个权限申请绑定到 View 上，同时绑定 View 的监听器，如果能获取所需要的所有权限则执行监听器函数
     * @param view 需要绑定的 View
     * @param onClickListener 需要绑定的监听器，如果不需要绑定监听器请传入 null ，此时将不会对成功获取所有权限的情况进行任何处理
     * @param deniedListener 如果没有成功获取所有权限，将会调用此监听器
     * @param permissions 所需的权限数组
     */
    public void bindViewPermission(final View view, @Nullable final View.OnClickListener onClickListener, @Nullable final OnPermissionDeniedListener deniedListener, final String...permissions) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                requestPermission(new RequestPermissionListener() {

                    @Override
                    public void onPermissionGranted(String[] grantedPermissions) {
                        if (Arrays.equals(grantedPermissions, permissions)) {
                            if (onClickListener != null) {
                                onClickListener.onClick(v);
                            }
                        }
                    }

                    @Override
                    public void onPermissionDenied(String[] deniedPermissions) {
                        if (deniedListener != null) {
                            deniedListener.onPermissionDenied(permissions);
                        }
                    }
                }, permissions);
            }
        });

    }

    /**
     * 重置此工具，一般来说完成每次操作之后工具会被自动重置
     */
    public static void reset() {
        instance = null;
    }

}

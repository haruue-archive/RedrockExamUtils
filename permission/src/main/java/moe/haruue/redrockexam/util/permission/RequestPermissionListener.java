package moe.haruue.redrockexam.util.permission;

/**
 * 获取权限状态监听器<br>
 *     使用权限获取工具之后获取授权状态，使用用户授予的权限组为参数调用 {@link RequestPermissionListener#onPermissionGranted(String[])} ，使用用户拒绝的权限组为参数调用 {@link RequestPermissionListener#onPermissionDenied(String[])} <br>
 *     如果申请的是一个权限组，在用户部分授权的情况下，{@link RequestPermissionListener#onPermissionGranted(String[])} 和 {@link RequestPermissionListener#onPermissionDenied(String[])} 可能会同时调用
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public interface RequestPermissionListener {

    /**
     * 成功获取的权限
     * @param permissions 被用户授予的权限
     */
    void onPermissionGranted(String[] permissions);

    /**
     * 获取失败的权限
     * @param permissions 被用户拒绝的权限
     */
    void onPermissionDenied(String[] permissions);

}

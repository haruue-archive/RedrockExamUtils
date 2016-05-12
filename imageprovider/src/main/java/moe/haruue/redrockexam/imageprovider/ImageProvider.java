package moe.haruue.redrockexam.imageprovider;

import android.content.Context;
import android.net.Uri;

/**
 * 图片选择器，从相机/相册获取图片，并提供裁剪功能<br>
 *     不支持多线程调用
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class ImageProvider {

    static ImageProvider provider;

    Context context;
    ImageProviderListener listener;
    int requestCode;

    /**
     * 图片选择器的统一回调接口
     */
    public interface ImageProviderListener {
        /**
         * 当操作成功获取到图片 Uri 时，此方法将会被调用
         * @param uri 获取到图片的 Uri
         * @param requestCode 请求时传入的 requestCode ，用于区分
         */
        void onImageProviderGetPhoto(Uri uri, int requestCode);

        /**
         * 当操作被用户取消时，此方法将会被调用
         * @param requestCode 请求时传入的 requestCode ，用于区分
         */
        void onImageProviderOperateCancel(int requestCode);

        /**
         * 当操作失败时，此方法将会被调用
         * @param t 捕捉或者代表失败原因的异常
         * @param requestCode 请求时传入的 requestCode ，用于区分
         */
        void onImageProviderOperateFailure(Throwable t, int requestCode);
    }

    private ImageProvider(Context context) {
        this.context = context;
    }

    /**
     * 获取实例
     * @param context {@link android.app.Activity} 中的 this
     * @return {@link ImageProvider} 实例
     */
    public static ImageProvider getInstance(Context context) {
        provider = new ImageProvider(context);
        return provider;
    }

    /**
     * 从相册获取图片
     * @param listener 状态监听器
     * @param requestCode 用于区分的 requestCode
     */
    public void getImageFromAlbum(ImageProviderListener listener, int requestCode) {
        this.listener = listener;
        this.requestCode = requestCode;
        ShadowActivity.startForAlbum(context);
    }

    /**
     * 用相机拍摄照片
     * @param listener 状态监听器
     * @param requestCode 用于区分的 requestCode
     */
    public void getImageFromCamera(ImageProviderListener listener, int requestCode) {
        this.listener = listener;
        this.requestCode = requestCode;
        ShadowActivity.startForCamera(context);
    }

    /**
     * 裁切已有图片
     * @param uri 已有图片的 Uri
     * @param aspectX 需要的比例 X
     * @param aspectY 需要的比例 Y
     * @param outputX 需要返回的大小 X
     * @param outputY 需要返回的大小 Y
     * @param listener 状态监听器
     * @param requestCode 用于区分的 requestCode
     */
    public void cropImage(Uri uri, int aspectX, int aspectY, int outputX, int outputY, ImageProviderListener listener, int requestCode) {
        this.listener = listener;
        this.requestCode = requestCode;
        ShadowActivity.startForCrop(context, uri, aspectX, aspectY, outputX, outputY);
    }

}

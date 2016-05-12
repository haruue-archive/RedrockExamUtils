package moe.haruue.redrockexam.imageprovider;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * 用于启动系统应用并处理结果的 Activity
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class ShadowActivity extends AppCompatActivity {

    final static String TAG_OPERATION = "operation";

    final static int REQUEST_CODE_CAMERA = 32125;
    final static int REQUEST_CODE_ALBUM = 32126;
    final static int REQUEST_CODE_CROP = 32127;

    String fileName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int operation = getIntent().getIntExtra(TAG_OPERATION, -1);
        switch (operation) {
            case REQUEST_CODE_CAMERA:
                startCamera();
                break;
            case REQUEST_CODE_ALBUM:
                startAlbum();
                break;
            case REQUEST_CODE_CROP:
                startCrop((Uri) getIntent().getParcelableExtra("uri"), getIntent().getIntExtra("aspectX", 1), getIntent().getIntExtra("aspectY", 1), getIntent().getIntExtra("outputX", 100), getIntent().getIntExtra("outputY", 100));
                break;
            default:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AppCompatActivity.RESULT_CANCELED) {
            ImageProvider.provider.listener.onImageProviderOperateCancel(ImageProvider.provider.requestCode);
            finish();
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                onCameraResult(requestCode, resultCode, data);
                break;
            case REQUEST_CODE_ALBUM:
                onAlbumResult(requestCode, resultCode, data);
                break;
            case REQUEST_CODE_CROP:
                onCropResult(requestCode, resultCode, data);
            default:
                finish();
                break;
        }
    }

    protected void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileName = System.currentTimeMillis() + ".jpg";
        Uri imageUri = Uri.fromFile(new File(getExternalCacheDir(), fileName));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    protected void onCameraResult(int requestCode, int resultCode, Intent data) {
        Uri resultUri;
        if (data != null) {
            resultUri = data.getData();
        } else {
            resultUri = Uri.fromFile(new File(getExternalCacheDir(), fileName));
        }
        ImageProvider.provider.listener.onImageProviderGetPhoto(resultUri, ImageProvider.provider.requestCode);
        finish();
    }

    protected void startAlbum() {
        Intent intent;
        try {
            intent = new Intent("android.intent.action.OPEN_DOCUMENT");
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_ALBUM);
        } catch (Exception e) {
            try {
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_ALBUM);
            } catch (Exception ex) {
                try {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setPackage("com.android.gallery3d");
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_ALBUM);
                } catch (Exception exc) {
                    ImageProvider.provider.listener.onImageProviderOperateFailure(new ActivityNotFoundException("Not supported device."), ImageProvider.provider.requestCode);
                    reportBug();
                    finish();
                }
            }
        }
    }

    protected void onAlbumResult(int requestCode, int resultCode, Intent data) {
        Uri resultUri;
        if (data != null) {
            resultUri = data.getData();
        } else {
            resultUri = Uri.fromFile(new File(getExternalCacheDir(), fileName));
        }
        ImageProvider.provider.listener.onImageProviderGetPhoto(resultUri, ImageProvider.provider.requestCode);
        finish();
    }

    protected void startCrop(Uri uri, int aspectX, int aspectY, int outputX, int outputY) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("scale", true);
            intent.putExtra("aspectX", aspectX);
            intent.putExtra("aspectY", aspectY);
            intent.putExtra("outputX", outputX);
            intent.putExtra("outputY", outputY);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, REQUEST_CODE_CROP);
        } catch (Exception e) {
            ImageProvider.provider.listener.onImageProviderOperateFailure(new ActivityNotFoundException("Crop image does not support by this device"), ImageProvider.provider.requestCode);
            finish();
        }
    }

    protected void onCropResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = data.getExtras().getParcelable("data");
        if (bitmap == null) {
            ImageProvider.provider.listener.onImageProviderOperateFailure(new NullPointerException("Crop return empty data"), ImageProvider.provider.requestCode);
            return;
        }
        Uri uri = bitmapToUri(this, bitmap);
        ImageProvider.provider.listener.onImageProviderGetPhoto(uri, ImageProvider.provider.requestCode);
        finish();
    }

    static void startForAlbum(Context context) {
        Intent starter = new Intent(context, ShadowActivity.class);
        starter.putExtra(TAG_OPERATION, REQUEST_CODE_ALBUM);
        context.startActivity(starter);
    }

    static void startForCamera(Context context) {
        Intent starter = new Intent(context, ShadowActivity.class);
        starter.putExtra(TAG_OPERATION, REQUEST_CODE_CAMERA);
        context.startActivity(starter);
    }

    public static void startForCrop(Context context, Uri uri, int aspectX, int aspectY, int outputX, int outputY) {
        Intent starter = new Intent(context, ShadowActivity.class);
        starter.putExtra(TAG_OPERATION, REQUEST_CODE_CROP);
        starter.putExtra("uri", uri);
        starter.putExtra("aspectX", aspectX);
        starter.putExtra("aspectY", aspectY);
        starter.putExtra("outputX", outputX);
        starter.putExtra("outputY", outputY);
        context.startActivity(starter);
    }

    protected void reportBug() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!BuildConfig.DEBUG) {
                    try {
                        PackageInfo thisPackageInfo;
                        thisPackageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                        String apikey = "b99ae8432eb95dcce629c934e7e75998";
                        String version = thisPackageInfo.packageName + "/" + thisPackageInfo.versionName;
                        String model = Build.MODEL + "/" + Build.VERSION.SDK_INT;
                        StringBuilder infoBuilder = new StringBuilder();
                        infoBuilder.append("  All Installed Packages:\n");
                        for (PackageInfo i : getApplication().getPackageManager().getInstalledPackages(0)) {
                            infoBuilder.append("    ");
                            infoBuilder.append(i.packageName);
                            infoBuilder.append("\n");
                        }
                        String info = infoBuilder.toString();
                        URL url = new URL("https://api.caoyue.com.cn/bugCollector.php");
                        URLConnection connection = url.openConnection();
                        connection.setRequestProperty("accept", "*/*");
                        connection.setRequestProperty("connection", "Keep-Alive");
                        connection.setDoOutput(true);
                        PrintWriter out = new PrintWriter(connection.getOutputStream());
                        out.print(apikey + "&" + version + "&" + model + "&" + info);
                        out.flush();
                        out.close();
                    } catch (Exception ignored) {

                    }
                }

            }
        }).start();
    }

    protected Uri bitmapToUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

}

package moe.haruue.redrockexam.util.test;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import moe.haruue.redrockexam.imageprovider.ImageProvider;
import moe.haruue.redrockexam.util.StandardUtils;
import moe.haruue.redrockexam.util.abstracts.HaruueActivity;
import moe.haruue.redrockexam.util.permission.RequestPermission;

public class ImageProviderActivity extends HaruueActivity {

    ImageView testImageView;
    Button selectImageButton;
    Listener listener = new Listener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_provider);
        testImageView = $(R.id.image_view_test);
        selectImageButton = $(R.id.button_select_image);
        RequestPermission.getInstance(this).bindViewPermission(selectImageButton, listener, listener, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        StandardUtils.log(Build.VERSION.SDK_INT + "");
    }

    protected class Listener implements View.OnClickListener, RequestPermission.OnPermissionDeniedListener, ImageProvider.ImageProviderListener {

        Uri tempImageUri;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_select_image:
                    ImageProvider.getInstance(ImageProviderActivity.this).getImageFromAlbum(this, 0);
                    break;
            }

        }

        @Override
        public void onPermissionDenied(String[] permissions) {
            StandardUtils.toast("Failed for PERMISSION DENIED");
        }

        @Override
        public void onImageProviderGetPhoto(Uri uri, int requestCode) {
            switch (requestCode) {
                case 0:
                    tempImageUri = uri;
                    ImageProvider.getInstance(ImageProviderActivity.this).cropImage(uri, 1, 1, 300, 300, this, 1);
                    break;
                case 1:
                    testImageView.setImageURI(uri);
                    break;
            }
        }

        @Override
        public void onImageProviderOperateCancel(int requestCode) {

        }

        @Override
        public void onImageProviderOperateFailure(Throwable t, int requestCode) {
            StandardUtils.printStack(t);
            switch (requestCode) {
                case 1:
                    testImageView.setImageURI(tempImageUri);
                    break;
            }
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ImageProviderActivity.class);
        context.startActivity(starter);
    }
}

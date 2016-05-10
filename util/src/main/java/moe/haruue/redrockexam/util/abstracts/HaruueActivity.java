package moe.haruue.redrockexam.util.abstracts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import moe.haruue.redrockexam.util.ActivityManager;
import moe.haruue.redrockexam.util.InstanceSaver;
import moe.haruue.redrockexam.util.StandardUtils;
import moe.haruue.redrockexam.util.ThreadUtils;

/**
 * 继承这个 Activity，轻松获取 Utils 所需的一切
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public abstract class HaruueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.push(this);
        StandardUtils.initializeInActivity(this);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        InstanceSaver.saveInstance(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        InstanceSaver.saveInstance(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.pop(this);
        ThreadUtils.onActivityDestroy(this);
    }

}

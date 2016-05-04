package moe.haruue.redrockexam.util.abstracts;

import android.app.Application;

import moe.haruue.redrockexam.util.ActivityManager;
import moe.haruue.redrockexam.util.InstanceSaver;
import moe.haruue.redrockexam.util.StandardUtils;
import moe.haruue.redrockexam.util.ThreadUtils;

/**
 * 继承这个 Application ，轻松获取 Utils 所需的一切
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public abstract class HaruueApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        StandardUtils.initialize(this);
        ActivityManager.initialize();
        InstanceSaver.initialize();
        ThreadUtils.initialize(this);
    }

}

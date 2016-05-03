package moe.haruue.redrockexam.util;

import android.app.Application;
import android.os.Handler;

import java.util.Locale;

/**
 * 标准工具类 <br>
 *     这个类必须在{@link Application}的子类里初始化，使用 {@link StandardUtils#initialize(Application)}，参数直接传入 this 即可。
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class StandardUtils {

    private static StandardUtils utils;

    private Application application;
    private Handler handler;

    private StandardUtils() {

    }

    /**
     * 初始化标准工具类<br>
     *     必须在{@link Application}的子类里完成初始化操作，直接传入 this
     * @param application {@link Application}的子类中的 this 对象
     */
    public static void initialize(Application application) {
        utils = new StandardUtils();
        utils.application = application;
        utils.handler = new Handler(application.getMainLooper());
    }

    /**
     * 在任何地方获取应用的{@link Application}
     * @return 应用的 {@link Application}实例
     */
    public static Application getApplication() {
        return utils.application;
    }

    /**
     * 在新线程中运行
     * @param runnable 需要运行的 {@link Runnable}实例
     * @return 正在运行的 {@link Thread}实例
     */
    public static Thread runOnNewThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    /**
     * 在主线程（UI 线程）中运行
     * @param runnable 需要运行的 {@link Runnable}实例
     */
    public static void runOnUIThread(Runnable runnable) {
        utils.handler.post(runnable);
    }

    /**
     * 获取当前的时间戳
     * @return 当前时间戳
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取当前的语言国家代码，如 zh-CN，en-US 等等
     * @return
     */
    public static String getLanguageCountryCode() {
        Locale locale = Locale.getDefault();
        return locale.getLanguage() + "-" + locale.getCountry();
    }

    /**
     * 默认值函数，判断第一个参数是否为 null ，若为 null 则返回第二个参数
     * @param o 需要判断是否为 null 的对象
     * @param defaultObject 当第一个参数为 null 时返回的对象
     * @param <T> 类型通配泛型
     * @return 结果，相当于 {@code o == null ? defaultObject : o}
     */
    public static <T> T defaultObject(T o, T defaultObject) {
        return o == null ? defaultObject : o;
    }

}

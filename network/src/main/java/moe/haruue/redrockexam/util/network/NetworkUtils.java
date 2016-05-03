package moe.haruue.redrockexam.util.network;

import android.app.Application;

/**
 * 网络工具类<br>
 *     这个类必须在{@link Application}的子类里初始化，使用 {@link NetworkUtils#initialize(Application, NetworkConfiguration)}，第一个参数直接传入 this 即可。
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class NetworkUtils {

    private static NetworkUtils utils;

    private NetworkUtils() {

    }

    private Application application;
    private NetworkConfiguration defaultConfiguration;

    /**
     * 初始化网络工具<br>
     *     必须在{@link Application}的子类里完成初始化操作
     * @param application {@link Application} 子类中的 this 对象
     * @param defaultConfiguration 默认设置，如果保持默认则可以传入 new {@link NetworkConfiguration()}
     */
    public static void initialize(Application application, NetworkConfiguration defaultConfiguration) {
        utils = new NetworkUtils();
        utils.application = application;
        utils.defaultConfiguration = defaultConfiguration;
    }

}

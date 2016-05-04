package moe.haruue.redrockexam.util.network;

import java.util.HashMap;

/**
 * 网络配置，这个类类使用修饰者模式
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class NetworkConfiguration {

    private HashMap<Integer, String> configuration = new HashMap<>(0);

    public class Configurations {
        public final static int REQUEST_METHOD = 1;
        public final static int CONNECT_TIMEOUT = 2;
        public final static int READ_TIMEOUT = 3;
        public final static int WRITE_TIMEOUT = 4;
    }

    public class RequestMethods {
        public final static String GET = "GET";
        public final static String POST = "POST";
    }

    /**
     * 设置请求方式，默认为 GET
     * @param method {@link RequestMethods} 中的一个常量字段值
     * @return 修饰者模式的 this 引用
     */
    public NetworkConfiguration setRequestMethod(String method) {
        configuration.put(Configurations.REQUEST_METHOD, method);
        return this;
    }

    String getRequestMethod() {
        return configuration.get(Configurations.REQUEST_METHOD);
    }

    /**
     * 设置连接超时时间，默认为 30 秒
     * @param timeout 连接超时时间
     * @return 修饰者模式的 this 引用
     */
    public NetworkConfiguration setConnectTimeout(int timeout) {
        configuration.put(Configurations.CONNECT_TIMEOUT, timeout + "");
        return this;
    }

    int getConnectTimeout() {
        try {
            return Integer.parseInt(configuration.get(Configurations.CONNECT_TIMEOUT));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 设置读取超时时间，默认为 60 秒
     * @param timeout 读取超时时间
     * @return 修饰者模式的 this 引用
     */
    public NetworkConfiguration setReadTimeout(int timeout) {
        configuration.put(Configurations.READ_TIMEOUT, timeout + "");
        return this;
    }

    int getReadTimeout() {
        try {
            return Integer.parseInt(configuration.get(Configurations.READ_TIMEOUT));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取写入超时时间，默认为 60 秒
     * @param timeout 写入超时时间
     * @return 修饰者模式的 this 引用
     */
    public NetworkConfiguration setWriteTimeout(int timeout) {
        configuration.put(Configurations.WRITE_TIMEOUT, timeout + "");
        return this;
    }

    int getWriteTimeout() {
        try {
            return Integer.parseInt(configuration.get(Configurations.WRITE_TIMEOUT));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 默认值函数，判断第一个参数是否为 null ，若为 null 则返回第二个参数
     * @param o 需要判断是否为 null 的对象
     * @param defaultObject 当第一个参数为 null 时返回的对象
     * @param <T> 类型通配泛型
     * @return 结果，相当于 {@code o == null ? defaultObject : o}
     */
    static <T> T defaultObject(T o, T defaultObject) {
        return o == null ? defaultObject : o;
    }

}

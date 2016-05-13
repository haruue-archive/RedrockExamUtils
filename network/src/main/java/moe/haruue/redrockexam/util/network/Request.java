package moe.haruue.redrockexam.util.network;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * 网络请求<br>
 *     <b>请注意：</b>这里的方法都是<b>同步</b>方法，请勿放入主线程<br>
 *     <b>为什么是同步？</b>在实际设计过程中，我们发现对于 API 的操作，常常不仅仅是连接个网络就返回字符串那<br>
 *                    么简单，我们常常还需要对返回的字符串进行一系列处理，通常，这些处理也是耗时操作，因此<br>
 *                    我们喜欢使用 RxJava 把他们串联在一起。在此过程中，我们把网络请求和返回值处理与实际的<br>
 *                    UI 分开，这就是所谓的 {@link ApiHelper} ,由于在 {@link ApiHelper} 中已经不属于主线程，为了避免线程<br>
 *                    太多浪费资源，所以所有网络请求相关的方法都是同步的。
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class Request {

    String url;
    Map<String, ?> param;
    String method;
    NetworkConfiguration configuration;

    /**
     * 创建一个请求，使用默认设置并从默认设置里取得请求方法
     * @param url 请求 URL
     * @param param 请求参数
     */
    public Request(String url, Map<String, ?> param) {
        this.url = url;
        this.param = param;
        this.configuration = NetworkUtils.getInstance().defaultConfiguration;
        this.method = configuration.getRequestMethod();
    }

    /**
     * 创建一个请求，使用指定的设置并从设置里取得请求方法
     * @param url 请求 URL
     * @param param 请求参数
     * @param configuration
     */
    public Request(String url, Map<String, ?> param, NetworkConfiguration configuration) {
        this.url = url;
        this.param = param;
        this.configuration = configuration;
        this.method = configuration.getRequestMethod();
    }

    public Request(String url, Map<String, ?> param, String method) {
        this.url = url;
        this.param = param;
        this.method = method;
        configuration = NetworkUtils.getInstance().defaultConfiguration;
    }

    public Request(String url, Map<String, ?> param, String method, NetworkConfiguration configuration) {
        this.url = url;
        this.param = param;
        this.method = method;
        this.configuration = configuration;
    }

    /**
     * 对网络请求过程的变量的包装，从而完成对结果的处理<br>
     *     使用完之后请使用 {@link Streams#closeAll()} 关闭资源
     */
    public class Streams {
        public PrintWriter out;
        public InputStream in;
        public HttpURLConnection connection;

        /**
         * 获取请求返回的 HTTP 状态码
         * @return HTTP 状态码
         */
        public int getResponseCode() {
            try {
                return connection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }

        /**
         * 获取请求返回的请求头
         * @return 请求头
         */
        public Map<String, List<String>> getHeader() {
            return connection.getHeaderFields();
        }

        /**
         * 获取远程服务器返回的 ResponseMessage
         * @return ResponseMessage 字符串
         */
        public String getResponseMessage() {
            try {
                return connection.getResponseMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 从连接输入流中获取字符串
         * @return 获取的字符串
         */
        public String getString() {
            StringBuilder result = new StringBuilder();
            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        /**
         * 从连接输入流中获取 {@link Bitmap} 对象
         * @return {@link Bitmap} 实例
         */
        public Bitmap getBitmap() {
            return BitmapFactory.decodeStream(in);
        }

        /**
         * 关闭所有流和连接
         */
        public void closeAll() {
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建连接并获取流以便操作，基本上是一切网络请求的前半段。通过获取的 {@link Streams#in} 可以得到各种对象
     * @return 包含 io 流以及 {@link URLConnection} 实例的 {@link Streams} 对象
     * @throws NetworkErrorException 无网络连接
     */
    public Streams getConnectionStreams() throws NetworkErrorException {
        String paramString = paramMapToString(param);
        try {
            if (!isNetworkConnected(NetworkUtils.getInstance().application)) {
                throw new NetworkErrorException("Can't connect network");
            }
        } catch (Exception e) {
            if (e instanceof NetworkErrorException) {
                throw e;
            } else {
                e.printStackTrace();
            }
        }
        // param in url if GET
        if (method.equals(NetworkConfiguration.RequestMethods.GET)) {
            url += "?" + paramString;
        }
        Streams streams = new Streams();
        try {
            URL mURL = new URL(url);
            streams.connection = (HttpURLConnection) mURL.openConnection();
            streams.connection.setConnectTimeout(configuration.getConnectTimeout());
            streams.connection.setReadTimeout(configuration.getReadTimeout());
            streams.connection.setRequestProperty("accept", "*/*");
            streams.connection.setRequestProperty("connection", "Keep-Alive");
            streams.connection.setDoOutput(true);
            streams.connection.setDoInput(true);
            // param in body if post
            if (method.equals(NetworkConfiguration.RequestMethods.POST)) {
                streams.out = new PrintWriter(streams.connection.getOutputStream());
                streams.out.print(paramString);
                streams.out.flush();
            }
            streams.in = streams.connection.getInputStream();
            return streams;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断网络可用性
     * @param context Application Context
     * @return 网络是否可用
     */
    /*package*/ static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 将键值映射转换为 x-www-form-urlencoded 的形式
     * @param paramMap 要转换的键值映射
     * @return x-www-form-urlencoded 字符串
     */
    private String paramMapToString(Map<String, ?> paramMap) {
        if (paramMap == null || paramMap.isEmpty()) {
            return "";
        }
        boolean isFirst = true;
        StringBuilder sb = new StringBuilder();
        for (Object k: paramMap.keySet()) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append("&");
            }
            sb.append(Utils.urlEncode(k.toString())).append("=").append(Utils.urlEncode(paramMap.get(k).toString()));
        }
        return sb.toString();
    }

}

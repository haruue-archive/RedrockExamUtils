package moe.haruue.redrockexam.util.network;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class Utils {

    /**
     * 对字符串进行 URL 编码
     * @param s 需要进行 URL 编码的字符串
     * @return 需要的 URL 编码
     */
    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

}

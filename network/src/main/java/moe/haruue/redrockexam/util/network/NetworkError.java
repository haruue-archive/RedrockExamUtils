package moe.haruue.redrockexam.util.network;

/**
 * 错误表<br>
 *     开发时可能会用到的统一错误代码表，用于错误表达和效验<br>
 *     继承这个类，然后添加特定的错误代码，建议自定义的错误代码小于 -100 或大于 1000
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class NetworkError {

    // Standard Errors
    public final static int NETWORK_ERROR = -1;
    public final static int SERVER_ERROR = -2;
    public final static int DATA_ENCODE_EXCEPTION = -3;
    public final static int DATA_DECODE_EXCEPTION = -4;

    // HTTP Errors
    public final static int HTTP_200_SUCCESS = 200;
    public final static int HTTP_400_BAD_REQUEST = 400;
    public final static int HTTP_401_UNAUTHORIZED = 401;
    public final static int HTTP_402_PAYMENT_REQUIRED = 402;
    public final static int HTTP_403_FORBIDDEN = 403;
    public final static int HTTP_404_NOT_FOUND = 404;

}

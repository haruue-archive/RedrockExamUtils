package moe.haruue.redrockexam.util;

import org.junit.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        System.out.println(EncryptUtils.urlEncode("春上冰月"));
        System.out.println(EncryptUtils.urlDecode("%E6%98%A5%E4%B8%8A%E5%86%B0%E6%9C%88"));

        System.out.println(EncryptUtils.nativeToAscii("春上冰月"));
        System.out.println("\u6625\u4e0a\u51b0\u6708");
        System.out.println(EncryptUtils.asciiToNative("\\u6625\\u4e0a\\u51b0\\u6708"));

    }
}
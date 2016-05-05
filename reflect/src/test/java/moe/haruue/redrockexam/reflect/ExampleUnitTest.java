package moe.haruue.redrockexam.reflect;

import org.junit.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        TestClass test = InstanceCreater.getInstance(TestClass.class, (Object[]) null);
        String result;
        result = (String) PrivateMethodInvoker.invoke(test, "testMethod", (Object[]) null);
        System.out.println(result);
    }
}


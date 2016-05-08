package moe.haruue.redrockexam.util.humanunit;

import org.junit.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        System.out.println(ByteHumanFormatter.formatByteSize(3255555, ByteHumanFormatter.BIN, ByteHumanFormatter.DEC_UNIT));
    }
}
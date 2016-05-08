package moe.haruue.redrockexam.util.humanunit;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class ByteHumanFormatter {

    public final static String[] DEC_UNIT = {"B", "KB", "MB", "GB", "TB", "PB"};
    public final static String[] BIN_UNIT = {"B", "KiB", "MiB", "GiB", "TiB", "PiB"};

    public final static int BIN = 1024;
    public final static int DEC = 1000;

    /**
     * 人可读性处理 Byte 大小数据，保留到小数点后 1 位
     * @param sizeInByte 字节数
     * @param round 进位，类似于 Windows 和 Linux 的 {@link ByteHumanFormatter#BIN} 和 {@link ByteHumanFormatter#DEC}
     * @param unitStrings 表示单位的字符串数组，其中 unitStrings[i] 必须是 10^i 时的国际单位制前缀加上单位，如 unitStrings[0]="B", unitStrings[2]="MB", 自带的 {@link ByteHumanFormatter#DEC_UNIT} 显示类似于 "MB"， {@link ByteHumanFormatter#BIN_UNIT} 显示类似于 "MiB"
     * @return 结果，如 2.3MB
     */
    public static String formatByteSize(int sizeInByte, int round, String[] unitStrings) {
        int exp = (int) (Math.log(sizeInByte) / Math.log(round));
        if (exp < unitStrings.length) {
            return approx((double) sizeInByte / Math.pow(round, exp), 1) + unitStrings[exp];
        } else {
            return approx((double) sizeInByte / Math.pow(sizeInByte, unitStrings.length - 1), 1) + unitStrings[unitStrings.length - 1];
        }
    }

    /**
     * 对 num 保留小数位数到小数点后的 fix 位
     * @param num 需要处理的数
     * @param fix 需要保留的位数
     * @return 结果
     */
    public static double approx(double num, int fix) {
        return (int) (num * Math.pow(10, fix)) / Math.pow(10, fix);
    }

}

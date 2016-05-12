package moe.haruue.redrockexam.util.database;

/**
 * 运行 SQL 监听器
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public interface ExecSqlListener {

    /**
     * 当 SQL 运行成功时，此方法将会被调用
     */
    void onExecSqlSuccess();

    /**
     * 当 SQL 运行失败时，此方法将会被调用
     * @param t 捕捉到的异常
     */
    void onExecSqlError(Throwable t);

}

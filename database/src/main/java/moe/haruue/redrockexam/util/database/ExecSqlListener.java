package moe.haruue.redrockexam.util.database;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public interface ExecSqlListener {

    void onExecSqlSuccess();
    void onExecSqlError(Throwable t);

}

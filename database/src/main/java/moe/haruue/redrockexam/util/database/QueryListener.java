package moe.haruue.redrockexam.util.database;

import android.database.Cursor;

/**
 * 查询监听器
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public interface QueryListener {

    /**
     * 当查询返回结果时将会调用此方法
     * @param cursor 查询结果 {@link Cursor} 如果在此方法里没有关闭将会自动关闭
     */
    void onQueryResult(Cursor cursor);

    /**
     * 当查询产生异常时将会调用此方法
     * @param t 捕捉到的异常
     */
    void onQueryError(Throwable t);

}

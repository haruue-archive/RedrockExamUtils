package moe.haruue.redrockexam.util.database;

import android.database.Cursor;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public interface QueryListener {

    void onQueryResult(Cursor cursor);
    void onQueryError(Throwable t);

}

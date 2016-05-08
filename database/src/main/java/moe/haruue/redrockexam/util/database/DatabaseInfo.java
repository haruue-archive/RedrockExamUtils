package moe.haruue.redrockexam.util.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public interface DatabaseInfo {

    void onCreate(SQLiteDatabase db);
    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

}

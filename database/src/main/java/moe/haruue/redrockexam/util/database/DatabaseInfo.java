package moe.haruue.redrockexam.util.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * 创建数据表必须的方法
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public interface DatabaseInfo {

    /**
     * 创建数据库，运行初始化操作，如创建表的 SQL
     * @param db 数据库
     */
    void onCreate(SQLiteDatabase db);

    /**
     * 升级数据库，在不同的数据库版本间进行升级，如修改表结构的 SQL
     * @param db 数据库
     * @param oldVersion 旧版本
     * @param newVersion 新版本
     */
    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

}

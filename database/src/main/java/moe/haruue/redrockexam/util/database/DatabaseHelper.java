package moe.haruue.redrockexam.util.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 封装的 {@link SQLiteOpenHelper}
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
class DatabaseHelper extends SQLiteOpenHelper {

    private DatabaseInfo info;

    /**
     * 构造一个 {@link DatabaseHelper}
     * @param context 任意 {@link Context} 实例
     * @param name 数据库名
     * @param version 数据库版本
     * @param info {@link DatabaseInfo} 实例，为数据库升级与创建提供接口
     */
    public DatabaseHelper(Context context, String name, int version, DatabaseInfo info) {
        super(context, name, null, version);
        this.info = info;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        info.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        info.onUpgrade(db, oldVersion, newVersion);
    }
}

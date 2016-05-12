package moe.haruue.redrockexam.util.database;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;

import java.util.ArrayList;

/**
 * 更方便的数据库异步操作<br>
 *     此工具类必须在 {@link Application} 的子类中使用 {@link #initialize(Application)} 方法初始化
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class DatabaseUtils {
    static Application application;
    static Handler handler;

    private static ArrayList<DatabaseUtils> utils = new ArrayList<>(0);

    private String name;
    private int version;
    private DatabaseHelper helper;
    private SQLiteDatabase database;

    /**
     * 根据数据库名称获取一个数据库管理工具
     * @param databaseName 数据库名称
     * @return 数据库管理工具的 {@link DatabaseUtils} 实例
     */
    public static DatabaseUtils getDatabaseByName(String databaseName) {
        for (DatabaseUtils i: utils) {
            if (i.getName().equals(databaseName)) {
                return i;
            }
        }
        return null;
    }

    /**
     * 新建一个数据库管理工具，此方法建议在 {@link Application} 子类中进行，同一个数据库只需要创建一次，如果需要获取已经创建的数据库，请使用 {@link DatabaseUtils#getDatabaseByName(String)}
     * @param databaseName 数据库名称
     * @param version 数据库版本号
     * @param info 数据库创建和升级时所必须的 {@link android.database.sqlite.SQLiteOpenHelper#onCreate(SQLiteDatabase)} 和 {@link android.database.sqlite.SQLiteOpenHelper#onUpgrade(SQLiteDatabase, int, int)} 接口
     */
    public DatabaseUtils(String databaseName, int version, DatabaseInfo info) {
        this.name = databaseName;
        this.version = version;
        this.helper = new DatabaseHelper(application, databaseName, version, info);
        this.database = this.helper.getWritableDatabase();
        utils.add(this);
    }

    /**
     * 在 {@link Application} 子类中初始化此工具类
     * @param application {@link Application} 子类的 this 引用
     */
    public static void initialize(Application application) {
        DatabaseUtils.application = application;
        DatabaseUtils.handler = new Handler(application.getMainLooper());
    }

    /**
     * 获取数据库名
     * @return 数据库名
     */
    public String getName() {
        return name;
    }

    /**
     * 获取数据库版本号
     * @return 数据库版本号
     */
    public int getVersion() {
        return version;
    }

    /**
     * 获取 database 以进行其他操作，同步
     * @return {@link SQLiteDatabase} 实例
     */
    public SQLiteDatabase getDatabase() {
        return database;
    }

    /**
     * 异步 execSQL 操作
     * @param sql 需要执行的 SQL 语句数组，语句中不允许用 {@code ;}，使用数组表示多条语句
     * @param listener 回调方法接口
     */
    public void exec(final String[] sql, final ExecSqlListener listener) {
        runOnNewThread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (String singleSql: sql)
                    database.execSQL(singleSql);
                } catch (Exception e) {
                    try {
                        listener.onExecSqlError(e);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return;
                }
                runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            listener.onExecSqlSuccess();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    /**
     * 异步的 rawQuery 操作，如果忘记了 close Cursor 此方法会自动关闭它
     * @param sql 需要运行的 sql 语句，使用 ? 来表示各个变化参数，不允许用 ; 实现多语句
     * @param selectionArgs ? 指代的参数
     * @param listener 回调方法接口
     */
    public void query(final String sql, final String[] selectionArgs, final QueryListener listener) {
        runOnNewThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Cursor cursor = database.rawQuery(sql, selectionArgs);
                    runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                listener.onQueryResult(cursor);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (cursor != null && !cursor.isClosed()) {
                                    cursor.close();
                                }
                            }
                        }
                    });
                } catch (Throwable t) {
                    try {
                        listener.onQueryError(t);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //////////////////
    // Thread Utils //
    //////////////////

    /**
     * 在新线程中运行
     * @param runnable 需要运行的 {@link Runnable}实例
     * @return 正在运行的 {@link Thread}实例
     */
    static Thread runOnNewThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }


    /**
     * 在主线程（UI 线程）中运行
     * @param runnable 需要运行的 {@link Runnable}实例
     */
    static void runOnUIThread(Runnable runnable) {
        DatabaseUtils.handler.post(runnable);
    }


}

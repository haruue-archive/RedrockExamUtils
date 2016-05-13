package moe.haruue.redrockexam.util.test;

import android.database.sqlite.SQLiteDatabase;

import moe.haruue.redrockexam.util.StandardUtils;
import moe.haruue.redrockexam.util.abstracts.HaruueApplication;
import moe.haruue.redrockexam.util.database.DatabaseInfo;
import moe.haruue.redrockexam.util.database.DatabaseUtils;
import moe.haruue.redrockexam.util.file.FileUtils;
import moe.haruue.redrockexam.util.network.NetworkConfiguration;
import moe.haruue.redrockexam.util.network.NetworkUtils;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class App extends HaruueApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        // Set Debug
        StandardUtils.setDebug(BuildConfig.DEBUG);

        // Test database
        DatabaseUtils.initialize(this);
        new DatabaseUtils("testDataBase", 1, new DatabaseInfo() {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("CREATE TABLE test (" +
                        "id INT PRIMARY KEY NOT NULL," +
                        "username TEXT UNIQUE NOT NULL," +
                        "password TEXT NOT NULL," +
                        "realname TEXT)");
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        });

        // Test file
        FileUtils.initialize(this);

        // Test Network
        NetworkUtils.initialize(this, new NetworkConfiguration());
    }
}

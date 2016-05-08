package moe.haruue.redrockexam.util.file;

import android.app.Application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 文件管理工具类<br>
 *     在 {@link Application} 的子类中使用 {@link FileUtils#initialize(Application)} 初始化这个工具类
 *     <b>注意：</b>这个工具类里的都是同步方法，一般用于管理对象序列化存储的小文件。<br>
 *     随便用啊。考虑好耗时就行。几十字节的文件根本没影响。——朱大
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class FileUtils {

    public static String FILES_DIR;
    public static String CACHE_DIR;

    /**
     * 在 {@link Application} 初始化此工具类
     * @param application {@link Application} 子类的 this 引用
     */
    public static void initialize(Application application) {
        FILES_DIR = application.getFilesDir().getPath();
        CACHE_DIR = application.getCacheDir().getPath();
    }

    /**
     * 序列化存储对象到指定路径<br>
     *     当文件存在时，将会直接覆盖现有文件
     * @param serializable 需要序列化存储的 {@link Serializable} 实例
     * @param path 指定的路径
     */
    public static void writeSerializableToFile(Serializable serializable, String path) {
        ObjectOutputStream objectOut = null;
        FileOutputStream fileOut = null;
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fileOut = new FileOutputStream(file, false);
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(serializable);
            fileOut.getFD().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (objectOut != null) {
                try {
                    objectOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从指定的路径读取文件并反序列化成对象<br>
     *     当文件不存在或者反序列化出错时返回 null
     * @param path 指定的路径
     * @return 反序列化的对象
     */
    public static Object readSerializableFromFile(String path) {
        ObjectInputStream objectIn = null;
        FileInputStream fileIn = null;
        Object object = null;
        File file = new File(path);
        try {
            fileIn = new FileInputStream(file);
            objectIn = new ObjectInputStream(fileIn);
            object = objectIn.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (objectIn != null) {
                try {
                    objectIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileIn != null) {
                try {
                    fileIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return object;
    }

    /**
     * 序列化存储对对象到 cache 目录，此目录里的数据可以直接通过“清空缓存”清除掉，适合临时存储
     * @param serializable 需要序列化存储的 {@link Serializable} 实例
     * @param name 文件名
     */
    public static void writeSerializableToCacheDirectory(Serializable serializable, String name) {
        writeSerializableToFile(serializable, CACHE_DIR + name);
    }

    /**
     * 从 cache 目录反序列化一个对象
     * @param name 文件名
     * @return 反序列化的对象
     */
    public static Object readSerializableFromCacheDirectory(String name) {
        return readSerializableFromFile(CACHE_DIR + name);
    }

    /**
     * 序列化存储到 files 目录，只有“清空数据”才能擦除掉，适合持久化存储
     * @param serializable 需要序列化存储是 {@link Serializable} 实例
     * @param name 文件名
     */
    public static void writeSerializableToFilesDirectory(Serializable serializable, String name) {
        writeSerializableToFile(serializable, FILES_DIR + name);
    }

    /**
     * 从 files 目录反序列化一个对象
     * @param name 文件名
     * @return 反序列化的对象
     */
    public static Object readSerializableToFilesDirectory(String name) {
        return readSerializableFromFile(FILES_DIR + name);
    }

}

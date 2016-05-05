package moe.haruue.redrockexam.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 新建任何类的新实例
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class InstanceCreater {

    /**
     * 获取构造器
     * @param objectClass 反射目标的 {@link Class} 对象
     * @param paramClass 构造器参数的 {@link Class} 对象
     * @return 找到的构造器对象
     * @throws NoSuchMethodException 没有找到构造器
     */
    private static Constructor<?> getConstructor(Class<?> objectClass, Class...paramClass) throws NoSuchMethodException {
        return objectClass.getDeclaredConstructor(paramClass);
    }

    /**
     * 获取任何类的新实例，此方法不适用于参数中含有基本类型参数的情况<br>
     *     <b>注意：</b>此方法适用于参数中只含有<b>非基本类型</b>参数的情况，如果参数列表中含有基本类型（如 int），请使用{@link InstanceCreater#getInstance(Class, Class[], Object...)}，否则将会抛出{@link NoSuchMethodException}。
     * @param objectClass 反射目标的 {@link Class} 对象
     * @param param 构造器参数，无参数请传入 (Object[]) null
     * @param <T> 反射目标的类型
     * @return 获取到的新实例
     * @throws InvocationTargetException 执行构造方法时出错
     * @throws NoSuchMethodException 找不到对应的构造方法
     * @throws InstantiationException 无法被实例化（类对象表示一个抽象类、接口、数组类、基本类型、void）
     * @throws IllegalAccessException 非法访问异常，一般是安全管理器不允许此操作
     */
    public static <T> T getInstance(Class<T> objectClass, Object...param) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class[] paramClass;
        if (param != null) {
            paramClass = new Class[param.length];
            for (int i = 0; i < param.length; i++) {
                paramClass[i] = param.getClass();
            }
        } else {
            paramClass = null;
        }
        return getInstance(objectClass, paramClass, param);
    }

    /**
     * 获取任何类的新实例，此方法适用于所有情况
     * @param objectClass 反射目标的 {@link Class} 对象
     * @param paramClass 构造器参数的 {@link Class} 数组，基本类型请使用类似于{@link Integer#TYPE}的形式，无参数请传入 (Class[]) null
     * @param param 构造器参数，无参数请传入 (Object[]) null
     * @param <T> 反射目标的类型
     * @return 获取到的新实例
     * @throws InvocationTargetException 执行构造方法时出错
     * @throws NoSuchMethodException 找不到对应的构造方法
     * @throws InstantiationException 无法被实例化（类对象表示一个抽象类、接口、数组类、基本类型、void）
     * @throws IllegalAccessException 非法访问异常，一般是安全管理器不允许此操作
     */
    public static <T> T getInstance(Class<T> objectClass, Class[] paramClass, Object...param) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor constructor = getConstructor(objectClass, paramClass);
        constructor.setAccessible(true);
        return (T) constructor.newInstance(param);
    }

}

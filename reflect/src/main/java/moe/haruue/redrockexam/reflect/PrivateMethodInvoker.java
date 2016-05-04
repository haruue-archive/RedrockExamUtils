package moe.haruue.redrockexam.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 反射递归调用私有方法<br>
 *     正常情况下，反射不会反射父类的方法，此工具类进行递归调用，在子类方法找不到时逐级获取父类私有方法
 */
public class PrivateMethodInvoker {

    /**
     * 递归获取指定的方法
     * @param objectClass 指定类的{@link Class}
     * @param methodName 指定的方法名
     * @param paramClass 参数的{@link Class}数组
     * @return 获取到方法的 Method 对象
     * @throws NoSuchMethodException 没有找到该方法
     * @throws SecurityException 安全管理器不允许此操作
     */
    private static Method getMethod(Class objectClass, String methodName, Class... paramClass) throws NoSuchMethodException, SecurityException {
        Method method;
        try {
            method = objectClass.getDeclaredMethod(methodName, paramClass);
        } catch (NoSuchMethodException e) {
            if (null == objectClass.getSuperclass()) {
                throw new NoSuchMethodException();
            } else {
                method = getMethod(objectClass.getSuperclass(), methodName, paramClass);
            }
        }
        return method;
    }

    /**
     * 调用私有方法，此方法不适用于参数中含有基本类型参数的情况<br>
     *     <b>注意：</b>此方法适用于参数中只含有<b>非基本类型</b>参数的情况，如果参数列表中含有基本类型（如 int），请使用{@link PrivateMethodInvoker#invoke(Object, String, Class[], Object...)}，否则将会抛出{@link NoSuchMethodException}。
     * @param object 反射的目标对象
     * @param methodName 方法名
     * @param param 参数数组，无参数请传入 (Object[]) null
     * @return 调用成功之后的函数返回值
     * @throws NoSuchMethodException 没有这种方法
     * @throws InvocationTargetException 调用的方法中产生异常
     * @throws IllegalAccessException 非法访问权限，一般是安全管理器不允许此操作
     */
    public static Object invoke(Object object, String methodName, Object... param) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class[] paramClass = new Class[param.length];
        for (int i = 0; i < param.length; i++) {
            paramClass[i] = param[i].getClass();
        }
        return invoke(object, methodName, paramClass, param);
    }

    /**
     * 调用私有方法，此方法适用于所有情况
     * @param object 反射的目标对象
     * @param methodName 方法名
     * @param paramClass 参数的{@link Class}数组，基本类型请使用类似于{@link Integer#TYPE}的形式，无参数请传入 (Class[]) null
     * @param param 参数数组，无参数请传入 (Object[]) null
     * @return 调用成功之后的函数返回值
     * @throws NoSuchMethodException 没有这种方法
     * @throws InvocationTargetException 调用的方法中产生异常
     * @throws IllegalAccessException 非法访问权限，一般是安全管理器不允许此操作
     */
    public static Object invoke(Object object, String methodName, Class[] paramClass, Object... param) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = getMethod(object.getClass(), methodName, paramClass);
        method.setAccessible(true);
        return method.invoke(object, param);
    }
}

package com.myrpc.core.server.container;

import com.myrpc.core.common.bo.MethodHandler;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ////////////////////////////////////////////////////////////////////
 * //                          _ooOoo_                               //
 * //                         o8888888o                              //
 * //                         88" . "88                              //
 * //                         (| ^_^ |)                              //
 * //                         O\  =  /O                              //
 * //                      ____/`---'\____                           //
 * //                    .'  \\|     |//  `.                         //
 * //                   /  \\|||  :  |||//  \                        //
 * //                  /  _||||| -:- |||||-  \                       //
 * //                  |   | \\\  -  /// |   |                       //
 * //                  | \_|  ''\---/''  |   |                       //
 * //                  \  .-\__  `-`  ___/-. /                       //
 * //                ___`. .'  /--.--\  `. . ___                     //
 * //              ."" '<  `.___\_<|>_/___.'  >'"".                  //
 * //            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
 * //            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
 * //      ========`-.____`-.___\_____/___.-`____.-'========         //
 * //                           `=---='                              //
 * //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
 * //         佛祖保佑           永无BUG           永不修改           //
 * //          佛曰:                                                 //
 * //                 写字楼里写字间，写字间里程序员;                 //
 * //                 程序人员写程序，又拿程序换酒钱.                 //
 * //                 酒醒只在网上坐，酒醉还来网下眠;                 //
 * //                 酒醉酒醒日复日，网上网下年复年.                 //
 * //                 但愿老死电脑间，不愿鞠躬老板前;                 //
 * //                 奔驰宝马贵者趣，公交自行程序员.                 //
 * //                 别人笑我忒疯癫，我笑自己命太贱;                 //
 * //                 不见满街漂亮妹，哪个归得程序员?                 //
 * ////////////////////////////////////////////////////////////////////
 *
 * @创建时间: 2019/9/22 17:40
 * @author: linzhou
 * @描述: ServiceContainerManager
 */
public class ServiceContainerManager {

    private Map<String, MethodHandler> methodContainer;


    public static final ServiceContainerManager CONTAINER = new ServiceContainerManager();

    private ServiceContainerManager() {
        methodContainer = new ConcurrentHashMap<>();
    }

    /**
     * 将对象加入到方法容器中
     * @param object
     * @throws Exception
     */
    public void registered(Object object) throws Exception {
        Class clazz = object.getClass();
        Class<?>[] interfaces = clazz.getInterfaces();
        Set<Method> methodSet = new HashSet<>();
        for (Class<?> anInterface : interfaces) {
            Method[] methods = anInterface.getMethods();
            for (Method method : methods) {
                String methodName = method.getName();
                Class<?>[] parameterTypes = method.getParameterTypes();
                Method objectMethod = clazz.getMethod(methodName,parameterTypes);
                methodSet.add(objectMethod);
            }
        }

        methodSet.forEach(method -> {
            MethodHandler methodHandler = new MethodHandler(object,method);
            registered(methodHandler);
        });
    }

    /**
     * 向容器中加入方法对象
     *
     * @param methodHandler
     */
    public void registered(MethodHandler methodHandler) {
        String[] classNames = methodHandler.getClazzNames();
        String[] parameterClassNames = methodHandler.getParameterClassNames();
        Set<String> keys = getMethodHandlerKeys(classNames, methodHandler.getMethodName(), parameterClassNames);
        if (keys != null) {
            for (String key : keys) {
                if (StringUtils.isNotBlank(key) && methodHandler != null) {
                    methodContainer.put(key, methodHandler);
                }
            }
        }
    }

    public MethodHandler getMethodHander(String[] classNames,String methodName,String[] paramClassNames) {
        MethodHandler rlt;
        Set<String> keys = getMethodHandlerKeys(classNames, methodName, paramClassNames);
        for (String key : keys) {
            rlt = methodContainer.get(key);
            if (rlt != null) {
                return rlt;
            }
        }
        return null;
    }

    private Set<String> getMethodHandlerKeys(String[] classNames, String methodName, String[] paramClassNames) {
        Set<String> keys = new HashSet<>();
        for (String className : classNames) {
            keys.add(getMethodHanderKey(className,methodName,paramClassNames));
        }
        return keys;
    }


    private String getMethodHanderKey(String className,String methodName,String[] paramClassNames){
        StringBuilder keySb = new StringBuilder();
        keySb.append(className).append("&").append(methodName).append("&");
        for (String paramClassName : paramClassNames) {
            keySb.append(paramClassName).append("&");
        }
        return keySb.toString();
    }

}

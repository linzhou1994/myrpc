package com.myrpc.core.common.bo;

import com.myrpc.utils.ReflectionUtil;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

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
 * @创建时间: 2019/9/22 17:43
 * @author: linzhou
 * @描述: MethodHandler
 */
public class MethodHandler implements Serializable {
    private static final Logger log = Logger.getLogger(MethodHandler.class);

    /**
     * 方法所在对象
     */
    private Object targ;

    /**
     * 目标对象的类名和接口名称
     */
    private String[] clazzNames;
    /**
     * 方法名称
     */
    private Method method;

    private String methodName;

    private String[] parameterClassNames;

    public MethodHandler(Object targ, Method method) {
        if (targ == null || method == null) {
            throw new IllegalArgumentException("targ or method cannot is null!");
        }
        this.targ = targ;
        this.method = method;
        //获取targ的类名及所有实现接口的名称
        Class targClass = targ.getClass();
        Class[] interfaces = targClass.getInterfaces();
        clazzNames = new String[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            clazzNames[i] = interfaces[i].getName();
        }
        //获取方法的所有参数类型
        parameterClassNames = ReflectionUtil.getMethodParameterTypeNames(this.method);
        methodName = method.getName();
    }


    public String[] getParameterClassNames() {
        return parameterClassNames;
    }

    public String getMethodName() {
        return methodName;
    }

    /**
     * 执行方法
     *
     * @param args 参数
     * @return
     */
    public Object invoke(Object... args) {
        log.info(Arrays.toString(args));
        return ReflectionUtil.invokeMethod(targ, method, args);
    }

    public String[] getClazzNames() {
        return clazzNames;
    }
}

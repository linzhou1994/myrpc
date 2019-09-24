package com.myrpc.utils;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 　　　　　　　　┏┓　　　┏┓+ +
 * 　　　　　　　┏┛┻━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　　 ████━████ ┃+
 * 　　　　　　　┃　　　　　　　┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃ + +
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃ + + + +
 * 　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　　┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 * 佛曰:
 * 写字楼里写字间，写字间里程序员；
 * 程序人员写程序，又拿程序换酒钱。
 * 酒醒只在网上坐，酒醉还来网下眠；
 * 酒醉酒醒日复日，网上网下年复年。
 * 但愿老死电脑间，不愿鞠躬老板前；
 * 奔驰宝马贵者趣，公交自行程序员。
 * 别人笑我忒疯癫，我笑自己命太贱；
 * 不见满街漂亮妹，哪个归得程序员？
 * ---------------------------
 * 项目名： MyUtils
 * 包名：   com.my.utils.fileutils
 * 创建者:  linzhou
 * 创建时间:18/10/21
 * 描述:
 */
public class PropsUtil {
    private static final Logger LOGGER = Logger.getLogger(PropsUtil.class);

    /**
     * 加载属性文件
     */
    public static Properties loadProps(String fileName) {
        Properties props = null;
        InputStream is = null;
        try {
            is = ClassUtil.getClassLoader().getResourceAsStream(fileName);
            if (is == null) {
                throw new FileNotFoundException(fileName + " file is not found");
            }
            props = new Properties();
            props.load(is);
        } catch (IOException e) {
            LOGGER.error("load properties file failure", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("close input stream failure", e);
                }
            }
        }
        return props;
    }

    /**
     * 获取 String 类型的属性值（默认值为空字符串）
     */
    public static String getString(Properties props, String key) {
        return getString(props, key, "");
    }

    /**
     * 获取 String 类型的属性值（可指定默认值）
     */
    public static String getString(Properties props, String key, String defaultValue) {
        String value = defaultValue;
        if (props.containsKey(key)) {
            value = props.getProperty(key);
        }
        return value;
    }

    /**
     * 获取 int 类型的属性值（默认值为 0）
     */
    public static int getInt(Properties props, String key) {
        return getInt(props, key, 0);
    }

    /**
     * 获取 int 类型的属性值（可指定默认值）
     */
    public static int getInt(Properties props, String key, int defaultValue) {
        int value = defaultValue;
        if (props.containsKey(key)) {
            try {
                value = Integer.parseInt(props.getProperty(key));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return value;
    }

    /**
     * 获取 boolean 类型属性（默认值为 false）
     */
    public static boolean getBoolean(Properties props, String key) {
        return getBoolean(props, key, false);
    }

    /**
     * 获取 boolean 类型属性（可指定默认值）
     */
    public static boolean getBoolean(Properties props, String key, boolean defaultValue) {
        boolean value = defaultValue;
        if (props.containsKey(key)) {
            try {
                value = Boolean.parseBoolean(props.getProperty(key));
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return value;
    }
    /**
     * 获取 boolean 类型属性（可指定默认值）
     */
    public static Long getLong(Properties props, String key) {
        Long rlt = null;
        if (props.containsKey(key)) {
            try {
                String value = props.getProperty(key);
                rlt = Long.parseLong(value);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return rlt;
    }
}

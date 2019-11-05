package com.myrpc;

import com.myrpc.zk.context.ZkDefaultContext;


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
 * @创建时间: 2019/9/22 20:52
 * @author: linzhou
 * @描述: ClientStart
 */
public class ClientStart {
    public static void main(String[] args) throws Exception {

        ZkDefaultContext context = new ZkDefaultContext("myrpc2.properties");
        context.registered(new Test3Impl());
        context.init();
        Thread.sleep(2000L);

        Test test = context.getProxyObject(Test.class);
        Test2 test2 = context.getProxyObject(Test2.class);


        String testString =test.testString("test");
        String test2String =test2.testString();
        test.testVoid();
        System.out.println(testString);
        System.out.println(test2String);
        Test3 test3 = context.getProxyObject(Test3.class);
        System.out.println(test3.Test3());
        System.out.println(test3.Test3());
        System.out.println(test3.Test3());
        System.out.println(test3.Test3());
        System.out.println(test3.Test3());
        System.out.println(test3.Test3());

    }
}

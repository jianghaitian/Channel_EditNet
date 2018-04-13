package com.bonc.channel_edit.utils;

/**
 * Created by Administrator on 2017/12/11.
 */

public class UrlPool {
    /**授权接口 oauth*/
    public static String HOST_1 = "oauth/";
    /**配置接口 entity*/
    public static String HOST_2 = "entity/adaptor.do?json&IFCODE=";
    /**逻辑接口 logic*/
    public static String HOST_3 = "logic/adaptor.do?json&IFCODE=";
    /**
     * 获取授权码
     * response_type    表示操作的响应类型，返回验权码code
     * appId        应用在接口平台的注册id
     * secrityId     应用在平台注册的安全码
     * http://192.168.6.198/kepler/oauth/authorize.do?secrityId=client&appId=client&response_type=code
     */
    public static String GET_AKCODE = HOST_1 + "authorize.do?";
    /**
     * 获取ATOK
     * user_id：		用户登录帐号
     * password： 	用户密码（加密后的密码）
     * atCode：     返回结果格式类型，2为json格式
     * AKCODE：       app验权码
     * grant_type：  接口操作类型
     * http://172.16.63.100:8080/kepler2015/oauth/getAccessAppToken.do?atCode=2&user_id=015659&grant_type=authorization_code&AKCODE=67d8f15d2f8af59c5684115b21f1f017&password=123456
     */
    //先md5再AES加密
    public static String GET_ATOK_SPECIAL = HOST_1 +"getAccessAppToken.do?";
    //普通状态下的url
    public static String GET_ATOK_NORMAL = HOST_1 + "accessToken.do?";
    /**
     *  有签名的获取atok接口
     *  http://172.16.63.183:8088/BAE/oauth/getAppToken.do?timestamp=1503538794243&nonce=3315&_data=qLhjxOi9KZPyqLVfFTXGpuZohN6pouiMB2RxjJ6p0KYjaO2B5wWE1SA+EbuLiFywbzr58/tqgc8MpzbHgjHUyP3p966MR6qDXq3ZZuKc8FZM9xf/7avx9W/4FPVxWphR4A9N6mtEmY5o0SjL5Yx8Hx7/INOWCOGnPw4sAstQECxf/sx4Ch3Yr84pEUGPsK2Qi07N/X17x3oAMujtrIybnAKWLNuX+UzmSyl7dvv2qeY+4p8JzAOlxvYlRnBpSTo6zmGIc0AkdDZS3wcSbsbEiQ==&signature=A0E9709FEE4B7B007BB0441809DFCD15
     */
    public static String GET_ATOK_SIGNATURE=HOST_1+"getAppToken.do?";
    /**
     * 获取我的菜单组信息
     * http://133.193.96.159:8800/qixin/logic/adaptor.do?json&IFCODE=getMyResourceGroupInfo&menu_group_id=1&ATOK=1660d8233fe2782bf6a79b39a2b88129
     */
    public static String GET_MYGROUP_INFO=HOST_3+"getMyResourceGroupInfo&";
    /**
     * 获取所有菜单信息
     * http://133.193.96.159:8800/qixin/logic/adaptor.do?json&IFCODE=getRoleCategoryResourceInfo&category_id=1&ATOK=64679246061548fbdd4aded2374e4e84&role_id=55&menu_group_id=
     */
    public static String GET_ALLMENU_INFO=HOST_3+"getMyResourceGroupInfo&";

}

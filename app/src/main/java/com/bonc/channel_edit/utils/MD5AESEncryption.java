package com.bonc.channel_edit.utils;

import android.text.TextUtils;

/**
 * Created by cuibg on 2017/5/9.
 * 先md5再AES
 */

public class MD5AESEncryption {
    public static String AESKEY = "nCbf67awoApYVlSB";
    public static String getMd5AESEncrption(String content){
        if(TextUtils.isEmpty(AES.getKey())){
            AES.setKey(AESKEY);
        }
        String md5ofStr = new MD5().getMD5ofStr(content);
        String MD5AESEncrption = AES.encode(md5ofStr);
        return MD5AESEncrption;
    }
}

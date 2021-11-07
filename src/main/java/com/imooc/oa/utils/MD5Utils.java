package com.imooc.oa.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Utils {
    public static String md5Digest(String source) {
        return DigestUtils.md5Hex(source);
    }


    /**
     * Md 5 digest string.
     *
     * @param source 源数据
     * @param salt   盐值
     * @return MD5摘要
     */
    public static String md5Digest(String source, Integer salt) {
        char[] ca = source.toCharArray();
        for (int i = 0; i < ca.length; i++) {
            ca[i] = (char) (ca[i] + salt);
        }
        String target = new String(ca);
        System.out.println(target);
        String md5 = DigestUtils.md5Hex(target);
        return md5;
    }

    public static void main(String[] args) {
        MD5Utils.md5Digest("test", 171);
    }
}

package com.baosight.message.util;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by yang on 2017/5/18.
 */
@Slf4j
public class SHA256Util {

    private static final String sha_algorithm = "SHA-256";

    public static String encode(String src){
        byte[] srcBytes = src.getBytes();
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(sha_algorithm);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(),e);
        }
        digest.update(srcBytes);
        byte[] destBytes = digest.digest();
        return bytes2String(destBytes);
    }

    private static String bytes2String(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for(byte b:bytes){
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

}

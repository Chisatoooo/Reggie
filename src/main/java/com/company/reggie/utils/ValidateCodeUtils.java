package com.company.reggie.utils;

import java.util.Random;

/**
 * @Author WYC
 * @Create 2022-12-10-下午 02:46
 *
 * 随机生成验证码工具类
 **/
public class ValidateCodeUtils {
    /**
     * 随机生成验证码
     * @param length 长度为4位或者6位
     * @return
     */
    public static Integer generateValidateCode(int length){
        Integer code = null;
        if(length == 4){
            code = new Random().nextInt(9999);//生成随机数，最大为9999
            if(code < 1000){
                code = code + 1000;//保证随机数为4位数字
            }
        }else if(length == 6){
            code = new Random().nextInt(999999);//生成随机数，最大为999999
            if(code < 100000){
                code = code + 100000;//保证随机数为6位数字
            }
        }else{
            throw new RuntimeException("只能生成4位或6位数字验证码");
        }
        return code;
    }

    /**
     * 随机生成指定长度字符串验证码
     * @param length 长度
     * @return
     */
    public static String generateValidateCode4String(int length){
        Random rdm = new Random();
        String hash1 = Integer.toHexString(rdm.nextInt());
        String capstr = hash1.substring(0, length);
        return capstr;
    }

    /**
     * 生成指定个数的随机验证码
     * @param length
     */
    public static String randomCode(int length){
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = randomNumber(0, 9);
            stringBuffer.append(number);
        }
        String code = stringBuffer.toString();
        return code;
    }

    /**
     * //产生随机数
     * @param min
     * @param max
     * @return
     */
    private static int randomNumber(int min, int max) {
        double number = Math.random();
        int result = min + (int)(number * (max - min));
        return result;
    }
}

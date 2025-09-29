package com.xingchuan.common.utils;

import java.util.Random;

/**
 * 随机数工具类
 */
public class RandomUtils {

    private static final Random random = new Random();

    // 生成指定范围内的随机整数 [min, max]
    public static int getRandomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    // 生成指定范围内的随机长整数 [min, max]
    public static long getRandomLong(long min, long max) {
        return (long) (random.nextDouble() * (max - min)) + min;
    }

    // 生成指定长度的随机大小写字母序列
    public static String getRandomCaseLetters(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char letter;
            if (random.nextBoolean()) {
                letter = (char) ('A' + random.nextInt(26)); // 大写字母 ASCII 范围：65-90
            } else {
                letter = (char) ('a' + random.nextInt(26)); // 小写字母 ASCII 范围：97-122
            }
            sb.append(letter);
        }
        return sb.toString();
    }

    // 生成随机浮点数 [0.0, 1.0)
    public static double getRandomDouble() {
        return random.nextDouble();
    }

    // 生成指定范围内的随机浮点数 [min, max)
    public static double getRandomDouble(double min, double max) {
        return random.nextDouble() * (max - min) + min;
    }

    // 生成指定长度的随机数字串
    public static String getRandomDigits(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // 数字范围：0-9
        }
        return sb.toString();
    }

    // 生成随机布尔值
    public static boolean getRandomBoolean() {
        return random.nextBoolean();
    }

    // 随机打乱数组
    public static void shuffleArray(Object[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            Object temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    public static String getAppUserAuthorizationCode(Long id) {
        int size = 32 - String.valueOf(id).length();
        if (size <= 0) {
            return String.valueOf(id);
        }
        return id + getRandomDigits(size);
    }
}

package com.lly835.bestpay.utils;

public class RandomUtil {

  private static final String RANDOM_STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

  private static final java.util.Random RANDOM = new java.util.Random();

  public static String getRandomStr() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 16; i++) {
      sb.append(RANDOM_STR.charAt(RANDOM.nextInt(RANDOM_STR.length())));
    }
    return sb.toString();
  }

}

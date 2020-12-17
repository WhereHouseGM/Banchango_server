package com.banchango.tools;

import java.util.Random;

public class PasswordGenerator {

    public static String generate() {
        StringBuffer passBuilder = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            int rIndex = random.nextInt(3);
            switch (rIndex) {
                case 0:
                    passBuilder.append((char) ((int) (random.nextInt(26)) + 97));
                    break;
                case 1:
                    passBuilder.append((char) ((int) (random.nextInt(26)) + 65));
                    break;
                case 2:
                    passBuilder.append(random.nextInt(10));
                    break;
            }
        }
        return passBuilder.toString().substring(0, 6);
    }
}

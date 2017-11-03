package com.hlb.haolaoban.activity;

/**
 * Created by heky on 2017/11/1.
 */

public class Test {

    public static void main(String[] args) {
        for (int i = 1; i <= 19; i++) {
            for (int j = 18; j > 19 - i; j--) {
                System.out.print(" ");
            }
            for (int k = 19; k >= i; k--) {
                System.out.print("* ");
            }
            System.out.println("");
        }
    }
}

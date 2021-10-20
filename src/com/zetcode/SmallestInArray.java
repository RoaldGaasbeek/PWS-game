package com.zetcode;

import java.util.ArrayList;

public class SmallestInArray {
    public static int getSmallest(ArrayList<Integer> a, int total) {
        int temp;
        for (int i = 0; i < total; i++) {
            for (int j = i + 1; j < total; j++) {
                if (a.get(i) > a.get(j)) {
                    temp = a.get(i);
                    a.set(i, a.get(j));
                    a.set(j, temp);
                }
            }
        }
        return a.get(0);
    }
}

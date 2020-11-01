package com.parallel;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws IOException {
        FileWriter myWriter = new FileWriter("pointed.txt");
        List<String> sizes = new ArrayList();
        List<Long> timePointedMM = new ArrayList();
        List<Long> timePointedMMParallelLine = new ArrayList();
        List<Long> timePointedMMParallelLiColumn new ArrayList();
        for (int size = 10; size < 1500; size += size >= 500 ? 100 : 20) {
            long startTime = System.currentTimeMillis();
            PointedMM.pointedMM(size, generateMatrix(size), generateMatrix(size));
            long endTime = System.currentTimeMillis();

            times.add(endTime - startTime);
        }
        for (int i = 0; i < sizes.size(); i++) {
            myWriter.write(sizes[i]);
        }
        myWriter.close();
    }

    private static int[] generateMatrix(int size) {
        int[] a = new int[size * size];
        for (int i = 0; i < size * size; i++) {
            a[i] = RANDOM.nextInt(201) - 100;
        }
        return a;
    }
}

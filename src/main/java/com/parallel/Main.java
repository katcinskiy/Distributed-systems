package com.parallel;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws IOException, InterruptedException {
        FileWriter sizeWriter = new FileWriter("size.txt");
        FileWriter timePointedMMWriter = new FileWriter("timePointedMM.txt");
        FileWriter timePointedMMParallelLineWriter = new FileWriter("timePointedMMParallelLine.txt");
        FileWriter timePointedMMParallelLiColumnWriter = new FileWriter("timePointedMMParallelColumn.txt");
        FileWriter timeBlockMMWriter = new FileWriter("timeBlockMM.txt");
        FileWriter timeBlockMMParallelLineWriter = new FileWriter("timeBlockMMParallelLine.txt");
        FileWriter timeBlockMMParallelColumnWriter = new FileWriter("timeBlockMMParallelColumn.txt");
        List<String> sizes = new ArrayList();
        List<Long> timePointedMM = new ArrayList();
        List<Long> timePointedMMParallelLine = new ArrayList();
        List<Long> timePointedMMParallelLiColumn = new ArrayList();
        List<Long> timeBlockMM = new ArrayList();
        List<Long> timeBlockMMParallelLine = new ArrayList();
        List<Long> timeBlockMMParallelColumn = new ArrayList();
        int[] blocksCount = new int[]{ 1, 2, 5, 10, 20, 30, 50, 100, 200, 500, 2000 };
        for (int size = 20; size <= 500; size += size >= 100 ? 100 : 20) {

            System.out.println(size);

            sizes.add(String.valueOf(size));

            int[] A = generateMatrix(size * size);
            int[] B = generateMatrix(size * size);

            long startTime = System.currentTimeMillis();
            PointedMM.pointedMM(size, A, B);
            long endTime = System.currentTimeMillis();
            timePointedMM.add(endTime - startTime);

            startTime = System.currentTimeMillis();
            PointedMM.pointedMMParalleledByLines(size, A, B, 5);
            endTime = System.currentTimeMillis();
            timePointedMMParallelLine.add(endTime - startTime);

            startTime = System.currentTimeMillis();
            PointedMM.pointedMMParalleledByColumns(size, A, B, 5);
            endTime = System.currentTimeMillis();
            timePointedMMParallelLiColumn.add(endTime - startTime);

            startTime = System.currentTimeMillis();
            BlockMM.blockMM(size, 2, A, B);
            endTime = System.currentTimeMillis();
            timeBlockMM.add(endTime - startTime);

            startTime = System.currentTimeMillis();
            BlockMM.blockMMParallelLines(size, 2, A, B, 5);
            endTime = System.currentTimeMillis();
            timeBlockMMParallelLine.add(endTime - startTime);

            startTime = System.currentTimeMillis();
            BlockMM.blockMMParallelColumns(size, 2, A, B, 5);
            endTime = System.currentTimeMillis();
            timeBlockMMParallelColumn.add(endTime - startTime);
        }
        for (int i = 0; i < sizes.size(); i++) {
            sizeWriter.write(sizes.get(i) + " ");
            timePointedMMWriter.write(String.valueOf(timePointedMM.get(i)) + " ");
            timePointedMMParallelLineWriter.write(String.valueOf(timePointedMMParallelLine.get(i)) + " ");
            timePointedMMParallelLiColumnWriter.write(String.valueOf(timePointedMMParallelLiColumn.get(i)) + " ");
            timeBlockMMWriter.write(String.valueOf(timeBlockMM.get(i)) + " ");
            timeBlockMMParallelLineWriter.write(String.valueOf(timeBlockMMParallelLine.get(i)) + " ");
            timeBlockMMParallelColumnWriter.write(String.valueOf(timeBlockMMParallelColumn.get(i)) + " ");
        }
        sizeWriter.close();
        timePointedMMWriter.close();
        timePointedMMParallelLineWriter.close();
        timePointedMMParallelLiColumnWriter.close();
        timeBlockMMWriter.close();
        timeBlockMMParallelLineWriter.close();
        timeBlockMMParallelColumnWriter.close();
    }

    private static int[] generateMatrix(int size) {
        int[] a = new int[size * size];
        for (int i = 0; i < size * size; i++) {
            a[i] = RANDOM.nextInt(201) - 100;
        }
        return a;
    }
}

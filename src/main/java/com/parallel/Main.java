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
        FileWriter ordinaryWriter = new FileWriter("ordinary.txt");
        FileWriter timePointedMMWriter = new FileWriter("timePointedMM.txt");
        FileWriter timePointedMMParallelLineWriter = new FileWriter("timePointedMMParallelLine.txt");
        FileWriter timePointedMMParallelLiColumnWriter = new FileWriter("timePointedMMParallelColumn.txt");
        FileWriter timeBlockMMWriter = new FileWriter("timeBlockMM.txt");
        FileWriter timeBlockMMParallelLineWriter = new FileWriter("timeBlockMMParallelLine.txt");
        FileWriter timeBlockMMParallelColumnWriter = new FileWriter("timeBlockMMParallelColumn.txt");
        List<String> sizes = new ArrayList();
        List<Long> timeOrdinary = new ArrayList();
        List<Long> timePointedMM = new ArrayList();
        List<Long> timePointedMMParallelLine = new ArrayList();
        List<Long> timePointedMMParallelLiColumn = new ArrayList();
        List<Long> timeBlockMM = new ArrayList();
        List<Long> timeBlockMMParallelLine = new ArrayList();
        List<Long> timeBlockMMParallelColumn = new ArrayList();

       // for (int size = 20; size <= 2000; size += size >= 100 ? 100 : 20) {
        for (int blocks = 4; blocks <= 500; blocks = blocks * 5) {
            int size = 1000;
            int[] A = new int[size * size];
            int[] B = new int[size * size];
            //int[][] A_ = new int[size][size];
            //int[][] B_ = new int[size][size];
            System.out.println(size);
            //int[][] expected = new int[size][size];
            sizes.add(String.valueOf(size));

          //  A_ = generateMatrixOr(A_);
          //  B_ = generateMatrixOr(B_);

            A = generateMatrix(A);
            B = generateMatrix(B);
            long startTime = System.currentTimeMillis();
            /*for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    for (int k = 0; k < size; k++) {
                        expected[i][j] += A_[i][k] * B_[k][j];
                    }
                }
            }*/
            long endTime = System.currentTimeMillis();
           // timeOrdinary.add(endTime - startTime);

            /*startTime = System.currentTimeMillis();
            PointedMM.pointedMM(size, A, B);
            endTime = System.currentTimeMillis();
            timePointedMM.add(endTime - startTime);

            startTime = System.currentTimeMillis();
            PointedMM.pointedMMParalleledByLines(size, A, B, 8);
            endTime = System.currentTimeMillis();
            timePointedMMParallelLine.add(endTime - startTime);

            startTime = System.currentTimeMillis();
            PointedMM.pointedMMParalleledByColumns(size, A, B, 8);
            endTime = System.currentTimeMillis();
            timePointedMMParallelLiColumn.add(endTime - startTime);*/

            startTime = System.currentTimeMillis();
            BlockMM.blockMM(size, blocks, A, B);
            endTime = System.currentTimeMillis();
            timeBlockMM.add(endTime - startTime);

            startTime = System.currentTimeMillis();
            BlockMM.blockMMParallelLines(size, blocks, A, B, 8);
            endTime = System.currentTimeMillis();
            timeBlockMMParallelLine.add(endTime - startTime);

            startTime = System.currentTimeMillis();
            BlockMM.blockMMParallelColumns(size, blocks, A, B, 8);
            endTime = System.currentTimeMillis();
            timeBlockMMParallelColumn.add(endTime - startTime);

            System.gc();
        }
        for (int i = 0; i < sizes.size(); i++) {
           // ordinaryWriter.write(timeOrdinary.get(i) + " ");
            sizeWriter.write(sizes.get(i) + " ");
            //timePointedMMWriter.write(String.valueOf(timePointedMM.get(i)) + " ");
            //timePointedMMParallelLineWriter.write(String.valueOf(timePointedMMParallelLine.get(i)) + " ");
            //timePointedMMParallelLiColumnWriter.write(String.valueOf(timePointedMMParallelLiColumn.get(i)) + " ");
            timeBlockMMWriter.write(String.valueOf(timeBlockMM.get(i)) + " ");
            timeBlockMMParallelLineWriter.write(String.valueOf(timeBlockMMParallelLine.get(i)) + " ");
            timeBlockMMParallelColumnWriter.write(String.valueOf(timeBlockMMParallelColumn.get(i)) + " ");
        }
        sizeWriter.close();
        //ordinaryWriter.close();
        //timePointedMMWriter.close();
        //timePointedMMParallelLineWriter.close();
        //timePointedMMParallelLiColumnWriter.close();
        timeBlockMMWriter.close();
        timeBlockMMParallelLineWriter.close();
        timeBlockMMParallelColumnWriter.close();
    }

    private static int[][] generateMatrixOr(int[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                a[i][j] = RANDOM.nextInt(201) - 100;
            }
        }
        return a;
    }

    private static int[] generateMatrix(int[] a) {
        for (int i = 0; i < a.length; i++) {
            a[i] = RANDOM.nextInt(201) - 100;
        }
        return a;
    }
}

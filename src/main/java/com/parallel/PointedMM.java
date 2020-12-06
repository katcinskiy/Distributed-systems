package com.parallel;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PointedMM {

    private static final Random RANDOM = new Random();

    public static int[] pointedMM(int size, int[] A_, int[] B_) {
        int[] A;
        int[] B;
        int[] C = new int[size * size];
        if (A_ != null) {
            A = A_;
            B = B_;
        } else {
            A = new int[size * size];
            B = new int[size * size];
            for (int i = 0; i < size * size; i++) {
                A[i] = RANDOM.nextInt(201) - 100;
                B[i] = RANDOM.nextInt(201) - 100;
            }
        }
        for (int i = 0; i < size * size; i++) {
            for (int k = 0; k < size; k++) {
                C[i] += A[k + (i / size) * size] * B[k * size + i % size];
            }
        }
        return C;
    }

    public static int[] pointedMMParalleledByLines(int size, int[] A_, int[] B_, int threadCount) throws InterruptedException {

        class Thread implements Runnable {

            private int size;
            private int[] A, B, C;
            private int startLineInclusive, endLineExclusive;

            public Thread(int size, int[] a, int[] b, int[] c, int startLineInclusive, int endLineExclusive) {
                this.size = size;
                A = a;
                B = b;
                C = c;
                this.startLineInclusive = startLineInclusive;
                this.endLineExclusive = endLineExclusive;
            }

            @Override
            public void run() {
                for (int i = startLineInclusive * size; i < endLineExclusive * size; i++) {
                    for (int k = 0; k < size; k++) {
                        C[i] += A[k + (i / size) * size] * B[k * size + i % size];
                    }
                }
            }
        }

        int[] A;
        int[] B;
        int[] C = new int[size * size];
        if (A_ != null) {
            A = A_;
            B = B_;
        } else {
            A = new int[size * size];
            B = new int[size * size];
            for (int i = 0; i < size * size; i++) {
                A[i] = RANDOM.nextInt(201) - 100;
                B[i] = RANDOM.nextInt(201) - 100;
            }
        }

        ExecutorService es = Executors.newCachedThreadPool();
        int linesPerThread = size / threadCount;
        for(int i = 0; i < threadCount - 1; i++) {
            es.execute(new Thread(size, A, B, C, i * linesPerThread, i * linesPerThread + linesPerThread));
        }
        es.execute(new Thread(size, A, B, C, (threadCount - 1) * linesPerThread, size));
        es.shutdown();
        es.awaitTermination(60, TimeUnit.MINUTES);

        return C;
    }

    public static int[] pointedMMParalleledByColumns(int size, int[] A_, int[] B_, int threadCount) throws InterruptedException {

        class Thread implements Runnable {

            private int size, line;
            private int[] A, B, C;
            private int startColumnInclusive, endColumnExclusive;

            public Thread(int size, int line, int[] a, int[] b, int[] c, int startColumnInclusive, int endColumnExclusive) {
                this.size = size;
                this.line = line;
                A = a;
                B = b;
                C = c;
                this.startColumnInclusive = startColumnInclusive;
                this.endColumnExclusive = endColumnExclusive;
            }

            @Override
            public void run() {
                for (int i = startColumnInclusive; i < endColumnExclusive; i++) {
                    for (int k = 0; k < size; k++) {
                        C[line * size + i] += A[k + line * size] * B[i + k * size];
                    }
                }
            }
        }

        int[] A;
        int[] B;
        int[] C = new int[size * size];
        if (A_ != null) {
            A = A_;
            B = B_;
        } else {
            A = new int[size * size];
            B = new int[size * size];
            for (int i = 0; i < size * size; i++) {
                A[i] = RANDOM.nextInt(201) - 100;
                B[i] = RANDOM.nextInt(201) - 100;
            }
        }

        int linesPerThread = size / threadCount;
        for (int i = 0; i < size; i++) {
            ExecutorService es = Executors.newCachedThreadPool();
            for(int j = 0; j < threadCount - 1; j++) {
                es.execute(new Thread(size, i, A, B, C, linesPerThread * j, linesPerThread * j + linesPerThread));
            }
            es.execute(new Thread(size, i, A, B, C, (threadCount - 1) * linesPerThread, size));
            es.shutdown();
            es.awaitTermination(60, TimeUnit.MINUTES);
        }

        return C;
    }
}

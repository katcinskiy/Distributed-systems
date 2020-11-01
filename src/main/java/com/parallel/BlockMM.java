package com.parallel;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BlockMM {

    private static final Random RANDOM = new Random();

    public static int[] blockMM(int size, int blockSize, int[] A_, int[] B_) {
        if (size % blockSize != 0) throw new IllegalArgumentException("size % blockSize != 0");
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
        int q = size / blockSize;

        for (int i = 0; i < q; i++) {
            for (int j = 0; j < q; j++) {
                for (int k = 0; k < q; k++) {
                    mul(A, B, C, size, blockSize, i, j, k);
                }
            }
        }

        return C;
    }

    public static int[] blockMMParallel(int size, int blockSize, int[] A_, int[] B_, int threadCount) throws InterruptedException {

        class Thread implements Runnable {

            private int size, blockSize, q, startQ, endQ;
            private int[] A, B, C;

            public Thread(int size, int blockSize, int q, int startQ, int endQ, int[] a, int[] b, int[] c) {
                this.size = size;
                this.blockSize = blockSize;
                this.q = q;
                this.startQ = startQ;
                this.endQ = endQ;
                A = a;
                B = b;
                C = c;
            }

            @Override
            public void run() {
                for (int i = startQ; i < endQ; i++) {
                    for (int j = 0; j < q; j++) {
                        for (int k = 0; k < q; k++) {
                            mul(A, B, C, size, blockSize, i, j, k);
                        }
                    }
                }
            }
        }

        if (size % blockSize != 0) throw new IllegalArgumentException("size % blockSize != 0");
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
        int q = size / blockSize;

        ExecutorService es = Executors.newCachedThreadPool();
        int blocksPerThread = q / threadCount;
        for(int i = 0; i < threadCount - 1; i++) {
            es.execute(new Thread(size, blockSize, q,  i * blocksPerThread, i * blocksPerThread + blocksPerThread, A, B, C));
        }
        es.execute(new Thread(size, blockSize, q,  (threadCount - 1) * blocksPerThread, (threadCount - 1) * blocksPerThread + blocksPerThread, A, B, C));
        es.shutdown();
        es.awaitTermination(60, TimeUnit.MINUTES);
//
//        for (int i = 0; i < q; i++) {
//            for (int j = 0; j < q; j++) {
//                for (int k = 0; k < q; k++) {
//                    mul(A, B, C, size, blockSize, i, j, k);
//                }
//            }
//        }

        return C;
    }

    public static void mul(int[] a, int[] b, int[] c, int size, int blockSize, int i_gl, int j_gl, int k_gl) {
        for (int i = 0; i < blockSize * blockSize; i++) {
            for (int k = 0; k < blockSize; k++) {
                c[i_gl * size * blockSize + j_gl * blockSize + (i / blockSize) * size + i % blockSize] += a[blockSize * k_gl + i_gl * size * blockSize + k + (i / blockSize) * size]
                        * b[blockSize * j_gl + k_gl * size * blockSize + k * size + i % blockSize];
            }
        }
    }
}
package com.parallel;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class BlockMMTest {

    private static final Random RANDOM = new Random();
    private static final int n = 800;
    static int[][] A = new int[n][n];
    static int[][] B = new int[n][n];
    static int[][] expected = new int[n][n];
    static int[] A_ = new int[n * n];
    static int[] B_ = new int[n * n];
    static int[] expected_ = new int[n * n];
    private final int threadCount = 3;

    static {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = RANDOM.nextInt(201) - 100;
                B[i][j] = RANDOM.nextInt(201) - 100;
            }
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    expected[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Brute force as matrices " + (endTime - startTime) + " milliseconds");
        for (int i = 0; i < n * n; i++) {
            A_[i] = A[i / n][i % n];
            B_[i] = B[i / n][i % n];
            expected_[i] = expected[i / n][i % n];
        }
    }

    @Test
    public void blockMMTest1() {
        int[] A = new int[] { 2, 3, 7, 8, 5, 1, 4, 9, 1, 2, 8, 7, 1, 2, 3, 4 };
        int[] B = new int[] { 7, 8, 9, 6, 5, 4, 3, 2, 1, 6, 8, 5, 1, 2, 4, 3 };
        int[] expected = new int[] { 44, 86, 115, 77, 53, 86, 116, 79, 32, 78, 107, 71, 24, 42, 55, 37 };

        int[] actual = BlockMM.blockMM(4, 2, A, B);

        assertEquals("Failed.", expected, actual);
    }

    @Test
    public void blockMMTest2() {

        long startTime = System.currentTimeMillis();

        int[] actual = BlockMM.blockMM(n, 50, A_, B_);

        long endTime = System.currentTimeMillis();

        System.out.println("Brute force " + (endTime - startTime) + " milliseconds");

        assertEquals("Failed.", expected_, actual);
    }

    @Test
    public void blockMMParallelTest1() throws InterruptedException {
        int[] A = new int[] { 2, 3, 7, 8, 5, 1, 4, 9, 1, 2, 8, 7, 1, 2, 3, 4 };
        int[] B = new int[] { 7, 8, 9, 6, 5, 4, 3, 2, 1, 6, 8, 5, 1, 2, 4, 3 };
        int[] expected = new int[] { 44, 86, 115, 77, 53, 86, 116, 79, 32, 78, 107, 71, 24, 42, 55, 37 };

        int[] actual = BlockMM.blockMMParallel(4, 2, A, B, 1);

        assertEquals("Failed.", expected, actual);
    }

    @Test
    public void blockMMParallelTest2() throws InterruptedException {
        long startTime = System.currentTimeMillis();

        int[] actual = BlockMM.blockMMParallel(n, 50, A_, B_, 4);

        long endTime = System.currentTimeMillis();

        System.out.println("Parallel " + (endTime - startTime) + " milliseconds");

        assertEquals("Failed.", expected_, actual);
    }
}

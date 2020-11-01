package com.parallel;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.springframework.test.util.AssertionErrors.assertEquals;

class PointedMMTest {

    private static final Random RANDOM = new Random();
    private static final int n = 1500;
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
    public void pointedMMTest1() {
        int[] A = new int[] { 12, 18, 19, 1, 9, 7, 23, 67, 99 };
        int[] B = new int[] { 87, 27, 12, 5, 67, 56, 28, 36, 25 };
        int[] expected = new int[] { 1666, 2214, 1627, 328, 882, 691, 5108, 8674, 6503 };

        int[] actual = PointedMM.pointedMM(3, A, B);

        assertEquals("Failed.", expected, actual);
    }

    @Test
    public void pointedMMTest2() {

        long startTime = System.currentTimeMillis();

        int[] actual = PointedMM.pointedMM(n, A_, B_);

        long endTime = System.currentTimeMillis();

        System.out.println("Brute force " + (endTime - startTime) + " milliseconds");

        assertEquals("Failed.", expected_, actual);
    }

    @Test
    public void pointedMMFirstParallelTest1() throws InterruptedException {
        int[] A = new int[] { 12, 18, 19, 1, 9, 7, 23, 67, 99 };
        int[] B = new int[] { 87, 27, 12, 5, 67, 56, 28, 36, 25 };
        int[] expected = new int[] { 1666, 2214, 1627, 328, 882, 691, 5108, 8674, 6503 };
        int[] actual = PointedMM.pointedMMParalleledByLines(3, A, B, threadCount);
        assertEquals("Failed", expected, actual);
    }

    @Test
    public void pointedMMFirstParallelTest2() throws InterruptedException {

        long startTime = System.currentTimeMillis();

        int[] actual = PointedMM.pointedMMParalleledByLines(n, A_, B_, threadCount);

        long endTime = System.currentTimeMillis();

        System.out.println("Brute force paralleled by lines " + (endTime - startTime) + " milliseconds");

        assertEquals("Failed", expected_, actual);
    }

    @Test
    public void pointedMMParalleledByColumnsTest1() throws InterruptedException {
        int[] A = new int[] { 12, 18, 19, 1, 9, 7, 23, 67, 99 };
        int[] B = new int[] { 87, 27, 12, 5, 67, 56, 28, 36, 25 };
        int[] expected = new int[] { 1666, 2214, 1627, 328, 882, 691, 5108, 8674, 6503 };
        int[] actual = PointedMM.pointedMMParalleledByColumns(3, A, B, threadCount);
        assertEquals("Failed", expected, actual);
    }

    @Test
    public void pointedMMParalleledByColumnsTest2() throws InterruptedException {

        long startTime = System.currentTimeMillis();

        int[] actual = PointedMM.pointedMMParalleledByColumns(n, A_, B_, threadCount);

        long endTime = System.currentTimeMillis();

        System.out.println("Brute force paralleled by columns " + (endTime - startTime) + " milliseconds");

        assertEquals("Failed", expected_, actual);
    }
}

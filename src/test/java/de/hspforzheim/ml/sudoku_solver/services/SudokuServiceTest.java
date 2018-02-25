/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hspforzheim.ml.sudoku_solver.services;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author marian
 */
public class SudokuServiceTest {
    //TODO: Add more test cases
    public SudokuServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testHasMatrixDuplicates() {
        System.out.println("Testing the hasMatrixDuplicates method");
        System.out.println("Testing matrices with duplicates");
        assertTrue(SudokuService.hasMatrixDuplicates(new Integer[]{1, 1, 3, 4, 5, 6, 7, 8, 9, 0}));
        assertTrue(SudokuService.hasMatrixDuplicates(new Integer[]{1, 2, 1}));
        assertTrue(SudokuService.hasMatrixDuplicates(new Integer[]{1, 123, 4, 120, 2139, 120}));
        assertTrue(SudokuService.hasMatrixDuplicates(new Integer[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}));
        assertTrue(SudokuService.hasMatrixDuplicates(new Integer[]{1, 0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 0}));
        assertTrue(SudokuService.hasMatrixDuplicates(new Integer[]{-2, 0, -22, 3, -4, 5, 6, -7, 8, -7, 10, 0}));
        assertTrue(SudokuService.hasMatrixDuplicates(new Integer[]{0, -0}));
        System.out.println("Testing matrices without duplicates");
        assertFalse(SudokuService.hasMatrixDuplicates(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}));
        assertFalse(SudokuService.hasMatrixDuplicates(new Integer[]{9, 8, 7, 6, 2, 3, 1, 4, 5}));
        assertFalse(SudokuService.hasMatrixDuplicates(new Integer[]{0, 1, -1, 2, -2, 3, -3}));
        assertFalse(SudokuService.hasMatrixDuplicates(new Integer[]{1}));

    }

    @Test
    public void testGetSudoku() {
        System.out.println("Testing the getSudoku method");
        System.out.println("Difficulty: Easy (1)");
        assertNotNull(SudokuService.getSudoku(SudokuService.EASY));

        System.out.println("Difficulty: Medium (2)");
        assertNotNull(SudokuService.getSudoku(SudokuService.MEDIUM));
        System.out.println("Difficulty: Hard (3)");
        assertNotNull(SudokuService.getSudoku(SudokuService.HARD));
        System.out.println("Difficulty: Very Hard (4)");
        assertNotNull(SudokuService.getSudoku(SudokuService.VERY_HARD));

    }

    @Test
    public void testIsSudokuValid() {

        System.out.println("Testing the isSudokuValid method");
        System.out.println("Testing valid sudokus");
        assertTrue(SudokuService.isSudokuValid(new Integer[9][9]));

        assertTrue(SudokuService.isSudokuValid(new Integer[][]{
            {5, 3, 4, 6, 7, 8, 9, 1, 2},
            {6, 7, 2, 1, 9, 5, 3, 4, 8},
            {1, 9, 8, 3, 4, 2, 5, 6, 7},
            {8, 5, 9, 7, 6, 1, 4, 2, 3},
            {4, 2, 6, 8, 5, 3, 7, 9, 1},
            {7, 1, 3, 9, 2, 4, 8, 5, 6},
            {9, 6, 1, 5, 3, 7, 2, 8, 4},
            {2, 8, 7, 4, 1, 9, 6, 3, 5},
            {3, 4, 5, 2, 8, 6, 1, 7, 9}
        }));
        assertTrue(SudokuService.isSudokuValid(new Integer[][]{
            {5, 3, 4, null, 7, 8, 9, 1, 2},
            {null, 7, 2, 1, 9, 5, 3, 4, 8},
            {1, 9, 8, 3, 4, 2, 5, 6, 7},
            {8, null, 9, 7, 6, null, 4, 2, 3},
            {4, 2, 6, 8, 5, 3, 7, 9, 1},
            {7, 1, null, 9, 2, 4, 8, 5, 6},
            {9, 6, 1, 5, 3, 7, 2, 8, 4},
            {null, 8, null, 4, 1, 9, 6, null, 5},
            {3, 4, 5, 2, 8, 6, 1, 7, 9}
        }));
        System.out.println("Testing invalid sudokus");
        assertFalse(SudokuService.isSudokuValid(new Integer[][]{
            {1, 1, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null}
        }));

        assertFalse(SudokuService.isSudokuValid(new Integer[][]{
            {1, null, null, null, null, null, null, null, null},
            {null, 1, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null}
        }));
        assertFalse(SudokuService.isSudokuValid(new Integer[][]{
            {1, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {1, null, null, null, null, null, null, null, null}
        }));
        assertFalse(SudokuService.isSudokuValid(new Integer[][]{
            {5, 2, 4, 6, 7, 8, 9, 1, 2},
            {6, 7, 2, 1, 9, 5, 3, 4, 8},
            {1, 9, 8, 3, 4, 2, 5, 6, 7},
            {8, 5, 9, 7, 6, 1, 4, 2, 3},
            {4, 2, 6, 8, 5, 3, 7, 9, 1},
            {7, 1, 3, 9, 2, 4, 8, 5, 6},
            {9, 6, 1, 5, 3, 7, 2, 8, 4},
            {2, 8, 7, 4, 1, 9, 6, 3, 5},
            {3, 4, 5, 2, 8, 6, 1, 7, 9}
        }));
        assertFalse(SudokuService.isSudokuValid(new Integer[][]{
            {3, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 3}
        }));

    }

    @Test
    public void testSolveSudoku() {

        System.out.println("Testing the solveSudoku method");
        CompletableFuture<Integer[][]> future = new CompletableFuture<>();

        System.out.println("Testing first sudoku");

        SudokuService.setSpeed(1.0);
        SudokuService.solveSudoku(new Integer[9][9], new SudokuService.SudokuSolverListener() {
            @Override
            public void sudokuSolvingFailed(int iterations, Duration duration, Exception e) {

                System.out.println("Sudoku solving failed");
                future.completeExceptionally(e);
            }

            @Override
            public void sudokuSolvingFinished(int iterations, Duration duration, Integer[][] finalSudokuMatrix) {
                System.out.println("Sudoku solving finished");
                future.complete(finalSudokuMatrix);

            }

            @Override
            public void sudokuValueChanged(int row, int col, int newValue) {
            }

            @Override
            public void sudokuValueSetToNull(int row, int col) {
            }
        });

        try {
            assertTrue(SudokuService.isSudokuValid(future.get()));
        } catch (ExecutionException ex) {
            fail(ex.getMessage());
        } catch (InterruptedException ex) {
            fail(ex.getMessage());

        }

        System.out.println("Testing second sudoku");
        CompletableFuture<Integer[][]> future2 = new CompletableFuture<>();

        SudokuService.solveSudoku(new Integer[][]{
            {5, 3, 4, null, 7, 8, 9, 1, 2},
            {null, 7, 2, 1, 9, 5, 3, 4, 8},
            {1, 9, 8, 3, 4, 2, 5, 6, 7},
            {8, null, 9, 7, 6, null, 4, 2, 3},
            {4, 2, 6, 8, 5, 3, 7, 9, 1},
            {7, 1, null, 9, 2, 4, 8, 5, 6},
            {9, 6, 1, 5, 3, 7, 2, 8, 4},
            {null, 8, null, 4, 1, 9, 6, null, 5},
            {3, 4, 5, 2, 8, 6, 1, 7, 9}
        }, new SudokuService.SudokuSolverListener() {
            @Override
            public void sudokuSolvingFailed(int iterations, Duration duration, Exception e) {

                System.out.println("Sudoku solving failed");
                future2.completeExceptionally(e);
            }

            @Override
            public void sudokuSolvingFinished(int iterations, Duration duration, Integer[][] finalSudokuMatrix) {
                System.out.println("Sudoku solving finished");
                future2.complete(finalSudokuMatrix);

            }

            @Override
            public void sudokuValueChanged(int row, int col, int newValue) {
            }

            @Override
            public void sudokuValueSetToNull(int row, int col) {
            }
        });

        try {
            Assert.assertArrayEquals(future2.get(), new Integer[][]{
                {5, 3, 4, 6, 7, 8, 9, 1, 2},
                {6, 7, 2, 1, 9, 5, 3, 4, 8},
                {1, 9, 8, 3, 4, 2, 5, 6, 7},
                {8, 5, 9, 7, 6, 1, 4, 2, 3},
                {4, 2, 6, 8, 5, 3, 7, 9, 1},
                {7, 1, 3, 9, 2, 4, 8, 5, 6},
                {9, 6, 1, 5, 3, 7, 2, 8, 4},
                {2, 8, 7, 4, 1, 9, 6, 3, 5},
                {3, 4, 5, 2, 8, 6, 1, 7, 9}
            });
            assertTrue(SudokuService.isSudokuValid(future2.get()));
        } catch (ExecutionException ex) {
            fail(ex.getMessage());
        } catch (InterruptedException ex) {
            fail(ex.getMessage());

        }
    }

}

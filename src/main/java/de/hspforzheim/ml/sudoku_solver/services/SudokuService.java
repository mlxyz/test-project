/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hspforzheim.ml.sudoku_solver.services;

import de.hspforzheim.ml.sudoku_solver.utility.NotifyingThread;
import de.hspforzheim.ml.sudoku_solver.utility.NotifyingThreadListener;
import java.awt.Point;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jsoup.*;
import org.jsoup.nodes.*;

/**
 *
 * @author marian
 */
public class SudokuService implements NotifyingThreadListener {

    SudokuSolverListener listener;

    public SudokuSolverListener getListener() {
        return listener;
    }

    public void setListener(SudokuSolverListener listener) {
        this.listener = listener;
    }
    Integer[][] sudokuMatrix;

    long startTime = 0, endTime = 0;

    private int iterations = 0;
    public static final int EASY = 1;
    public static final int HARD = 3;
    public static final int MEDIUM = 2;
    public static final int VERY_HARD = 4;

    public Integer[][] getSudokuMatrix() {
        return sudokuMatrix;
    }

    public void setSudokuMatrix(Integer[][] sudokuMatrix) {
        this.sudokuMatrix = sudokuMatrix;
    }

    private static int waitTime = 1000;

    public static double getSpeed() {
        return (double) (waitTime - 1000) / 1000.0;
    }

    /**
     * Sets the global speed of all current and future solving processes
     *
     * @param speed The speed parameter between 0.0 (slowest) and 1.0 (fastest)
     */
    public static void setSpeed(double speed) {
        waitTime = (int) (1000.0 - (double) (speed * 1000.0));
    }

    /**
     * Gets a sudoku from websudoku.com
     *
     * @param difficulty
     * @return
     * @see SudokuService.EASY
     * @see SudokuService.MEDIUM
     * @see SudokuService.HARD
     * @see SudokuService.VERY_HARD
     */
    public static Integer[][] getSudoku(int difficulty) {
        final String BASE_URL = "http://de2.websudoku.com";
        Integer[][] sudoku = new Integer[9][9];

        Document doc;
        try {
            doc = Jsoup.connect(BASE_URL + "/?level=" + difficulty).get();
        } catch (IOException ex) {
            return null;
        }
        Element table = doc.getElementById("puzzle_grid");

        for (Element el : table.getElementsByTag("input")) {

            if (el.hasAttr("READONLY")) {

                int value = Integer.parseInt(el.attr("VALUE"));

                sudoku[Integer.parseInt(String.valueOf(el.attr("NAME").charAt(el.attr("NAME").length() - 1))) - 1][Integer.parseInt(String.valueOf(el.attr("NAME").charAt(el.attr("NAME").length() - 2))) - 1] = value;
            }

        }

        return sudoku;

    }

    /**
     * Starts solving the given sudoku matrix. Will give feedback (update on
     * valueChange and finished and finished with error) by listeners.
     *
     * @param sudokuMatrix
     * @param listener
     * @see SudokuSolverListener
     */
    public static void solveSudoku(Integer[][] sudokuMatrix, SudokuSolverListener listener) {

        SudokuService service = new SudokuService(sudokuMatrix);
        service.setListener(listener);
        new NotifyingThread() {
            @Override
            public void doRun() throws NotSolvableException, InterruptedException {
                service.start();
            }
        }.addListener(service).start();

    }
    List<Point> changes = new ArrayList<>();

    public SudokuService(Integer[][] sudokuMatrix) {
        this.sudokuMatrix = sudokuMatrix;
    }

    private Point findNextNull() {

        for (int row = 0; row < getSudokuMatrix().length; row++) {
            for (int col = 0; col < getSudokuMatrix()[0].length; col++) {
                if (getSudokuMatrix()[row][col] == null) {
                    return new Point(row, col);
                }
            }
        }
        return null;
    }

    /**
     * Tries to increase the number at x,y to the next valid number.
     *
     * @param x
     * @param y
     * @return Returns true if a valid number was found, returns false if no
     * valid number was found
     * @throws InterruptedException
     */
    private boolean incTilValid(int x, int y) throws InterruptedException {
        iterations++;
        //check if the cell is empty or already has a value
        int i = getSudokuMatrix()[x][y] == null ? 0 : getSudokuMatrix()[x][y];
        do {

            if (i == 9) {
                //no valid number was found --> clearing cell
                setToNull(x, y);

                return false;
            }

            setValueAt(x, y, ++i);

            Thread.sleep(waitTime);

        } while (!isSudokuValid(sudokuMatrix)); //do while invalid
        changes.add(new Point(x, y));
        return true;

    }

    /**
     * Checks if the given sudokuMatrix is valid
     *
     * @param grid
     * @return
     */
    public static boolean isSudokuValid(Integer[][] grid) {

        for (int row = 0; row < grid.length; row++) {
            if (hasMatrixDuplicates(grid[row])) {

                return false;

            }
            Integer[] col = new Integer[grid[0].length];
            Integer[] subgrid = new Integer[grid.length];
            for (int currentCol = 0; currentCol < grid[0].length; currentCol++) {
                col[currentCol] = grid[currentCol][row];

                subgrid[currentCol] = grid[(row / 3) * 3 + currentCol / 3][row * 3 % 9 + currentCol % 3];
            }
            if (hasMatrixDuplicates(col) || hasMatrixDuplicates(subgrid)) {
                return false;
            }

        }

        return true;

    }

    /**
     * Checks if the given array has duplicate numbers
     *
     * @param check
     * @return
     */
    public static boolean hasMatrixDuplicates(Integer[] check) {
        Set<Integer> lump = new HashSet<Integer>();//set of unique values that are already in the array
        for (Integer i : check) {
            if (i != null && lump.contains(i)) {
                return true;
            }
            lump.add(i);
        }
        return false;
    }

    private void setValueAt(int row, int col, int newValue) {
        getSudokuMatrix()[row][col] = newValue;
        listener.sudokuValueChanged(row, col, newValue);

    }

    private void setToNull(int row, int col) {
        getSudokuMatrix()[row][col] = null;
        listener.sudokuValueSetToNull(row, col);

    }

    private void start() throws InterruptedException, NotSolvableException {

        startTime = System.nanoTime();

        //do while the sudoku is not solved
        while (findNextNull() != null) {

            Point currentEl = findNextNull(); //get next empty cell
            //revert changes as long as the current cell can not be set to a valid number
            while (!incTilValid(currentEl.x, currentEl.y)) {//as long as the current cell can not be set
                if (changes.size() < 1) {
                    //means that the sudoku is not solvable, because no combination we tried worked
                    throw new NotSolvableException();
                }
                //current number not solvable, so we must have made an error earlier --> reverting
                currentEl = changes.get(changes.size() - 1);
                changes.remove(currentEl);

            }

        }

    }

    @Override
    public void threadFinished(Object sender) {
        endTime = System.nanoTime();
        listener.sudokuSolvingFinished(iterations, Duration.ofNanos(endTime - startTime), sudokuMatrix);

    }

    @Override
    public void threadThrewException(Exception e, Object sender) {
        endTime = System.nanoTime();
        listener.sudokuSolvingFailed(iterations, Duration.ofNanos(endTime - startTime), e);
    }

    public class NotSolvableException extends  Exception{

        public NotSolvableException() {
        }

        public NotSolvableException(String message) {
            super(message);
        }

    }

    public interface SudokuSolverListener {

        public void sudokuSolvingFinished(int iterations, Duration duration, Integer[][] finalSudokuMatrix);

        public void sudokuSolvingFailed(int iterations, Duration duration, Exception e);

        public void sudokuValueChanged(int row, int col, int newValue);

        public void sudokuValueSetToNull(int row, int col);
    }

}

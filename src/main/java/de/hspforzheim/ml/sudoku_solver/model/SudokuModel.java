/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hspforzheim.ml.sudoku_solver.model;

import de.hspforzheim.ml.sudoku_solver.services.SudokuService;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 *
 * @author marian
 */
public class SudokuModel {

    private Set<Integer> invalidColumns = new HashSet<>();
    private Set<Integer> invalidRows = new HashSet<>();
    private Set<Integer> invalidSubGrids = new HashSet<>();

    private Set<SudokuModelValueChangedListener> listeners = new CopyOnWriteArraySet<>();

    private Integer[][] sudokuMatrix
            = new Integer[9][9];

    public void addListener(SudokuModelValueChangedListener listener) {
        listeners.add(listener);
    }

    /**
     * Checks, whether the sudoku is valid and fills the list of invalid rows,
     * columns and subgrids.
     *
     * @param grid
     * @return Returns true, if the sudoku valid and false, if it is not
     */
    private boolean checkSudokuStatus(Integer[][] grid) {

        invalidRows.clear();
        invalidColumns.clear();
        invalidSubGrids.clear();

        boolean valid = true;
        for (int row = 0; row < grid.length; row++) {
            if (SudokuService.hasMatrixDuplicates(grid[row])) {
                invalidRows.add(row);
                valid = false;

            }
            Integer[] col = new Integer[grid[0].length];
            Integer[] subgrid = new Integer[grid.length];
            for (int currentCol = 0; currentCol < grid[0].length; currentCol++) {
                col[currentCol] = grid[currentCol][row];

                //fills the subgrid array whilst iterating through rows
                subgrid[currentCol] = grid[(row / 3) * 3 + currentCol / 3][row * 3 % 9 + currentCol % 3];
            }
            if (SudokuService.hasMatrixDuplicates(col)) {
                valid = false;
                invalidColumns.add(row);
            }
            if (SudokuService.hasMatrixDuplicates(subgrid)) {
                valid = false;
                invalidSubGrids.add(row);
            }

        }

        return valid;

    }

    public void clear() {
        sudokuMatrix = new Integer[9][9];

        listeners.forEach(l -> l.valueChangedEverywhere());
    }

    public Set<Integer> getInvalidColumns() {
        return invalidColumns;
    }

    public Set<Integer> getInvalidRows() {
        return invalidRows;
    }

    public Set<Integer> getInvalidSubGrids() {
        return invalidSubGrids;
    }

    public Integer[][] getSudokuMatrix() {
        return sudokuMatrix;
    }

    public void setSudokuMatrix(Integer[][] sudokuMatrix) {
        this.sudokuMatrix = sudokuMatrix;

        listeners.forEach(l -> l.valueChangedEverywhere());
    }

    public boolean isValid() {
        return checkSudokuStatus(sudokuMatrix);
    }

    public void removeListener(SudokuModelValueChangedListener listener) {
        listeners.remove(listener);
    }

    public void setToNull(int row, int col) {
        sudokuMatrix[row][col] = null;

        listeners.forEach(l -> l.valueChangedToNull(row, col));

    }

    public void setValueAt(int row, int col, int newValue) {
        sudokuMatrix[row][col] = newValue;

        listeners.forEach(l -> l.valueChanged(row, col, newValue));

    }

    public interface SudokuModelValueChangedListener {

        public void valueChanged(int row, int col, int newValue);

        public void valueChangedToNull(int row, int col);

        public void valueChangedEverywhere();
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hspforzheim.ml.sudoku_solver.controller;

import de.hspforzheim.ml.sudoku_solver.controls.SudokuField;
import de.hspforzheim.ml.sudoku_solver.model.SudokuModel;
import de.hspforzheim.ml.sudoku_solver.services.SudokuService;
import de.hspforzheim.ml.sudoku_solver.services.SudokuService.SudokuSolverListener;
import de.hspforzheim.ml.sudoku_solver.view.MainWindow;
import java.time.Duration;
import javax.swing.JOptionPane;
import java.time.Clock;

/**
 *
 * @author marian
 */
public class MainWindowController implements SudokuField.SudokuFieldValueChangedListener, SudokuSolverListener {

    private MainWindow view;
    private SudokuModel model;

    public static void main(String[] args) {

        setUpUI();
        new MainWindowController().showView();

    }

    public void setSpeed(double speed) {
        SudokuService.setSpeed((double) ((double) speed / 100));
    }

    public MainWindowController() {
        model = new SudokuModel();
        view = new MainWindow(this);
        view.getSudokuField().setModel(model);
        view.getSudokuField().addListener(this);

    }

    public void getSudoku(int difficulty) {
        Integer[][] sudoku = SudokuService.getSudoku(difficulty);
        if (sudoku != null) {
            model.setSudokuMatrix(sudoku);

        }
    }

    public void solveSudoku() {
        view.getSolveButton().setEnabled(false);
        view.getGetButton().setEnabled(false);
        view.getClearButton().setEnabled(false);
        SudokuService.solveSudoku(model.getSudokuMatrix(), this);

    }

    public void clearSudoku() {
        model.clear();
    }

    public void showView() {
        view.setVisible(true);
    }

    public void shouldShowFeedback(boolean shouldShowFeedback) {

        view.getSudokuField().setShouldGiveVisualFeedback(shouldShowFeedback);
        view.getSudokuField().updateCellContent();
    }

    @Override
    public void sudokuSolvingFailed(int iterations, Duration duration, Exception e) {
        view.getSolveButton().setEnabled(true);
        view.getGetButton().setEnabled(true);
        view.getClearButton().setEnabled(true);
        JOptionPane.showMessageDialog(null, "Das Sudoku ist nicht lösbar!", "Nicht lösbar", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void sudokuSolvingFinished(int iterations, Duration duration, Integer[][] finalSudokuMatrix) {
        view.getSolveButton().setEnabled(true);
        view.getGetButton().setEnabled(true);
        view.getClearButton().setEnabled(true);
        JOptionPane.showMessageDialog(null, "Das Sudoku wurde mit " + iterations + " Iteration" + (iterations == 1 ? "" : "en") + " in " + duration.toMillis() + " Millisekunden gelöst!", "Gelöst", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void sudokuValueChanged(int row, int col, int newValue) {
        model.setValueAt(row, col, newValue);
    }

    @Override
    public void sudokuValueSetToNull(int row, int col) {
        model.setToNull(row, col);
    }

    @Override
    public void valueChanging(int row, int col, int newValue) {
        model.setValueAt(row, col, newValue);

    }

    @Override
    public void valueChangingToNull(int row, int col) {
        model.setToNull(row, col);

    }

    private static void setUpUI() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

    }
}

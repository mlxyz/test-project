/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hspforzheim.ml.sudoku_solver.controls;

import de.hspforzheim.ml.sudoku_solver.model.SudokuModel;
import de.hspforzheim.ml.sudoku_solver.model.SudokuModel.SudokuModelValueChangedListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;

/**
 *
 * @author marian
 */
public class SudokuField extends JPanel implements SudokuModelValueChangedListener {

    private boolean shouldGiveVisualFeedback = true;

    public boolean isShouldGiveVisualFeedback() {
        return shouldGiveVisualFeedback;
    }

    public void setShouldGiveVisualFeedback(boolean shouldGiveVisualFeedback) {
        this.shouldGiveVisualFeedback = shouldGiveVisualFeedback;
        if (shouldGiveVisualFeedback) {
            updateValidationStatus();
        } else {
            for (int row = 0; row < model.getSudokuMatrix().length; row++) {
                for (int col = 0; col < model.getSudokuMatrix()[0].length; col++) {
                    cells[row][col].setWrongNumber(false);
                }
            }
        }
    }

    public static final int CELL_SIZE = 60;

    public static final Font FONT_NUMBERS = new Font("Open Sans", Font.PLAIN, 21);
    public static final int GRID_SIZE = 9;    // Size of the 
    public static final int CANVAS_WIDTH = CELL_SIZE * GRID_SIZE;
    public static final int CANVAS_HEIGHT = CELL_SIZE * GRID_SIZE;
    public static final int SUBGRID_SIZE = 3;
    private SudokuJLabel[][] cells = new SudokuJLabel[GRID_SIZE][GRID_SIZE];
    private boolean editable = true;
    Set<SudokuFieldValueChangedListener> listeners = new CopyOnWriteArraySet<>();
    private SudokuModel model;

    /**
     * Constructor to setup the game and the UI Components
     */
    public SudokuField() {
        this.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        this.setBackground(new Color(239, 239, 239));
    }

    public void addListener(SudokuFieldValueChangedListener listener) {
        listeners.add(listener);
    }

    private Point findJLabel(JLabel label) {
        for (int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells[0].length; col++) {
                if (label.equals(cells[row][col])) {
                    return new Point(row, col);
                }
            }
        }
        return null;

    }

    public void setModel(SudokuModel model) {
        this.model = model;

        model.addListener(this);
        this.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));

        for (int row = 0; row < model.getSudokuMatrix().length; row++) {
            for (int col = 0; col < model.getSudokuMatrix()[0].length; col++) {
                cells[row][col] = new SudokuJLabel() {
                    @Override
                    public boolean isEditable() {
                        return editable;
                    }

                    private SudokuJLabel getOuter() {

                        return this;
                    }

                    @Override
                    public void setUpKeyListeners() {
                        this.addKeyListener(new KeyListener() {
                            @Override
                            public void keyPressed(KeyEvent e) {

                                if (isEditable()) {
                                    try {
                                        if (Integer.parseInt(String.valueOf(e.getKeyChar())) == 0) {

                                            Point cur = findJLabel(getOuter());
                                            listeners.forEach(l -> l.valueChangingToNull(cur.x, cur.y));

                                            return;

                                        }
                                    } catch (Exception exception) {
                                        e.consume();
                                        return;
                                    }

                                    Point cur = findJLabel(getOuter());
                                    listeners.forEach(l -> l.valueChanging(cur.x, cur.y, Integer.parseInt(String.valueOf(e.getKeyChar()))));

                                }
                            }

                            @Override
                            public void keyReleased(KeyEvent e) {
                            }

                            @Override
                            public void keyTyped(KeyEvent e) {
                            }
                        });
                        this.addMouseListener(new MouseListener() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                            }

                            @Override
                            public void mouseEntered(MouseEvent e) {
                                requestFocus();
                            }

                            @Override
                            public void mouseExited(MouseEvent e) {

                                if (hasFocus()) {
                                    getOuter().getParent().requestFocusInWindow();
                                }

                            }

                            @Override
                            public void mousePressed(MouseEvent e) {
                            }

                            @Override
                            public void mouseReleased(MouseEvent e) {
                            }
                        });
                    }
                };

                cells[row][col].setBorder(new MatteBorder(row == 6 ? 3 : 1, col == 6 ? 3 : 1, row == 2 ? 3 : 1, col == 2 ? 3 : 1, Color.BLACK));

                this.add(cells[row][col]);

                cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                cells[row][col].setFont(FONT_NUMBERS);
            }
        }
        updateCellContent();
    }

    /**
     * Paints the cells in all invalid rows, columns and subgrids red You have
     * to call model.isValid() before calling this method to ensure, that the
     * model is updated
     *
     */
    public void updateValidationStatus() {
        model.isValid();

        for (int row = 0; row < model.getSudokuMatrix().length; row++) {
            for (int col = 0; col < model.getSudokuMatrix()[0].length; col++) {
                cells[row][col].setWrongNumber(model.getInvalidRows().contains(row) || model.getInvalidColumns().contains(col) || model.getInvalidSubGrids().contains(row / 3 * 3 + col / 3));

            }

        }

    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {

        this.editable = editable;
    }

    public void removeListener(SudokuFieldValueChangedListener listener) {
        listeners.remove(listener);
    }

    /**
     * Updates all cells content to the respective number in the model
     */
    public void updateCellContent() {
        for (int row = 0; row < model.getSudokuMatrix().length; ++row) {
            for (int col = 0; col < model.getSudokuMatrix()[0].length; ++col) {
                if (model.getSudokuMatrix()[row][col] != null) {
                    cells[row][col].setText(model.getSudokuMatrix()[row][col] + "");
                } else {
                    cells[row][col].setText("");
                }
            }

        }
    }

    @Override
    public void valueChanged(int row, int col, int newValue) {

        cells[row][col].setText(String.valueOf(newValue));
        if (shouldGiveVisualFeedback) {
            updateValidationStatus();
        }
    }

    @Override
    public void valueChangedEverywhere() {
        updateCellContent();
        if (shouldGiveVisualFeedback) {
            updateValidationStatus();
        }
    }

    @Override
    public void valueChangedToNull(int row, int col) {
        cells[row][col].setText("");
        if (shouldGiveVisualFeedback) {
            updateValidationStatus();
        }
    }

    public interface SudokuFieldValueChangedListener {

        public void valueChanging(int row, int col, int newValue);

        public void valueChangingToNull(int row, int col);
    }
}

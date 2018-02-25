/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hspforzheim.ml.sudoku_solver.controls;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JLabel;

/**
 *
 * @author marian
 */
public abstract class SudokuJLabel extends JLabel {

    public static Color CELL_BG = new Color(239, 239, 239);
    public static final Color CELL_FOCUSED = new Color(173, 184, 201);

    private boolean wrongNumber = false;
    private Color backgroundColor = CELL_BG;

    public boolean isWrongNumber() {
        return wrongNumber;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {

        this.backgroundColor = backgroundColor;

    }

    public abstract boolean isEditable();

    public abstract void setUpKeyListeners();

    public void setWrongNumber(boolean wrongNumber) {
        this.wrongNumber = wrongNumber;
        updateBackground();

    }

    public SudokuJLabel() {
        this.setOpaque(true);
        setBackground(CELL_BG);
        setForeground(Color.BLACK);

        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

                updateBackground();
                setOpaque(true);

            }

            @Override
            public void focusLost(FocusEvent e) {
                updateBackground();
                // setOpaque(false);

            }
        });
        setUpKeyListeners();
    }

    private void updateBackground() {
        if (hasFocus()) {
            setBackground(CELL_FOCUSED);
        } else if (isWrongNumber()) {
            setBackground(Color.RED);

        } else {
            setBackground(CELL_BG);
        }
    }

}

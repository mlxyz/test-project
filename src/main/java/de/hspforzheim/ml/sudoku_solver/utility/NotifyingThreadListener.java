/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hspforzheim.ml.sudoku_solver.utility;

/**
 *
 * @author marian
 */
public interface NotifyingThreadListener {

    public void threadFinished(Object sender);

    public void threadThrewException(Exception e, Object sender);
}

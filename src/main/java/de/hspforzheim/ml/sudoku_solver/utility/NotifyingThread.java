/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hspforzheim.ml.sudoku_solver.utility;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 
 * @author marian
 */
public abstract class NotifyingThread extends Thread {

    Set<NotifyingThreadListener> listeners = new CopyOnWriteArraySet<>();

    public NotifyingThread addListener(NotifyingThreadListener listener) {
        listeners.add(listener);
        return this;
    }

    public NotifyingThread removeListener(NotifyingThreadListener listener) {
        listeners.remove(listener);
        return this;
    }

    @Override
    public void run() {
        try {

            doRun();
        } catch (Exception e) {

            listeners.forEach(l -> l.threadThrewException(e, this));
            return;

        }

        listeners.forEach(l -> l.threadFinished(this));

    }

    public abstract void doRun() throws Exception;

}

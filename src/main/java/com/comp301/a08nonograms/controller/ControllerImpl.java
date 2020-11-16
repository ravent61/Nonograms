package com.comp301.a08nonograms.controller;

import com.comp301.a08nonograms.model.Clues;
import com.comp301.a08nonograms.model.Model;
import com.comp301.a08nonograms.PuzzleLibrary;

import java.util.Random;

public class ControllerImpl implements Controller {
    Model model;


    public ControllerImpl(Model model) {
        this.model = model;
    }
    @Override
    public Clues getClues() {
        return PuzzleLibrary.create().get(model.getPuzzleIndex());
    }

    @Override
    public boolean isSolved() {
        return model.isSolved();
    }

    @Override
    public boolean isShaded(int row, int col) {
        return model.isShaded(row, col);
    }

    @Override
    public boolean isEliminated(int row, int col) {
        return model.isEliminated(row, col);
    }

    @Override
    public void toggleShaded(int row, int col) {
        model.toggleCellShaded(row, col);
    }

    @Override
    public void toggleEliminated(int row, int col) {
        model.toggleCellEliminated(row, col);
    }

    public void toggleSpace(int row, int col) {
        model.toggleCellSpace(row, col);
    }

    @Override
    public void nextPuzzle() {
        if ((model.getPuzzleIndex() + 1) > (model.getPuzzleCount() - 1)){
            model.setPuzzleIndex(0);
        }else{
            model.setPuzzleIndex(model.getPuzzleIndex() + 1);
        }
    }

    @Override
    public void prevPuzzle() {
        if ((model.getPuzzleIndex() - 1) < 0){
            model.setPuzzleIndex(model.getPuzzleCount() - 1);
        }else{
            model.setPuzzleIndex(model.getPuzzleIndex() - 1);
        }

    }

    @Override
    public void randPuzzle() {
        Random random = new Random();
        model.setPuzzleIndex(random.nextInt(model.getPuzzleCount()));
    }

    @Override
    public void clearBoard() {
        model.clear();
    }

    @Override
    public int getPuzzleIndex() {
        return model.getPuzzleIndex();
    }

    @Override
    public int getPuzzleCount() {
        return model.getPuzzleCount();
    }

    public int getHeight() {
        return model.getHeight();
    }

    public int getWidth() {
        return model.getWidth();
    }

    public int getRowCluesLength() {
        return model.getRowCluesLength();
    }

    public int getColCluesLength() {
        return model.getColCluesLength();
    }
}

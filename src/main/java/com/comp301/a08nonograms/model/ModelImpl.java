package com.comp301.a08nonograms.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelImpl implements Model{
    private List<Clues> clues;
    private int puzzle_index;
    private Clues active_clues;
    private int [] [] board;
    private List<ModelObserver> observers;

    public ModelImpl(List<Clues> clues) {
        this.clues = clues;
        this.puzzle_index = 0;
        this.active_clues = clues.get(0);
        this.board = new int [active_clues.getHeight()] [active_clues.getWidth()];
        this.observers = new ArrayList<ModelObserver>();
    }

    @Override
    public int getPuzzleCount() {
        return clues.size();
    }

    @Override
    public int getPuzzleIndex() {
        return puzzle_index;
    }

    @Override
    public void setPuzzleIndex(int index) {
        puzzle_index = index;
        active_clues = clues.get(puzzle_index);
        board = new int [active_clues.getHeight()] [active_clues.getWidth()];
        for (ModelObserver observer : observers) {
            observer.update(this);
        }
    }

    @Override
    public void addObserver(ModelObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ModelObserver observer) {
        observers.remove(observer);
    }

    @Override
    public boolean isSolved() {
        for (int row = 0; row < active_clues.getHeight(); row++){
            int [] rowClues = getRowClues(row);
            int alike_cells = 0;
            int clue_idx = 0;
            while (rowClues[clue_idx] == 0) {
                clue_idx++;
                if (clue_idx == rowClues.length) {
                    break;
                }
            }
            for (int col = 0; col < getWidth(); col++) {
                if (clue_idx >= rowClues.length) {
                    break;
                }
                if (board[row][col] == 1){
                    alike_cells++;
                }else{
                    if (alike_cells > 0){
                        if (alike_cells != rowClues[clue_idx]) {
                            return false;
                        }
                        clue_idx++;
                    }
                    alike_cells = 0;
                }
            }
            System.out.println(row);
        }
        for (int col = 0; col < active_clues.getWidth(); col++){
            int [] colClues = getColClues(col);
            int alike_cells = 0;
            int clue_idx = 0;
            while (colClues[clue_idx] == 0) {
                clue_idx++;
                if(clue_idx == colClues.length){
                    break;
                }
            }
            for (int row = 0; row < getHeight(); row++) {
                if (clue_idx >= colClues.length) {
                    break;
                }
                if (board[row][col] == 1){
                    alike_cells++;
                }else{
                    if (alike_cells > 0){
                        if (alike_cells != colClues[clue_idx]) {
                            return false;
                        }
                        clue_idx++;
                    }
                    alike_cells = 0;
                }
            }

        }
        //for (ModelObserver observers : observers){
        //observers.update(this);
        //}
        System.out.println("SOLVED!");
        return true;
    }

    @Override
    public boolean isShaded(int row, int col) {
        return board[row][col] == 1;
    }

    @Override
    public boolean isEliminated(int row, int col) {
        return board[row][col] == -1;
    }

    @Override
    public boolean isSpace(int row, int col) {
        return board[row][col] == 0;
    }

    @Override
    public void toggleCellShaded(int row, int col) {
        if (board[row][col] == 1) {
            board[row][col] = 0;
        }else{
            board[row][col] = 1;
        }
        for (ModelObserver observers : observers){
            observers.update(this);
        }
    }

    @Override
    public void toggleCellEliminated(int row, int col) {
        if (board[row][col] == -1) {
            board[row][col] = 0;
        }else{
            board[row][col] = -1;
        }        for (ModelObserver observers : observers) {
            observers.update(this);
        }
    }
    public void toggleCellSpace(int row, int col) {
        board[row][col]= 0;
        for (ModelObserver observers : observers) {
            observers.update(this);
        }
    }

    @Override
    public void clear() {
        for (int row = 0; row < getHeight(); row++) {
            for (int col = 0; col < getWidth(); col++) {
                board[row][col] = 0;
            }
        }
        for (ModelObserver observers : observers) {
            observers.update(this);
        }
    }

    @Override
    public int getWidth() {
        return active_clues.getWidth();
    }

    @Override
    public int getHeight() {
        return active_clues.getHeight();
    }

    @Override
    public int[] getRowClues(int index) {
        return active_clues.getRowClues(index);
    }

    @Override
    public int[] getColClues(int index) {
        return active_clues.getColClues(index);
    }

    @Override
    public int getRowCluesLength() {
        return active_clues.getRowCluesLength();
    }

    @Override
    public int getColCluesLength() {
        return active_clues.getColCluesLength();
    }
}

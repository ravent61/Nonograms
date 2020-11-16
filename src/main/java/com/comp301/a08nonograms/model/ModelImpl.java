package com.comp301.a08nonograms.model;

import java.util.ArrayList;
import java.util.List;

public class ModelImpl implements Model {
  private List<Clues> clues;
  private int puzzle_index;
  private Clues active_clues;
  private int[][] board;
  private List<ModelObserver> observers;

  public ModelImpl(List<Clues> clues) {
    this.clues = clues;
    this.puzzle_index = 0;
    this.active_clues = clues.get(0);
    this.board = new int[active_clues.getHeight()][active_clues.getWidth()];
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
    board = new int[active_clues.getHeight()][active_clues.getWidth()];
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
    // check if rows match rows clues
    for (int row = 0; row < getHeight(); row++) {
      int[] rowsClues = getRowClues(row);
      int alike_cells = 0;
      boolean max_cells = false;
      int sum_clues = 0;
      // check if any rows are 00
      for (int i = 0; i < rowsClues.length; i++) {
        sum_clues += rowsClues[i];
      }
      if (sum_clues == 0) {
        for (int col = 0; col < getWidth(); col++) {
          if (board[row][col] == 1) {
            return false;
          }
        }
      } else {
        int sum_cells = 0;
        for (int col = 0; col < getWidth(); col++) {
          if (board[row][col] == 1) {
            sum_cells += board[row][col];
          }
        }
        if (sum_cells != sum_clues) {
          return false;
        }
      }
    }
    for (int col = 0; col < getWidth(); col++) {
      int[] colClues = getColClues(col);
      int sum_clues = 0;
      //check if any rows are 00
      for (int i = 0; i < colClues.length; i++) {
        sum_clues += colClues[i];
      }
      if (sum_clues == 0) {
        for (int row = 0; row < getHeight(); row++) {
          if (board[row][col] == 1) {
            return false;
          }
        }
      } else {
        int sum_cells = 0;
        for (int row = 0; row < getHeight(); row++) {
          if (board[row][col] == 1) {
            sum_cells ++;
          }
        }
        if (sum_cells != sum_clues) {
          return false;
        }
      }
    }
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
    } else {
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
    } else {
      board[row][col] = -1;
    }
    for (ModelObserver observers : observers) {
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

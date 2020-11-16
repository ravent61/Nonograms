package com.comp301.a08nonograms.view;

import com.comp301.a08nonograms.PuzzleLibrary;
import com.comp301.a08nonograms.controller.Controller;
import com.comp301.a08nonograms.controller.ControllerImpl;
import com.comp301.a08nonograms.model.Clues;
import com.comp301.a08nonograms.model.Model;
import com.comp301.a08nonograms.model.ModelImpl;
import com.comp301.a08nonograms.model.ModelObserver;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.util.Arrays;

public class AppLauncher extends Application {

  @Override
  public void start(Stage stage) {
    Model model = new ModelImpl(PuzzleLibrary.create());
    Controller controller = new ControllerImpl(model);
    stage.setTitle("Raven's Nonograms!");
    Board board = new Board(controller);
    model.addObserver(
        new ModelObserver() {
          @Override
          public void update(Model model1) {
            stage.setScene(new Scene(board.render()));
            stage.show();
          }
        });

    stage.setScene(new Scene(board.render()));
    stage.show();
  }

  private class Board implements FXComponent {
    Controller controller;

    public Board(Controller controller) {
      this.controller = controller;
    }

    @Override
    public Parent render() {
      GridPane root = new GridPane();
      root.setPrefSize(500, 500);

      // Display gameboard
      for (int col = 0; col < controller.getWidth(); col++) {
        for (int row = 0; row < controller.getHeight(); row++) {
          Button cell = new Button();
          if (controller.isEliminated(row, col)) {
            cell.setBackground(Background.EMPTY);
            cell.setText("X");
            cell.setStyle("-fx-border-color: black");
          } else {
            if (controller.isShaded(row, col)) {
              cell.setStyle("-fx-background-color: black");
            } else {
              cell.setBackground(Background.EMPTY);
              cell.setStyle("-fx-border-color: black");
            }
          }
          cell.setPrefSize(30, 30);
          int finalRow = row;
          int finalCol = col;
          cell.setOnMouseClicked(
              new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                  if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    controller.toggleShaded(finalRow, finalCol);
                    if (controller.isShaded(finalRow, finalCol)) {
                      cell.setStyle("-fx-background-color: black");
                      cell.setText("");
                    } else {
                      cell.setText("");
                      cell.setBackground(Background.EMPTY);
                      cell.setStyle("-fx-border-color: black");
                    }
                  }
                  if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    controller.toggleEliminated(finalRow, finalCol);
                    if (controller.isEliminated(finalRow, finalCol)) {
                      cell.setBackground(Background.EMPTY);
                      cell.setText("X");
                    } else {
                      cell.setText("");
                      cell.setBackground(Background.EMPTY);
                    }
                    cell.setStyle("-fx-border-color: black");
                  }
                }
              });
          cell.setTranslateX((col * 30) + 8 + (12.5 * controller.getRowCluesLength()));
          cell.setTranslateY((row * 30) + 20 + (10 * controller.getColCluesLength()));
          root.getChildren().add(cell);
        }
      }

      // Display Clues
      Clues clues = controller.getClues();
      for (int row = 0; row < controller.getHeight(); row++) {
        Text clue = new Text();
        int[] rowClue = clues.getRowClues(row);
        String string = Arrays.toString(rowClue);
        string = string.replace("[", "");
        string = string.replace("]", "");
        string = string.replace(",", "");
        string = string.replace("", " ");
        clue.setText(string);
        clue.setTranslateY((row * 30) + 20 + (10 * controller.getColCluesLength()));
        root.getChildren().add(clue);
      }
      for (int col = 0; col < controller.getWidth(); col++) {
        Text clue = new Text();
        int[] colClue = clues.getColClues(col);
        String string = Arrays.toString(colClue);
        string = string.replace("[", "");
        string = string.replace("]", "");
        string = string.replace(",", "");
        string = string.replace(" ", "");
        clue.setText(string);
        clue.setLineSpacing(1);
        clue.setTranslateX((col * 30) + 20 + (12.5 * controller.getRowCluesLength()));
        clue.setTranslateY(10);
        clue.setWrappingWidth(1);
        root.getChildren().add(clue);
      }

      // Add buttons and success message
      if (controller.isSolved()) {
        Text success = new Success().render(controller);
        root.getChildren().add(success);
      }

      Button previous = new Previous().render(controller);
      Button reset = new Reset().render(controller);
      Button next = new Next().render(controller);
      Button random = new RandomPuzzle().render(controller);
      Text puzzle = new PuzzleIndex().render(controller);

      root.getChildren().add(previous);
      root.getChildren().add(random);
      root.getChildren().add(next);
      root.getChildren().add(reset);
      root.getChildren().add(puzzle);
      root.setAlignment(Pos.TOP_LEFT);
      return root;
    }
  }

  private class Success extends Text {
    Text render(Controller control) {
      Text text = new Text();
      text.setText("Puzzle Solved!");
      text.setTranslateY(450);
      text.setTranslateX(150);
      text.setFont(Font.font("verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 20));
      return text;
    }
  }

  private class Previous extends Button {
    Button render(Controller control) {
      Button previous = new Button();
      previous.setText("Previous Puzzle");
      previous.setTranslateY(400);
      previous.setTranslateX(50);
      previous.setOnMouseClicked(
          new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
              control.prevPuzzle();
            }
          });
      return previous;
    }
  }

  private class Next extends Button {
    Button render(Controller control) {
      Button next = new Button();
      next.setText("Next Puzzle");
      next.setTranslateY(400);
      next.setTranslateX(175);
      next.setOnMouseClicked(
          new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
              control.nextPuzzle();
            }
          });
      return next;
    }
  }

  private class Reset extends Button {
    Button render(Controller control) {
      Button reset = new Button();
      reset.setText("Reset Puzzle");
      reset.setTranslateY(400);
      reset.setTranslateX(275);
      reset.setOnMouseClicked(
          new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
              control.clearBoard();
            }
          });
      return reset;
    }
  }

  private class PuzzleIndex extends Text {
    Text render(Controller control) {
      Text puzzle_index = new Text();
      puzzle_index.setText(
          "Puzzle " + (control.getPuzzleIndex() + 1) + " of " + control.getPuzzleCount());
      puzzle_index.setTranslateX(400);
      return puzzle_index;
    }
  }

  private class RandomPuzzle extends Button {
    Button render(Controller control) {
      Button random = new Button();
      random.setTranslateX(385);
      random.setTranslateY(400);
      random.setText("Random Puzzle");
      random.setOnMouseClicked(
          new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
              control.randPuzzle();
            }
          });
      return random;
    }
  }
}

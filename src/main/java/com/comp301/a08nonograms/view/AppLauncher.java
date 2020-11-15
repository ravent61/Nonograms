package com.comp301.a08nonograms.view;

import com.comp301.a08nonograms.PuzzleLibrary;
import com.comp301.a08nonograms.controller.Controller;
import com.comp301.a08nonograms.controller.ControllerImpl;
import com.comp301.a08nonograms.model.Clues;
import com.comp301.a08nonograms.model.Model;
import com.comp301.a08nonograms.model.ModelImpl;
import com.comp301.a08nonograms.model.ModelObserver;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

public class AppLauncher extends Application {

  @Override
  public void start(Stage stage) {
      // TODO: Launch your GUI here
      Model model = new ModelImpl(PuzzleLibrary.create());
      Controller controller = new ControllerImpl(model);
      stage.setTitle("Raven's Nonograms!");
      Board board = new Board(model, controller);
      stage.setScene(new Scene(board.render()));
      ModelObserver observer = new ModelObserver() {
          @Override
          public void update(Model model) {
              System.out.println("something changed");
              System.out.println(model.isSolved());
          }
      };
      model.addObserver(observer);
      //this is a comment
      stage.show();

  }

  class Board implements FXComponent {
      Model model;
      Controller controller;
      public Board(Model model, Controller controller) {
          this.model = model;
          this.controller = controller;
      }
      @Override
      public Parent render() {
          GridPane root = new GridPane();
          root.setPrefSize(500, 500);
          for (int col = 0; col < model.getWidth(); col++) {
              for (int row = 0; row < model.getHeight(); row++) {
                  Button cell = new Button();
                  cell.setBackground(Background.EMPTY);
                  cell.setStyle("-fx-border-color: black");
                  cell.setPrefSize(30, 30);
                  int finalRow = row;
                  int finalCol = col;
                  cell.setOnMouseClicked(
                          new EventHandler<MouseEvent>() {
                              @Override
                              public void handle(MouseEvent mouseEvent) {
                                  if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                                      if (!controller.isShaded(finalRow, finalCol)){
                                          cell.setStyle("-fx-background-color: pink");
                                          controller.toggleShaded(finalRow, finalCol);

                                      }else{
                                          cell.setBackground(Background.EMPTY);
                                          cell.setStyle("-fx-border-color: black");
                                          controller.toggleSpace(finalRow, finalCol);
                                      }
                                  }
                                  if (mouseEvent.getButton() == MouseButton.SECONDARY){
                                      if (!controller.isEliminated(finalRow, finalCol)){
                                          controller.toggleEliminated(finalRow, finalCol);
                                          cell.setStyle("-fx-background-color: red");
                                      }else{
                                          cell.setBackground(Background.EMPTY);
                                          cell.setStyle("-fx-border-color: black");
                                          controller.toggleSpace(finalRow, finalCol);
                                      }
                                  }
                              }
                          });
                  cell.setTranslateX((col * 30) + 50);
                  cell.setTranslateY((row * 30) + 50);
                  root.getChildren().add(cell);
              }
          }
          Clues clues = controller.getClues();
          for (int row = 0; row < model.getHeight(); row++) {
              Text clue = new Text();
              int [] rowClue = clues.getRowClues(row);
              String string = Arrays.toString(rowClue);
              string = string.replace("[", "");
              string = string.replace("]", "");
              string = string.replace(",", "");
              string = string.replace("", " ");
              clue.setText(string);
              clue.setTranslateY(row * 30 + 50);
              root.getChildren().add(clue);
          }
          for(int col = 0; col < model.getWidth(); col++) {
              Text clue = new Text();
              int [] colClue = clues.getColClues(col);
              String string = Arrays.toString(colClue);
              string = string.replace("[", "");
              string = string.replace("]", "");
              string = string.replace(",", "");
              string = string.replace(" ", "");
              clue.setText(string);
              clue.setLineSpacing(1);
              clue.setTranslateX(col * 30 + 55);
              clue.setTranslateY(10);
              clue.setWrappingWidth(1);
              root.getChildren().add(clue);
          }

          return root;
      }
  }



}

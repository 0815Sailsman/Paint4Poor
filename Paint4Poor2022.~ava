import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.control.ColorPicker;
import java.io.*;
import javax.imageio.ImageIO;
import javafx.scene.image.*;
import java.nio.ByteBuffer;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.event.Event;


public class Paint4Poor2022 extends Application {
  private Pane root;
  ColorPicker colorPicker = new ColorPicker();
  private Leinwand leinwand = new Leinwand(16, 16, 30, colorPicker);
  private Button save_button = new Button();
  private Button load_button = new Button();
  
  public void start(Stage primaryStage) { 
    root = new Pane();
    Scene scene = new Scene(root, 700, 508);
    // Anfang Komponenten
    save_button.setOnAction(
    (event) -> {save_button_action(event);} 
    );
    save_button.setText("Save Image to file");
    save_button.setLayoutX(550);
    save_button.setLayoutY(100);
    root.getChildren().add(save_button);

    load_button.setOnAction(
    (event) -> {load_button_action(event);} 
    );
    load_button.setText("Load Image from file");
    load_button.setLayoutX(550);
    load_button.setLayoutY(200);
    root.getChildren().add(load_button);
    
    colorPicker.setLayoutX(550);
    colorPicker.setLayoutY(50);
    root.getChildren().add(colorPicker);
    // Ende Komponenten
    
    // Paint the buttons onto the pane
    leinwand.draw_to(root);
    
    primaryStage.setOnCloseRequest(e -> System.exit(0));
    primaryStage.setTitle("Paint4Poor2022");
    primaryStage.setScene(scene);
    primaryStage.show();
  }   
  // Anfang Methoden
  
  // Hauptprogramm
  public static void main(String[] args) {
    launch(args);
  } 

  public void save_button_action(Event evt) {
    WritableImage temp = new WritableImage(leinwand.leinwand.length, leinwand.leinwand[0].length);
    for (int y=0; y<leinwand.leinwand.length; y++) {
      for (int x=0; x<leinwand.leinwand[0].length; x++) {
        temp.getPixelWriter().setColor(x, y, leinwand.leinwand[x][y].getFarbe());
      }
    }
    Image actual = new ImageView(temp).getImage();
    File outputFile = new File("saved.png");
    BufferedImage bImage = SwingFXUtils.fromFXImage(actual, null);
    try {
      ImageIO.write(bImage, "png", outputFile);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  public void load_button_action(Event evt) {
    System.out.println("Hello World");
  }  
  // Ende Methoden

} 


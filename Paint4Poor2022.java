import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;
import java.util.*;

public class Paint4Poor2022 extends Application {
  private Pane root;
  private Stage mainStage;
  ColorPicker colorPicker = new ColorPicker();
  private Leinwand leinwand = new Leinwand(16, 48, colorPicker);
  private Button save_button = new Button();
  private Button load_button = new Button();
  private Button invert_colors_button = new Button();
  private Button delete_all_pixels_button = new Button();
  private Button new_canvas_button = new Button();
  private MenuButton filetype_button = new MenuButton("Select Filetype");
  private String selected_filetype = ".png";
  private int comps_in_pane;
  
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
    load_button.setLayoutY(150);
    root.getChildren().add(load_button);
    
    filetype_button.setLayoutX(550);
    filetype_button.setLayoutY(200);
    EventHandler<ActionEvent> menuItemEvent = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            selected_filetype = ((MenuItem)e.getSource()).getText();
            System.out.println(selected_filetype);
        }
    };
    MenuItem pngMenu = new MenuItem(".png");
    MenuItem jpgMenu = new MenuItem(".jpg");
    pngMenu.setOnAction(menuItemEvent);
    jpgMenu.setOnAction(menuItemEvent);
    filetype_button.getItems().addAll(pngMenu, jpgMenu);
    root.getChildren().add(filetype_button);
    
    colorPicker.setLayoutX(550);
    colorPicker.setLayoutY(50);
    root.getChildren().add(colorPicker);
    
    invert_colors_button.setOnAction(
    (event) -> {invert_colors_button_action(event);} 
    );
    invert_colors_button.setText("Invert all colors");
    invert_colors_button.setLayoutX(550);
    invert_colors_button.setLayoutY(250);
    root.getChildren().add(invert_colors_button);
    
    delete_all_pixels_button.setOnAction(
    (event) -> {delete_all_pixels_button_action(event);} 
    );
    delete_all_pixels_button.setText("Clear");
    delete_all_pixels_button.setLayoutX(550);
    delete_all_pixels_button.setLayoutY(300);
    root.getChildren().add(delete_all_pixels_button);
    
    new_canvas_button.setOnAction(
    (event) -> {new_canvas_button_action(event);} 
    );
    new_canvas_button.setText("New");
    new_canvas_button.setLayoutX(550);
    new_canvas_button.setLayoutY(350);
    root.getChildren().add(new_canvas_button);
    // Ende Komponenten
    
    comps_in_pane = root.getChildren().size();
    
    // Paint the buttons onto the pane
    leinwand.draw_to(root);
    
    primaryStage.setOnCloseRequest(e -> System.exit(0));
    primaryStage.setTitle("Paint4Poor2022");
    primaryStage.setScene(scene);
    primaryStage.show();
    
    mainStage = primaryStage;
  }   
  // Anfang Methoden
  
  // Hauptprogramm
  public static void main(String[] args) {
    launch(args);
  } 

  public void save_button_action(Event evt) {
    WritableImage temp = new WritableImage(leinwand.leinwand[0].length, leinwand.leinwand.length);
    for (int y=0; y<(leinwand.leinwand.length); y++) {
      for (int x=0; x<(leinwand.leinwand[y].length); x++) {
        try {
          temp.getPixelWriter().setColor(x, y, leinwand.leinwand[y][x].getFarbe());
        } catch(Exception e) {
          System.out.println("Index Error accessing data x:" + x + " y:" + y + "\n Even though height is " + leinwand.leinwand.length + " and width " + leinwand.leinwand[y].length);
        }
      }
    }
    Image actual = new ImageView(temp).getImage();
    File outputFile = new File("saved" + selected_filetype);
    System.out.println(outputFile.getName());
    BufferedImage bImage = SwingFXUtils.fromFXImage(actual, null);
    try {
      ImageIO.write(bImage, selected_filetype.substring(1), outputFile);
    } catch (IOException e) {
      System.out.println("Error saving");
      throw new RuntimeException(e);
    }
  }
  
  public void load_button_action(Event evt) {
    delete_all_pixels();
    System.out.println(root.getChildren().size());
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Resource File");
    fileChooser.getExtensionFilters().addAll(
           new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
    File selectedFile = fileChooser.showOpenDialog(mainStage);
    if (selectedFile != null) {
      System.out.println(selectedFile.getName());
      // Load file into Image object
      Image loaded_img = new Image(selectedFile.toURI().toString());      
      leinwand.load(loaded_img);
      leinwand.draw_to(root);
    }
  }  
  
  public void invert_colors_button_action(Event evt) {
    leinwand.invert();
  }
  
  public void delete_all_pixels_button_action(Event evt) {
    delete_all_pixels();
  }
  
  public void delete_all_pixels() {
    if (root.getChildren().size() > comps_in_pane) {
     root.getChildren().remove(comps_in_pane+1, root.getChildren().size()); 
    }
  }
  
  public void new_canvas_button_action(Event evt) {
    int height;
    int width;
    Dialog<int[]> dialog = new Dialog<>();
    dialog.setTitle("New Canvas");
    dialog.setHeaderText("Enter the dimensions of the new canvas. \n");
    dialog.setResizable(true);
     
    Label label1 = new Label("Height: ");
    Label label2 = new Label("Width: ");
    TextField nr1 = new TextField();
    TextField nr2 = new TextField();
             
    GridPane grid = new GridPane();
    grid.add(label1, 1, 1);
    grid.add(nr1, 2, 1);
    grid.add(label2, 1, 2);
    grid.add(nr2, 2, 2);
    dialog.getDialogPane().setContent(grid);
             
    ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
     

    // Convert the result to a username-password-pair when the login button is clicked.
    dialog.setResultConverter(dialogButton -> {
        if (dialogButton == buttonTypeOk) {
            int[] res = {Integer.parseInt(nr1.getText()), Integer.parseInt(nr2.getText())};
            return res;
            //return new Pair<>(username.getText(), password.getText());
        }
        return null;
    });
             
    Optional<int[]> result = dialog.showAndWait();
             
    if (result.isPresent()) {
        height = result.get()[0];
        width = result.get()[1];
        leinwand = new Leinwand(width, height, colorPicker);
        leinwand.draw_to(root);
    }
  }

  // Ende Methoden

} 


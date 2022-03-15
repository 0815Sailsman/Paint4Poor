import javafx.scene.control.*;
import javafx.scene.paint.*;
import javafx.event.*;
import javafx.scene.layout.Pane;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.*;


public class Leinwand {

  public Pixel[][] leinwand;
  private String grundStyle = "-fx-border-width: 0;-fx-background-radius: 0;-fx-border-color:LIGHTGRAY;-fx-border-insets: 0;-fx-border-radius: 0;";
  public ColorPicker picker;
  private int linkerRand;
  private int obererRand;
  private final double max_widht = 480.0;
  private final double max_height = 480.0;

  public Leinwand(int spalten, int zeilen, ColorPicker picker) {
    double pixelbreite = this.max_widht / spalten;
    double pixelhöhe = this.max_height / zeilen;
    this.linkerRand = 10;
    this.obererRand = 10;
    String pixelStyle;
    this.leinwand = new Pixel[spalten][zeilen];
    for (int y = 0; y < zeilen; y++) {
      for (int x = 0; x < spalten; x++) {
        this.leinwand[x][y] = new Pixel(x, y);
        this.leinwand[x][y].setLayoutX(this.linkerRand + x * pixelbreite);
        this.leinwand[x][y].setLayoutY(this.obererRand + y * pixelhöhe);
        this.leinwand[x][y].setPrefHeight(pixelbreite);
        this.leinwand[x][y].setPrefWidth(pixelbreite);
        pixelStyle = this.grundStyle + "-fx-background-color: #" + leinwand[x][y].getFarbe().toString().substring(2)+";";
        this.leinwand[x][y].setStyle(pixelStyle);                      
        this.leinwand[x][y].setOnAction(
        (event) -> {this.leinwand_Action(event);} 
        );
      }
    }
    this.picker = picker;
    picker.setValue(Color.BLACK);
  }
  
  // wenn irgendein Button der Leinwand gedrückt wird
  public void leinwand_Action(Event evt) {
    int x = ((Pixel) evt.getSource()).getX();
    int y = ((Pixel) evt.getSource()).getY();
    this.leinwand[x][y].setFarbe(this.picker.getValue());
    this.leinwand[x][y].setStyle(this.grundStyle + "-fx-background-color: #" + leinwand[x][y].getFarbe().toString().substring(2)+";"); 
  } // end of button1_Action
  
  public void draw_to(Pane root) {
    for (int i=0; i<(this.leinwand.length); i++) {
      System.out.println("" + i);
      for (int j=0; j<(this.leinwand[i].length); j++) {
        try {
         root.getChildren().add(this.leinwand[i][j]); 
        } catch(Exception e) {
          System.out.println("Index Error for x:" + i + " y:" + j);
        }
      }
      System.out.println("Done");
    }
    System.out.println("Drawing complete");
  }
  
  public void load(Image img) {    
    double pixelbreite = this.max_widht / img.getWidth();
    double pixelhöhe = this.max_height / img.getHeight();
    String pixelStyle;
    this.leinwand = new Pixel[(int) img.getHeight()][(int) img.getWidth()]; 
    System.out.println(this.leinwand.length);
    System.out.println(this.leinwand[0].length);  
    for (int y=0; y<(int) img.getHeight(); y++) {
      System.out.println(y);
      for (int x=0; x<(int) img.getWidth(); x++) {
        this.leinwand[y][x] = new Pixel(y, x);
        this.leinwand[y][x].setFarbe(img.getPixelReader().getColor(x, y));
        this.leinwand[y][x].setLayoutX(this.linkerRand + x * pixelbreite);
        this.leinwand[y][x].setLayoutY(this.obererRand + y * pixelhöhe);
        this.leinwand[y][x].setPrefHeight(pixelbreite);
        this.leinwand[y][x].setPrefWidth(pixelbreite);
        pixelStyle = this.grundStyle + "-fx-background-color: #" + leinwand[y][x].getFarbe().toString().substring(2)+";";
        this.leinwand[y][x].setStyle(pixelStyle);                      
        this.leinwand[y][x].setOnAction(
        (event) -> {this.leinwand_Action(event);} 
        );
      }
    }
  }
}


import javafx.scene.control.*;
import javafx.scene.paint.*;
import javafx.event.*;
import javafx.scene.layout.Pane;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.*;
import java.lang.Math.*;


public class Leinwand {

  public Pixel[][] leinwand;
  private String grundStyle = "-fx-border-width: 0;-fx-background-radius: 0;-fx-border-color:LIGHTGRAY;-fx-border-insets: 0;-fx-border-radius: 0;";
  public ColorPicker picker;
  private int linkerRand;
  private int obererRand;
  private final double max_widht = 480.0;
  private final double max_height = 480.0;
  private boolean override_picker = false;
  public Color last_color = null;

  public Leinwand(int spalten, int zeilen, ColorPicker picker) {
    double pixelbreite = this.max_widht / spalten;
    double pixelh�he = this.max_height / zeilen;
    this.linkerRand = 10;
    this.obererRand = 10;
    String pixelStyle;
    this.leinwand = new Pixel[zeilen][spalten];
    for (int y = 0; y < zeilen; y++) {
      for (int x = 0; x < spalten; x++) {
        this.leinwand[y][x] = new Pixel(x, y);
        this.leinwand[y][x].setLayoutX(this.linkerRand + x * pixelbreite);
        this.leinwand[y][x].setLayoutY(this.obererRand + y * pixelh�he);
        this.leinwand[y][x].setPrefHeight(pixelh�he);
        this.leinwand[y][x].setPrefWidth(pixelbreite);
        pixelStyle = this.grundStyle + "-fx-background-color: #" + leinwand[y][x].getFarbe().toString().substring(2)+";";
        this.leinwand[y][x].setStyle(pixelStyle);                      
        this.leinwand[y][x].setOnAction(
        (event) -> {this.leinwand_Action(event);} 
        );
      }
    }
    this.picker = picker;
    picker.setValue(Color.BLACK);
  }
  
  // wenn irgendein Button der Leinwand gedr�ckt wird
  public void leinwand_Action(Event evt) {
    int x = ((Pixel) evt.getSource()).getX();
    int y = ((Pixel) evt.getSource()).getY();
    if (!override_picker) {
      this.leinwand[y][x].setFarbe(this.picker.getValue());
      this.leinwand[y][x].setStyle(this.grundStyle + "-fx-background-color: #" + this.leinwand[y][x].getFarbe().toString().substring(2)+";");       
    } else {
      picker.setValue(this.leinwand[y][x].getFarbe()); 
      toggle_picker();    
      }
  } // end of button1_Action
  
  public void draw_to(Pane root) {
    for (int i=0; i<(this.leinwand.length); i++) {
      for (int j=0; j<(this.leinwand[i].length); j++) {
        try {
         root.getChildren().add(this.leinwand[i][j]); 
        } catch(Exception e) {
          System.out.println("Index Error for x:" + i + " y:" + j);
        }
      }
    }
  }
  
  public void load(Image img) {    
    double pixelbreite = this.max_widht / img.getWidth();
    double pixelh�he = this.max_height / img.getHeight();
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
        this.leinwand[y][x].setLayoutY(this.obererRand + y * pixelh�he);
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
  
  public void invert() {
   for (int y=0; y<leinwand.length; y++) {
     for (int x=0; x<leinwand[y].length; x++) {
       leinwand[y][x].setFarbe(leinwand[y][x].getFarbe().invert());
       this.leinwand[y][x].setStyle(this.grundStyle + "-fx-background-color: #" + leinwand[y][x].getFarbe().toString().substring(2)+";");
     }
   } 
  }
  
  public void turn_90() {
    Pixel[][] temp = new Pixel[this.leinwand[0].length][this.leinwand.length];
    double pixelbreite = this.max_widht / temp[0].length;
    double pixelh�he = this.max_height / temp.length;
    String pixelStyle;
      
    for (int x=0; x<this.leinwand[0].length; x++) {
      for (int y=this.leinwand.length-1; y>=0; y--) {
        temp[x][y] = new Pixel(y, x);
        temp[x][y].setLayoutX(this.linkerRand + y * pixelbreite);
        temp[x][y].setLayoutY(this.obererRand + x * pixelh�he);
        temp[x][y].setPrefHeight(pixelh�he);
        temp[x][y].setPrefWidth(pixelbreite);
        pixelStyle = this.grundStyle + "-fx-background-color: #" + this.leinwand[Math.abs((y-this.leinwand.length)+1)][x].getFarbe().toString().substring(2)+";";
        temp[x][y].setStyle(pixelStyle);                      
        temp[x][y].setFarbe(this.leinwand[Math.abs((y-this.leinwand.length)+1)][x].getFarbe());
        temp[x][y].setOnAction(
        (event) -> {this.leinwand_Action(event);} 
        );
      }
    }
    this.leinwand = temp;
  }
  
  public void mirror_along_y() {
    Pixel[][] temp = new Pixel[this.leinwand.length][this.leinwand[0].length];
    double pixelbreite = this.max_widht / temp[0].length;
    double pixelh�he = this.max_height / temp.length;
    String pixelStyle;
      
    for (int y=0; y<this.leinwand.length; y++) {
      for (int x=this.leinwand[0].length-1; x>=0; x--) {
        temp[y][x] = new Pixel(x, y);
        temp[y][x].setLayoutX(this.linkerRand + x * pixelbreite);
        temp[y][x].setLayoutY(this.obererRand + y * pixelh�he);
        temp[y][x].setPrefHeight(pixelh�he);
        temp[y][x].setPrefWidth(pixelbreite);
        pixelStyle = this.grundStyle + "-fx-background-color: #" + this.leinwand[y][Math.abs(x-this.leinwand[0].length+1)].getFarbe().toString().substring(2)+";";
        temp[y][x].setStyle(pixelStyle);                      
        temp[y][x].setFarbe(this.leinwand[y][Math.abs(x-this.leinwand[0].length+1)].getFarbe());
        temp[y][x].setOnAction(
        (event) -> {this.leinwand_Action(event);} 
        );
      }
    }
    this.leinwand = temp;
  }
  
  public void mirror_along_x() {
    Pixel[][] temp = new Pixel[this.leinwand.length][this.leinwand[0].length];
    double pixelbreite = this.max_widht / temp[0].length;
    double pixelh�he = this.max_height / temp.length;
    String pixelStyle;
      
    for (int x=0; x<this.leinwand[0].length; x++) {
      for (int y=this.leinwand.length-1; y>=0; y--) {
        temp[y][x] = new Pixel(x, y);
        temp[y][x].setLayoutX(this.linkerRand + x * pixelbreite);
        temp[y][x].setLayoutY(this.obererRand + y * pixelh�he);
        temp[y][x].setPrefHeight(pixelh�he);
        temp[y][x].setPrefWidth(pixelbreite);
        pixelStyle = this.grundStyle + "-fx-background-color: #" + this.leinwand[Math.abs(y-this.leinwand.length+1)][x].getFarbe().toString().substring(2)+";";
        temp[y][x].setStyle(pixelStyle);                      
        temp[y][x].setFarbe(this.leinwand[Math.abs(y-this.leinwand.length+1)][x].getFarbe());
        temp[y][x].setOnAction(
        (event) -> {this.leinwand_Action(event);} 
        );
      }
    }
    this.leinwand = temp;
  }
  
  public void toggle_picker() {
    if (override_picker) {
      override_picker = false;
    } else {
      override_picker = true;
    }
  }

}


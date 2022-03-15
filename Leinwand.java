import javafx.scene.control.*;
import javafx.scene.paint.*;
import javafx.event.*;
import javafx.scene.layout.Pane;
import javafx.scene.control.ColorPicker;

public class Leinwand {

  public Pixel[][] leinwand;
  private String grundStyle = "-fx-border-width: 0;-fx-background-radius: 0;-fx-border-color:LIGHTGRAY;-fx-border-insets: 0;-fx-border-radius: 0;";
  public ColorPicker picker;

  public Leinwand(int spalten, int zeilen, int pixelbreite, ColorPicker picker) {
    int linkerRand = 10;
    int obererRand = 10;
    String pixelStyle;
    this.leinwand = new Pixel[spalten][zeilen];
    for (int y = 0; y < zeilen; y++) {
      for (int x = 0; x < spalten; x++) {
        this.leinwand[x][y] = new Pixel(x, y);
        this.leinwand[x][y].setLayoutX(linkerRand + x * pixelbreite);
        this.leinwand[x][y].setLayoutY(obererRand + y * pixelbreite);
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
    for (int i=0; i<this.leinwand.length; i++) {
      for (int j=0; j<this.leinwand[i].length; j++) {
        root.getChildren().add(this.leinwand[j][i]);
      }
    }
  }
}


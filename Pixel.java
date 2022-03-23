import javafx.scene.control.*;
import javafx.scene.paint.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Pixel extends Button {
  
  private int posX;
  private int posY;
  private Color farbe;
  private String grund_style = "-fx-border-width: 0;-fx-background-radius: 0;-fx-border-color:LIGHTGRAY;-fx-border-insets: 0;-fx-border-radius: 0;";
  private ImageView selected_iv;
  public boolean selected = false;

  public Pixel(int spalte, int zeile) {
    super();
    this.posX = spalte;
    this.posY = zeile; 
    this.farbe = Color.WHITE;
    this.selected_iv = new ImageView(new Image("selected-img.png"));
  }
  
  public int getX() {
    return this.posX;
  }
  
  public int getY() {
    return this.posY;
  }
  
  public void setFarbe(Color f) {
    this.farbe = f;  
  }
  
  public Color getFarbe() {
    return this.farbe;  
  }
  
  public void set_selected(boolean new_value) {
    if (!new_value) {
      this.selected = false;
      // Change button visu to color
      this.setGraphic(null);
      String pixelStyle = this.grund_style + "-fx-background-color: #" + this.getFarbe().toString().substring(2)+";";
      this.setStyle(pixelStyle);
    } else {
      this.selected = true;
      // Change button visu to "selected-img"
      this.selected_iv.setFitHeight(this.getHeight());
      this.selected_iv.setFitWidth(this.getWidth());
      this.setGraphic(this.selected_iv);
    }
  }
}

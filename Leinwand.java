import javafx.scene.control.*;
import javafx.scene.paint.*;
import javafx.event.*;
import javafx.scene.layout.Pane;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.*;
import java.lang.Math.*;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;


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
  private Pixel drag_start_pixel;
  private Pixel drag_end_pixel;
  private boolean any_selected = false;
  private int selection_mode = SelectionMode.SELECT_MODE;

  public Leinwand(int spalten, int zeilen, ColorPicker picker) {
    double pixelbreite = this.max_widht / spalten;
    double pixelhöhe = this.max_height / zeilen;
    this.linkerRand = 10;
    this.obererRand = 10;
    String pixelStyle = "";
    this.leinwand = new Pixel[zeilen][spalten];
    for (int y = 0; y < zeilen; y++) {
      for (int x = 0; x < spalten; x++) {
        Pixel tmpxl = new Pixel(x, y);
        tmpxl.setLayoutX(this.linkerRand + x * pixelbreite);
        tmpxl.setLayoutY(this.obererRand + y * pixelhöhe);
        tmpxl.setPrefHeight(pixelhöhe);
        tmpxl.setPrefWidth(pixelbreite);
        pixelStyle = this.grundStyle + "-fx-background-color: #" + tmpxl.getFarbe().toString().substring(2)+";";
        tmpxl.setStyle(pixelStyle);                      
        tmpxl.setOnAction(
        (event) -> {this.leinwand_Action(event);} 
        );
        apply_drag_events_to(tmpxl);
        this.leinwand[y][x] = tmpxl;
      }
    }
    this.picker = picker;
    picker.setValue(Color.BLACK);
  }
  
  // wenn irgendein Button der Leinwand gedrückt wird
  public void leinwand_Action(Event evt) {
    int x = ((Pixel) evt.getSource()).getX();
    int y = ((Pixel) evt.getSource()).getY();
    unselect_all();
    if (!override_picker) {
      this.leinwand[y][x].setFarbe(this.picker.getValue());
      this.leinwand[y][x].setStyle(this.grundStyle + "-fx-background-color: #" + this.leinwand[y][x].getFarbe().toString().substring(2)+";");       
    } else {
      picker.setValue(this.leinwand[y][x].getFarbe()); 
      toggle_picker();    
      }
  }
  
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
    double pixelhöhe = this.max_height / img.getHeight();
    String pixelStyle;
    this.leinwand = new Pixel[(int) img.getHeight()][(int) img.getWidth()]; 
    System.out.println(this.leinwand.length);
    System.out.println(this.leinwand[0].length);  
    for (int y=0; y<(int) img.getHeight(); y++) {
      System.out.println(y);
      for (int x=0; x<(int) img.getWidth(); x++) {
        Pixel tmpxl = new Pixel(y, x);
        tmpxl.setFarbe(img.getPixelReader().getColor(x, y));
        tmpxl.setLayoutX(this.linkerRand + x * pixelbreite);
        tmpxl.setLayoutY(this.obererRand + y * pixelhöhe);
        tmpxl.setPrefHeight(pixelbreite);
        tmpxl.setPrefWidth(pixelbreite);
        pixelStyle = this.grundStyle + "-fx-background-color: #" + tmpxl.getFarbe().toString().substring(2)+";";
        tmpxl.setStyle(pixelStyle);                      
        tmpxl.setOnAction(
        (event) -> {this.leinwand_Action(event);} 
        );
        apply_drag_events_to(tmpxl);
        this.leinwand[y][x] = tmpxl;
      }
    }
  }
  
  public void invert() {
    unselect_all();
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
    double pixelhöhe = this.max_height / temp.length;
    String pixelStyle;
      
    for (int x=0; x<this.leinwand[0].length; x++) {
      for (int y=this.leinwand.length-1; y>=0; y--) {
        Pixel tmpxl = new Pixel(y, x);
        tmpxl.setLayoutX(this.linkerRand + y * pixelbreite);
        tmpxl.setLayoutY(this.obererRand + x * pixelhöhe);
        tmpxl.setPrefHeight(pixelhöhe);
        tmpxl.setPrefWidth(pixelbreite);
        pixelStyle = this.grundStyle + "-fx-background-color: #" + this.leinwand[Math.abs((y-this.leinwand.length)+1)][x].getFarbe().toString().substring(2)+";";
        tmpxl.setStyle(pixelStyle);                      
        tmpxl.setFarbe(this.leinwand[Math.abs((y-this.leinwand.length)+1)][x].getFarbe());
        tmpxl.setOnAction(
        (event) -> {this.leinwand_Action(event);} 
        );
        apply_drag_events_to(tmpxl);
        temp[x][y] = tmpxl;
      }
    }
    this.leinwand = temp;
  }
  
  public void mirror_along_y() {
    Pixel[][] temp = new Pixel[this.leinwand.length][this.leinwand[0].length];
    double pixelbreite = this.max_widht / temp[0].length;
    double pixelhöhe = this.max_height / temp.length;
    String pixelStyle;
      
    for (int y=0; y<this.leinwand.length; y++) {
      for (int x=this.leinwand[0].length-1; x>=0; x--) {
        Pixel tmpxl = new Pixel(x, y);
        tmpxl.setLayoutX(this.linkerRand + x * pixelbreite);
        tmpxl.setLayoutY(this.obererRand + y * pixelhöhe);
        tmpxl.setPrefHeight(pixelhöhe);
        tmpxl.setPrefWidth(pixelbreite);
        pixelStyle = this.grundStyle + "-fx-background-color: #" + this.leinwand[y][Math.abs(x-this.leinwand[0].length+1)].getFarbe().toString().substring(2)+";";
        tmpxl.setStyle(pixelStyle);                      
        tmpxl.setFarbe(this.leinwand[y][Math.abs(x-this.leinwand[0].length+1)].getFarbe());
        tmpxl.setOnAction(
        (event) -> {this.leinwand_Action(event);} 
        );
        apply_drag_events_to(tmpxl);
        temp[y][x] = tmpxl;
      }
    }
    this.leinwand = temp;
  }
  
  public void mirror_along_x() {
    Pixel[][] temp = new Pixel[this.leinwand.length][this.leinwand[0].length];
    double pixelbreite = this.max_widht / temp[0].length;
    double pixelhöhe = this.max_height / temp.length;
    String pixelStyle;
      
    for (int x=0; x<this.leinwand[0].length; x++) {
      for (int y=this.leinwand.length-1; y>=0; y--) {
        Pixel tmpxl = new Pixel(x, y);
        tmpxl.setLayoutX(this.linkerRand + x * pixelbreite);
        tmpxl.setLayoutY(this.obererRand + y * pixelhöhe);
        tmpxl.setPrefHeight(pixelhöhe);
        tmpxl.setPrefWidth(pixelbreite);
        pixelStyle = this.grundStyle + "-fx-background-color: #" + this.leinwand[Math.abs(y-this.leinwand.length+1)][x].getFarbe().toString().substring(2)+";";
        tmpxl.setStyle(pixelStyle);                      
        tmpxl.setFarbe(this.leinwand[Math.abs(y-this.leinwand.length+1)][x].getFarbe());
        tmpxl.setOnAction(
        (event) -> {this.leinwand_Action(event);} 
        );
        apply_drag_events_to(tmpxl);
        temp[y][x] = tmpxl;
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
  
  public void select_area() {
    System.out.println("from " + drag_start_pixel.getX() + " " + drag_start_pixel.getY()); 
    System.out.println("to " + drag_end_pixel.getX() + " " + drag_end_pixel.getY());
    Pixel leftmost;
    Pixel rightmost;
    Pixel top;
    Pixel bot;
    
    // Sort for x
    if (drag_start_pixel.getX() < drag_end_pixel.getX()) {
      leftmost = drag_start_pixel;
      rightmost = drag_end_pixel;
    } else {
      rightmost = drag_start_pixel;
      leftmost = drag_end_pixel;
    }
    // Sort for y
    if (drag_start_pixel.getY() < drag_end_pixel.getY()) {
      top = drag_start_pixel;
      bot = drag_end_pixel;
    } else {
      bot = drag_start_pixel;
      top = drag_end_pixel;
    }
    
    for (int y=top.getY(); y<=bot.getY(); y++) {
      for (int x=leftmost.getX(); x<=rightmost.getX(); x++) {
        any_selected = true;
        this.leinwand[y][x].set_selected(true);
      }
    }
  }
  
  public void unselect_all() {
    any_selected = false;
    for (int y = 0; y<this.leinwand.length; y++) {
      for (int x = 0; x<this.leinwand[y].length; x++) {
        this.leinwand[y][x].set_selected(false);
      }
    }
  }
  
  public void apply_drag_events_to(Pixel pxl) {        
    // Initiate drag, used for keeping track of original Pixel
    pxl.setOnMouseDragged(new EventHandler<MouseEvent>() { 
      public void handle(MouseEvent evt) { 
        if (selection_mode == SelectionMode.SELECT_MODE) {
          System.out.println("Hello Drag"); 
          System.out.println(((Pixel) evt.getSource()).getX());  
          System.out.println(((Pixel) evt.getSource()).getY());
          drag_start_pixel = pxl;
        } else if (selection_mode == SelectionMode.PAINT_MODE) {
            unselect_all();
            pxl.setFarbe(picker.getValue());
            pxl.setStyle(grundStyle + "-fx-background-color: #" + pxl.getFarbe().toString().substring(2)+";");
        }
      } 
    });
    
    // Necessary for somehow "keeping the drag alive"
    pxl.setOnDragDetected(new EventHandler<MouseEvent>() { 
      public void handle(MouseEvent evt) { 
        pxl.startFullDrag();
        if (selection_mode == SelectionMode.SELECT_MODE) {
          System.out.println("Drag with select");  
        } else if (selection_mode == SelectionMode.PAINT_MODE) {
            pxl.setFarbe(picker.getValue());
            pxl.setStyle(grundStyle + "-fx-background-color: #" + pxl.getFarbe().toString().substring(2)+";");
          }
      } 
    });
    
    // Get the nodes (coordinates of them) that are being passed over during
    // during the movement
    pxl.setOnMouseDragOver(new EventHandler<MouseEvent>() { 
      public void handle(MouseEvent evt) {
        if (selection_mode == SelectionMode.SELECT_MODE) {
          System.out.println("Drag Over"); 
          System.out.println(((Pixel) evt.getSource()).getX());  
          System.out.println(((Pixel) evt.getSource()).getY());
        } else if (selection_mode == SelectionMode.PAINT_MODE) {
            pxl.setFarbe(picker.getValue());
            pxl.setStyle(grundStyle + "-fx-background-color: #" + pxl.getFarbe().toString().substring(2)+";");
          }
      } 
    });
    
    // On drag stop, get final x and y from here (or just get node directly)
    pxl.setOnMouseDragReleased(new EventHandler<MouseEvent>() { 
      public void handle(MouseEvent evt) { 
        if (selection_mode == SelectionMode.SELECT_MODE) {
          System.out.println("Goodbye Drag"); 
          System.out.println(((Pixel) evt.getSource()).getX());  
          System.out.println(((Pixel) evt.getSource()).getY());
          drag_end_pixel = pxl;
          select_area();
        } else if (selection_mode == SelectionMode.PAINT_MODE) {
            pxl.setFarbe(picker.getValue());
            pxl.setStyle(grundStyle + "-fx-background-color: #" + pxl.getFarbe().toString().substring(2)+";");
          }
      } 
    });
  }
  
  public void fill_area() {
    if (any_selected) {
      for (int y = 0; y<this.leinwand.length; y++) {
        for (int x = 0; x<this.leinwand[y].length; x++) {
          if (this.leinwand[y][x].selected) {
            this.leinwand[y][x].setFarbe(picker.getValue());
          }
        }
      }
    }    
  }
  
  public void set_selection_mode(int new_mode) {
    selection_mode = new_mode;
  }
}


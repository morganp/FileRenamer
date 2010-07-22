package filerenamer;

import javax.swing.*;
import java.awt.Color;

//For the ItemListener
import java.awt.event.*;

public class JLabelRule extends JLabel { //implements ItemListener, MouseListener {
   
   final static Color evenLine    = new Color(255,255,255);
   final static Color oddLine     = new Color(240,240,240);
   final static Color highlighted = new Color(160,160,160);
   final static int   labelHeight = 16;
   
   public boolean selected;
   int index;
    
   
   
   public JLabelRule(int index, String text) {
      super(text);
      this.selected = false;
      this.index = index;
      //addMouseListener(this);

      System.out.println("rendering rule " + index);
      reColor();


   }
   
   public int getIndex() {
      return index;
   }
   
   public boolean isSelected() {
      return selected;
   }
   
   public void select(){
      selected = true;
      reColor();
   }

   public void deselect() {
      selected = false;
      reColor();
   }

   public void reColor() {
      this.setOpaque(true);
      if (selected == true) { 
         this.setBackground(highlighted);
      } else if ((this.index%2) == 0) {
       this.setBackground(evenLine);
      } else {
         this.setBackground(oddLine);
      }
   }
   
      
   //#######################
   

        
   
}

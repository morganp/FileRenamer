package filerenamer;

import javax.swing.*;
import java.util.Vector;

//For the Font
import java.awt.Font;

//For the ItemListener
import java.awt.event.*;

public class ViewCurrentRules extends JFrame { // implements MouseListener {
   JPanel      topPanel;
   JScrollPane scrollPanel;
   
   LoadRegularExpressions loadedReqExp;
   Vector rules;
   JLabel title;
   
   Vector  oneRuleJLabel ; //LabelRule

   JPopupMenu popup;

   protected int          lastPositionSelected = -1;
   MouseAdapter[]         newLocationAction;   

   private String minLength(String text, int len) {
     int toAdd = len - text.length();
     String newString = text ;
     for (int i=0; i< toAdd; i++){
         newString = newString + " " ;
      }
      return newString; 
   }

   public ViewCurrentRules(LoadRegularExpressions loadedReqExp) {
      super("FileRenamer | Rules List");
      this.loadedReqExp = loadedReqExp;
      this.setResizable(true);
      this.setSize(500, 300);

      topPanel = new JPanel();
      topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

      title = new JLabel("Current Search and Replace Rules");
      
      scrollPanel = new javax.swing.JScrollPane(topPanel);
      
      //this.addMouseListener(this);
      //topPanel.addMouseListener(this);
      //scrollPanel.addMouseListener(this);
      
      getContentPane().add( scrollPanel, java.awt.BorderLayout.CENTER );
      //this.add(topLevelScrollPane);

      draw();
   }
     
   public void draw() {  
      
      System.out.println("draw()");
      
      //this.rules         = new Vector();
      this.oneRuleJLabel = new Vector();
      
      //Clear Panel 
      topPanel.removeAll();
      topPanel.add(title);    
      
      for (int i=0; i<loadedReqExp.searchList.size() ; i++) {
         FindReplace oneRule = (FindReplace) loadedReqExp.searchList.elementAt(i);
         oneRuleJLabel.add( 
            new JLabelRule(i,"" + minLength(FindReplace.labels[oneRule.mode], 40)  +
               minLength(" Find : "+ oneRule.find, 30) +
               " Replace : "+ oneRule.replace
            )
         );
         
         ((JLabel) oneRuleJLabel.get(i)).setFont(new Font("Courier New", Font.PLAIN, 14));
         //oneRuleJLabel.addMouseListener(this);
         JLabelRule newRule = (JLabelRule) oneRuleJLabel.get(i);

         newRule.addMouseListener(mouseAction);
         topPanel.add( newRule );
                  
         System.out.println("Looping over rule :" + i);
      }
      
      
            
      createPopupMenu();

      

      topPanel.revalidate();
      repaint();   
   } 

   public void createPopupMenu() {
            System.out.println("createPopupMenu called");
            //Create the popup menu.
                     popup       = new javax.swing.JPopupMenu();

           JMenuItem menuUp      = new JMenuItem("Move Up");
           JMenuItem menuEdit    = new JMenuItem("Edit");
           JMenuItem menuDown    = new JMenuItem("Move Down");
      
            menuUp.addActionListener   (actionUp);
            menuEdit.addActionListener (actionEdit);
            menuDown.addActionListener (actionDown);
            
            popup.add(menuUp);
            popup.add(menuEdit);
            popup.add(menuDown);
            //popup.addSeparator();
            
            //Add listener to components that can bring up popup menus.
            MouseListener popupListener = new PopupListener();
            this.addMouseListener(popupListener);
            topPanel.addMouseListener(popupListener);
            scrollPanel.addMouseListener(popupListener);
            
            for (int i=0; i<oneRuleJLabel.size() ;i++) {
               ((JLabelRule) oneRuleJLabel.get(i)).addMouseListener(popupListener);
            }
                  
            // menuEdit.addActionListener(new ActionListener(){
            //                   public void actionPerformed(ActionEvent e){}
            //                   });
            //                 this.addMouseListener(new MouseAdapter(){
            //                   public void mouseReleased(MouseEvent Me){
            //                     if(Me.isPopupTrigger()){
            //                       popup.show(Me.getComponent(), Me.getX(), Me.getY());
            //                     }
            //                   }
            //                 });
            //    
            //Add listener to the text area so the popup menu can come up.
            //MouseListener popupListener = new PopupListener(popup);
            //for (int i=0; i< listOfFilesLabels.length; i++) {
            //   listOfFilesLabels[i].addMouseListener(popupListener);
           //}
           //this.addMouseListener(popupListener);
        }
      //inner class
        class PopupListener extends MouseAdapter {
            public void mousePressed(MouseEvent e) {
                maybeShowPopup(e);
            }

            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }

            private void maybeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popup.show(e.getComponent(),
                               e.getX(), e.getY());
                }
            }
        }

   ActionListener actionUp = new ActionListener () {
      public void actionPerformed(ActionEvent e) {
         System.out.println("ActionListener Up");

         JLabelRule tempLabel;
         int selected = -1;
         for (int i = 0 ; i < oneRuleJLabel.size() ; i++ ){
            tempLabel  = (JLabelRule) oneRuleJLabel.get(i) ;
            if (tempLabel.isSelected()) {
               selected = i;
            }
         }

         if ((selected > 0) && (selected < oneRuleJLabel.size() ) ) {
             tempLabel  = (JLabelRule) oneRuleJLabel.get(selected) ;
            //((JLabelRule) oneRuleJLabel.get(selected - 1)).select();
            FindReplace oneRule = (FindReplace) loadedReqExp.searchList.remove( selected );
            loadedReqExp.searchList.insertElementAt( oneRule ,selected - 1);
         }
         draw() ;   
         //0
         //2 Selected
         //1
         //3
         //4

         
      }
   };

   ActionListener actionEdit = new ActionListener () {
      public void actionPerformed(ActionEvent e) {
         System.out.println("ActionListener Edit");
      }
   };

   ActionListener actionDown = new ActionListener () {
      public void actionPerformed(ActionEvent e) {
         System.out.println("ActionListener Down");
      }
   };


   MouseListener mouseAction = new MouseAdapter() {  
      public void mouseClicked(MouseEvent me){
         System.out.println("Mouse Clicked  ");

         JLabelRule label; // = (JLabelRule) me.getComponent(); 
         
         for (int i=0; i<oneRuleJLabel.size(); i++ ){
            label = (JLabelRule)  oneRuleJLabel.get(i) ;
            label.deselect();
         }

         label = (JLabelRule) me.getComponent(); 
         label.select();
      }

   };


}

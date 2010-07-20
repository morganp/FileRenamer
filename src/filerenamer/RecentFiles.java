package filerenamer;

import javax.swing.JMenu;
//import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.Action;
import javax.swing.AbstractAction;

import javax.swing.JMenuItem;

import java.io.File;

import java.util.Vector;

//For preferences
import java.util.prefs.*;

public class RecentFiles {
   public final static String FILE0 = new String("File0");
   public final static String FILE1 = new String("File1");
   public final static String FILE2 = new String("File2");
   public final static String FILE3 = new String("File3");
   public final static String FILE4 = new String("File4");

   //Preference System
   protected Preferences prefLastUsedFiles;
   // protected Preferences prefLastUsedFile1;
   // protected Preferences prefLastUsedFile2;
   // protected Preferences prefLastUsedFile3;
   // protected Preferences prefLastUsedFile4;

   protected Vector recentConfigFiles  = new Vector();
   protected Vector recentConfigMenuI  = new Vector();
   protected Vector recentConfigAction = new Vector();
   
   JMenu                  fileMenu;
   LoadRegularExpressions loadedReqExp;
   
   public RecentFiles(JMenu fileMenu, LoadRegularExpressions loadedReqExp) {
      this.fileMenu     = fileMenu;
      this.loadedReqExp = loadedReqExp;
      
      prefLastUsedFiles = Preferences.userRoot().node("net/amaras/filerenamer/file"); 
      recentConfigFiles.add(new File(prefLastUsedFiles.get(FILE0, "empty")));
      recentConfigFiles.add(new File(prefLastUsedFiles.get(FILE1, "empty")));
      recentConfigFiles.add(new File(prefLastUsedFiles.get(FILE2, "empty")));
      recentConfigFiles.add(new File(prefLastUsedFiles.get(FILE3, "empty")));
      recentConfigFiles.add(new File(prefLastUsedFiles.get(FILE4, "empty")));
      
      for (int i=0; i<recentConfigFiles.size(); i++){
         File rx = (File) recentConfigFiles.get(i);
         if (!rx.getName().equals("empty")){
            System.out.println("Looking at String "+ rx);
            Action tempAction = new openConfigActionClass (rx.getName(), rx);
            recentConfigAction.add(tempAction);
            recentConfigMenuI.add(new JMenuItem(tempAction));
            fileMenu.add((JMenuItem) recentConfigMenuI.get(i));
         }
      }
   }
   
   public void openFile(File newFile) {
      try {
         boolean loadedAlreadyInList = false;
         //Loop looking to see if newFile is already in our List of recent files
         for (int i=0; i<recentConfigFiles.size() ; i++) {  
            if (newFile.equals((File) recentConfigFiles.get(i))) {
               //Not sure how to exit loop correctly so just flag when match found
               loadedAlreadyInList = true;
            }
         }
         if (loadedAlreadyInList == false){
            System.out.println("Adding " + newFile.getCanonicalPath());
            //Move all current files in list down one position
            for (int i=recentConfigFiles.size()-1; i>=1; i--) {
               System.out.println("Trying to sort out file List :" + i + ":" + (i-1));
               //Move Element down 1
               recentConfigFiles.setElementAt(recentConfigFiles.get(i-1),i);
            }
            recentConfigFiles.setElementAt(newFile ,0);
         
            //Update history for next time file is loaded
            prefLastUsedFiles.put(FILE0, ((File) recentConfigFiles.get(0)).getCanonicalPath());
            prefLastUsedFiles.put(FILE1, ((File) recentConfigFiles.get(1)).getCanonicalPath());
            prefLastUsedFiles.put(FILE2, ((File) recentConfigFiles.get(2)).getCanonicalPath());
            prefLastUsedFiles.put(FILE3, ((File) recentConfigFiles.get(3)).getCanonicalPath());
            prefLastUsedFiles.put(FILE4, ((File) recentConfigFiles.get(4)).getCanonicalPath());

         }
      } catch (java.io.IOException ex) {
         System.out.println("RecentFiles.java:openFile");
         System.out.println(ex);
      }
      
      
   }
   
   //Action Method for menu items
   public class openConfigActionClass extends AbstractAction {
      File filePath;
      public openConfigActionClass(String text, File filePath) {
          super(text);
          this.filePath = filePath;
       }
       public void actionPerformed(ActionEvent e) {
         System.out.println("opening Config File");
         loadedReqExp.update(filePath);
			//I DO not like this
			//This is position based and works for Now but will break if extra options added above the edit option
			//Enable the Edit option after loading a config file (can only edit when have a file to save to)
 			fileMenu.getItem(4).setEnabled(true);
      }
    }
   
}
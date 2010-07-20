package filerenamer;
/**
 * A simple example showing how to use {@link FileDrop}
 * @author Robert Harder, rob@iharder.net
 */

import filerenamer.*;
import filedrop.*;
import javax.swing.*;

//import java.awt.Color;



//Required for the actionListener
import java.awt.event.*;

//Required for the file chooser
import java.io.File;

import java.util.Vector;

//For preferences
import java.util.prefs.*;

//Required for the 
import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.KeyEvent;

//import java.awt.event.*;
//import java.awt.Color;
//import java.io.*;
import javax.swing.event.*;

public class FileRenamer {
   JFrame         frame;
   JPanel         topPanel;
   Vector         file_names = new Vector();
   final JPanel   old_names_view;
   final JPanel   new_names_view;
   
   

   //Menu bar is not static, each window has its own menu bar.
   //   They just all look the same
   //final
   JMenuBar mainMenuBar = new JMenuBar();   
   private JMenuItem editCongifJMenu; //Only menu which is diabled/enabled

   //Menus
   protected JMenu fileMenu, editMenu, helpMenu;
   protected Action exitAction, createConfigAction, editConfigAction, loadConfigAction, showAboutAction;
   protected Action clearAction;
   //protected Vector recentConfigAction = new Vector();
   
   RecentFiles fileList;
   
   protected AboutBox aboutBox;
   protected ViewCurrentRules editJFrame;
   //Used by the Config Loader actions in menu
   final JFileChooser fc = new JFileChooser();
   File               configFile;

   static LoadRegularExpressions loadedReqExp;

    //FileRenamer Constructor
    public FileRenamer () {
       loadedReqExp = new LoadRegularExpressions();
		
       frame = new JFrame( "FileRenamer" );
       JScrollPane topLevelScrollPane =  new JScrollPane();

       old_names_view = new JPanel();
       old_names_view.setBorder(BorderFactory.createLineBorder(Color.black));
       old_names_view.setLayout(new BoxLayout(old_names_view, BoxLayout.Y_AXIS));

       new_names_view = new JPanel();
       new_names_view.setBorder(BorderFactory.createLineBorder(Color.black));
       new_names_view.setLayout(new BoxLayout(new_names_view, BoxLayout.Y_AXIS));

       topPanel = new JPanel();
       topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

       JPanel buttonsPanel = new JPanel();
       buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
         
       JButton clearButton = new JButton("Clear");
       clearButton.addActionListener(clearFileAction);
       buttonsPanel.add(clearButton);

       JButton previewButton = new JButton("Preview Names");
       previewButton.addActionListener(previewAction);
       buttonsPanel.add(previewButton);
         
       JButton renameButton = new JButton("Apply");
       renameButton.addActionListener(renameAction);
       buttonsPanel.add(renameButton);
       topPanel.add(buttonsPanel);

       topPanel.add(new JLabel("Drag Files Here"));

       JPanel sideBySide = new JPanel();
       sideBySide.setLayout(new BoxLayout(sideBySide, BoxLayout.X_AXIS));
       sideBySide.add(old_names_view);
       sideBySide.add(new_names_view);
         
       topPanel.add(sideBySide);
       topPanel.add(new JLabel("Drag Files Here"));
      
       frame.getContentPane().add( 
            new javax.swing.JScrollPane(topPanel), //text), //, 
            java.awt.BorderLayout.CENTER );
        
        new FileDrop( System.out, topPanel, /*dragBorder,*/ new FileDrop.Listener() {   
            public void filesDropped( java.io.File[] files ) {
               for( int i = 0; i < files.length; i++ ) {
                  //try {
                     FileForRename current = new FileForRename(files[i], loadedReqExp);
                     file_names.add(current);
                     old_names_view.add(current.get_old_label());
                     new_names_view.add(current.get_new_label());
                   //} catch( java.io.IOException e ) {
                   //}
                }   // end for: through each dropped file
            }   // end filesDropped
        }); // end FileDrop.Listener



        addMenus();
        
        frame.setBounds( 100, 100, 500, 200 );
        frame.setDefaultCloseOperation( frame.EXIT_ON_CLOSE );
        frame.setVisible(true);
    }// end Constructor
   
   // private void retrieveRecentConfigFiles() {
   //       //recentConfigFiles
   //       prefLastUsedFile0 = Preferences.userRoot().node("net/amaras/filerenamer/file0"); 
   //       String file_0     = prefLastUsedFile0.get(FILE0, null);
   //       recentConfigFiles.add(file_0);
   //    
   //       prefLastUsedFile1 = Preferences.userRoot().node("net/amaras/filerenamer/file1"); 
   //       String file_1     = prefLastUsedFile1.get(FILE1, null);
   //       recentConfigFiles.add(file_1);
   //    }
   
   public void addMenus() {
      //mainMenuBar = new JMenuBar();

      //for (int i = 0; i < mainMenuBar.getMenuCount(); i++ ){
      //   mainMenuBar.remove( mainMenuBar.getComponent(i) );
      //}
      if ( fileMenu != null ) {
         mainMenuBar.remove(fileMenu);
      }
      if ( helpMenu != null ) {
         mainMenuBar.remove(helpMenu);
      }


      showAboutAction    = new showAboutActionClass("About");

      createConfigAction = new createConfigActionClass("Create"); //,
            //KeyStroke.getKeyStroke(KeyEvent.VK_W, shiftCtrlMask) );
   	
		editConfigAction   = new editConfigActionClass("Edit");

      loadConfigAction   = new loadConfigActionClass("Load"); //,
            //KeyStroke.getKeyStroke(KeyEvent.VK_W, shiftCtrlMask) );

      clearAction        = new clearActionClass("Clear History") ; //,

      exitAction         = new exitActionClass("Exit") ; //,
            //KeyStroke.getKeyStroke(KeyEvent.VK_W, shiftCtrlMask) );

      fileMenu = new JMenu("File");
      //editMenu = new JMenu("Edit");
      helpMenu = new JMenu("Help");

      editCongifJMenu = new JMenuItem(editConfigAction);
		editCongifJMenu.setEnabled(false);  
      //Build File Menu
      fileMenu.add(new JMenuItem(showAboutAction));
      fileMenu.addSeparator();
      fileMenu.add(new JMenuItem(createConfigAction));
      fileMenu.add(new JMenuItem(loadConfigAction));
		fileMenu.add(editCongifJMenu);
      fileMenu.addSeparator();
      fileList = new RecentFiles( fileMenu, loadedReqExp);
      fileMenu.addSeparator();
      fileMenu.add(new JMenuItem( clearAction ));
      fileMenu.addSeparator();
      fileMenu.add(new JMenuItem( exitAction ));
      
      //Build Edit Menu
      //Build Help Menu
      helpMenu.add(new JMenuItem(showAboutAction));
      
      mainMenuBar.add(fileMenu);
      //mainMenuBar.add(editMenu);
      mainMenuBar.add(helpMenu);
      frame.setJMenuBar (mainMenuBar);
   }

   //##########################################
   //### Button Actions 
   //##########################################
   ActionListener clearFileAction = new ActionListener () {
      public void actionPerformed(ActionEvent e) {
         System.out.println("Clear Pressed");
         clearFileList();
      }
   };

  public void clearFileList() {
      for (int i=0; i<file_names.size(); i++) {
         FileForRename current = (FileForRename) file_names.get(i);
         old_names_view.remove(current.get_old_label());
         new_names_view.remove(current.get_new_label());
      }
      file_names.clear();
      //topPanel.update();
      topPanel.updateUI();
   }

   ActionListener previewAction = new ActionListener () {
      public void actionPerformed(ActionEvent e) {
         System.out.println("Preview Pressed");
         for (int i=0; i<file_names.size(); i++) {
            FileForRename current = (FileForRename) file_names.get(i);
            current.set_loadedReqExp(loadedReqExp);
            current.preview_new_name();
         }
      }
   };



   ActionListener renameAction = new ActionListener () {
      public void actionPerformed(ActionEvent e) {
         System.out.println("Rename Pressed");
         for (int i=0; i<file_names.size(); i++) {
            FileForRename current = (FileForRename) file_names.get(i);
            current.apply_new_name();
         }
         clearFileList();
      }
   };
 


 public class loadConfigActionClass extends AbstractAction {
      public loadConfigActionClass(String text) {
         super(text);
      }
      public loadConfigActionClass(String text, KeyStroke shortcut) {
         super(text);
         putValue(ACCELERATOR_KEY, shortcut);
      }

      public void actionPerformed(ActionEvent e) {
         System.out.println("Load Config File");
         int returnVal = fc.showOpenDialog(frame);

         if (returnVal == JFileChooser.APPROVE_OPTION) {
				editCongifJMenu.setEnabled(true);  
            configFile = fc.getSelectedFile();
            System.out.println("Opening: " + configFile.getName());

            //Sort out pref system
            fileList.openFile(configFile);
            loadedReqExp.update(configFile);
            addMenus();
         } else {
            System.out.println("Open command cancelled by user.");
         }
      }
   }

   public class createConfigActionClass extends AbstractAction {
      public createConfigActionClass(String text) {
         super(text);
			
      }
      public createConfigActionClass(String text, KeyStroke shortcut) {
         super(text);
         putValue(ACCELERATOR_KEY, shortcut);
      }
      
      public void actionPerformed(ActionEvent e) {
         System.out.println("Create Config File");
			//This should trigger the save file menu
			//once this is done enable the edit menu
         
         //TODO want proper save file dialog
         String wd = System.getProperty("user.dir");
         JFileChooser fc = new JFileChooser(wd);
         int rc = fc.showDialog(null, "Create File");
         if (rc == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String filename = file.getAbsolutePath();
            // call your function here
            //configFile = ; //File
            //JFrame frame = new JFrame();
            //String newFile = JOptionPane.showInputDialog(frame, "New Config File Name");
            configFile = new File(filename);

            //TODO Create Empty File

            //Added to prefs menu
            fileList.openFile(configFile);

			   editCongifJMenu.setEnabled(true); 
            addMenus();
   

         } else {
            System.out.println("File chooser cancel button clicked");
         }
     }

   }

	public class editConfigActionClass extends AbstractAction {
      public editConfigActionClass(String text) {
         super(text);
			
      }
      public editConfigActionClass(String text, KeyStroke shortcut) {
         super(text);
         putValue(ACCELERATOR_KEY, shortcut);
      }
      
      public void actionPerformed(ActionEvent e) {
			launchEditJFrame();
         System.out.println("Edit Config File");
      }
   }

	private void launchEditJFrame() {
		if (editJFrame == null) {
         editJFrame = new ViewCurrentRules(loadedReqExp);
      }
      editJFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		editJFrame.setVisible(true);
	}

   public class clearActionClass extends AbstractAction {
      public clearActionClass(String text) {
         super(text);
      }
      public clearActionClass(String text, KeyStroke shortcut) {
         super(text);
         putValue(ACCELERATOR_KEY, shortcut);
      }
      
      public void actionPerformed(ActionEvent e) {
         System.out.println("Clear History");
         try {
         //quit();
         //System.exit(0);
         //Preferences prefLastUsedFiles = Preferences.userRoot().node("net/amaras/filerenamer/file"); 
         if (Preferences.userRoot().nodeExists("net/amaras/filerenamer/file")) {
            Preferences prefLastUsedFiles = Preferences.userRoot().node("net/amaras/filerenamer/file"); 
            prefLastUsedFiles.removeNode();
            
            addMenus();

         }
         } catch(java.util.prefs.BackingStoreException ex) {
            System.out.println("Caught Exception");
            System.out.println(ex);
         
         }

      }
   }



   public class exitActionClass extends AbstractAction {
      public exitActionClass(String text) {
         super(text);
      }
      public exitActionClass(String text, KeyStroke shortcut) {
         super(text);
         putValue(ACCELERATOR_KEY, shortcut);
      }
      
      public void actionPerformed(ActionEvent e) {
         System.out.println("exit");
         //quit();
         System.exit(0);
      }
   }

   //#######################################################
   //### About Box
   //#######################################################
   public class showAboutActionClass extends AbstractAction {
      public showAboutActionClass(String text) {
         super(text);
      }
      public void actionPerformed(ActionEvent e) {
         about();
      }
   }

   // public void about(ApplicationEvent e) {
   //       aboutBox.setDefaultCloseOperation(javax.swing.DISPOSE_ON_CLOSE);
   //       aboutBox.setResizable(false);
   //       aboutBox.setVisible(true);
   //    }

   public void about() {
      if (aboutBox == null) {
         aboutBox = new AboutBox();
      }
      aboutBox.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      //aboutBox.setResizable(false);
      aboutBox.setVisible(true);
   }

}

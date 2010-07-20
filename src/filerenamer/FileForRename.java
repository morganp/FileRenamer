package filerenamer; 

import java.io.File;
import javax.swing.*;

public class FileForRename {
	public  File   old_name;
	public  File   new_name;
	private LoadRegularExpressions loadedReqExp;
	private JLabel old_label;
	private JLabel new_label;
	
	
	public FileForRename(File old_name, LoadRegularExpressions loadedReqExp) {
		this.old_name    = old_name;
		//this.configFile  = configFile;
		this.loadedReqExp = loadedReqExp;
		old_label        = new JLabel(old_name.getName());
		new_label        = new JLabel("default");
	}
	
	public JLabel get_old_label(){
		return old_label;
	}
	
	public JLabel get_new_label(){
		return new_label;
	}
	
	public void set_new_name(String name){
		new_name = new File(name);
		
		int position = name.lastIndexOf( java.io.File.separator );
		if (position >0 ) {
			name = name.substring(position+1);
		}
		new_label.setText(name);
		
	}
	
	public void set_loadedReqExp(LoadRegularExpressions loadedReqExp) {
		this.loadedReqExp = loadedReqExp;
	}
	
	public void preview_new_name() {
		//Rename Algo is called fitted in here
		//Once
		//replaceFirst(String regex, String replacement) 
		//Greedy
		//replaceAll(String regex, String replacement) 
		String new_name = old_name.getName() ; 
		
		//TODO toplevel function to choose search and replace file
		
		//This reads the file for every file to rename very inefficient
		//LoadRegularExpressions loadedReqExp = new LoadRegularExpressions(configFile);
		if (loadedReqExp != null) {
		for (int i=0; i< loadedReqExp.searchList.size(); i++) {
			FindReplace aPair = (FindReplace) loadedReqExp.searchList.get(i);
			if (aPair.mode == FindReplace.regExpAll) {
			   new_name = new_name.replaceAll(aPair.find, aPair.replace);	
			} else if (aPair.mode == FindReplace.regExpAll_iCase) {
				//TODO Proper case insensitive match
				new_name = new_name.replaceAll("(?i)" + aPair.find, aPair.replace);	
			
			} else if (aPair.mode == FindReplace.regExpOne) {
				new_name = new_name.replaceFirst(aPair.find, aPair.replace);
			} else if (aPair.mode == FindReplace.regExpOne_iCase) {
				//TODO Proper case insensitive match
				new_name = new_name.replaceFirst("(?i)" + aPair.find, aPair.replace);
			
			} else if 	(aPair.mode == FindReplace.textOne) {
				new_name = replaceString(new_name, aPair.find, aPair.replace, true);
			} else if 	(aPair.mode == FindReplace.textOne_iCase) {
				new_name = replaceString(new_name, aPair.find, aPair.replace, false);
			
			} else if 	(aPair.mode == FindReplace.textAll) {
				//TODO Replace all function
				new_name = replaceString(new_name, aPair.find, aPair.replace, true);
			} else if 	(aPair.mode == FindReplace.textAll_iCase) {
				//TODO Replace All function
				new_name = replaceString(new_name, aPair.find, aPair.replace, false);
			}
		}
		
		boolean keep_file_extension = true;
		if (keep_file_extension) {
			//Get extension from old_name.getName()
			int position = old_name.getName().lastIndexOf('.');
			String file_extension = old_name.getName().substring(position);
			//If new name not end with extension add it
			if (! new_name.endsWith(file_extension) ) {
				new_name = new_name + file_extension;
			}
			
		}
		int position = old_name.getPath().lastIndexOf(java.io.File.separator );
		String path = new String();
		if (position >0 ) {
			path = old_name.getPath().substring(0, position+1);
		}	else {
			System.out.println("no "+ java.io.File.pathSeparator +" in Path:" + old_name.getPath() );
		}
		
		set_new_name(path + new_name);
	} else {
		set_new_name("Config File Not Set");
	} 
	
	}
	
	public void apply_new_name() {
		if (old_name.isFile() && (new_name != null)) {
			System.out.println("Is a File");
			boolean result = old_name.renameTo(new_name);
			System.out.println("Rename result: " + result);
		}
	}
	
	public String replaceString(String input, String find, String replace, boolean casesensitive){
		//System.out.println("input:" + input + ", find:" +find+ ", :replace" + replace);
		String input_case_adjusted = input;
		if (casesensitive == false) {
			//For Case Insensitive searchs 
			//Lowercase everything (but replace in the original string)
			input_case_adjusted = input.toLowerCase() ;
			find                = find.toLowerCase() ;
		}
		
		
		int    startPosition = input_case_adjusted.indexOf(find);
		String start         = "";
		String end           = "";
		
		//System.out.println("Start Position:" + startPosition);
		if (startPosition >= 0) {
			if (startPosition > 0) {
			   start = input.substring(0, startPosition);
		   }
			end = input.substring(startPosition + find.length());
			
			return start + replace + end;
		} else {
			return input;
		}
	}
	
	public String replaceStringAll(String input, String find, String replace){
		System.out.println("FileForRename:replaceStringAll NOT IMPLEMENTED");
		return input;
	}
	
	
}
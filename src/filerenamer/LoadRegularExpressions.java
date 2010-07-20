package filerenamer; 

import java.io.File;
import java.util.Vector;
//Adding this for the buffered reader
import java.io.*;


public class LoadRegularExpressions {
   public Vector searchList = new Vector();
	public File   currentConfigFile;

   public LoadRegularExpressions(){
   }
   
   public LoadRegularExpressions(File configFile){
      update(configFile); 
   }

   public void update (File configFile){
		this.currentConfigFile = configFile;
      
      // [Search and replace]
      //       use="0"
      //       casesensitive="0"
      //       once="0"
      //       search="[ .-]720p"
      //       series="0"
      //       guid="CA3DE
      
      //Java regular expressions
      //http://java.sun.com/j2se/1.5.0/docs/api/java/util/regex/Pattern.html

       try  {
           searchList.clear();
           BufferedReader reader = new BufferedReader(new FileReader(configFile));
           
           System.out.println("Loading Config File");
           //... Loop as long as there are input lines.
           String line = reader.readLine();
           line        = trim(line);
           while (line != null) {
               
               
               //Test for search nad replace mode
               if ( line.compareToIgnoreCase("[Search and replace]") == 0 ){
                  boolean nextToken     = false;
                  String  search        = new String("");
                  String  replace       = new String("");
                  boolean casesensitive = true;
                  int     once          = 0;
                  int     use           = 0;

                  while (!nextToken) {
                     
                     line = reader.readLine();
                     //System.out.println(line);
                     try {
                        line = trim(line);
                        //Decide when to break current token loop
                        if ((line.startsWith("[")) || (line == null)){
                           nextToken = true;
                        }

                        if (line.startsWith("search")){
                           line = line.replace("search=\"", "");
                           line = line.replace("\"", "");
                           search = line;
                           //System.out.println("Setting Search Pattern:" + search);
                        }
                        if (line.startsWith("replace")){
                           line = line.replace("replace=\"", "");
                           line = line.replace("\"", "");
                           if (line.contains("\\")) {
                              char slash = '\\';
                              char dollar = '$';
                              //System.out.println("Found \\ in the replace String");
                              line = line.replace(slash, dollar);
                           }
                           
                           replace = line;
                           //System.out.println("Setting Replace String:" + replace);
                        }
                        if (line.startsWith("once")){
                           line = line.replace("once=\"", "");
                           line = line.replace("\"", "");
                           once = (new Integer(line)).intValue();
                        }
                        if (line.startsWith("use")){
                           line = line.replace("use=\"", "");
                           line = line.replace("\"", "");
                           use  = (new Integer(line)).intValue();
                        }
                        if (line.startsWith("casesensitive")){
                           line = line.replace("casesensitive=\"", "");
                           line = line.replace("\"", "");
                           casesensitive  = (new Boolean(line)).booleanValue();
                        }
                        if (line.startsWith("id3tag")){
                        }
                     } catch (java.lang.NullPointerException e) {
                        System.out.println("End of File");
                        nextToken = true;
                     }
                     //Last run through before moving onto next section
                     //All data extracted from this token
                     if (nextToken == true){
                        //System.out.println("Search:"+ search + ", Replace:"+ replace);
                        //System.out.println("   use:" + use + ", once:" + once);
                        int mode = 0;
                        if ((use == 0) && (once==1) && (casesensitive == true)) {
                           mode = FindReplace.regExpOne;
                        }
                        if ((use == 0) && (once==1) && (casesensitive == false)) {
                           mode = FindReplace.regExpOne_iCase;
                           //System.out.println("regExpOne_iCase Mode Not implemented");
                        }
                        if ((use == 0) && (once==0) && (casesensitive == true)) {
                              mode = FindReplace.regExpAll;
                        }
                        if ((use == 0) && (once==0) && (casesensitive == false)) {
                              mode = FindReplace.regExpAll_iCase;
                              //System.out.println("regExpAll_iCase Mode Not implemented");
                        }
                        
                        //Straight String Replace
                        if ((use == -1) && (once==1) && (casesensitive == true)){
                           mode = FindReplace.textOne;
                        } 
                        if ((use == -1) && (once==1) && (casesensitive == false)){
                              mode = FindReplace.textOne_iCase;
                        }
                        if ((use == -1) && (once==0) && (casesensitive == true)){
                           mode = FindReplace.textAll;
                           //System.out.println("textAll Mode Not implemented");
                        } 
                        if ((use == -1) && (once==0) && (casesensitive == false)){
                              mode = FindReplace.textAll_iCase;
                              //System.out.println("textAll_iCase Mode Not implemented");
                        }
                        searchList.add(new FindReplace(mode, search, replace));  
                     }
                  }
               } else {
                  line=reader.readLine();
               }
               
           }

           //... Close reader and writer.
           reader.close();  // Close to unlock.
       } catch (IOException e){
         System.out.println("ERROR : IOException");
         System.out.println(e);
       }
    
      
      
      //parse config one line at a time
      //have some sort of syntax to say find one or all
      //searchList.add(new FindReplace(String find, String replace))
      
   }
   
   private String trim (String input){
      String output = input.trim();
      //System.out.println(output);
      return output;
   } 
   
} 
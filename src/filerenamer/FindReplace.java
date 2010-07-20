package filerenamer; 


public class FindReplace{
	int mode;
	public String find;
	public String replace;
	
	public final static int regExpAll       = 0;
	public final static int regExpOne       = 1;
	public final static int textAll         = 2;
	public final static int textOne         = 3;
	
	//Case insensitive versions
	public final static int regExpAll_iCase = 4;
	public final static int regExpOne_iCase = 5;
	public final static int textAll_iCase   = 6;
	public final static int textOne_iCase   = 7;
	
	public final static String[] labels = 
		{"Regular Expression All", 
		 "Regular Expression One",
		 "Text Replace All",
		 "Text Replace Once",
		 "Regular Expression All ignore case",
		 "Regular Expression One ignore case",
		 "Text Replace All ignore case",
		 "Text Replace Once ignore case"
		};
	
	public FindReplace(int mode, String find, String replace){
		this.mode = mode; 
		this.find = find;
		this.replace= replace;
	}
	
}
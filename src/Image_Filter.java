

import java.io.*; 


public class Image_Filter extends javax.swing.filechooser.FileFilter {
	
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = getExtension(f);
        if (extension != null) {
            if (extension.equals("jpeg") ||
                extension.equals("png") ||
                extension.equals("jpg")){
                    return true;
            } else {
                return false;
            }
        }

        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "Supported Image files";
    }
    
    /*
	 *Determines the Extension
	 *@param f File to return the extension of
	 *@return Returns the String representing the extension
	 */
	protected static String getExtension(File f)
	{
		String s = f.getName();
		int i = s.lastIndexOf('.');
		if (i > 0 &&  i < s.length() - 1) 
		  return s.substring(i+1).toLowerCase();
		return "";
	}	
}
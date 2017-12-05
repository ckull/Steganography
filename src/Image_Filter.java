

import java.io.*; 

/**
 *
 * This class use for get extension from the input file and check it is the correct extension that appropriate for
 * our program or not.
 * The extensions that our program can import into encode processing are jpeg and png
 *
 * Created by Kullapat Siribodhi 58070503404
 *            Thanadol Nimitchuchai 58070503442, 20 Nov 2017
 * **/
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
    
    /**
     * This method use for define the extension of input file
     *
     * @param f is the file that need to return the extension
     * @return return string that can represent the extension
     * **/
     protected static String getExtension(File f)
	{
		String s = f.getName();
		int i = s.lastIndexOf('.');
		if (i > 0 &&  i < s.length() - 1) 
		  return s.substring(i+1).toLowerCase();
		return "";
	}	
}

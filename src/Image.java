import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;



public class Image
{
	/*
	 *Returns the complete path of a file, in the form: path\name.ext
	 *@param path   The path (folder) of the file
	 *@param name The name of the file
	 *@param ext	  The extension of the file
	 *@return A String representing the complete path of a file
	 */
	public String image_path(String path, String name, String ext)
	{
		return path + "/" + name + "." + ext;
	}
	
	
	
	/*
	 *Set method to save an image file
	 *@param image The image file to save
	 *@param file	  File  to save the image to
	 *@param ext	  The extension and thus format of the file to be saved
	 *@return Returns true if the save is succesful
	 */
	public boolean setImage(BufferedImage image, File file, String ext)
	{
		try
		{
			file.delete(); //delete resources used by the File
			ImageIO.write(image,ext,file);
			return true;
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, 
				"File could not be saved!","Error",JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	/*
	 *Gets the byte array of an image
	 *@param image The image to get byte data from
	 *@return Returns the byte array of the image supplied
	 *@see Raster
	 *@see WritableRaster
	 *@see DataBufferByte
	 */
	public byte[] get_byte_data(BufferedImage image)
	{
		WritableRaster raster   = image.getRaster();
		DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
		return buffer.getData();
	}
	
	/*
	 *Creates a user space version of a Buffered Image, for editing and saving bytes
	 *@param image The image to put into user space, removes compression interferences
	 *@return The user space version of the supplied image
	 */
	public BufferedImage user_space(BufferedImage image)
	{
		//create new_img with the attributes of image
		BufferedImage new_img  = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D	graphics = new_img.createGraphics();
		graphics.drawRenderedImage(image, null);
		graphics.dispose(); //release all allocated memory for this image
		return new_img;
	}
	
	/*
	 *Get method to return an image file
	 *@param f The complete path name of the image.
	 *@return A BufferedImage of the supplied file path
	 *@see	Steganography.image_path
	 */
	public BufferedImage getImage(String f)
	{
		BufferedImage 	image	= null;
		File 		file 	= new File(f);
		
		try
		{
			image = ImageIO.read(file);
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null, 
				"Image could not be read!","Error",JOptionPane.ERROR_MESSAGE);
		}
		return image;
	}
}

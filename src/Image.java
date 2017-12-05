import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


/**
 *
 * This class use for do everything which concern with the image such as duplicate, read image file, transform pixel
 * of the image into array byte.
 *
 * Created by Kullapat Siribodhi 58070503404
 *            Thanadol Nimitchuchai 58070503442, 20 Nov 2017
 * **/
public class Image
{
	/**
	 * This class use for return the complete form of extension in the form: path\name.ext
	 * @param path : The folder or address of the file
	 * @param name : The name of the file
	 * @param ext : The extension of the file
	 * @return return the string that can represent path of file
	 **/
	public static String image_path(String path, String name, String ext)
	{
		return path + "/" + name + "." + ext;
	}
	
	
	
	/**
	 * 
	 *This method use for save an image file that will be the encoded image file
	 * @param image : The image file that we need to save
	 * @param file : The File that use for save the image
	 * @param ext : The extension and format of the file that have saved
	 * @return return boolean value for tell the process succeeds or not
	 * 
	 **/
	public static boolean setImage(BufferedImage image, File file, String ext)
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
	
	/**
	 * This class use for transform the pixel in the image into byte array
	 *
	 * @param image : The image that need to change into byte array data
	 * @return return the byte array of the image
	 *
	 **/
	public static byte[] get_byte_data(BufferedImage image)
	{
		WritableRaster raster   = image.getRaster();
		DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
		return buffer.getData();
	}
	
	/**
	 *  This class use to create the imitate version of Buffered image for void any effect that influence
	 * to the original picture when editing and saving bytes
	 *
	 * @param image : The original image that we choose from device to put into imitate version
	 * @return The imitate version of the supplied image
	 *
	 **/
	public static BufferedImage duplicateImage(BufferedImage image)
	{
		//create new_img with the attributes of image
		BufferedImage new_img  = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D	graphics = new_img.createGraphics();
		graphics.drawRenderedImage(image, null);
		graphics.dispose(); //release all allocated memory for this image
		return new_img;
	}
	
	/**
	 * This method use for return image that has correct form of extension and have
	 * completed path name
	 *
	 * @param f : The complete path name of the image.
	 * @return return a BufferedImage that already has correct file path and extension
	 *
	 **/
	public static BufferedImage getImage(String f)
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

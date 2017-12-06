 
/*
 *import list
 */
import java.io.File;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.util.Base64;
import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.image.DataBufferByte;

import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * This class use for collect main method that need to use in this program
 * The methods will refer to use in the controller and send the result for
 * show in the view.
 *
 * Created by Kullapat Siribodhi 58070503404 
 *            Thanadol Nimitchuchai 58070503442, 20 Nov 2017
 * 
 **/
public class Steganography
{
	
	/**
	* This method use for recieve the image by duplicate method and bring it to encode
	* with the text and send boolean about the process succeed or not
	*
	* @param path : The folder or address of the file
	* @original : The name of the file
	* @ext1 : The extension of the file
	* @stegan :The output file name
	* @message : The text that we want to hide it in the image
	* @return return the boolean about encode process succeed or not
	*
	**/
	public boolean encode(String path, String original, String ext1, String stegan, String message)
	{
		String			file_name 	= Image.image_path(path,original,ext1);
		BufferedImage 	image_orig	= Image.getImage(file_name);
		
		BufferedImage image = Image.duplicateImage(image_orig);

		image = add_text(image,message);
		
		
		return(Image.setImage(image,new File(Image.image_path(path,stegan,"png")),"png"));
	}
	
	/**
	* This method use for recieve the image by duplicate method and bring it to encode
	* with the text. There are many types of text because they depends on the algorithms that user choose    
	and send boolean about the process succeed or not after the process finished
	*
	* @param path : The folder or address of the file
	* @original : The name of the file
	* @ext1 : The extension of the file
	* @stegan : the output file name
	* @message : The text that we want to hide it in the image
	* @algorithm : Type of algorithm inside java security library
	* @return return the boolean about encode process succeed or not
	*
	**/
	public boolean encode(String path, String original, String ext1, String stegan, String message, String algorithm)
	{
		String			file_name 	= Image.image_path(path,original,ext1);
		BufferedImage 	image_orig	= Image.getImage(file_name);
		
		BufferedImage image = Image.duplicateImage(image_orig);
		String hash = getHash(message, algorithm);
		System.out.println("Original Text: " + message);
		System.out.println("Hash: " + hash);
		image = add_text(image,hash);
		
		
		return(Image.setImage(image,new File(Image.image_path(path,stegan,"png")),"png"));
	}

	
     /**
     * This method use for bring image from the device that has .png type to extract the hidden text from the image
     *
     * @param path : the path of the image that we want to extract message with a decode process
     * @param name : the name of the image that we want to extract message with a decode process
     * @return return the string that are hidden in the image file
     **/
	public String decode(String path, String name)
	{
		byte[] decode;
		try
		{
			//user space is necessary for decrypting
			BufferedImage image  = Image.duplicateImage(Image.getImage(Image.image_path(path,name,"png")));
			decode = decode_text(Image.get_byte_data(image));
			return(new String(decode));
		}
		catch(Exception e)
		{
			// JOptionPane.showMessageDialog(null, 
			// 	"There is no hidden message in this image!","Error",
			// 	JOptionPane.ERROR_MESSAGE);
			// return "";
			return "";
		}
	}
	
	
	
	/**
	 * This method use for add the hidden text into the image by use replacement byte between image and text
	 *
	 * @param image : The image that want to add hidden text into
	 * @param text : The text that we want to hide it in the image
	 * @return : return the image with the embedded text like secret message in the picture
	 **/
	private BufferedImage add_text(BufferedImage image, String text)
	{
		//convert all items to byte arrays: image, message, message length
		byte img[]  = Image.get_byte_data(image);
		byte msg[] = text.getBytes();
		byte len[]   = bit_conversion(msg.length);
		try
		{
			System.out.println("bit_conversion: " + len.toString());
			encode_text(img, len,  0); //0 first position 
			encode_text(img, msg, 32); //4 bytes of space for length: 4bytes*8bit = 32 bits
			System.out.println("msg: " + msg.length);
			System.out.println("len_bitconversion: " + img.length);
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, 
"Target File cannot hold message!", "Error",JOptionPane.ERROR_MESSAGE);
		}
		return image;
	}
	

     /**
     * This method use for generate byte format for store the binary value that come from
     * quantity of letter, 1 byte can store 256 letters and store in the next byte when quantity is over 256
     * The capacity of letter that program can process is 256^4 letters
     *
     * @param i : The integer that come from message length  to convert
     * @return return a byte[4] array that come from converting message length to bytes
     **/
	
	private byte[] bit_conversion(int i)
	{
			
		//only using 4 bytes
		byte byte3 = (byte)((i & 0xFF000000) >>> 24); //0
		byte byte2 = (byte)((i & 0x00FF0000) >>> 16); //0
		byte byte1 = (byte)((i & 0x0000FF00) >>> 8 ); //0
		byte byte0 = (byte)((i & 0x000000FF)	   );

		//{0,0,0,byte0} is equivalent, since all shifts >=8 will be 0
		System.out.println("byte0 test: " + (int)byte0);
		System.out.println("byte1 test: " + (int)byte1);
		System.out.println("byte2 test: " + (int)byte2);
		System.out.println("byte3 test: " + (int)byte3);
		return(new byte[]{byte3,byte2,byte1,byte0});
	}
	
     /**
     * This method use for encode byte arrays of text into bytes arrays of image if the capacity of image
     * can fit the text
     *
     * @param image : Byte array of data represent an image
     * @param addition : Byte array of data that add into image data array
     * @param offset : The offset that need to use when the image array add the addition data into it
     * @return return data array image that has data Array of merged between image and addition data
     **/
	
	private byte[] encode_text(byte[] image, byte[] addition, int offset)
	{
		//check that the data + offset will fit in the image
		if(addition.length + offset > image.length)
		{
			throw new IllegalArgumentException("File not long enough!");
		}
		//loop through each addition byte
		for(int i=0; i<addition.length; ++i)
		{
			//loop through the 8 bits of each byte
			int add = addition[i];
			for(int bit=7; bit>=0; --bit) //ensure the new offset value carries on through both loops
			{
				//assign an integer to b, shifted by bit spaces AND 1
				//a single bit of the current byte
				int b = (add >>> bit) & 1;
				//assign the bit by taking: [(previous byte value) AND 0xfe] OR bit to add
				//changes the last bit of the byte in the image to be the bit of addition
				image[offset] = (byte)((image[offset] & 0xFE) | b );
				offset++;
			}
		}
		return image;
	}
	
     /**
     * This method use for extract the hidden text that hide in the image and get string like a result
     * 
     * @param image : Array of data that represent the image that has hidden text
     * @return Array of data that represent the hidden text
     **/
	private byte[] decode_text(byte[] image)
	{
		int length = 0;
		int offset  = 32;
		//loop through 32 bytes of data to determine text length
		for(int i=0; i<32; i++) 
		{
			length = (length << 1) | (image[i] & 1);
		}
		
		System.out.println("length: " + length);
		byte[] result = new byte[length];
		
		//loop through each byte of text
		for(int b=0; b<result.length; ++b )
		{
			//loop through each bit within a byte of text
			for(int i=0; i<8; i++, offset++)
			{
				//assign bit: [(new byte value) << 1] OR [(text byte) AND 1]
				result[b] = (byte)((result[b] << 1) | (image[offset] & 1));
			}
		}
		return result;
	}
	
     /**
     * This method use for transform normal text into hash function form depend on
     * the hash function type that we choose
     *
     * @param text, Input text that we want to hide it into image
     * @param hashType, Type of hash function inside java security library
     * return string that generate the result by use hash function that we choose
     **/
	
	public  String getHash(String text, String hashType) {
	        try {
	                    java.security.MessageDigest md = java.security.MessageDigest.getInstance(hashType);
	                    byte[] array = md.digest(text.getBytes());
	                    StringBuffer sb = new StringBuffer();
	                    for (int i = 0; i < array.length; ++i) {
	                        sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
	                 }
	                    return sb.toString();
	            } catch (java.security.NoSuchAlgorithmException e) {
	                System.out.println("Error Algorithm");
	            }
	            return null;
	    }

}

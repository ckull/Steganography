 
/*
 *@author  William_Wilson
 *@version 1.6
 *Created: May 8, 2007
 */

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
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
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

/*
 *Class Steganography
 */
public class Steganography
{
	/*
	 *Encrypt an image with text, the output file will be of type .png
	 *@param path		 The path (folder) containing the image to modify
	 *@param original	The name of the image to modify
	 *@param ext1		  The extension type of the image to modify (jpg, png)
	 *@param stegan	  The output name of the file
	 *@param message  The text to hide in the image
	 *@param type	  integer representing either basic or advanced encoding
	 */
	Image imageManager = new Image();
	
	public boolean encode(String path, String original, String ext1, String stegan, String message)
	{
		String			file_name 	= imageManager.image_path(path,original,ext1);
		BufferedImage 	image_orig	= imageManager.getImage(file_name);
		
		
		//user space is not necessary for Encrypting
		BufferedImage image = imageManager.user_space(image_orig);
//		String md5 = getHash(message, "MD5");
//		System.out.println("Original Text: " + message);
//		System.out.println("Hash: " + md5);
		image = add_text(image,message);
		
		
		return(imageManager.setImage(image,new File(imageManager.image_path(path,stegan,"png")),"png"));
	}
	
	public boolean encode(String path, String original, String ext1, String stegan, String message, String algorithm)
	{
		String			file_name 	= imageManager.image_path(path,original,ext1);
		BufferedImage 	image_orig	= imageManager.getImage(file_name);
		
		
		//user space is not necessary for Encrypting
		BufferedImage image = imageManager.user_space(image_orig);
		String hash = getHash(message, algorithm);
		System.out.println("Original Text: " + message);
		System.out.println("Hash: " + hash);
		image = add_text(image,hash);
		
		
		return(imageManager.setImage(image,new File(imageManager.image_path(path,stegan,"png")),"png"));
	}
	
	public boolean encode(String path, String original, String ext1, String stegan, String message, boolean advance) throws Exception
	{
		String			file_name 	= imageManager.image_path(path,original,ext1);
		BufferedImage 	image_orig	= imageManager.getImage(file_name);
		BufferedImage image = imageManager.user_space(image_orig);
		
		if(advance == true){
			//user space is not necessary for Encrypting
			String publicKey = getRSAKey(message);
			System.out.println("Original Text: " + message);
			System.out.println("publicKey: " + publicKey);
			image = add_text(image,publicKey);
			JOptionPane.showMessageDialog(new View("Steganography"), "Public key: " + publicKey);
		}
		
		
		return(imageManager.setImage(image,new File(imageManager.image_path(path,stegan,"png")),"png"));
	}
	
	/*
	 *Decrypt assumes the image being used is of type .png, extracts the hidden text from an image
	 *@param path   The path (folder) containing the image to extract the message from
	 *@param name The name of the image to extract the message from
	 *@param type integer representing either basic or advanced encoding
	 */
	public String decode(String path, String name)
	{
		byte[] decode;
		try
		{
			//user space is necessary for decrypting
			BufferedImage image  = imageManager.user_space(imageManager.getImage(imageManager.image_path(path,name,"png")));
			decode = decode_text(imageManager.get_byte_data(image));
			return(new String(decode));
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, 
				"There is no hidden message in this image!","Error",
				JOptionPane.ERROR_MESSAGE);
			return "";
		}
	}
	
	
	
	/*
	 *Handles the addition of text into an image
	 *@param image The image to add hidden text to
	 *@param text	 The text to hide in the image
	 *@return Returns the image with the text embedded in it
	 */
	private BufferedImage add_text(BufferedImage image, String text)
	{
		//convert all items to byte arrays: image, message, message length
		byte img[]  = imageManager.get_byte_data(image);
		byte msg[] = text.getBytes();
		byte len[]   = bit_conversion(msg.length);
		try
		{
			encode_text(img, len,  0); //0 first position 
			encode_text(img, msg, 32); //4 bytes of space for length: 4bytes*8bit = 32 bits
			System.out.println("msg: " + msg.length);
			System.out.println("len_bitconversion: ");
			System.out.println(img.length);
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, 
"Target File cannot hold message!", "Error",JOptionPane.ERROR_MESSAGE);
		}
		return image;
	}
	
	
	
	
	/*
	 *Gernerates proper byte format of an integer
	 *@param i The integer to convert
	 *@return Returns a byte[4] array converting the supplied integer into bytes
	 */
	private byte[] bit_conversion(int i)
	{
		//originally integers (ints) cast into bytes
		//byte byte7 = (byte)((i & 0xFF00000000000000L) >>> 56);
		//byte byte6 = (byte)((i & 0x00FF000000000000L) >>> 48);
		//byte byte5 = (byte)((i & 0x0000FF0000000000L) >>> 40);
		//byte byte4 = (byte)((i & 0x000000FF00000000L) >>> 32);
		
		//only using 4 bytes
		byte byte3 = (byte)((i & 0xFF000000) >>> 24); //0
		byte byte2 = (byte)((i & 0x00FF0000) >>> 16); //0
		byte byte1 = (byte)((i & 0x0000FF00) >>> 8 ); //0
		byte byte0 = (byte)((i & 0x000000FF)	   );
		//{0,0,0,byte0} is equivalent, since all shifts >=8 will be 0
		return(new byte[]{byte3,byte2,byte1,byte0});
	}
	
	/*
	 *Encode an array of bytes into another array of bytes at a supplied offset
	 *@param image	 Array of data representing an image
	 *@param addition Array of data to add to the supplied image data array
	 *@param offset	  The offset into the image array to add the addition data
	 *@return Returns data Array of merged image and addition data
	 */
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
				System.out.println("offset: " + offset);
				offset++;
			}
		}
		return image;
	}
	
	/*
	 *Retrieves hidden text from an image
	 *@param image Array of data, representing an image
	 *@return Array of data which contains the hidden text
	 */
	private byte[] decode_text(byte[] image)
	{
		int length = 0;
		int offset  = 32;
		//loop through 32 bytes of data to determine text length
		for(int i=24; i<32; ++i) //i=24 will also work, as only the 4th byte contains real data
		{
			length = (length << 1) | (image[i] & 1);
		}
		
		System.out.println("length: " + length);
		byte[] result = new byte[length];
		
		//loop through each byte of text
		for(int b=0; b<result.length; ++b )
		{
			//loop through each bit within a byte of text
			for(int i=0; i<8; ++i, ++offset)
			{
				//assign bit: [(new byte value) << 1] OR [(text byte) AND 1]
				result[b] = (byte)((result[b] << 1) | (image[offset] & 1));
			}
		}
		return result;
	}
	
	public void hashGenerate(String text) throws Exception 
	{
		
		byte[] byteOfMessage = text.getBytes("UTF-8");
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] digest = md.digest(byteOfMessage);
		
		System.out.println("md: " + md);
		System.out.println("byte: " + digest.toString());
		
		
		
	}
	
	 public  String getHash(String txt, String hashType) {
	        try {
	                    java.security.MessageDigest md = java.security.MessageDigest.getInstance(hashType);
	                    byte[] array = md.digest(txt.getBytes());
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

	 public String getRSAKey(String message) throws Exception{
			// Get an instance of the RSA key generator
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			// Generate the keys — might take sometime on slow computers
			KeyPair myPair = kpg.generateKeyPair();
			
			// Get an instance of the Cipher for RSA encryption/decryption
			Cipher c = Cipher.getInstance("RSA");
			// Initiate the Cipher, telling it that it is going to Encrypt, giving it the public key
			c.init(Cipher.ENCRYPT_MODE, myPair.getPublic()); 
			
			String publicKey = encodeBase64(myPair.getPublic().getEncoded());
			System.out.println("Public key: " + publicKey);
			SealedObject myEncryptedMessage= new SealedObject( message, c);
	
			
			Cipher dec = Cipher.getInstance("RSA");
			// Initiate the Cipher, telling it that it is going to Decrypt, giving it the private key
			dec.init(Cipher.DECRYPT_MODE, myPair.getPrivate());
			String privateKey = encodeBase64(myPair.getPrivate().getEncoded());
			System.out.println("Private key: " + privateKey);
			String result = (String) myEncryptedMessage.getObject(dec);
			System.out.println("foo = "+ result);
			
		return publicKey;
		}
	 
	 public String encodeBase64(byte[] keyByte){
		 Base64.Encoder encoder = Base64.getEncoder();
		 String keyString = encoder.encodeToString(keyByte);
		 
		 return keyString;
	 }
	 
	 public void decodePrivateKey(String publicKey, String privateKey) throws Exception{
		 
			
			byte[] decoder = Base64.getDecoder().decode(privateKey.getBytes("UTF-8"));
	
			System.out.println("Public key: " + decoder.toString());
			
			
			
			Cipher dec = Cipher.getInstance("RSA");
			// Initiate the Cipher, telling it that it is going to Decrypt, giving it the private key
			//Takes your byte array of the key as constructor parameter
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(decoder);

			//Takes algorithm used to generate keys (DSA, RSA, DiffieHellman, etc.) as 1st parameter
			//Takes security provider (SUN, BouncyCastle, etc.) as second parameter
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");

			//Creates a new PublicKey object
			PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
			dec.init(Cipher.DECRYPT_MODE, pubKey);
			SealedObject myEncryptedMessage= new SealedObject(privateKey ,dec);
			
			System.out.println("Private key: " + privateKey);
			String result = (String) myEncryptedMessage.getObject(dec);
			System.out.println("foo = "+ result);
			
	
		}

}

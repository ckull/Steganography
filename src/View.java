import java.awt.Checkbox;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.*; 

/**
 *
 * This class is our program user interface which need to show to user for process every method
 * We separate into 3 parts encode panel, decode panel and image panel
 * - Encode panel need to receive text by type some strings in the panel, import image from device
 * by press encode button and give the name to the output file that will appear in your device
 * - Decode panel need to receive the image file that user want to decode for extract hidden message from the image
 * - Image panel use for show original picture and the picture that already encode text into the image
 *
 * Created by Kullapat Siribodhi 58070503404
 *            Thanadol Nimitchuchai 58070503442, 20 Nov 2017
 * **/
public class View extends JFrame
{
	JFrame f = new JFrame();
	private static int WIDTH = 600;
	private static int HEIGHT = 500;
	
	private JTextArea input;
	private JTextArea output;
	private JPanel encode_panel;
	private JPanel decode_panel;
	private JPanel image_panel;
	private JButton encodeButton;
	private JButton decodeButton;
	private JTabbedPane tp;
	private JLabel image_input;
	private JLabel image_output;
	private JCheckBox md5; /* type of hash function */
	private JCheckBox sha1; /* type of hash function */
	private JCheckBox sha256; /* type of hash function */
	private JCheckBox nText; /* type of hash function */
	
	public View(String name)
	{
		super(name);
	 
	    encode_panel = new JPanel();  
	    decode_panel = new JPanel();  
	    image_panel = new JPanel();
	    tp = new JTabbedPane();
	    
	    tp.add("encode", new Encode_Panel());  
	    tp.add("decode", new Decode_Panel());   
	    tp.add("result", new Image_Panel());
	    f.add(tp);  

	    f.setResizable(true);
	    f.setSize(WIDTH,HEIGHT);
	    f.setVisible(true); 
	}
	
	public JCheckBox getMD5(){
		return md5;
	}
	
	public JCheckBox getSHA1(){
		return sha1;
	}
	
	public JCheckBox getSHA256(){
		return sha256;
	}
	
	public JCheckBox getNTEXT(){
		return nText;
	}
	
	public JTextArea getInputText() {
		return input;
	}
	
	public JTextArea getOutputText(){
		return output;
	}
	
	public JPanel getEncodePanel() {
		return encode_panel;
	}
	
	public JPanel getDecodePanel() {
		return decode_panel;
	}
	
	public JLabel getImageInput(){
		return image_input;
	}
	
	public JLabel getImageOutput(){
		return image_output;
	}
	
	public JButton getEncodeButton() {
		return encodeButton;
	}
	
	public JButton getDecodeButton() {
		return decodeButton;
	}
	
	public JTabbedPane getTabPanel(){
		return tp;
	}
	
	/** 
	 * This class is the panel of the encode method which need to input the text for
	 * hide in the image like secret message. There are 4 types of hash function types which have different algorithms and different
	 * result. The encode button is the main button that start the process and we need to define output
	 * filename which will appear in your device after encode processing succeed.
	 * 
	 * **/
	private class  Encode_Panel extends JPanel{
		public Encode_Panel(){
			GridBagLayout layout = new GridBagLayout(); 
			GridBagConstraints layoutConstraints = new GridBagConstraints(); 
			setLayout(layout);		
			
			input = new JTextArea();
			layoutConstraints.gridx 	= 0; layoutConstraints.gridy = 0; 
			layoutConstraints.gridwidth = 20; layoutConstraints.gridheight = 1; 
			layoutConstraints.fill 		= GridBagConstraints.BOTH; 
			layoutConstraints.insets 	= new Insets(0,0,0,0); 
			layoutConstraints.anchor 	= GridBagConstraints.CENTER; 
			layoutConstraints.weightx 	= 1.0; layoutConstraints.weighty = 50.0;
			JScrollPane scroll = new JScrollPane(input,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
			layout.setConstraints(scroll,layoutConstraints);
	    	add(scroll);
	    	
	    	// Line wrap inside JTextArea
	    	input.setLineWrap(true);
	    	input.setWrapStyleWord(true);
	    	
	    	encodeButton = new JButton("Encode");
	    	layoutConstraints.gridx 	= 0; layoutConstraints.gridy = 1; 
			layoutConstraints.gridwidth = 1; layoutConstraints.gridheight = 1; 
			layoutConstraints.fill 		= GridBagConstraints.BOTH; 
			layoutConstraints.insets 	= new Insets(0,-5,-5,-5); 
			layoutConstraints.anchor 	= GridBagConstraints.CENTER; 
			layoutConstraints.weightx 	= 1.0; layoutConstraints.weighty = 1.0;
			layout.setConstraints(encodeButton,layoutConstraints);
	    	add(encodeButton);
	    	
	    	ButtonGroup group = new ButtonGroup();
	    	nText = new JCheckBox("NORMAL TEXT");
	    	layoutConstraints.gridx = 0; 
	    	layoutConstraints.gridy = 2;
	    	layoutConstraints.fill = GridBagConstraints.HORIZONTAL;
	    	layoutConstraints.anchor 	= GridBagConstraints.SOUTH; 
	    	layout.setConstraints(nText ,layoutConstraints);
	    	
	    	md5 = new JCheckBox("MD5");
	    	layoutConstraints.gridx = 1; 
	    	layoutConstraints.gridy = 2;
	    	layoutConstraints.fill = GridBagConstraints.HORIZONTAL;
	    	layoutConstraints.anchor 	= GridBagConstraints.SOUTH; 
	    	layout.setConstraints(md5 ,layoutConstraints);
	    	
	    	sha1 = new JCheckBox("SHA-1");
	    	layoutConstraints.gridx = 2; 
	    	layoutConstraints.gridy = 2;
	    	layoutConstraints.fill = GridBagConstraints.HORIZONTAL;
	    	layoutConstraints.anchor 	= GridBagConstraints.SOUTH; 
	    	layout.setConstraints(sha1 ,layoutConstraints);
	    	
	    	sha256 = new JCheckBox("SHA-256");
	    	layoutConstraints.gridx = 3; 
	    	layoutConstraints.gridy = 2;
	    	layoutConstraints.fill = GridBagConstraints.HORIZONTAL;
	    	layoutConstraints.anchor 	= GridBagConstraints.SOUTH; 
	    	layout.setConstraints(sha256 ,layoutConstraints);
	    	
	    	group.add(md5);
	    	group.add(sha1);
	    	group.add(sha256);
	    	group.add(nText);
	    
	    	add(md5);
	    	add(sha1);
	    	add(sha256);
	    	add(nText);
	    	
	    	
		}
	}
	
	/**
	 *  This class is the panel of decode method which will show the string after we extract the hidden text
	 * from the image. The decode button is the main button that start the process and we need to input the image file
	 * for start decode processing.
	 * 
	 * **/
	private class Decode_Panel extends JPanel{
		public Decode_Panel(){
			GridBagLayout layout = new GridBagLayout(); 
			GridBagConstraints layoutConstraints = new GridBagConstraints(); 
			setLayout(layout);		
	    	
	    	decodeButton = new JButton("Decode");
	    	layoutConstraints.gridx 	= 0; layoutConstraints.gridy = 1; 
			layoutConstraints.gridwidth = 1; layoutConstraints.gridheight = 1; 
			layoutConstraints.fill 		= GridBagConstraints.BOTH; 
			layoutConstraints.insets 	= new Insets(0,-5,-5,-5); 
			layoutConstraints.anchor 	= GridBagConstraints.CENTER; 
			layoutConstraints.weightx 	= 1.0; layoutConstraints.weighty = 1.0;
			layout.setConstraints(decodeButton,layoutConstraints);
	    	add(decodeButton);
	    	
	    	output = new JTextArea();
			layoutConstraints.gridx 	= 0; layoutConstraints.gridy = 0; 
			layoutConstraints.gridwidth = 20; layoutConstraints.gridheight = 1; 
			layoutConstraints.fill 		= GridBagConstraints.BOTH; 
			layoutConstraints.insets 	= new Insets(0,0,0,0); 
			layoutConstraints.anchor 	= GridBagConstraints.CENTER; 
			layoutConstraints.weightx 	= 1.0; layoutConstraints.weighty = 50.0;
			JScrollPane scroll = new JScrollPane(output,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
			layout.setConstraints(scroll,layoutConstraints);
	    	add(scroll);
	    	
	    	// Line Wrap inside JTextArea
	    	output.setLineWrap(true);
	    	output.setWrapStyleWord(true);
		}
	}
	
	/** 
	 * This class is the panel that use for show original image and the image which already encode by add text
	 * into image by byte array replacement. Two pictures will be very similarly.
	 * 
	 * **/
	private class Image_Panel extends JPanel {
		public Image_Panel()
		{
			GridLayout layout = new GridLayout(0, 2); 
			setLayout(layout);
			
			image_input = new JLabel("Input", SwingConstants.CENTER);
			image_output = new JLabel("Output", SwingConstants.CENTER);
			image_input.setVerticalAlignment(SwingConstants.TOP);
			image_output.setVerticalAlignment(SwingConstants.TOP);
			
			JScrollPane scroll1 = new JScrollPane(image_input,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
			image_input.setHorizontalAlignment(JLabel.CENTER);
	    	add(scroll1);
	    	
	    	JScrollPane scroll2 = new JScrollPane(image_output,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	    	JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
	    	image_output.setHorizontalAlignment(JLabel.CENTER);
	    	add(scroll2);

	    }
	}
	

}




import java.io.File;
import java.io.IOException;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.imageio.ImageIO;
import javax.swing.*; 


public class Controller
{
	private JButton encodeButton;
	private JButton decodeButton;
	private JButton decodePKButton;
	private View view;
	private Steganography model;
	private Controller controller;
	private JPanel encodePanel;
	private JPanel decodePanel;
	private JLabel imageInput;
	private JTabbedPane tabPanel;
	private boolean encoded = false;
	private String inputText;
	private String ext;
	private String fileName;
	private String filePath;
	private JTextArea output;
	private JTextArea input;
	private String outputFileName;
	private JLabel imageOutput;
	private JCheckBox md5;
	private JCheckBox sha1;
	private JCheckBox sha256;
	private JCheckBox rsa;
	
	public Controller(View v, Steganography m){
		view = v;
		model = m;
		
		encodePanel = view.getEncodePanel();
		decodePanel = view.getDecodePanel();
		imageInput = view.getImageInput();
		imageOutput = view.getImageOutput();
		
		encodeButton = view.getEncodeButton();
		decodeButton = view.getDecodeButton();
		decodePKButton = view.getDecodePKButton();
		
		output = view.getOutputText();
		input = view.getInputText();
		
		tabPanel = view.getTabPanel();
		
		md5 = view.getMD5();
		sha1 = view.getSHA1();
		sha256 = view.getSHA256();
		rsa = view.getRSA();
		
		encodeButton.addActionListener(new EncodeButton());
		decodeButton.addActionListener(new DecodeButton());
		decodePKButton.addActionListener(new DecodePKButton());
		
	}
	
	private class EncodeButton implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			JFileChooser chooser = new JFileChooser("./");
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			chooser.setFileFilter(new Image_Filter());
			int returnVal = chooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File fileSelected = chooser.getSelectedFile();
				try{
					inputText = input.getText();
					ext = Image_Filter.getExtension(fileSelected);
					fileName = fileSelected.getName();
					filePath = fileSelected.getPath();
					System.out.println("Before " + "InputText: " + inputText + " ext: " + ext + " fileName: " + fileName + " filePath: " + filePath);
					filePath = filePath.substring(0,filePath.length()-fileName.length()-1);
					fileName = fileName.substring(0, fileName.length()-4);
					System.out.println("InputText: " + inputText + " ext: " + ext + " fileName: " + fileName + " filePath: " + filePath);
					outputFileName = JOptionPane.showInputDialog("Please assign an output file name: ");
					
					if(md5.isSelected()){
						if(model.encode(filePath, fileName, ext, outputFileName, inputText, "md5")){
							JOptionPane.showMessageDialog(view, "Successfully encoded an image");
							SuccessEncoded(filePath, fileName, ext, outputFileName);
						}else{
							JOptionPane.showMessageDialog(view, "Cannot encode files");
						}
					}else if(sha1.isSelected()){
						if(model.encode(filePath, fileName, ext, outputFileName, inputText, "SHA-1")){
							JOptionPane.showMessageDialog(view, "Successfully encoded an image");
							SuccessEncoded(filePath, fileName, ext, outputFileName);
						}else{
							JOptionPane.showMessageDialog(view, "Cannot encode files");
						}
					}else if(sha256.isSelected()){
						if(model.encode(filePath, fileName, ext, outputFileName, inputText, "SHA-256")){
							JOptionPane.showMessageDialog(view, "Successfully encoded an image");
							SuccessEncoded(filePath, fileName, ext, outputFileName);
						}else{
							JOptionPane.showMessageDialog(view, "Cannot encode files");
						}
					}else if(rsa.isSelected()){
							if(model.encode(filePath, fileName, ext, outputFileName, inputText, true)){
								JOptionPane.showMessageDialog(view, "Successfully encoded an image");
								SuccessEncoded(filePath, fileName, ext, outputFileName);
							}else{
								JOptionPane.showMessageDialog(view, "Cannot encode files");
							}
							
					}
					else{
						if(model.encode(filePath, fileName, ext, outputFileName, inputText)){
							JOptionPane.showMessageDialog(view, "Successfully encoded an image");
							SuccessEncoded(filePath, fileName, ext, outputFileName);
						}else{
							JOptionPane.showMessageDialog(view, "Cannot encode files");
						}
					}
					
					
					
				}
				catch(Exception except) {
					JOptionPane.showMessageDialog(view, "File cannot open");
				}
			}
			
		}
	}
	
	public void Update(){
		outputFileName = "";
		encoded = false;
		input.setText("");
		imageInput.setIcon(null);
	}
	
	private class DecodePKButton implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(!encoded){
				JFileChooser chooser = new JFileChooser("./");
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				chooser.setFileFilter(new Image_Filter());
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File fileSelected = chooser.getSelectedFile();
					try{
						ext = Image_Filter.getExtension(fileSelected);
						outputFileName = fileSelected.getName();
						filePath = fileSelected.getPath();
						filePath = filePath.substring(0,filePath.length()-outputFileName.length()-1);
						outputFileName = outputFileName.substring(0, outputFileName.length()-4);
						System.out.println("outputanme:"+ outputFileName);
						System.out.println("Length outputFile Byte:" + new File(filePath + "/" + outputFileName + ".png").length() + "byte");
						
					}
					catch(Exception except){
						JOptionPane.showMessageDialog(view, "File cannot open");
						System.out.println("outputanme:"+ outputFileName);
					}
				}
			}
				String message = model.decode(filePath, outputFileName);
				if(message != ""){
					String privateKey = JOptionPane.showInputDialog("Please assign private key(Base 64): ");
					try
					{
						model.decodePrivateKey(message, privateKey);
					}
					catch (Exception e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(view, message);
					try{
						imageInput.setIcon(new ImageIcon(ImageIO.read(new File(filePath + "/" + outputFileName + ".png"))));
					}
					catch(Exception except){
						JOptionPane.showMessageDialog(view, "Cannot show result");
					}
					
//					output.setText(message);
					
				}else{
					JOptionPane.showMessageDialog(view, "No encoded Text");
				}
			}
	}
	
	private class DecodeButton implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(!encoded){
				JFileChooser chooser = new JFileChooser("./");
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				chooser.setFileFilter(new Image_Filter());
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File fileSelected = chooser.getSelectedFile();
					try{
						ext = Image_Filter.getExtension(fileSelected);
						outputFileName = fileSelected.getName();
						filePath = fileSelected.getPath();
						filePath = filePath.substring(0,filePath.length()-outputFileName.length()-1);
						outputFileName = outputFileName.substring(0, outputFileName.length()-4);
						System.out.println("outputanme:"+ outputFileName);
						System.out.println("Length outputFile Byte:" + new File(filePath + "/" + outputFileName + ".png").length() + "byte");
						
						
					}
					catch(Exception except){
						JOptionPane.showMessageDialog(view, "File cannot open");
						System.out.println("outputanme:"+ outputFileName);
					}
				}
			}
			
			String message = model.decode(filePath, outputFileName);
			if(message != ""){
				JOptionPane.showMessageDialog(view, message);
				try{
					imageInput.setIcon(new ImageIcon(ImageIO.read(new File(filePath + "/" + outputFileName + ".png"))));
				}
				catch(Exception except){
					JOptionPane.showMessageDialog(view, "Cannot show result");
				}
				
				System.out.println(message);
				output.setText(message);
				
			}else{
				JOptionPane.showMessageDialog(view, "No encoded Text");
			}
		}
	}

	public void SuccessEncoded(String filePath, String fileName, String ext, String outputFileName) throws Exception{
		tabPanel.setSelectedIndex(2);
		
		imageInput.setIcon(new ImageIcon(ImageIO.read(new File(filePath + "/" + fileName + "." + ext))));
		imageOutput.setIcon(new ImageIcon(ImageIO.read(new File(filePath + "/" + outputFileName + ".png"))));
		encoded = true;
		System.out.println("Length inputFile Byte:" + new File(filePath + "/" + fileName + ".png").length() + "byte");
		System.out.println("Length outputFile Byte:" + new File(filePath + "/" + outputFileName + ".png").length() + "byte");
	}
	
	public static void main(String args[])
	{
		new Controller(new View("Steganography"), new Steganography());
	}
	
}

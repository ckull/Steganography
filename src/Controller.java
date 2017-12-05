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
//	private JButton decodePKButton;
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
	private JCheckBox nText;

	
	public Controller(View v, Steganography m){
		view = v;
		model = m;
		
		encodePanel = view.getEncodePanel();
		decodePanel = view.getDecodePanel();
		imageInput = view.getImageInput();
		imageOutput = view.getImageOutput();
		
		encodeButton = view.getEncodeButton();
		decodeButton = view.getDecodeButton();
		
		output = view.getOutputText();
		input = view.getInputText();
		
		tabPanel = view.getTabPanel();
		
		md5 = view.getMD5();
		sha1 = view.getSHA1();
		sha256 = view.getSHA256();
		nText = view.getNTEXT();
		
		encodeButton.addActionListener(new EncodeButton());
		decodeButton.addActionListener(new DecodeButton());
		
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
								JOptionPane.showMessageDialog(view, "successfully encoded an image");
								successEncoded(filePath, fileName, ext, outputFileName);
							}else{
								JOptionPane.showMessageDialog(view, "Cannot encode files");
							}
						}else if(sha1.isSelected()){
							if(model.encode(filePath, fileName, ext, outputFileName, inputText, "SHA-1")){
								JOptionPane.showMessageDialog(view, "successfully encoded an image");
								successEncoded(filePath, fileName, ext, outputFileName);
							}else{
								JOptionPane.showMessageDialog(view, "Cannot encode files");
							}
						}else if(sha256.isSelected()){
							if(model.encode(filePath, fileName, ext, outputFileName, inputText, "SHA-256")){
								JOptionPane.showMessageDialog(view, "successfully encoded an image");
								successEncoded(filePath, fileName, ext, outputFileName);
							}else{
								JOptionPane.showMessageDialog(view, "Cannot encode files");
							}
						}
						else if(nText.isSelected()){
							if(model.encode(filePath, fileName, ext, outputFileName, inputText)){
								JOptionPane.showMessageDialog(view, "successfully encoded an image");
								successEncoded(filePath, fileName, ext, outputFileName);
							}else{
								JOptionPane.showMessageDialog(view, "Cannot encode files");
							}
						}else{
							if(model.encode(filePath, fileName, ext, outputFileName, inputText)){
								JOptionPane.showMessageDialog(view, "successfully encoded an image");
								successEncoded(filePath, fileName, ext, outputFileName);
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
		
	private class DecodeButton implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(!encoded){
				JFileChooser chooser = new JFileChooser("./");
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				chooser.setFileFilter(new Image_Filter());
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File fileSelected = chooser.getSelectedFile();
						try	{
							ext = Image_Filter.getExtension(fileSelected);
							outputFileName = fileSelected.getName();
							filePath = fileSelected.getPath();
							filePath = filePath.substring(0,filePath.length()-outputFileName.length()-1);
							outputFileName = outputFileName.substring(0, outputFileName.length()-4);
							System.out.println("outputname:"+ outputFileName);
							System.out.println("Length outputFile Byte:" + new File(filePath + "/" + outputFileName + ".png").length() + "byte");
						}
						catch(Exception except){
							JOptionPane.showMessageDialog(view, except);
							System.out.println("outputname:"+ outputFileName);
						}
				}
			}else{
				String message = model.decode(filePath, outputFileName);
				if(message != ""){
					try{
						imageInput.setIcon(new ImageIcon(ImageIO.read(new File(filePath + "/" + outputFileName + ".png"))));
					}
					catch(Exception except){
						JOptionPane.showMessageDialog(view, "Cannot show result");
					}
					
					System.out.println("message: " + message);
					output.setText(message);
					
				}else{
					JOptionPane.showMessageDialog(view, "No encoded Text");
				}
			}
		}
	}

	public void successEncoded(String filePath, String fileName, String ext, String outputFileName) throws Exception{
		tabPanel.setSelectedIndex(2);
		
		imageInput.setIcon(new ImageIcon(ImageIO.read(new File(filePath + "/" + fileName + "." + ext))));
		imageOutput.setIcon(new ImageIcon(ImageIO.read(new File(filePath + "/" + outputFileName + ".png"))));
		encoded = true;
		JOptionPane.showMessageDialog(view, "Input file size: " + new File(filePath + "/" + fileName + ".png").length() + " byte" + "\n"
		+ "Output file size: " + new File(filePath + "/" + outputFileName + ".png").length() + " byte");	
		System.out.println("Length of input file: " + new File(filePath + "/" + fileName + ".png").length() + " byte");
		System.out.println("Length of output file: " + new File(filePath + "/" + outputFileName + ".png").length() + " byte");
	}
	
	public static void main(String args[])
	{
		new Controller(new View("Steganography"), new Steganography());
	}
	
}

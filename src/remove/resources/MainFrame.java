package remove.resources;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtFile;

	private File lintFile;
	private JTextArea textArea;
	private JButton btnAnalyze;
	private JButton btnDeleteFiles;
	private JTextField textFieldModule;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {		
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("Android remover unused resources");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 950, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnChooseFile = new JButton("Choose file");
		btnChooseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionChooseFile();
			}
		});
		btnChooseFile.setBounds(816, 18, 117, 29);
		contentPane.add(btnChooseFile);

		btnAnalyze = new JButton("Analyze");
		btnAnalyze.setEnabled(false);
		btnAnalyze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionAnalyze();
			}
		});
		btnAnalyze.setBounds(816, 46, 117, 29);
		contentPane.add(btnAnalyze);

		btnDeleteFiles = new JButton("Delete files");
		btnDeleteFiles.setEnabled(false);
		btnDeleteFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 actionDeleteFiles();
			}
		});
		btnDeleteFiles.setBounds(816, 76, 117, 29);
		contentPane.add(btnDeleteFiles);

		JButton btnClearLog = new JButton("Clear log");
		btnClearLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionClearLog();
			}
		});
		btnClearLog.setBounds(816, 106, 117, 29);
		contentPane.add(btnClearLog);

		txtFile = new JTextField();
		txtFile.setText("Select lint xml file");
		txtFile.setEnabled(false);
		txtFile.setEditable(false);
		txtFile.setBounds(81, 18, 723, 26);
		contentPane.add(txtFile);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 177, 910, 377);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);

		textFieldModule = new JTextField();
		textFieldModule.setEnabled(false);
		textFieldModule.setBounds(81, 46, 723, 26);
		contentPane.add(textFieldModule);
		textFieldModule.setColumns(10);

		JLabel lblNewLabel = new JLabel("Lint file:");
		lblNewLabel.setBounds(20, 23, 65, 16);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Module:");
		lblNewLabel_1.setBounds(20, 51, 61, 16);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("✔ res/layout");
		lblNewLabel_2.setBounds(20, 106, 99, 16);
		contentPane.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("✔ res/drawable");
		lblNewLabel_3.setBounds(20, 125, 117, 16);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("✔ res/anim");
		lblNewLabel_4.setBounds(20, 144, 99, 16);
		contentPane.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("✔ res/color");
		lblNewLabel_5.setBounds(149, 106, 99, 16);
		contentPane.add(lblNewLabel_5);
		
		JLabel lblNewLabel_2_1 = new JLabel("✔ res/font");
		lblNewLabel_2_1.setBounds(149, 125, 99, 16);
		contentPane.add(lblNewLabel_2_1);
		
		JLabel lblNewLabel_3_1 = new JLabel("✔ res/menu");
		lblNewLabel_3_1.setBounds(149, 144, 117, 16);
		contentPane.add(lblNewLabel_3_1);
		
		JLabel lblNewLabel_4_1 = new JLabel("✔ res/transition");
		lblNewLabel_4_1.setBounds(278, 106, 117, 16);
		contentPane.add(lblNewLabel_4_1);
		
		JLabel lblNewLabel_5_1 = new JLabel("✔ res/xml");
		lblNewLabel_5_1.setBounds(278, 125, 99, 16);
		contentPane.add(lblNewLabel_5_1);
		
		JLabel lblNewLabel_2_2 = new JLabel("✔ res/values");
		lblNewLabel_2_2.setBounds(278, 144, 99, 16);
		contentPane.add(lblNewLabel_2_2);
		
		JLabel lblNewLabel_6 = new JLabel("Resources supported:");
		lblNewLabel_6.setBounds(20, 81, 215, 16);
		contentPane.add(lblNewLabel_6);
	}
	
	// Actions region

	private void actionChooseFile() {
		File selectedFile = openFile("Select lint xml path");

		if (selectedFile == null) {
			return;
		}

		boolean isXml = selectedFile.getAbsolutePath().endsWith(".xml");

		btnDeleteFiles.setEnabled(isXml);
		btnAnalyze.setEnabled(isXml);
		textFieldModule.setEnabled(isXml);

		if (isXml) {
			lintFile = selectedFile;
			txtFile.setText(selectedFile.getAbsolutePath());
			log("XML file successfully selected.");
			return;
		} else {
			log("You must select an XML file.");
		}
	}

	private void actionAnalyze() {
		log("Loading...");
		List<UnusedResource> listUnusedResource = getUnusedResources();

		if (listUnusedResource == null) {
			return;
		}
		

		List<UnusedResource> layoutList = listUnusedResource.stream()
				.filter(article -> article.getResourceType() == ResourceTypeEnum.LAYOUT).collect(Collectors.toList());
		List<UnusedResource> drawableList = listUnusedResource.stream()
				.filter(article -> article.getResourceType() == ResourceTypeEnum.DRAWABLE).collect(Collectors.toList());
		List<UnusedResource> animList = listUnusedResource.stream()
				.filter(article -> article.getResourceType() == ResourceTypeEnum.ANIM).collect(Collectors.toList());
		List<UnusedResource> colorList = listUnusedResource.stream()
				.filter(article -> article.getResourceType() == ResourceTypeEnum.COLOR).collect(Collectors.toList());
		List<UnusedResource> fontList = listUnusedResource.stream()
				.filter(article -> article.getResourceType() == ResourceTypeEnum.FONT).collect(Collectors.toList());
		List<UnusedResource> menuList = listUnusedResource.stream()
				.filter(article -> article.getResourceType() == ResourceTypeEnum.MENU).collect(Collectors.toList());
		List<UnusedResource> navigationList = listUnusedResource.stream()
				.filter(article -> article.getResourceType() == ResourceTypeEnum.NAVIGATION).collect(Collectors.toList());
		List<UnusedResource> transitionList = listUnusedResource.stream()
				.filter(article -> article.getResourceType() == ResourceTypeEnum.TRANSITION).collect(Collectors.toList());
		List<UnusedResource> xmlList = listUnusedResource.stream()
				.filter(article -> article.getResourceType() == ResourceTypeEnum.XML).collect(Collectors.toList());
		List<UnusedResource> valuesList = listUnusedResource.stream()
				.filter(article -> article.getResourceType() == ResourceTypeEnum.VALUES).collect(Collectors.toList());
		List<UnusedResource> otherList = listUnusedResource.stream()
				.filter(article -> article.getResourceType() == ResourceTypeEnum.OTHER).collect(Collectors.toList());

		log("res/layout: " + layoutList.size());
		log("res/drawable: " + drawableList.size());
		log("res/anim: " + animList.size());
		log("res/color: " + colorList.size());
		log("res/font: " + fontList.size());
		log("res/menu: " + menuList.size());
		log("res/navigation: " + navigationList.size());
		log("res/transition: " + transitionList.size());
		log("res/xml: " + xmlList.size());
		log("res/values: " + valuesList.size());
		log("other: " + otherList.size());

		log("TOTAL RESOURCES UNUSED: " + listUnusedResource.size());
		log("\nRESULT: SUCCESS ANALYSIS");
	}
	
	private void actionDeleteFiles() {
		log("Deleting unused files, it will take a few seconds...");
		Timer timer = new Timer(1000, new ActionListener() {
			  @Override
			  public void actionPerformed(ActionEvent arg0) {
				  deleteUnusedFiles();
			  }
			});
			timer.setRepeats(false);
			timer.start();
		
	}
	
	private void actionClearLog() {
		textArea.setText("");
	}
	
	// end region

	private List<UnusedResource> getUnusedResources() {
		List<UnusedResource> listUnusedResource = new ArrayList<UnusedResource>();
		String lintPath = lintFile.getAbsolutePath();

		try {
			// Read document
			Document doc = getDocument(lintPath);

			if (doc == null) {
				return null;
			}

			NodeList nodeList = doc.getElementsByTagName("issue");

			for (int i = 0; i < nodeList.getLength(); i++) {

				Node node = nodeList.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					String id = element.getAttribute("id");

					if (id.replace("_", "").equals("UnusedResources")) {

						Node locationNode = element.getElementsByTagName("location").item(0);
						Element locationElement = (Element) locationNode;

						String strLocation = locationElement.getAttribute("file");

						String module = textFieldModule.getText().trim();

						if (strLocation.contains(module)) {

							String errorLine1 = element.getAttribute("errorLine1");
							
							System.out.println("Resource analyzed: " + errorLine1 + "\n");

							
							//R.style.PayModApp.Widget.CheckoutWeb.Button.Primary.Default
							String resourceXmlName = null;
							
							if(errorLine1.contains("name=")) {
								String[] splitResourceName = errorLine1.split("\"");

								resourceXmlName = splitResourceName[1];
							}
							
							
							UnusedResource unusedResource = new UnusedResource(strLocation, resourceXmlName);
							listUnusedResource.add(unusedResource);

							log(module.toUpperCase());
							log(unusedResource.getFile() + "\n");
						}
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log("\n" + e.toString());
			log("\nRESULT: FAILED ANALYSIS");
			return null;
		}

		return listUnusedResource;
	}

	private void deleteUnusedFiles() {

		List<UnusedResource> listUnusedResource = getUnusedResources();

		if (listUnusedResource == null) {
			return;
		}

		// Filter
		
		List<UnusedResource> layoutList = listUnusedResource.stream()
				.filter(article -> article.getResourceType() == ResourceTypeEnum.LAYOUT).collect(Collectors.toList());

		List<UnusedResource> drawableList = listUnusedResource.stream()
				.filter(article -> article.getResourceType() == ResourceTypeEnum.DRAWABLE).collect(Collectors.toList());
		
		List<UnusedResource> animList = listUnusedResource.stream()
				.filter(article -> article.getResourceType() == ResourceTypeEnum.ANIM).collect(Collectors.toList());
		
		List<UnusedResource> colorList = listUnusedResource.stream()
				.filter(article -> article.getResourceType() == ResourceTypeEnum.COLOR).collect(Collectors.toList());
		
		List<UnusedResource> fontList = listUnusedResource.stream()
				.filter(article -> article.getResourceType() == ResourceTypeEnum.FONT).collect(Collectors.toList());
		
		List<UnusedResource> menuList = listUnusedResource.stream()
				.filter(article -> article.getResourceType() == ResourceTypeEnum.MENU).collect(Collectors.toList());
		
		List<UnusedResource> navigationList = listUnusedResource.stream()
				.filter(article -> article.getResourceType() == ResourceTypeEnum.NAVIGATION).collect(Collectors.toList());
		
		List<UnusedResource> transitionList = listUnusedResource.stream()
				.filter(article -> article.getResourceType() == ResourceTypeEnum.TRANSITION).collect(Collectors.toList());
		
		List<UnusedResource> xmlList = listUnusedResource.stream()
				.filter(article -> article.getResourceType() == ResourceTypeEnum.XML).collect(Collectors.toList());
		
		List<UnusedResource> valuesList = listUnusedResource.stream()
				.filter(article -> article.getResourceType() == ResourceTypeEnum.VALUES).collect(Collectors.toList());
	
		List<UnusedResource> otherList = listUnusedResource.stream()
				.filter(article -> article.getResourceType() == ResourceTypeEnum.OTHER).collect(Collectors.toList());
		
		// Generated

		List<UnusedResource> layoutGenerateList = generateLayoutFilesPath(layoutList);
		
		List<UnusedResource> drawableGenerateList = generateDrawablesFilesPath(drawableList);

		List<UnusedResource> valuesGenerateList = generateValuesFilesPath(valuesList);
		
		int layoutDeleteFiles = deleteFiles(layoutGenerateList);
		int drawableDeleteFiles = deleteFiles(drawableGenerateList);
		int animDeleteFiles = deleteFiles(animList);
		int colorDeleteFiles = deleteFiles(colorList);
		int fontDeleteFiles = deleteFiles(fontList);
		int menuDeleteFiles = deleteFiles(menuList);
		//deleteFiles(navigationList);
		int transitionDeleteFiles = deleteFiles(transitionList);
		int xmlDeleteFiles = deleteFiles(xmlList);
		
		int valuesDeleteFiles = deleteUnusedResourceInXml(valuesGenerateList);

		log("res/layout files delete: " + layoutDeleteFiles);
		log("res/drawable files delete: " + drawableDeleteFiles);
		log("res/anim files delete: " + animDeleteFiles);
		log("res/color files delete: " + colorDeleteFiles);
		log("res/font files delete: " + fontDeleteFiles);
		log("res/menu files delete: " + menuDeleteFiles);
		log("res/transition files delete: " + transitionDeleteFiles);
		log("res/xml files delete: " + xmlDeleteFiles);
		log("res/values: " + valuesDeleteFiles);
		
		log("res/navigation files not delete: " + navigationList.size());
		log("other files not delete: " + otherList.size());
		log("Files were not removed, check usages \n");
		
		log("\nRESULT: SUCCESS DELETED");
	}

	private int deleteFiles(List<UnusedResource> list) {

		int filesDeleted = 0;

		for (UnusedResource unusedResource : list) {
			File fileLocation = new File(unusedResource.getFile());

			if (fileLocation.exists()) {
				if (fileLocation.delete()) {
					filesDeleted++;
					System.out.println("File Deleted: " + unusedResource.getFile() + "\n");
				} else {
					System.out.println("File not Deleted: " + unusedResource.getFile() + "\n");
				}
			} else {
				log("File " + unusedResource.getFile() + " not exists." + "\n");
			}
		}

		return filesDeleted;

	}

	private int deleteUnusedResourceInXml(List<UnusedResource> list) {

		int elementsDeleted = 0;

		for (UnusedResource unusedResource : list) {
			File fileLocation = new File(unusedResource.getFile());

			if (fileLocation.exists()) {

				System.out.println("Resource Deleted: " + unusedResource.getFile() + "\n");

				Document doc = getDocument(unusedResource.getFile());

				NodeList nodeList = doc.getDocumentElement().getElementsByTagName("*");

				for (int i = 0; i < nodeList.getLength(); i++) {
					Element element = (Element) nodeList.item(i);
					String elementName = element.getAttribute("name");

					if (elementName.equals(unusedResource.getXmlName())) {
						System.out.println("XML_FILE: " + elementName + " = " + unusedResource.getXmlName() + " ?");
						
						 Node prevElem = element.getPreviousSibling();
					        if (prevElem != null && 
					            prevElem .getNodeType() == Node.TEXT_NODE &&
					            prevElem .getNodeValue().trim().length() == 0) {
					        	element.getParentNode().removeChild(prevElem );
					        }
						
						element.getParentNode().removeChild(element);
						
						System.out.println("XML_NODE_LENGTH: " + nodeList.getLength() + "\n");
						
						if (nodeList.getLength() == 0) {
														
							if (fileLocation.exists()) {
								if (fileLocation.delete()) {
									System.out.println("File Deleted: " + unusedResource.getFile() + "\n");
								} else {
									System.out.println("File not Deleted: " + unusedResource.getFile() + "\n");
								}
							} else {
								log("File " + unusedResource.getFile() + " not exists." + "\n");
							}
							
						} else {
							String newContent = transformDocumentToString(doc);
							
							replaceContent(unusedResource.getFile(), newContent);
						}
						
						elementsDeleted++;
						
						
					}
				}

			} else {
				log("File " + unusedResource.getFile() + " not exists." + "\n");
			}
		}

		return elementsDeleted;
	}

	private String transformDocumentToString(Document doc) {
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "no");
			 transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			    //transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);

			String xmlString = result.getWriter().toString();
			return xmlString.replace(" standalone=\"no\"", "").replace("<resources", "\n<resources");
		} catch (TransformerException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	private void replaceContent(String path, String newContent) {
		File file=new File(path);
		DataOutputStream outstream= null;
		try {
			 outstream= new DataOutputStream(new FileOutputStream(file,false));
			String body = newContent;
			outstream.write(body.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(outstream != null) {
					outstream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		
	}

	private File openFile(String title) {

		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setFileFilter(new FileNameExtensionFilter("*.xml", "xml"));
		jfc.setDialogTitle(title);

		int returnValue = jfc.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			return jfc.getSelectedFile();
		} else {
			return null;
		}
	}

	private Document getDocument(String path) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(path);
			return doc;
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
			log("\n" + e.toString());
			log("\nRESULT: GET XML DOCUMENT FAILED");
			return null;
		}
	}

	private void log(String text) {
		textArea.append(text + "\n");
	}
	
	// Generate paths region

		private List<UnusedResource> generateDrawablesFilesPath(List<UnusedResource> list) {

			List<UnusedResource> drawableGenerateList = new ArrayList<UnusedResource>();

			for (UnusedResource unusedResource : list) {

				if (unusedResource.getFile().contains("drawable/")) {

					drawableGenerateList.add(generateDrawable(unusedResource, "drawable/", "drawable/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable/", "drawable-mdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable/", "drawable-hdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable/", "drawable-nodpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable/", "drawable-xhdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable/", "drawable-xxhdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable/", "drawable-xxxhdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable/", "drawable-v24/"));

				} else if (unusedResource.getFile().contains("drawable-mdpi/")) {

					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-mdpi/", "drawable/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-mdpi/", "drawable-mdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-mdpi/", "drawable-hdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-mdpi/", "drawable-nodpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-mdpi/", "drawable-xhdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-mdpi/", "drawable-xxhdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-mdpi/", "drawable-xxxhdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-mdpi/", "drawable-v24/"));

				} else if (unusedResource.getFile().contains("drawable-nodpi/")) {

					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-nodpi/", "drawable/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-nodpi/", "drawable-mdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-nodpi/", "drawable-hdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-nodpi/", "drawable-nodpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-nodpi/", "drawable-xhdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-nodpi/", "drawable-xxhdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-nodpi/", "drawable-xxxhdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-nodpi/", "drawable-v24/"));

				} else if (unusedResource.getFile().contains("drawable-xhdpi/")) {

					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-xhdpi/", "drawable/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-xhdpi/", "drawable-mdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-xhdpi/", "drawable-hdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-xhdpi/", "drawable-nodpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-xhdpi/", "drawable-xhdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-xhdpi/", "drawable-xxhdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-xhdpi/", "drawable-xxxhdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-xhdpi/", "drawable-v24/"));


				} else if (unusedResource.getFile().contains("drawable-xxhdpi/")) {

					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-xxhdpi/", "drawable/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-xxhdpi/", "drawable-mdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-xxhdpi/", "drawable-hdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-xxhdpi/", "drawable-nodpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-xxhdpi/", "drawable-xhdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-xxhdpi/", "drawable-xxhdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-xxhdpi/", "drawable-xxxhdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-xxhdpi/", "drawable-v24/"));


				} else if (unusedResource.getFile().contains("drawable-xxxhdpi/")) {

					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-xxxhdpi/", "drawable/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-xxxhdpi/", "drawable-mdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-xxxhdpi/", "drawable-hdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-xxxhdpi/", "drawable-nodpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-xxxhdpi/", "drawable-xhdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-xxxhdpi/", "drawable-xxhdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-xxxhdpi/", "drawable-xxxhdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-xxxhdpi/", "drawable-v24/"));


				} else if (unusedResource.getFile().contains("drawable-v24/")) {

					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-v24/", "drawable/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-v24/", "drawable-mdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-v24/", "drawable-hdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-v24/", "drawable-nodpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-v24/", "drawable-xhdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-v24/", "drawable-xxhdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-v24/", "drawable-xxxhdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-v24/", "drawable-xxxhdpi/"));
					drawableGenerateList.add(generateDrawable(unusedResource, "drawable-v24/", "drawable-v24/"));


				}
			}

			return drawableGenerateList;
		} 

		private UnusedResource generateDrawable(UnusedResource unusedResource, String source, String ext) {
			String file = unusedResource.getFile().replace(source, ext);
			return new UnusedResource(file, null, ResourceTypeEnum.DRAWABLE);
		}
		
		private List<UnusedResource> generateLayoutFilesPath(List<UnusedResource> list) {
			List<UnusedResource> layoutGenerateList = new ArrayList<UnusedResource>();
			
			for (UnusedResource unusedResource : list) {

				if (unusedResource.getFile().contains("layout/")) {
					layoutGenerateList.add(generateLayout(unusedResource, "layout/", "layout-w360dp/"));
					layoutGenerateList.add(generateLayout(unusedResource, "layout/", "layout/"));
				} else if (unusedResource.getFile().contains("layout-w360dp/")) {
					layoutGenerateList.add(generateLayout(unusedResource, "layout-w360dp/", "layout-w360dp/"));
					layoutGenerateList.add(generateLayout(unusedResource, "layout-w360dp/", "layout/"));
				}
			}
			
			return layoutGenerateList;
		}
		
		private UnusedResource generateLayout(UnusedResource unusedResource, String source, String ext) {
			String file = unusedResource.getFile().replace(source, ext);
			return new UnusedResource(file, null, ResourceTypeEnum.LAYOUT);
		}
		
		private List<UnusedResource> generateValuesFilesPath(List<UnusedResource> list) {
			List<UnusedResource> valuesGenerateList = new ArrayList<UnusedResource>();
			
			for (UnusedResource unusedResource : list) {

				if (unusedResource.getFile().contains("values/")) {
					valuesGenerateList.add(generateValues(unusedResource, "values/", "values/"));
					valuesGenerateList.add(generateValues(unusedResource, "values/", "values-en/"));
					valuesGenerateList.add(generateValues(unusedResource, "values/", "values-es/"));
					valuesGenerateList.add(generateValues(unusedResource, "values/", "values-pt/"));
					valuesGenerateList.add(generateValues(unusedResource, "values/", "values-v26/"));
					valuesGenerateList.add(generateValues(unusedResource, "values/", "values-v27/"));
				} else if (unusedResource.getFile().contains("values-pt/")) {
					valuesGenerateList.add(generateValues(unusedResource, "values-pt/", "values/"));
					valuesGenerateList.add(generateValues(unusedResource, "values-pt/", "values-en/"));
					valuesGenerateList.add(generateValues(unusedResource, "values-pt/", "values-es/"));
					valuesGenerateList.add(generateValues(unusedResource, "values-pt/", "values-pt/"));
					valuesGenerateList.add(generateValues(unusedResource, "values-pt/", "values-v26/"));
					valuesGenerateList.add(generateValues(unusedResource, "values-pt/", "values-v27/"));
				}
			}
			
			return valuesGenerateList;
		}
		
		private UnusedResource generateValues(UnusedResource unusedResource, String source, String ext) {
			String file = unusedResource.getFile().replace(source, ext);
			return new UnusedResource(file, unusedResource.getXmlName(), ResourceTypeEnum.VALUES);
		}
		
		// End region	
}

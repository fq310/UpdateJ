package file;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import exception.UpdateException;


public class BuildFile {
	private static final String LOCATION = "location";
	private static final String PREFIX = "${ECLIPSE_HOME}/plugins/";
	private static final String PATH = "@@P";
	private static final String ERROR = "@@E";
	private Path path;
	private Map<String, String> id2name;	//map from this XML file
	private Map<String, String> newId2name;	//map from the new plugin folder
	private Document XMLDocument;
	
	public BuildFile(Path path, Map<String, String> newId2name) {
		this.path = path;
		this.newId2name = newId2name;
		try {
			parseFile();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
			throw new UpdateException("Parse build file failed. Path: " + path);
		}
	}

	private void parseFile() throws ParserConfigurationException, SAXException, IOException {
		XMLDocument = getDoc(); 
		NodeList nodeList = getLocationNodeList();
		for (int i = 0; i < nodeList.getLength(); ++i) {
			Element element = (Element)nodeList.item(i);
			String location = element.getAttribute(LOCATION);
			if (!location.startsWith(PREFIX)) continue;
			location = location.substring(PREFIX.length());
			if (isInvalidLocation(location)) 
				throw new UpdateException("Parse build file failed. Invalid location string: " + location + ". From file: " + path);
			PluginName pluginName = new PluginName(location);
			id2name.put(pluginName.getPluginNameWithoutVersionNumber(), location);
		}
	}

	private Document getDoc() throws ParserConfigurationException,
			SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		builder = factory.newDocumentBuilder();
		Document document = builder.parse(path.toFile());
		return document;
	}
	
	public void update() {
		updateMap(newId2name, id2name);
		saveToFile();
	}
	
	public List<String> getCompareResults() {
		List<String> result = new ArrayList<>();
		result.add(PATH + "File: " + path);
		for (Entry<String, String> entry : id2name.entrySet()) {
			String id = entry.getKey();
			String newLocation = newId2name.get(id);
			if (newLocation == null) {
				result.add(ERROR + " Valide File failed. Cannot find plugin in new plugin folder, name: " + id + 
						"full location: " + entry.getKey() + ". From file: " + path);
			} else {
				result.add(id + " ----> " + newLocation); }
		}
		return result;
	}
	
	public String getFileName() {
		return path.toString();
	}
	
	private void updateMap(Map<String, String> newId2name, Map<String, String> oldId2name) {
		Map<String, String> tempMap = new HashMap<>();
		for (Entry<String, String> entry : oldId2name.entrySet()) {
			String id = entry.getKey();
			String newLocation = newId2name.get(id);
			if (newLocation == null) {
				throw new UpdateException("Parse build file failed. Cannot find plugin in new plugin folder, name: " + id + 
						"full location: " + entry.getKey() + ". From file: " + path);
			} else {
				tempMap.put(id, newLocation); }
		}
		oldId2name.clear();
		oldId2name.putAll(tempMap);
	}

	private void saveToFile() {
		updateXmlDocument();
		try {
			writeToXmlFile();
		} catch (TransformerFactoryConfigurationError | TransformerException e) {
			e.printStackTrace();
			throw new UpdateException("Write to xml file failed. " + ". File: " + path);
		}
	}

	private void updateXmlDocument() {
		NodeList nodeList = getLocationNodeList();  
		for (int i = 0; i < nodeList.getLength(); ++i) {
			Element element = (Element)nodeList.item(i);
			String location = element.getAttribute(LOCATION);
			if (!location.startsWith(PREFIX)) continue;
			location = location.substring(PREFIX.length());
			PluginName pluginName = new PluginName(location);
			element.getAttributeNode(LOCATION).setValue(id2name.get(pluginName.getPluginNameWithoutVersionNumber()));
		}
	}

	private void writeToXmlFile() throws TransformerFactoryConfigurationError, TransformerException {
		TransformerFactory tFactory =TransformerFactory.newInstance();         
    	Transformer transformer = null;
		transformer = tFactory.newTransformer();
    	DOMSource source = new DOMSource(XMLDocument);         
    	StreamResult result = new StreamResult(path.toFile());         
		transformer.transform(source, result);
	}

	private NodeList getLocationNodeList() {
		return XMLDocument.getElementsByTagName("pathelement");
	}

	private boolean isInvalidLocation(String location) {
		if (location.indexOf('/') != location.lastIndexOf('/')) return true;
		return false;
	}
	
}

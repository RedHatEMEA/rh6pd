package rh6pd.processController.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import rh6pd.processController.model.Dataset;
import rh6pd.processController.model.DatasetValues;

/**
 * @deprecated This class looks like junk. Refactor or remove please.
 */
@Deprecated
public class ParseXML {

	public static Dataset ParseDataSetXML(String xml) throws SAXException, IOException, ParserConfigurationException {

		InputSource is = new InputSource(new StringReader(xml));
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(is);
		doc.getDocumentElement().normalize();
		Dataset dataset = new Dataset();

		if (!doc.getDocumentElement().getNodeName().isEmpty() && doc.getDocumentElement().getNodeName().equalsIgnoreCase("dataset")) {

			dataset.setDatasetRef(doc.getDocumentElement().getAttribute("ref"));

			NodeList nList = doc.getElementsByTagName("data");

			ArrayList<DatasetValues> dataArray = new ArrayList<DatasetValues>();
			for (int i = 0; i < nList.getLength(); i++) {

				Node nNode = nList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					DatasetValues datasetValues = new DatasetValues();

					datasetValues.setDataKey(eElement.getAttribute("key"));
					datasetValues.setValue(getTagValue("value", eElement));
					datasetValues.setJavaType(eElement.getAttribute("javaType"));

					dataArray.add(datasetValues);
				}
			}
			dataset.setData(dataArray);
		}
		return dataset;
	}

	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();

		Node nValue = nlList.item(0);

		if (nValue == null)
			return "";

		return nValue.getNodeValue();
	}

}

package rh6pd.processController.model;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HumanTaskHtmlForm {
	private final Elements elements; 
	
	public HumanTaskHtmlForm(String html) {
		Document d = Jsoup.parse(html);
		this.elements = d.select("input");
	}
	
	public void setElementValue(String elementName, String elementValue) {
		Element element = elements.select("input[name=" + elementName + "]").first();
		
		if (element == null) {
			throw new RuntimeException("Could not find an input element with the name: " + elementName);		
		}
		
		element.attr("value", elementValue);
	}
	  
	public String getElementValue(String elementName) {
		Element element = elements.select("input[name=" + elementName + "]").first();
		 
		if (element == null) {
			throw new RuntimeException("Could not find an input element with the name: " + elementName);		
		}
		
		return element.attr("value");
	}

	public int getElementCount() { 
		return elements.select("input").size(); 
	}

	public Map<String, Object> getFields(String append) {
		Map<String, Object> fields = new HashMap<String, Object>();
		
		for (Element element : this.elements.select("input")) {
			fields.put(element.attr("name"), element.attr("value") + append);
		}
		
		return fields; 
	} 
}

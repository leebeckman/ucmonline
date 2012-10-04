package ucmsite.xmltransforming;

import java.util.LinkedList;

import org.jdom.Content;
import org.jdom.Element;

public class MultiElement extends Element {

	private LinkedList<Content> elements = new LinkedList<Content>();
	private String name = null;
	
	public MultiElement(String name) {
		super(name);
	}

	public LinkedList<Content> getElements() {
		return elements;
	}
	
	public void addElement(Content element) {
		elements.add(element);
	}
	
}

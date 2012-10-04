package ucmsite.xmltransforming;

import java.util.LinkedList;

import org.jdom.Content;
import org.jdom.Element;

public class HeaderTransformer extends BaseTransformer {

	public LinkedList<Content> transformBackGround(Element root) throws Exception {
		Element backgroundHeader = root.getChild("backgroundheader");
		LinkedList<Content> elements = new LinkedList<Content>();
		
		childTransformHelper(elements, backgroundHeader);
		
		return elements;
	}
	
	public LinkedList<Content> transformVisible(Element root) throws Exception {
		Element visibleHeader = root.getChild("visibleheader");
		LinkedList<Content> elements = new LinkedList<Content>();
		
		childTransformHelper(elements, visibleHeader);
		
		return elements;
	}
	
}

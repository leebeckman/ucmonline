package ucmsite.xmltransforming;

import java.util.LinkedList;

import org.jdom.Content;
import org.jdom.Element;


public class FooterTransformer extends BaseTransformer {
	
	public LinkedList<Content> transform(Element root) throws Exception {
		if (root != null) {
			Element footerDiv = new Element("div");
			addAttribute(footerDiv, "id", "footer");
			
			LinkedList<Content> elements = new LinkedList<Content>();
			childTransformHelper(elements, root);
			
			footerDiv.addContent(elements);
			
			LinkedList<Content> returnVal = new LinkedList<Content>();
			returnVal.add(footerDiv);
			return returnVal;
		}
		return null;
	}	

	public LinkedList<Content> _text(Element text) {
		Element htmlText = new Element("p");
		addAttribute(htmlText, "class", "footertext");

		transformInline(text);
		
		htmlText.addContent(text.cloneContent());
		
		LinkedList<Content> returnVal = new LinkedList<Content>();
		returnVal.add(htmlText);
		return returnVal;
	}
}

package ucmsite.xmltransforming;

import java.util.LinkedList;

import org.jdom.Content;
import org.jdom.Element;

import ucmsite.pagegeneration.GoogleAccessor;
import ucmsite.pagegeneration.PageGenerator;

public class NavigationTransformer extends BaseTransformer {

	public LinkedList<Content> _imageitem(Element item) throws Exception {
		if (!(item instanceof MultiElement)) {
			Element htmlLi = new Element("li");
			addAttribute(htmlLi, "class", "menuimage");
			addAttribute(htmlLi, "style", "background-image: url('" + pageGen.getAbsoluteURL(GoogleAccessor.getGoogleAccessor().getPicasaURL(item.getAttributeValue("picasasrc"))) + "')");

			Element htmlA = new Element("a");
			addAttribute(htmlA, "class", "navheadlink");
			if (item.getAttributeValue("url") != null) {
				addAttribute(htmlA, "href", pageGen.getAbsoluteURL(item.getAttributeValue("url")));
			}
			htmlLi.addContent(htmlA);

			LinkedList<Content> elements = new LinkedList<Content>();
			childTransformHelper(elements, item);
			
			for(Content subitem : elements)
				htmlLi.addContent(subitem);
			
			MultiElement multiItem = new MultiElement(item.getName());
			multiItem.addElement(htmlLi);
			
			LinkedList<Content> returnVal = new LinkedList<Content>();
			returnVal.add(multiItem);
			return returnVal;
		}
		else {
			Element htmlUl = new Element("ul");
			addAttribute(htmlUl, "id", "navigationbar");
			
			for(Content liItem : ((MultiElement)item).getElements()) {
				htmlUl.addContent(liItem);
			}
			
			LinkedList<Content> returnVal = new LinkedList<Content>();
			returnVal.add(htmlUl);
			return returnVal;
		}
	}
	
	public LinkedList<Content> _subitem(Element item) throws Exception {
		if (!(item instanceof MultiElement)) {
			Element htmlLi = new Element("li");

			Element htmlA = new Element("a");
			addAttribute(htmlA, "href", pageGen.getAbsoluteURL(item.getAttributeValue("url")));
			htmlA.setText(item.getAttributeValue("title"));
			htmlLi.addContent(htmlA);

			LinkedList<Content> elements = new LinkedList<Content>();
			childTransformHelper(elements, item);
			
			for(Content subitem : elements)
				htmlLi.addContent(subitem);
			
			MultiElement multiItem = new MultiElement(item.getName());
			multiItem.addElement(htmlLi);
			
			LinkedList<Content> returnVal = new LinkedList<Content>();
			returnVal.add(multiItem);
			return returnVal;
		}
		else {
			Element htmlUl = new Element("ul");
			addAttribute(htmlUl, "class", "toplevelnav");
			
			for(Content liItem : ((MultiElement)item).getElements()) {
				htmlUl.addContent(liItem);
			}
			
			LinkedList<Content> returnVal = new LinkedList<Content>();
			returnVal.add(htmlUl);
			return returnVal;
		}
	}
	
}

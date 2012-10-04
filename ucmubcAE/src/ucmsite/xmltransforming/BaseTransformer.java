package ucmsite.xmltransforming;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import org.jdom.Comment;
import org.jdom.Content;
import org.jdom.Element;

import ucmsite.pagegeneration.PageGenerator;


public abstract class BaseTransformer {

	PageGenerator pageGen = PageGenerator.getPageGenerator();
	
	public LinkedList<Content> transform(Element root) throws Exception {
		if (root != null) {
			LinkedList<Content> elements = new LinkedList<Content>();
			childTransformHelper(elements, root);
			return elements;
		}
		return null;
	}
	
	public LinkedList<Content> getErrorElement(String message) {
		Element error = new Element("div");
		addAttribute(error, "class", "error");
		error.setText(message);
		LinkedList<Content> returnVal = new LinkedList<Content>();
		returnVal.add(error);
		return returnVal;
	}
	
	public void addAttribute(Element element, String attribute, String addition) {
		if (addition != null) {
			if (element.getAttributeValue(attribute) != null)
				element.setAttribute(attribute, element.getAttributeValue(attribute) + " " + addition);
			else
				element.setAttribute(attribute, addition);
		}
	}
	
	public void childTransformHelper(LinkedList<Content> addTo, Element addFrom) throws Exception {
		ListIterator<Content> children = (ListIterator<Content>) addFrom.getChildren().iterator();
		while (children.hasNext()) {
			Content child = children.next();
			Method transMethod = TransformMethodReflector.getTranformMethodReflector().getMethod(this, ((Element)child).getName());
			LinkedList<Element> htmlChild = (LinkedList<Element>) transMethod.invoke(this, child);
			if (htmlChild == null) {
				throw new Exception();
			}
			
			if (htmlChild.size() > 0) {
				while (htmlChild.getFirst() instanceof MultiElement) {
					int iterIndex = children.previousIndex();
					if (children.hasNext() && ((Element)children.next()).getName().equals(htmlChild.getFirst().getName())) {
						children.previous();
						MultiElement nextMulti = (MultiElement) ((LinkedList<Content>)transMethod.invoke(this, children.next())).getFirst();
						((MultiElement)htmlChild.getFirst()).addElement(nextMulti.getElements().getFirst());
					}
					else {
						if (children.previousIndex() != iterIndex)
							children.previous();
						htmlChild = (LinkedList<Element>) transMethod.invoke(this, htmlChild.getFirst());	
						break;
					}
				}
			}
			
			for(Content htmlChildChild : htmlChild) {
				if (htmlChildChild instanceof Element || htmlChildChild instanceof Comment)
					addTo.add(htmlChildChild);
			}
		}
	}
	
	public LinkedList<Content> _spacer(Element spacer) {
		Element htmlSpacer = new Element("div");
		if (spacer.getAttributeValue("height") != null)
			addAttribute(htmlSpacer, "style", "width: 1px; height: " + spacer.getAttributeValue("height") + "px;");
		else
			addAttribute(htmlSpacer, "style", "width: 1px; height: 10px;");
		
		LinkedList<Content> returnVal = new LinkedList<Content>();
		returnVal.add(htmlSpacer);
		return returnVal;
	}
	
	public LinkedList<Content> _split(Element split) throws Exception {
		if (!(split instanceof MultiElement)) {
			Element htmlColumn = new Element("td");
			
			LinkedList<Content> elements = new LinkedList<Content>();
			childTransformHelper(elements, split);
			
			for(Content item : elements)
				htmlColumn.addContent(item);
			
			MultiElement multiSplit = new MultiElement(split.getName());
			multiSplit.addElement(htmlColumn);
			
			LinkedList<Content> returnVal = new LinkedList<Content>();
			returnVal.add(multiSplit);
			return returnVal;
		}
		else {
			Element htmlTable = new Element("table");
			Element htmlRow = new Element("tr");
			htmlTable.addContent(htmlRow);
			
			for(Content column : ((MultiElement)split).getElements()) {
				htmlRow.addContent(column);
			}
			
			LinkedList<Content> returnVal = new LinkedList<Content>();
			returnVal.add(htmlTable);
			return returnVal;
		}
	}
	
	public LinkedList<Content> _header(Element header) {
		Element htmlHeader = new Element("p");
		addAttribute(htmlHeader, "class", "header");
		
		htmlHeader.addContent(header.getTextTrim());
		
		LinkedList<Content> returnVal = new LinkedList<Content>();
		returnVal.add(htmlHeader);
		return returnVal;
	}
	
	public LinkedList<Content> _subheader(Element subHeader) {
		Element htmlSubHeader = new Element("p");
		addAttribute(htmlSubHeader, "class", "subheader");
		
		htmlSubHeader.addContent(subHeader.getTextTrim());
			
		LinkedList<Content> returnVal = new LinkedList<Content>();
		returnVal.add(htmlSubHeader);
		return returnVal;
	}
	
	public LinkedList<Content> _text(Element text) {
		Element htmlText = new Element("p");
		addAttribute(htmlText, "class", "text");

		transformInline(text);
		
		htmlText.addContent(text.cloneContent());
		
		LinkedList<Content> returnVal = new LinkedList<Content>();
		returnVal.add(htmlText);
		return returnVal;
	}
	
	public LinkedList<Content> _point(Element point) {
		if (!(point instanceof MultiElement)) {
			Element htmlLi = new Element("li");
			
			transformInline(point);
			
			htmlLi.addContent(point.cloneContent());
			
			MultiElement multiPoint = new MultiElement(point.getName());
			multiPoint.addElement(htmlLi);
			
			LinkedList<Content> returnVal = new LinkedList<Content>();
			returnVal.add(multiPoint);
			return returnVal;
		}
		else {
			Element htmlList = new Element("ul");
			addAttribute(htmlList, "class", "content text");
			
			for(Content pointItem : ((MultiElement)point).getElements()) {
				htmlList.addContent(pointItem);
			}
			
			LinkedList<Content> returnVal = new LinkedList<Content>();
			returnVal.add(htmlList);
			return returnVal;
		}
	}
	
	public LinkedList<Content> _numpoint(Element point) {
		if (!(point instanceof MultiElement)) {
			Element htmlLi = new Element("li");
			
			transformInline(point);
			
			htmlLi.addContent(point.cloneContent());
			
			MultiElement multiPoint = new MultiElement(point.getName());
			multiPoint.addElement(htmlLi);
			
			LinkedList<Content> returnVal = new LinkedList<Content>();
			returnVal.add(multiPoint);
			return returnVal;
		}
		else {
			Element htmlList = new Element("ol");
			addAttribute(htmlList, "class", "content text");
			
			for(Content pointItem : ((MultiElement)point).getElements()) {
				htmlList.addContent(pointItem);
			}
			
			LinkedList<Content> returnVal = new LinkedList<Content>();
			returnVal.add(htmlList);
			return returnVal;
		}
	}
	
	public LinkedList<Content> _line(Element line) {
		if (!(line instanceof MultiElement)) {
			Element htmlLi = new Element("li");
			
			transformInline(line);
			
			htmlLi.addContent(line.cloneContent());
			
			MultiElement multiPoint = new MultiElement(line.getName());
			multiPoint.addElement(htmlLi);
			
			LinkedList<Content> returnVal = new LinkedList<Content>();
			returnVal.add(multiPoint);
			return returnVal;
		}
		else {
			Element htmlList = new Element("ul");
			addAttribute(htmlList, "class", "lines text");
			
			for(Content pointItem : ((MultiElement)line).getElements()) {
				htmlList.addContent(pointItem);
			}
			
			LinkedList<Content> returnVal = new LinkedList<Content>();
			returnVal.add(htmlList);
			return returnVal;
		}
	}
	
	public LinkedList<Content> _video(Element video) {
		Element htmlVideo = new Element("div");
		addAttribute(htmlVideo, "id", "mediaplayer");
		addAttribute(htmlVideo, "class", "rounded");
		
		Element spacer = new Element("div");
		addAttribute(spacer, "style", "height:10px; width:10px;");
		htmlVideo.addContent(spacer);
		
		Element videoObject = new Element("object");
		addAttribute(videoObject, "id", "youtubeobject");
		addAttribute(videoObject, "width", "320");
		addAttribute(videoObject, "height", "265");
		
		Element wmodeParam = new Element("param");
		addAttribute(wmodeParam, "name", "wmode");
		addAttribute(wmodeParam, "value", "transparent");
		videoObject.addContent(wmodeParam);
		
		Element movieParam = new Element("param");
		addAttribute(movieParam, "name", "movie");
		addAttribute(movieParam, "value", pageGen.getAbsoluteURL(video.getAttributeValue("src").replace("watch?v=", "v/")));
		videoObject.addContent(movieParam);
		
		Element fullScreenParam = new Element("param");
		addAttribute(fullScreenParam, "name", "allowFullScreen");
		addAttribute(fullScreenParam, "value", "true");
		videoObject.addContent(fullScreenParam);
		
		Element scriptParam = new Element("param");
		addAttribute(scriptParam, "name", "allowscriptaccess");
		addAttribute(scriptParam, "value", "always");
		videoObject.addContent(scriptParam);
		
		Element embedObject = new Element("embed");
		addAttribute(embedObject, "wmode", "transparent");
		addAttribute(embedObject, "src", pageGen.getAbsoluteURL(video.getAttributeValue("src").replace("watch?v=", "v/")));
		addAttribute(embedObject, "type", "application/x-shockwave-flash");
		addAttribute(embedObject, "allowscriptaccess", "always");
		addAttribute(embedObject, "allowfullscreen", "true");
		addAttribute(embedObject, "width", "320");
		addAttribute(embedObject, "height", "265");
		videoObject.addContent(embedObject);
		
		htmlVideo.addContent(videoObject);	
		
		LinkedList<Content> returnVal = new LinkedList<Content>();
		returnVal.add(htmlVideo);
		return returnVal;
	}
	
	public LinkedList<Content> _break(Element spacer) {
		LinkedList<Content> returnVal = new LinkedList<Content>();
		return returnVal;
	}
	
	public LinkedList<Content> _html(Element html) {
		LinkedList<Content> returnVal = new LinkedList<Content>();
		returnVal.addAll(html.cloneContent());
		
		return returnVal;
	}
	
	public LinkedList<Content> _ucmpage(Element page) throws Exception {
		BaseTransformer transformer = TransformMethodReflector.getTranformMethodReflector().getTransformer(page);
		return (transformer.transform(page));
	}
	
	//helpers
	public void transformInline(Element item) {
		Iterator<Element> emailElements = item.getChildren("email").iterator();
		transformEmails(emailElements);
		
		Iterator<Element> linkElements = item.getChildren("link").iterator();
		transformLinks(linkElements);
		
		Iterator<Element> highlightElements = item.getChildren("highlight").iterator();
		transformHighLights(highlightElements);
		
//		Iterator<Element> styleElements = item.getChildren().iterator();
//		transformStyles(styleElements);
	}
	
//	private void transformStyles(Iterator<Element> elements) {
//		while (elements.hasNext()) {
//			Element element = elements.next();
//		}
//	}
	
	private void transformEmails(Iterator<Element> emails) {
		while (emails.hasNext()) {
			Element email = emails.next();
			email.setName("a");
			addAttribute(email, "href", "mailto:" + email.getText());
			addAttribute(email, "class", "email");
			
			String prepend = "";
			if (email.getAttributeValue("arrows") != null && email.getAttributeValue("arrows").equals("false"))
				prepend = "";
			else
				prepend = ">> ";
			
			if (email.getAttributeValue("title") != null)
				email.setText(prepend + email.getAttributeValue("title"));
			else
				email.setText(prepend + email.getText());
		}
	}
	
	private void transformLinks(Iterator<Element> links) {
		while (links.hasNext()) {
			Element link = links.next();
			link.setName("a");
			
			String prepend = "";
			if (link.getAttributeValue("arrows") != null && link.getAttributeValue("arrows").equals("false"))
				prepend = "";
			else
				prepend = ">> ";
			
			String append = "";
			if (link.getText().trim().equals("")) {
				append = " (Coming soon)";
			}
			addAttribute(link, "href", pageGen.getAbsoluteURL(link.getText()));
			addAttribute(link, "class", "link");
			if (link.getAttributeValue("title") != null)
				link.setText(prepend + link.getAttributeValue("title") + append);
			else
				link.setText(prepend + link.getText() + append);
		}
	}
	
	private void transformHighLights(Iterator<Element> highlights) {
		while (highlights.hasNext()) {
			Element highlight = highlights.next();
			highlight.setName("span");
			addAttribute(highlight, "class", "highlight");
		}
	}
	
	/*
	 * We could an element for a kind of click and pop-up map for directions. Could be done with google maps or images.
	 * Most of the code below is probably useless.
	 */
//	public LinkedList<Content> _map(Element map) {
//			String compactName = "mb_" + map.getAttributeValue("title").replace(" ", "").trim();
//			
//			Element htmlImage = new Element("a");
//			addAttribute(htmlImage, "href", "#" + compactName);
//			addAttribute(htmlImage, "rel", "lightbox[inline 800 500]");
//			
//			Element fullDescDiv = new Element("div");
//			addAttribute(fullDescDiv, "id", compactName);
//			addAttribute(fullDescDiv, "style", "display: none;");
//			
//			Element scrollDiv = new Element("div");
//			addAttribute(scrollDiv, "style", "height: 520px; overflow-y: scroll;");
//			fullDescDiv.addContent(scrollDiv);
//			
//			Element descTable = new Element("table");
//			Element descRow = new Element("tr");
//			Element descTextCol = new Element("td");
//			Element descImageCol = new Element("td");
//			
//			scrollDiv.addContent(descTable);
//			descTable.addContent(descRow);
//			descRow.addContent(descTextCol);
//			descRow.addContent(descImageCol);
//			
//			LinkedList<Content> elements = new LinkedList<Content>();
//			childTransformHelper(elements, map);
//			
//			
//			Element htmlFullImage = new Element("img");
//			addAttribute(htmlFullImage, "src", pageGen.getAbsoluteURL(map.getAttributeValue("fullimagesrc")));
//			addAttribute(htmlFullImage, "class", "profilefullpic");
//			descImageCol.addContent(htmlFullImage);
//			
//			Element closeLink = new Element("a");
//			addAttribute(closeLink, "class", "email");
//			addAttribute(closeLink, "style", "color: #FD0101");
//			closeLink.setText("Close this window");
//			addAttribute(closeLink, "href", "");
//			addAttribute(closeLink, "onclick", "Mediabox.close();return false;");
//			htmlCloseLi.addContent(closeLink);
//			
//			MultiElement multiBio = new MultiElement(map.getName());
//			htmlExec.addContent(fullDescDiv);
//			multiBio.addElement(htmlExec);
//			
//			LinkedList<Content> returnVal = new LinkedList<Content>();
//			returnVal.add(htmlHeader);
//			returnVal.add(htmlTable);
//			return returnVal;
//	}
	
}

package ucmsite.xmltransforming;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jdom.Content;
import org.jdom.Element;

import ucmsite.pagegeneration.GoogleAccessor;
import ucmsite.pagegeneration.PageGenerator;

public class HomeTransformer extends BaseTransformer {
	
	public LinkedList<Content> _slideshow(Element slideShow) {
		Element htmlSlideShow = new Element("div");
		addAttribute(htmlSlideShow, "id", "adrotator");
		addAttribute(htmlSlideShow, "class", "rounded");
		
		Element slideScript = new Element("script");
		addAttribute(slideScript, "type", "text/javascript");
		slideScript.setText("function startGallery() { " +
								"var myGallery = new gallery($('myGallery'), {" +
									"timed: true" +
									", textShowCarousel: 'Browse'" +
									", showInfopane: false" +
								"});" +
							"}" + 
							"window.addEvent('domready',startGallery);");
		htmlSlideShow.addContent(slideScript);
		
		Element htmlGallery = new Element("div");
		addAttribute(htmlGallery, "id", "myGallery");
		addAttribute(htmlGallery, "style", "text-align: left;");		
		
		List slides = slideShow.getChildren("slide");
		for (Object slide : slides) {
			Element htmlSlide = new Element("div");
			addAttribute(htmlSlide, "class", "imageElement");
			
			Element slideTitle = new Element("h3");
//			slideTitle.setText(slide.getAttributeValue("title"));
			Element slideDesc = new Element("p");
//			slideDesc.setText(slide.getAttributeValue("text"));
			Element slideLink = new Element("a");
			addAttribute(slideLink, "href", pageGen.getAbsoluteURL(((Element)slide).getAttributeValue("url")));
			addAttribute(slideLink, "class", "open");
			Element slideImg = new Element("img");
			addAttribute(slideImg, "src", pageGen.getAbsoluteURL(GoogleAccessor.getGoogleAccessor().getPicasaURL(((Element)slide).getAttributeValue("picasasrc"))));
			addAttribute(slideImg, "class", "full");
			Element thumbImg = new Element("img");
			addAttribute(thumbImg, "src", pageGen.getAbsoluteURL(GoogleAccessor.getGoogleAccessor().getPicasaURL(((Element)slide).getAttributeValue("picasathumbsrc"))));
			addAttribute(thumbImg, "class", "thumbnail");
			
			htmlSlide.addContent(slideTitle);
			htmlSlide.addContent(slideDesc);
			htmlSlide.addContent(slideLink);
			htmlSlide.addContent(slideImg);
			htmlSlide.addContent(thumbImg);
			
			htmlGallery.addContent(htmlSlide);
		}
		htmlSlideShow.addContent(htmlGallery);
		
		LinkedList<Content> returnVal = new LinkedList<Content>();
		returnVal.add(htmlSlideShow);
		return returnVal;
	}
	
	public LinkedList<Content> _boxlinks(Element boxLinks) {
		Element htmlBoxes = new Element("div");
		addAttribute(htmlBoxes, "id", "bottombuttons");
		
		Iterator boxes = boxLinks.getChildren("box").iterator();
		while (boxes.hasNext()) {
			Element box = (Element) boxes.next();
			
			Element container = new Element("div");
			addAttribute(container, "class", "buttoncontainer");
			
			Element button = new Element("a");
			addAttribute(button, "class", "rounded bottombutton");
			if (!boxes.hasNext()) {
				addAttribute(container, "style", "text-align: right;");
				addAttribute(button, "style", "margin-left: auto;");
			}
			addAttribute(button, "style", "background-image: url('" + pageGen.getAbsoluteURL(GoogleAccessor.getGoogleAccessor().getPicasaURL(box.getAttributeValue("picasasrc"))) + "')");
			addAttribute(button, "href", pageGen.getAbsoluteURL(box.getAttributeValue("url")));
			
			container.addContent(button);
			htmlBoxes.addContent(container);
		}
		
		LinkedList<Content> returnVal = new LinkedList<Content>();
		returnVal.add(htmlBoxes);
		return returnVal;
	}
	
	public LinkedList<Content> _content(Element content) throws Exception {
		Element htmlContent = new Element("div");
		addAttribute(htmlContent, "id", "homepagecontent");
		addAttribute(htmlContent, "class", "rounded");
		
		LinkedList<Content> elements = new LinkedList<Content>();
		childTransformHelper(elements, content);
		
		for(Content item : elements)
			htmlContent.addContent(item);
		
		LinkedList<Content> returnVal = new LinkedList<Content>();
		returnVal.add(htmlContent);
		return returnVal;
	}
	
	public LinkedList<Content> _rightbuttons(Element buttonContainer) {
		Element htmlBoxes = new Element("div");
		addAttribute(htmlBoxes, "id", "rightbuttons");
		
		Iterator rbuttons = buttonContainer.getChildren("rightbutton").iterator();
		while (rbuttons.hasNext()) {
			Element rbutton = (Element) rbuttons.next();
			
			Element buttonlink = new Element("a");
			addAttribute(buttonlink, "class", "rightbutton");
			addAttribute(buttonlink, "style", "background-image: url('" + pageGen.getAbsoluteURL(GoogleAccessor.getGoogleAccessor().getPicasaURL(rbutton.getAttributeValue("picasasrc"))) + "')");
			addAttribute(buttonlink, "href", pageGen.getAbsoluteURL(rbutton.getAttributeValue("url")));

			htmlBoxes.addContent(buttonlink);
		}
		
		LinkedList<Content> returnVal = new LinkedList<Content>();
		returnVal.add(htmlBoxes);
		return returnVal;
	}

}

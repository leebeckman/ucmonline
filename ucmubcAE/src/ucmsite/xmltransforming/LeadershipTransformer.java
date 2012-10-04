package ucmsite.xmltransforming;

import java.util.Iterator;
import java.util.LinkedList;

import org.jdom.Content;
import org.jdom.Element;

import ucmsite.pagegeneration.GoogleAccessor;
import ucmsite.pagegeneration.PageGenerator;

public class LeadershipTransformer extends BaseTransformer {
	
	public LinkedList<Content> _exec(Element bio) throws Exception {
		if (!(bio instanceof MultiElement)) {
			String compactName = "mb_" + bio.getAttributeValue("name").replace(" ", "").trim();
			
			Element htmlExec = new Element("td");
			addAttribute(htmlExec, "class", "triimagecolumn");
			
			Element htmlList = new Element("ul");
			addAttribute(htmlList, "class", "lines");
			
			Element htmlB = new Element("b");
			Element htmlName = new Element("li");
			htmlB.setText(bio.getAttributeValue("name"));
			
			Element htmlPosition = new Element("li");
			htmlPosition.setText(bio.getAttributeValue("position"));
			
			htmlList.addContent(htmlB);
			htmlList.addContent(htmlPosition);
			
			Element htmlImage = new Element("a");
			addAttribute(htmlImage, "style", "background-image: url('" + GoogleAccessor.getGoogleAccessor().getPicasaURL((bio.getAttributeValue("picasasmallsrc"))) + "');");
			addAttribute(htmlImage, "class", "rounded profilepic");
			addAttribute(htmlImage, "href", "#" + compactName);
			addAttribute(htmlImage, "rel", "lightbox[inline 800 500]");
			
			htmlExec.addContent(htmlList);
			htmlExec.addContent(htmlImage);
			
			Element fullDescDiv = new Element("div");
			addAttribute(fullDescDiv, "id", compactName);
			addAttribute(fullDescDiv, "style", "display: none;");
			
			Element scrollDiv = new Element("div");
			addAttribute(scrollDiv, "style", "height: 520px; overflow-y: scroll;");
			fullDescDiv.addContent(scrollDiv);
			
			Element descTable = new Element("table");
			Element descRow = new Element("tr");
			Element descTextCol = new Element("td");
			Element descImageCol = new Element("td");
			
			scrollDiv.addContent(descTable);
			descTable.addContent(descRow);
			descRow.addContent(descTextCol);
			descRow.addContent(descImageCol);
			
			LinkedList<Content> elements = new LinkedList<Content>();
			childTransformHelper(elements, bio);
			
			for(Content item : elements)
				descTextCol.addContent(item);
			
			if (descTextCol.getContent().size() == 0) {
				Element htmlFillDiv = new Element("div");
				addAttribute(htmlFillDiv, "style", "width: 450px; height: 10px");
				descTextCol.addContent(htmlFillDiv);
			}
			
			Element htmlFullImage = new Element("img");
			addAttribute(htmlFullImage, "src", GoogleAccessor.getGoogleAccessor().getPicasaURL((bio.getAttributeValue("picasafullsrc"))));
			addAttribute(htmlFullImage, "class", "profilefullpic");
			descImageCol.addContent(htmlFullImage);
			
			Element htmlProfileData = new Element("ul");
			addAttribute(htmlProfileData, "class", "profiledata");
			
			Element htmlFullName = new Element("li");
			addAttribute(htmlFullName, "class", "profileheader");
			htmlFullName.setText(bio.getAttributeValue("name"));
			htmlProfileData.addContent(htmlFullName);
			
			Element htmlFullPosition = new Element("li");
			addAttribute(htmlFullPosition, "class", "profileposition");
			htmlFullPosition.setText(bio.getAttributeValue("position"));
			htmlProfileData.addContent(htmlFullPosition);
			
			Element htmlFullEmailLi = new Element("li");
			Element htmlFullEmail = new Element("a");
			addAttribute(htmlFullEmail, "class", "email");
			addAttribute(htmlFullEmail, "href", "mailto:" + bio.getAttributeValue("email"));
			htmlFullEmail.setText(bio.getAttributeValue("email"));
			htmlFullEmailLi.addContent(htmlFullEmail);
			htmlProfileData.addContent(htmlFullEmailLi);
			descImageCol.addContent(htmlProfileData);
			
			Element htmlCloseLi = new Element("li");
			
			Element closeLink = new Element("a");
			addAttribute(closeLink, "class", "email");
			addAttribute(closeLink, "style", "color: #FD0101");
			closeLink.setText("Close this window");
			addAttribute(closeLink, "href", "");
			addAttribute(closeLink, "onclick", "Mediabox.close();return false;");
			htmlCloseLi.addContent(closeLink);
			
			htmlProfileData.addContent(new Element("li"));
			htmlProfileData.addContent(htmlCloseLi);
			
			MultiElement multiBio = new MultiElement(bio.getName());
			htmlExec.addContent(fullDescDiv);
			multiBio.addElement(htmlExec);
			
			LinkedList<Content> returnVal = new LinkedList<Content>();
			returnVal.add(multiBio);
			return returnVal;
		}
		else {
			Element htmlHeader = new Element("p");
			addAttribute(htmlHeader, "class", "subheader");
			htmlHeader.setText("UCM EXEC");
			
			Element htmlTable = new Element("table");
			Element htmlRow = null;
			
			for(int i = 0; i < ((MultiElement)bio).getElements().size(); i++) {
				if (i % 3 == 0) {
					htmlRow = new Element("tr");
					htmlTable.addContent(htmlRow);
				}
				
				Content cell = ((MultiElement)bio).getElements().get(i);
				htmlRow.addContent(cell);
			}
			
			LinkedList<Content> returnVal = new LinkedList<Content>();
			returnVal.add(htmlHeader);
			returnVal.add(htmlTable);
			return returnVal;
		}
	}
	
	public LinkedList<Content> _apprentice(Element apprentice) throws Exception {
		if (!(apprentice instanceof MultiElement)) {
			return _exec(apprentice);
		}
		else {
			Element htmlHeader = new Element("p");
			addAttribute(htmlHeader, "class", "subheader");
			htmlHeader.setText("UCM APPRENTICES");
			
			Element htmlTable = new Element("table");
			Element htmlRow = null;
			
			for(int i = 0; i < ((MultiElement)apprentice).getElements().size(); i++) {
				if (i % 3 == 0) {
					htmlRow = new Element("tr");
					htmlTable.addContent(htmlRow);
				}
				
				Content cell = ((MultiElement)apprentice).getElements().get(i);
				htmlRow.addContent(cell);
			}
			
			LinkedList<Content> returnVal = new LinkedList<Content>();
			returnVal.add(htmlHeader);
			returnVal.add(htmlTable);
			return returnVal;
		}
	}
	
	public LinkedList<Content> _staff(Element staff) throws Exception {
		if (!(staff instanceof MultiElement)) {
			return _exec(staff);
		}
		else {
			Element htmlHeader = new Element("p");
			addAttribute(htmlHeader, "class", "subheader");
			htmlHeader.setText("UCM STAFF");
			
			Element htmlTable = new Element("table");
			Element htmlRow = null;
			
			for(int i = 0; i < ((MultiElement)staff).getElements().size(); i++) {
				if (i % 3 == 0) {
					htmlRow = new Element("tr");
					htmlTable.addContent(htmlRow);
				}
				
				Content cell = ((MultiElement)staff).getElements().get(i);
				htmlRow.addContent(cell);
			}
			
			LinkedList<Content> returnVal = new LinkedList<Content>();
			returnVal.add(htmlHeader);
			returnVal.add(htmlTable);
			return returnVal;
		}
	}
	

}

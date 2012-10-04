package ucmsite.xmltransforming;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.jdom.Content;
import org.jdom.Element;
import org.jdom.Text;
import org.jdom.filter.ElementFilter;

import com.google.appengine.tools.development.gwt.AppEngineLauncher;

import ucmsite.pagegeneration.GoogleAccessor;
import ucmsite.pagegeneration.GoogleAccessor.SpreadSheet;
import ucmsite.util.AppEngineLogger;

public class DefaultTransformer extends BaseTransformer {
	
	public LinkedList<Content> transform(Element root) throws Exception {
		if (root != null) {
			Element bannerRoot = new Element("banners");
			bannerRoot.addContent(root.removeContent(new ElementFilter("banner")));
			
			LinkedList<Content> elements = new LinkedList<Content>();
			childTransformHelper(elements, root);
			LinkedList<Content> bannerElements = new LinkedList<Content>();
			childTransformHelper(bannerElements, bannerRoot);
			
			Element logoTable = null;
			if (root.getAttributeValue("sidelogo") != null) {
				Element sideLogo = new Element("img");
				addAttribute(sideLogo, "src", pageGen.getAbsoluteURL(GoogleAccessor.getGoogleAccessor().getPicasaURL(root.getAttributeValue("sidelogo"))));
				addAttribute(sideLogo, "id", "sidelogo");
				addAttribute(sideLogo, "style", "float: left");
				
				logoTable = new Element("table");
				Element logoRow = new Element("tr");
				Element logoColumn = new Element("td");
				addAttribute(logoColumn, "id", "logoColumn");
				Element logoContent = new Element("td");
				logoTable.addContent(logoRow);
				logoRow.addContent(logoColumn);
				logoRow.addContent(logoContent);
				
				Element spacerColumn = new Element("td");
				addAttribute(spacerColumn, "id", "rightContentGap");
				logoRow.addContent(spacerColumn);
				
				logoColumn.addContent(sideLogo);
				logoContent.addContent(elements);
			}
			
			Element roundWrapper = new Element("div");
			addAttribute(roundWrapper, "class", "rounded roundwrapper");
			if (logoTable != null)
				roundWrapper.addContent(logoTable);
			else
				roundWrapper.addContent(elements);
			
			LinkedList<Content> returnVal = new LinkedList<Content>();
			returnVal.addAll(bannerElements);
			returnVal.add(roundWrapper);
			return returnVal;
		}
		return null;
	}
	
	public LinkedList<Content> _indent(Element indent) throws Exception {
		Element htmlIndent = new Element("div");
		addAttribute(htmlIndent, "class", "indent");
		
		LinkedList<Content> elements = new LinkedList<Content>();
		childTransformHelper(elements, indent);
		
		for(Content item : elements)
			htmlIndent.addContent(item);
		
		LinkedList<Content> returnVal = new LinkedList<Content>();
		returnVal.add(htmlIndent);
		return returnVal;		
	}
	
	public LinkedList<Content> _banner(Element banner) {
		if (!(banner instanceof MultiElement)) {
			Text bannerpush = new Text(pageGen.getAbsoluteURL(GoogleAccessor.getGoogleAccessor().getPicasaURL(banner.getAttributeValue("picasasrc"))));
			
			MultiElement multiBanner = new MultiElement(banner.getName());
			multiBanner.addElement(bannerpush);
			
			LinkedList<Content> returnVal = new LinkedList<Content>();
			returnVal.add(multiBanner);
			return returnVal;
		}
		else {
			Element htmlDiv = new Element("div");
			addAttribute(htmlDiv, "id", "mainbanner");
			addAttribute(htmlDiv, "class", "rounded pagebanner");
			Element bannerScript = new Element("script");
			addAttribute(bannerScript, "type", "text/javascript");
			htmlDiv.addContent(bannerScript);
			
			Element preDiv = new Element("div");
			addAttribute(preDiv, "style", "display:none");
			
			for(Content imageurl : ((MultiElement)banner).getElements()) {
				Element preImg = new Element("img");
				addAttribute(preImg, "src", ((Text)imageurl).getText());
				preDiv.addContent(preImg);
				bannerScript.addContent("pagebanners.push(\"" + ((Text)imageurl).getText() + "\");\n");
			}
			bannerScript.addContent("startBanner();");

			LinkedList<Content> returnVal = new LinkedList<Content>();
			returnVal.add(preDiv);
			returnVal.add(htmlDiv);
			return returnVal;
		}
	}
	
	public LinkedList<Content> _tripletimage(Element image) {
		if (!(image instanceof MultiElement)) {
			Element htmlTriplet = new Element("td");
			addAttribute(htmlTriplet, "class", "triimagecolumn");
			
			Element htmlImage = new Element("div");
			addAttribute(htmlImage, "style", "background-image: url('" + pageGen.getAbsoluteURL(GoogleAccessor.getGoogleAccessor().getPicasaURL(image.getAttributeValue("picasasrc"))) + "');");
			addAttribute(htmlImage, "class", "rounded profilepic");
			
			htmlTriplet.addContent(htmlImage);
			
			MultiElement multiImage = new MultiElement(image.getName());
			multiImage.addElement(htmlTriplet);
			
			LinkedList<Content> returnVal = new LinkedList<Content>();
			returnVal.add(multiImage);
			return returnVal;
		}
		else {
			Element htmlTable = new Element("table");
			Element htmlRow = null;
			
			for(int i = 0; i < ((MultiElement)image).getElements().size(); i++) {
				if (i % 3 == 0) {
					htmlRow = new Element("tr");
					htmlTable.addContent(htmlRow);
				}
				
				Content cell = ((MultiElement)image).getElements().get(i);
				htmlRow.addContent(cell);
			}
			
			LinkedList<Content> returnVal = new LinkedList<Content>();
			returnVal.add(htmlTable);
			return returnVal;
		}
	}
	
	public LinkedList<Content> _image(Element image) {
		Element htmlImage = new Element("img");
		addAttribute(htmlImage, "src", pageGen.getAbsoluteURL(GoogleAccessor.getGoogleAccessor().getPicasaURL(image.getAttributeValue("picasasrc"))));
		
		if ("true".equals(image.getAttributeValue("rounded"))) {
			addAttribute(htmlImage, "class", "rounded");
		}
		
		if (image.getAttributeValue("height") != null)
			addAttribute(htmlImage, "style", "height:" + image.getAttributeValue("height") + "px;");
		
		if (image.getAttributeValue("width") != null)
				addAttribute(htmlImage, "style", "width:" + image.getAttributeValue("width") + "px;");
		
		if (image.getAttributeValue("href") != null) {
			Element htmlA = new Element("a");
			addAttribute(htmlA, "href", pageGen.getAbsoluteURL(image.getAttributeValue("src")));
			htmlA.addContent(htmlImage);
			htmlImage = htmlA;
		}
		
		LinkedList<Content> returnVal = new LinkedList<Content>();
		returnVal.add(htmlImage);
		return returnVal;
	}
	
	public LinkedList<Content> _googleformbutton(Element formbutton) {
		Element buttonA = new Element("a");
		addAttribute(buttonA, "href", "");
		addAttribute(buttonA, "class", "formbutton");
		addAttribute(buttonA, "id", "button" + formbutton.getAttributeValue("name").replace(" ", "").replace("-", ""));
		addAttribute(buttonA, "style", "width: " + formbutton.getAttributeValue("width") + "px ;background-image: url('" + pageGen.getAbsoluteURL(GoogleAccessor.getGoogleAccessor().getPicasaURL(formbutton.getAttributeValue("picasasrc"))) + "')");
		
		LinkedList<Content> returnVal = new LinkedList<Content>();
		returnVal.add(buttonA);
		return returnVal;
	}
	
	public LinkedList<Content> _googleform(Element form) {
		String formkey = GoogleAccessor.getGoogleAccessor().getFormAddress(form.getAttributeValue("name"));
		String formId = form.getAttributeValue("name").replace(" ", "").replace("-", "");
		
		Element overFlowDiv = new Element("div");
		addAttribute(overFlowDiv, "style", "overflow: hidden;");
		
		if ("true".equals(form.getAttributeValue("blockregistration"))) {
			Element blockp = new Element("p");
			addAttribute(blockp, "class", "text highlight");
			blockp.setText("Sorry, registration is not available at this time.");
			overFlowDiv.addContent(blockp);
		}
		else {
			Element formFrame = new Element("iframe");
			addAttribute(formFrame, "src", "http://spreadsheets.google.com/embeddedform?key=" + formkey);
			addAttribute(formFrame, "width", "100%");
			addAttribute(formFrame, "height", form.getAttributeValue("height"));
			addAttribute(formFrame, "frameborder", "0");
			addAttribute(formFrame, "marginheight", "0");
			addAttribute(formFrame, "marginwidth", "0");
			formFrame.setText("Loading Form...");
			addAttribute(formFrame, "id", formId);
			overFlowDiv.addContent(formFrame);
		}
		addAttribute(overFlowDiv, "id", form.getAttributeValue("name").replace(" ", ""));
		
		Element hideScript = new Element("script");
		addAttribute(hideScript, "type", "text/javascript");
		hideScript.setText("window.addEvent('domready', function(){" +
							"var " + formId + " = new Fx.Reveal('" + formId + "', {mode: 'horizontal', heightOverride: " + form.getAttributeValue("height") + ", duration: 0});" +
							formId + ".dissolve();" +
							"$('button" + formId + "').addEvent('click', function(e){" +
								"e = new Event(e);" +
								formId + ".toggle();" +
								"e.stop();" +
							"});" +
						  "});");
		
		LinkedList<Content> returnVal = new LinkedList<Content>();
		returnVal.add(overFlowDiv);
		returnVal.add(hideScript);
		return returnVal;
	}
	
	public LinkedList<Content> _googletable(Element table) {
		
		Iterator<SpreadSheet> spreadSheets;
		if (table.getAttributeValue("tab") != null)
			spreadSheets = GoogleAccessor.getGoogleAccessor().getSpreadSheet(table.getAttributeValue("name"), table.getAttributeValue("tab"));
		else
			spreadSheets = GoogleAccessor.getGoogleAccessor().getSpreadSheet(table.getAttributeValue("name"), null);
		
		if (spreadSheets == null) {
			return null;
		}
		
		//positioning/tabbing elements
		Element centerDiv = new Element("div");
		addAttribute(centerDiv, "style", "text-align: center; margin-left: 10px; margin-right: 10px");
		
		Element tabsDiv = new Element("div");
		if (table.getAttributeValue("width") != null) {
			addAttribute(tabsDiv, "style", "margin-left: auto; margin-right: auto; width: " + table.getAttributeValue("width"));
		}
		String tabsId = "i" + UUID.randomUUID().toString().replace("-", "");
		addAttribute(tabsDiv, "id", tabsId);
		addAttribute(tabsDiv, "class", "documentTabs");
		
		Element tabsUl = new Element("ul");
		addAttribute(tabsUl, "class", "documentTabs");
		addAttribute(tabsUl, "id", tabsId + "-nav");
		
		Element tabDivs = new Element("div");
		addAttribute(tabDivs, "class", "tabs-container");
		
		//script to do tabbing
		Element tabScript = new Element("script");
		addAttribute(tabScript, "type", "text/javascript");
		tabScript.setText("var " + tabsId + " = new Yetii({" +
							"id: '" + tabsId + "'" +
						  "});");
		
		while(spreadSheets.hasNext()) {
			Element htmlSpreadSheet = new Element("table");
			addAttribute(htmlSpreadSheet, "class", "spreadsheet");
			SpreadSheet spreadSheet = spreadSheets.next();
			String tabId = "i" + UUID.randomUUID().toString().replace("-", "");
			Element tabLi = new Element("li");
			Element tabLiA = new Element("a");
			addAttribute(tabLiA, "href", "#" + tabId);
			tabLiA.setText(spreadSheet.getName());
			tabLi.addContent(tabLiA);
			tabsUl.addContent(tabLi);
			
			Element tabDiv = new Element("div");
			addAttribute(tabDiv, "class", "tab");
			addAttribute(tabDiv, "id", tabId);
			tabDivs.addContent(tabDiv);
			
			String rowStyle = "spreadsheethead";
			while(spreadSheet.getSheet().hasNext()) {
				Iterator<String> row = spreadSheet.getSheet().next();
				Element htmlRow = new Element("tr");
				htmlSpreadSheet.addContent(htmlRow);
				
				while(row.hasNext()) {
					String column = row.next();
					Element htmlColumn = new Element("td");
					addAttribute(htmlColumn, "class", rowStyle);
					//if (!("".equals(column.trim()))) {
						htmlColumn.setText(column);
						htmlRow.addContent(htmlColumn);
					//}
				}
				if (rowStyle.equals("spreadsheethead"))
					rowStyle = "spreadsheetrow";
			}
			tabDiv.addContent(htmlSpreadSheet);
		}
		tabsDiv.addContent(tabsUl);
		tabsDiv.addContent(tabDivs);
				
		centerDiv.addContent(tabsDiv);
		LinkedList<Content> returnVal = new LinkedList<Content>();
		returnVal.add(centerDiv);
		returnVal.add(tabScript);
		return returnVal;
	}
	
	public LinkedList<Content> _ucmdownload(Element image) {
		Element htmlDownloadList = new Element("a");
		addAttribute(htmlDownloadList, "src", pageGen.getAbsoluteURL(GoogleAccessor.getGoogleAccessor().getPicasaURL(image.getAttributeValue("picasasrc"))));
		
		if ("true".equals(image.getAttributeValue("rounded"))) {
			addAttribute(htmlDownloadList, "class", "rounded");
		}
		
		if (image.getAttributeValue("height") != null)
			addAttribute(htmlDownloadList, "style", "height:" + image.getAttributeValue("height") + "px;");
		
		if (image.getAttributeValue("width") != null)
				addAttribute(htmlDownloadList, "style", "width:" + image.getAttributeValue("width") + "px;");
		
		if (image.getAttributeValue("href") != null) {
			Element htmlA = new Element("a");
			addAttribute(htmlA, "href", pageGen.getAbsoluteURL(image.getAttributeValue("src")));
			htmlA.addContent(htmlDownloadList);
			htmlDownloadList = htmlA;
		}
		
		LinkedList<Content> returnVal = new LinkedList<Content>();
		returnVal.add(htmlDownloadList);
		return returnVal;
	}
}

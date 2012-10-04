package ucmsite.pagegeneration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.google.appengine.api.datastore.Text;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class CachedPage {
	@PrimaryKey
	@Persistent(defaultFetchGroup = "true") 
	private String pagePath;
	
	@Persistent(defaultFetchGroup = "true") 
	private Text pageHTML;
	
	@Persistent(defaultFetchGroup = "true") 
	private String dateString;
	
	public CachedPage (String pagePath, String pageHTML, String dateString) {
		this.pagePath = pagePath;
		this.pageHTML = new Text(pageHTML);
		this.dateString = dateString;
	}
	
	public void updatePageHTML(String pageHTML, String dateString) {
		this.pageHTML = new Text(pageHTML);
		this.dateString = dateString;
	}
	
	public String getDateString() {
		return dateString;
	}
	
	public InputStream getHTML() {
		return new ByteArrayInputStream(pageHTML.getValue().getBytes());
	}
	
	public String getHTMLString() {
		return pageHTML.getValue();
	}
}

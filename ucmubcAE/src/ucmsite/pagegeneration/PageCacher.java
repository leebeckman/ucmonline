package ucmsite.pagegeneration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.servlet.http.HttpServlet;

import com.google.appengine.api.taskqueue.*;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.google.appengine.api.taskqueue.TaskOptions.Builder.*;
import com.google.gdata.data.docs.DocumentEntry;
import com.google.gdata.data.docs.DocumentListEntry;

import ucmsite.servlets.RecacheServlet;
import ucmsite.util.AppEngineLogger;

/*
 * Eventually we should add a last modified check
 * so that the server doesn't need to be restarted to
 * regenerate 'cached' pages.
 */
public class PageCacher {
	
	private static PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("transactions-optional");

	private static PageCacher self = null;
//	private PersistenceManagerFactory pmf;
	//private HashMap<String, CachedPage> pageCache; see below for comment about just using datastor
	
	private List<String> contentFolders;
	private String headerURL;
	private String headerVisibleURL;
	private String navURL;
	private String footerURL;
	private HashMap<String, DocumentListEntry> docsMap;
	
	private PageCacher(HttpServlet recacheServ) {
//		pmf = JDOHelper.getPersistenceManagerFactory("transactions-optional");
		//pageCache = new HashMap<String, CachedPage>(); just using datastor
		contentFolders = Arrays.asList(recacheServ.getServletContext().getInitParameter("contentFolders").split(","));
		headerURL = recacheServ.getServletContext().getInitParameter("headerURL");
		headerVisibleURL = headerURL + "/visible";
		navURL = recacheServ.getServletContext().getInitParameter("navigationURL");
		footerURL = recacheServ.getServletContext().getInitParameter("footerURL");
		docsMap = new HashMap<String, DocumentListEntry>();
	}
	
	public static PageCacher getPageCacher(HttpServlet recacheServ) {
		if (self == null) {
			self = new PageCacher(recacheServ);
		}
		
		return self;
	}
	
	public static PageCacher getPageCacher() {
		return self;
	}
	
	public String getCachedHTML(String url) {
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			AppEngineLogger.log("GETCACHEDHTML TRYING TO GET URL: " + url);
			return pm.getObjectById(CachedPage.class, url).getHTMLString();
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
	}
	
	public InputStream getCachedStream(String url) {
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			return pm.getObjectById(CachedPage.class, url).getHTML();
		}
		catch (JDOObjectNotFoundException e) {
			return null;
		}
	}
	
	public void recacheFolders(boolean forceUpdate) {
		if (docsMap.isEmpty()) {
			remapDocs();
		}
		Queue defaultQueue = QueueFactory.getDefaultQueue();
		
		for(String key : new String[] {headerURL, headerVisibleURL, navURL, footerURL}) {
			TaskOptions newTask;
			if (forceUpdate)
				newTask = TaskOptions.Builder.withUrl("/tasks/queuedcache").param("componentkeyForce", key);
			else
				newTask = TaskOptions.Builder.withUrl("/tasks/queuedcache").param("componentkey", key);
			newTask.method(Method.GET);
			defaultQueue.add(newTask);
		}
		
		for(String key : docsMap.keySet()) {
			TaskOptions newTask;
			if (forceUpdate)
				newTask = TaskOptions.Builder.withUrl("/tasks/queuedcache").param("dockeyForce", key);
			else
				newTask = TaskOptions.Builder.withUrl("/tasks/queuedcache").param("dockey", key);
			newTask.method(Method.GET);
			defaultQueue.add(newTask);
		}
		
	}
	
	private void remapDocs() {
		List<DocumentListEntry> folderDocs = GoogleAccessor.getGoogleAccessor().getFolderDocuments(contentFolders);
		
		for(DocumentListEntry entry: folderDocs) {
			docsMap.put(getDocUrl(entry), entry);
		}
	}
	
	public void recacheComponent(String componentURL, boolean forceUpdate) {
		if (docsMap.isEmpty()) {
			remapDocs();
		}
		PersistenceManager pm = pmf.getPersistenceManager();
		
		String newDateString = null;
		
		if (componentURL.trim().equals(headerVisibleURL.trim())) {
			newDateString = docsMap.get(headerURL).getUpdated().toString();
		}
		else {
			newDateString = docsMap.get(componentURL).getUpdated().toString();
		}
		
		CachedPage checkpage = null; 
		/* Commenting this out as application caching
		* strategy failed due to app being cycled
		* out. For now we'll just use the datastor.
		*
		*= pageCache.get(url);
		*if (checkpage != null) {
		*	//if appcache is okay, therefore so is datastor
		*	if (newDateString.equals(checkpage.getDateString())) {
		*		PageGenerator.getPageGenerator().cacheComponent(url, checkpage);
		*		continue;
		*	}	
		*}
		*/
		try {
			checkpage = pm.getObjectById(CachedPage.class, componentURL);
		}
		catch (Exception e) {
		}
		if (checkpage != null) {
			if (newDateString.equals(checkpage.getDateString()) && !forceUpdate) {
				return;
			}
		}
		
		try {
			checkpage = PageGenerator.getPageGenerator().getComponent(componentURL, newDateString);
		} catch (Exception e) {
			pm.close();
			return;
		}
		//pageCache.put(url, checkpage); just using datastor
        pmf.getPersistenceManager().makePersistent(checkpage);
	        
		pm.close();
	}
	
	public void recacheDoc(String docURL, boolean forceUpdate) {
		if (docsMap.isEmpty()) {
			remapDocs();
		}
		PersistenceManager pm = pmf.getPersistenceManager();
		if (!docURL.equals(headerURL) && !docURL.equals(headerVisibleURL) && !docURL.equals(navURL) && !docURL.equals(footerURL)) {
			String newDateString = docsMap.get(headerURL).getUpdated().toString() +
				docsMap.get(navURL).getUpdated().toString() +
				docsMap.get(footerURL).getUpdated().toString() +
				docsMap.get(docURL).getUpdated().toString();
			
			CachedPage checkpage = null;
			
			/*= pageCache.get(docURL); just using datastor
			* if (checkpage != null) {
			*	//if appcache is okay, therefore so is datastor
			*	if (newDateString.equals(checkpage.getDateString())) {
			*		return;
			*	}	
			*}
			*/
			try {
				checkpage = pm.getObjectById(CachedPage.class, docURL);
			}
			catch (Exception e) {
			}
			if (checkpage != null) {
				if (newDateString.equals(checkpage.getDateString()) && !forceUpdate) {
					//pageCache.put(docURL, checkpage); just using datastor
					return;
				}
			}
			
			try {
				checkpage = PageGenerator.getPageGenerator().getPage(docURL, newDateString);
			} catch (Exception e) {
				pm.close();
				AppEngineLogger.log("Page Generation Fail on " + docURL);
				return;
			}
			
			if (checkpage != null) {
				//pageCache.put(docURL, checkpage); just using datastor
	        	pmf.getPersistenceManager().makePersistent(checkpage);
			}
		}
		pm.close();
	}
	
	private String getDocUrl(DocumentListEntry document) {
		String foldername = null;
		if(document.getParentLinks().size() > 0) {
			foldername = document.getParentLinks().get(0).getTitle().trim();
		}
		
		if (foldername != null) {
			return "/" + foldername + "/" + document.getTitle().getPlainText().trim() + ".html";
		}
		else {
			return null;
		}
	}
	
	public String getHeaderURL() {
		return headerURL;
	}
	
	public String getHeaderVisibleURL() {
		return headerVisibleURL;
	}
	
	public String getNavURL() {
		return navURL;
	}
	
	public String getFooterURL() {
		return footerURL;
	}

}

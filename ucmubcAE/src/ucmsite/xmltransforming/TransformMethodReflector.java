package ucmsite.xmltransforming;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.jdom.Content;
import org.jdom.Element;

public class TransformMethodReflector {

	private static TransformMethodReflector self = null;
	private Class[] args = new Class[1];
	private HashMap<String, Class> transformers;
	
	private TransformMethodReflector() {
		args[0] = Element.class;
	}
	
	public static TransformMethodReflector getTranformMethodReflector() {
		if (self == null) {
			self = new TransformMethodReflector();
		}
		
		return self;
	}
	
	public Method getMethod(BaseTransformer transformer, String name) {
		Class transClass = transformer.getClass();

		try {
			return transClass.getMethod("_" + name, args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public BaseTransformer getTransformer(Element page) {
		String transformerName = page.getAttributeValue("customtransformer");
		if (transformerName == null)
			transformerName = "DefaultTransformer";
		transformerName = "ucmsite.xmltransforming." + transformerName;
		
		Class<BaseTransformer> transClass = null;
		BaseTransformer transformer = null;
		try {
			transClass = (Class<BaseTransformer>) Class.forName(transformerName);
			transformer = transClass.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return transformer;
	}
}

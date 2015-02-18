package pro.deta.detatrak.util;

import java.util.ResourceBundle;

public class ResourceProperties {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4812726784952927768L;
	private ResourceBundle bundle = null;
	public ResourceProperties(ResourceBundle bundle) {
		this.bundle = bundle;
	}
	
	public String getString(String key) {
		if( !bundle.containsKey(key))
			return key;
		return (String) bundle.getObject(key);
	}
}

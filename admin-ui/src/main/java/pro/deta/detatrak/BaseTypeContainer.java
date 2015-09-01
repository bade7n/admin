package pro.deta.detatrak;

import java.io.Serializable;

public class BaseTypeContainer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5367872762741920560L;
	private Object value = null;
	
	public BaseTypeContainer(Object value) {
		this.value = value;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void  setValue(Object val) {
		value = val;
	}
	
	public String toString() {
		return value.toString();
	}
}

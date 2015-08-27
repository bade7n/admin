package pro.deta.detatrak;

import java.io.Serializable;

public class BaseTypeContainer<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5367872762741920560L;
	private T value = null;
	
	public BaseTypeContainer(T value) {
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}
	
	public void  setValue(T val) {
		value = val;
	}
	
	public String toString() {
		return value.toString();
	}
}

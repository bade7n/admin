package pro.deta.detatrak.view.layout;

import com.vaadin.data.fieldgroup.FieldGroup;

public class FormParameter<E> {
	private E data;
	private FieldGroup binder;
	private int currentTabIndex;
	
	public FormParameter(E data,FieldGroup binder) {
		this.data = data;
		this.binder = binder;
	}
	
	public FieldGroup getBinder() {
		return binder;
	}

	public E getBean() {
		return data;
	}

	public int getCurrentTabIndex() {
		return currentTabIndex;
	}

	public void setCurrentTabIndex(int currentTabIndex) {
		this.currentTabIndex = currentTabIndex;
	}

	public void incrementTabIndex() {
		currentTabIndex++;
	}
}

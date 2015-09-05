package pro.deta.detatrak.view.layout;

import com.vaadin.data.Container;

public class ValuesContainer<T> {
	private String valueField = "name";
	private Container container;

	public ValuesContainer(Container valueContainer,String valueField) {
		this(valueContainer);
		this.valueField = valueField;
	}

	public ValuesContainer(Container valuesContainer) {
		this.container = valuesContainer;
	}

	public Container getContainer() {
		return container;
	}
	
	public String getValueField() {
		return valueField;
	}
	public void setValueField(String valueField) {
		this.valueField = valueField;
	}
	
	
}

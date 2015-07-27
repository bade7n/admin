package pro.deta.detatrak.view.layout;

import com.vaadin.addon.jpacontainer.EntityContainer;
import com.vaadin.data.Container;

public class ValuesContainer<T> {
	private EntityContainer<T> entityContainer;
	private String valueField = "name";
	private Container container;

	public ValuesContainer(Container valueContainer,String valueField) {
		this(valueContainer);
		this.valueField = valueField;
	}

	public ValuesContainer(Container valuesContainer) {
		if(valuesContainer instanceof EntityContainer)
			this.entityContainer = (EntityContainer<T>) valuesContainer;

		this.container = valuesContainer;
	}

	public EntityContainer<T> getEntityContainer() {
		return entityContainer;
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

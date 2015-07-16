package pro.deta.detatrak.view.layout;

import com.vaadin.addon.jpacontainer.EntityContainer;
import com.vaadin.data.Container;

public class ValuesContainer<T> {
	private EntityContainer<T> entityContainer;
	private String valueField = "name";
	private Container container;

	public ValuesContainer(Container container,String valueField) {
		this(container);
		this.valueField = valueField;
	}

	public ValuesContainer(Container сontainer) {
		if(container instanceof EntityContainer)
			this.entityContainer = (EntityContainer<T>) container;

		this.container = сontainer;
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

package pro.deta.detatrak.view.layout;

import com.vaadin.addon.jpacontainer.EntityContainer;

public class ValuesContainer<T> {
	private EntityContainer<T> entityContainer;
	private String valueField = "name";
	
	public ValuesContainer(EntityContainer<T> entityContainer,String valueField) {
		this.entityContainer = entityContainer;
		this.valueField = valueField;
	}
	public ValuesContainer(EntityContainer<T> entityContainer) {
		this.entityContainer = entityContainer;
	}

	public EntityContainer<T> getContainer() {
		return entityContainer;
	}
	public String getValueField() {
		return valueField;
	}
	public void setValueField(String valueField) {
		this.valueField = valueField;
	}
	
	
}

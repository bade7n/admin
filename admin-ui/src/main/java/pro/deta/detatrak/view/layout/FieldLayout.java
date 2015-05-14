package pro.deta.detatrak.view.layout;

import pro.deta.detatrak.common.ComponentsBuilder;

import com.vaadin.ui.Component;
import com.vaadin.ui.Field;


public class FieldLayout implements Layout {
	public enum FieldType {
		TEXTFIELD,CKEDITOR,TWINCOLSELECT,ACCESSCOMBOBOX
	}
	private String caption;
	private String field;
	private FieldType type;
	private ValuesContainer<? extends Object> valuesContainer;

	public FieldLayout(String caption,String field,FieldType type) {
		this.caption = caption;
		this.field = field;
		this.type = type;
	}

	public FieldLayout(String caption,String field,FieldType type,ValuesContainer<? extends Object> valuesContainer) {
		this(caption,field,type);
		this.valuesContainer = valuesContainer;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public <T> Component build(BuildLayoutParameter<T> param) throws LayoutDefinitionException {
		validate();
		@SuppressWarnings("rawtypes")
		Field c = null;
		switch (type) {
		case TEXTFIELD:
			c = ComponentsBuilder.createTextFieldNoBind(getCaption());
			break;
		case CKEDITOR:
			c = ComponentsBuilder.createCKEditorTextFieldNoBind(getCaption());
			break;
		case TWINCOLSELECT:
			c = ComponentsBuilder.createTwinColSelectNoBind(getCaption(), valuesContainer.getContainer(),valuesContainer.getValueField());
			break;
		case ACCESSCOMBOBOX:
			c = ComponentsBuilder.createAccessComboBoxNoBind(caption);
			break;
		}
		if(c!= null)
			param.getBinder().bind(c, field);
		return c;
	}

	private void validate() throws LayoutDefinitionException {
		if(type == FieldType.TWINCOLSELECT && valuesContainer == null) {
			throw new LayoutDefinitionException("Current field " + caption +" / " + field +" / " + type+ " should have valuesContainer filled.");
		}
	}

}

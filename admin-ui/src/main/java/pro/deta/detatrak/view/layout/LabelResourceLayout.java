package pro.deta.detatrak.view.layout;

import pro.deta.detatrak.MyUI;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

public class LabelResourceLayout implements Layout<FormParameter<Object>> {
	private String field;
	
	public LabelResourceLayout(String field) {
		this.field = field;
	}
	
	@Override
	public Component build(BuildLayoutParameter<FormParameter<Object>> param)
			throws LayoutDefinitionException {
		String value = (String) param.getData().getBinder().getItemDataSource().getItemProperty(field).getValue();
		value = value == null ? "" : MyUI.getCurrentUI().getBundle().getString(value);
		return new Label(value);
	}

	@Override
	public void save(BuildLayoutParameter<FormParameter<Object>> p)
			throws LayoutRuntimeException {
	}

	@Override
	public void cancel(BuildLayoutParameter<FormParameter<Object>> p)
			throws LayoutRuntimeException {
	}

}

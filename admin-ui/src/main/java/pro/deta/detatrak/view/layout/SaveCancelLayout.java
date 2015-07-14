package pro.deta.detatrak.view.layout;

import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.common.IAction;

import com.vaadin.ui.Component;

public class SaveCancelLayout implements Layout<FormParameter<Object>> {
	private IAction action;
	
	public SaveCancelLayout(IAction action) {
		this.action = action;
	}
	
	@Override
	public Component build(BuildLayoutParameter<FormParameter<Object>> param) {
		return ComponentsBuilder.createSaveCancelButtons(action);
	}
	public void save(BuildLayoutParameter<FormParameter<Object>> p) throws LayoutRuntimeException {
	}
	
	public void cancel(BuildLayoutParameter<FormParameter<Object>> p) throws LayoutRuntimeException {
	}
}

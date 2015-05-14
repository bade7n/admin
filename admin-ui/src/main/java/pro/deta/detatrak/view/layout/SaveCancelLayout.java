package pro.deta.detatrak.view.layout;

import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.common.IAction;

import com.vaadin.ui.Component;

public class SaveCancelLayout implements Layout {
	private IAction action;
	
	public SaveCancelLayout(IAction action) {
		this.action = action;
	}
	
	@Override
	public <T> Component build(BuildLayoutParameter<T> param) {
		return ComponentsBuilder.createSaveCancelButtons(action);
	}

}

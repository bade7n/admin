package pro.deta.detatrak.view.layout;

import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;

public class DetaFormLayout implements Layout<FormParameter<Object>>{
	private String caption;
	private Layout<? extends FormParameter<Object>>[] layouts;
	
	
	@SafeVarargs
	public DetaFormLayout(String caption,Layout<? extends FormParameter<Object>> ...layouts) {
		this.caption = caption;
		this.layouts = layouts;
	}
	
	@Override
	public Component build(BuildLayoutParameter<FormParameter<Object>> p) throws LayoutDefinitionException {
		FormLayout fl = new FormLayout();
		fl.setCaption(caption);
		for (Layout<? extends FormParameter<Object>> l : layouts) {
			fl.addComponent(l.build(p));
		}
		return fl;
	}

}

package pro.deta.detatrak.view.layout;

import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;

public class DetaFormLayout implements Layout<FormParameter<Object>>{
	private String caption;
	private Layout<FormParameter<Object>>[] layouts;
	
	
	@SafeVarargs
	public DetaFormLayout(String caption,Layout<FormParameter<Object>> ...layouts) {
		this.caption = caption;
		this.layouts = layouts;
	}
	
	@Override
	public Component build(BuildLayoutParameter<FormParameter<Object>> p) throws LayoutDefinitionException {
		FormLayout fl = new FormLayout();
		fl.setCaption(caption);
		for (Layout<FormParameter<Object>> l : layouts) {
			fl.addComponent(l.build(p));
		}
		return fl;
	}

}

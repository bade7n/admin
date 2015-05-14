package pro.deta.detatrak.view.layout;

import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;

public class DetaFormLayout implements Layout{
	private String caption;
	private Layout[] layouts;
	
	
	@SafeVarargs
	public DetaFormLayout(String caption,Layout ...layouts) {
		this.caption = caption;
		this.layouts = layouts;
	}
	
	@Override
	public <T> Component build(BuildLayoutParameter<T> p) throws LayoutDefinitionException {
		FormLayout fl = new FormLayout();
		fl.setCaption(caption);
		for (Layout l : layouts) {
			fl.addComponent(l.build(p));
		}
		return fl;
	}

}

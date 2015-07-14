package pro.deta.detatrak.view.layout;

import org.slf4j.LoggerFactory;

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
			try {
				fl.addComponent(l.build(p));
			} catch (Exception e) {
				LoggerFactory.getLogger(DetaFormLayout.class).error("Error while rendering layout for field " + l +" form caption " + caption,e);
			}
		}
		return fl;
	}
	
	public void save(BuildLayoutParameter<FormParameter<Object>> p) throws LayoutRuntimeException {
		for (Layout<FormParameter<Object>> layout : layouts) {
			layout.save(p);
		}
	}
	
	public void cancel(BuildLayoutParameter<FormParameter<Object>> p) throws LayoutRuntimeException {
		for (Layout<FormParameter<Object>> layout : layouts) {
			layout.cancel(p);
		}		
	}

}

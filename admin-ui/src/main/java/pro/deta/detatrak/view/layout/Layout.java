package pro.deta.detatrak.view.layout;

import com.vaadin.ui.Component;

public interface Layout<E> {
	public Component build(BuildLayoutParameter<E> param) throws LayoutDefinitionException;

	public void save(BuildLayoutParameter<FormParameter<Object>> p) throws LayoutRuntimeException;
	
	public void cancel(BuildLayoutParameter<FormParameter<Object>> p) throws LayoutRuntimeException;
}

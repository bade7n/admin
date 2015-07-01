package pro.deta.detatrak.view.layout;

import com.vaadin.ui.Component;

public interface Layout<E> {
	public Component build(BuildLayoutParameter<E> param) throws LayoutDefinitionException;

}

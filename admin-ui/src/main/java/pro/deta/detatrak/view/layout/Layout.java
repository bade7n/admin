package pro.deta.detatrak.view.layout;

import com.vaadin.ui.Component;

public interface Layout {
	public <E> Component build(BuildLayoutParameter<E> param) throws LayoutDefinitionException;
}

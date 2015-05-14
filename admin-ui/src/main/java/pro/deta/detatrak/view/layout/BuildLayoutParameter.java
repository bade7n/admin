package pro.deta.detatrak.view.layout;

import com.vaadin.data.fieldgroup.FieldGroup;

public interface BuildLayoutParameter<E> {
	public FieldGroup getBinder();
	public E getBean();
}

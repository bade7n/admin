package pro.deta.detatrak.util;

import com.vaadin.addon.jpacontainer.EntityContainer;

public interface EntityContainerHandler<T> {
	public void setEntityContainer(EntityContainer<T> container);
}

package pro.deta.detatrak.util;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.VerticalLayout;

public class MyVerticalLayout extends VerticalLayout {
	public MyVerticalLayout(String style,AbstractComponent content) {
		addStyleName(style);
		setWidth(null);
		setHeight("100%");
		addComponent(content);
	}
}

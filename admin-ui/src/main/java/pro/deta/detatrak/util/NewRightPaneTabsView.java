package pro.deta.detatrak.util;

import pro.deta.detatrak.view.layout.Layout;
import pro.deta.detatrak.view.layout.LayoutDefinitionException;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Component;

public abstract class NewRightPaneTabsView extends RightPaneView {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3156992393922833761L;
	Layout rootLayout;

    public NewRightPaneTabsView() {
    	rootLayout = getLayoutDefinition();
    	
    	Component component = null;
		try {
			component = rootLayout.build(null);
		} catch (LayoutDefinitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        root.addComponent(component);
    	root.setExpandRatio(component, 1.0f);
        root.setSizeFull();
    }
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    	changeView(viewChangeEvent);
    }

    public abstract void changeView(ViewChangeListener.ViewChangeEvent viewChangeEvent);
    
    public abstract Layout getLayoutDefinition();

	protected void updateSelectedTab() {
	}

	public abstract String getCaption();
    
}

package pro.deta.detatrak.util;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.TabSheet;

public abstract class RightPaneTabsView extends RightPaneView {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3156992393922833761L;
	private TabSheet tabs;

    public RightPaneTabsView() {
    	tabs = new TabSheet();
    	tabs.setSizeFull();
        initTabs(tabs);
        
        root.addComponent(tabs);
    	root.setExpandRatio(tabs, 1.0f);
        root.setSizeFull();
    }
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
//    	updateSelectedTab();
    	changeView(viewChangeEvent);
    }

    public abstract void changeView(ViewChangeListener.ViewChangeEvent viewChangeEvent);

	protected void updateSelectedTab() {
	}

    public abstract void initTabs(TabSheet tabs);

	public abstract String getCaption();
    
}

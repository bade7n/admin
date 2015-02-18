package pro.deta.detatrak.util;

import pro.deta.detatrak.common.TableBuilder;

import com.vaadin.data.Container;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.AbstractComponent;
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

	public void addTab(TableBuilder tb) {
        AbstractComponent panel = tb.createTable();
        tabs.addTab(panel,tb.getCaption());
    }
	
    public abstract void initTabs(TabSheet tabs);

	public abstract String getCaption();
    
    protected TableBuilder createTableBuilder(String caption, String navKey, Container container) {
    	TableBuilder panel = new TableBuilder()
                .setBeanContainer(container)
                .setEditItemKey(navKey);
        panel.setCaption(caption);
        return panel;
    }
}

package pro.deta.detatrak.view.layout;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

public class TabSheetLayout implements Layout {
	private List<Layout> tabs = new ArrayList<>();
	
	@Override
	public <T> Component build(BuildLayoutParameter<T> p) throws LayoutDefinitionException {
		TabSheet ts = new TabSheet();
		for (Layout layout : tabs) {
			Component c = layout.build(p);
			ts.addTab(c);
		}
		return ts;
	}

	public void addTab(Layout e) {
		tabs.add(e);
	}

	public List<Layout> getTabs() {
		return tabs;
	}


	public void setTabs(List<Layout> tabs) {
		this.tabs = tabs;
	}
	
	
}

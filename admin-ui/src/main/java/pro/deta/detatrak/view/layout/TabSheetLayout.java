package pro.deta.detatrak.view.layout;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

public class TabSheetLayout implements Layout<FormParameter<Object>> {
	private List<Layout<FormParameter<Object>>> tabs = new ArrayList<>();
	
	@Override
	public Component build(BuildLayoutParameter<FormParameter<Object>> p) throws LayoutDefinitionException {
		TabSheet ts = new TabSheet();
		for (Layout<FormParameter<Object>> layout : tabs) {
			Component c = layout.build(p);
			ts.addTab(c);
		}
		ts.setSizeFull();
		return ts;
	}

	public void addTab(Layout<FormParameter<Object>> e) {
		tabs.add(e);
	}

	public List<Layout<FormParameter<Object>>> getTabs() {
		return tabs;
	}


	public void setTabs(List<Layout<FormParameter<Object>>> tabs) {
		this.tabs = tabs;
	}
	
	
}

package pro.deta.detatrak.view.layout;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

public class TabSheetLayout implements Layout<FormParameter<Object>> {
	public static final Logger logger = LoggerFactory.getLogger(TabSheetLayout.class);
	private List<Layout<FormParameter<Object>>> tabs = new ArrayList<>();

	@Override
	public Component build(BuildLayoutParameter<FormParameter<Object>> p) throws LayoutDefinitionException {
		TabSheet ts = new TabSheet();
		for (Layout<FormParameter<Object>> layout : tabs) {
			try {
				Component c = layout.build(p);
				ts.addTab(c);
			} catch(Throwable e) {
				logger.error("Error while rendering layout " + layout +" error: " + e.getMessage(),e);
			}
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

package pro.deta.detatrak.util;

import java.util.ArrayList;
import java.util.Iterator;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

public class MyViewDisplay implements ViewDisplay {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5941038494752423681L;
	private ComponentContainer container;
	private ArrayList<ComponentContainer> containerList = new ArrayList<ComponentContainer>();
	private ArrayList<ContainerState> restoreList = new ArrayList<ContainerState>();
	
	
	public MyViewDisplay(ComponentContainer container) {
        this.container = container;
    }
	
	public void setContainer(ComponentContainer container) {
		containerList.add(container);
	}

	@Override
	public void showView(View view) {
		ComponentContainer current = container;
		if(!restoreList.isEmpty()) {
			ContainerState state = restoreList.remove(0);
			if (view instanceof Component) {
	            state.container.removeAllComponents();
	            for (Component component : state.restoreList) {
					state.container.addComponent(component);
				}
			}
		}

		
		if(!containerList.isEmpty()) {
			current = containerList.remove(0);
			restoreList.add(new ContainerState(current));
		}
		
		if (view instanceof Component) {
            current.removeAllComponents();
            current.addComponent((Component) view);
        } else {
            throw new IllegalArgumentException("View is not a component: " + view);
        }
		
	}
	
	class ContainerState {
		ComponentContainer container;
		ArrayList<Component> restoreList = new ArrayList<Component>();
		
		public ContainerState(ComponentContainer container) {
			this.container = container;
			for (Component component : container) {
				restoreList.add(component);
			}
		}
	}

}

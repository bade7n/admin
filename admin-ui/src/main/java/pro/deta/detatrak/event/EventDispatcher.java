package pro.deta.detatrak.event;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pro.deta.detatrak.presenter.TabViewPresenter;
import pro.deta.detatrak.view.Initializable;

import com.vaadin.navigator.View;
import com.vaadin.ui.Panel;

public abstract class EventDispatcher extends Panel implements View,Initializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2656099379099946471L;
	private ArrayList<EventDispatcher> subViews = new ArrayList<>(); 

    List<IEditViewListener> listeners = new LinkedList<>();
    
    public EventDispatcher() {
    	setSizeFull();
    }

    public void addListener(IEditViewListener listener) {
        listeners.add(listener);
    }

    protected void dispatchEvent(EventBase event) {
        for (IEditViewListener listener : listeners) {
            listener.onEvent(event);
        }
    }
    
    abstract public String getNavKey();
    
	public void init(TabViewPresenter p) {
		for (EventDispatcher view : subViews) {
			p.createView(view);
		}
	}

	public void addForInitialization(EventDispatcher e) {
		subViews.add(e);
	}
	
}

package pro.deta.detatrak.event;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pro.deta.detatrak.presenter.JPAEntityViewBase;
import pro.deta.detatrak.presenter.TabViewPresenter;
import pro.deta.detatrak.util.NewRightPaneTabsView;
import pro.deta.detatrak.view.Initializable;

import com.vaadin.addon.jpacontainer.EntityContainer;
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

	@Deprecated
	public void addForInitialization(EventDispatcher e) {
		subViews.add(e);
	}
	
	public void addForInitialization(NewRightPaneTabsView e) {
		subViews.add(e);
	}

	protected <T> void addForInitialization(JPAEntityViewBase<T> view,
			EntityContainer<T> container) {
		view.setEntityContainer(container);
		subViews.add(view);
	}
}

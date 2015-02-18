package pro.deta.detatrak.presenter;

import java.io.Serializable;

import com.vaadin.navigator.Navigator;

import pro.deta.detatrak.controls.extra.ConfigurationView;
import pro.deta.detatrak.event.EventBase;
import pro.deta.detatrak.event.EventDispatcher;
import pro.deta.detatrak.event.IEditViewListener;

public class TabViewPresenter implements IEditViewListener,Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7753276519183923398L;
	private final Navigator navigator;
    private final String mainViewNavigatorKey;

    public TabViewPresenter(Navigator navigator, String key) {
        this.navigator = navigator;
        mainViewNavigatorKey = key;
    }

    public Navigator getNavigator() {
		return navigator;
	}

	@Override
    public void onEvent(EventBase event) {
        if (event.name.equals("cancel") || event.name.equals("save")) {
            navigator.navigateTo(mainViewNavigatorKey);
        }
    }
	
	public EventDispatcher createView(EventDispatcher view) {
		view.addListener(this);
		getNavigator().addView(view.getNavKey(), view);
		return view;
	}
}

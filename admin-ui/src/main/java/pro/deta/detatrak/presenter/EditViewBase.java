package pro.deta.detatrak.presenter;

import pro.deta.detatrak.common.IAction;
import pro.deta.detatrak.event.EventBase;
import pro.deta.detatrak.event.EventDispatcher;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.VerticalLayout;

public abstract class EditViewBase extends EventDispatcher implements IAction, View {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8488537591307968712L;
	protected VerticalLayout root = new VerticalLayout();
	protected FormLayout form = new FormLayout();

	public EditViewBase() {
		this.setContent(root);
		root.addComponent(form);
	}

	@Override
    public void save() {
        dispatchEvent(new EventBase("save"));
    }
    
    @Override
    public void cancel() {
        dispatchEvent(new EventBase("cancel"));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        String parameter = viewChangeEvent.getParameters();
        buildUI(parameter);
    }

    protected abstract void buildUI(String parameter);
    

    public void addComponent(Component form) {
    	root.addComponent(form);
	}

	public void removeAllComponents() {
		root.removeAllComponents();
	}
}

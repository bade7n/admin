package pro.deta.detatrak.util;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.event.EventDispatcher;
import ru.yar.vi.rm.data.OfficeDO;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public abstract class RightPaneView extends EventDispatcher {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3156992393922833761L;
	private ObjectProperty<String> userProperty = new ObjectProperty<String>(MyUI.getCurrentUI().getUser().getDescription() + " ("+MyUI.getCurrentUI().getUser().getName()+")");
	protected ResourceProperties bundle;
	protected VerticalLayout root = new VerticalLayout();
	ListSelect officeSelect = new ListSelect();

    public RightPaneView() {
    	setContent(root);
    	this.addStyleName("slot-view-content");
    	this.bundle = MyUI.getCurrentUI().getBundle();
    	root.addComponent(createTopRight());
    	
    	Label l1 = new Label(getCaption());
    	l1.addStyleName(Reindeer.LABEL_H1);
    	root.addComponent(l1);
    }
    

    private Component createTopRight() {
    	CustomLayout cl = new CustomLayout("top-right");

    	Label tf = new Label();
    	tf.setPropertyDataSource(userProperty);
    	tf.addStyleName("small");
    	cl.addComponent(tf,"user");
    	
    	Button officeButton = new Button();
    	officeButton.addStyleName("small");
    	officeButton.setCaption(MyUI.getCurrentUI().getBundle().getString("label.show"));
    	ClickListener listener = new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Object value = officeSelect.getConvertedValue();
				if(value != null) {
					OfficeDO office = MyUI.getCurrentUI().getOfficeContainer().getItem(value).getEntity();
					MyUI.getCurrentUI().setOffice(office);
				} else {
					MyUI.getCurrentUI().setOffice(null);
				}
				Navigator navigator = MyUI.getCurrentUI().getNavigator();
                navigator.navigateTo(navigator.getState());
			}
		};
		officeButton.addClickListener(listener );
		cl.addComponent(officeButton , "chooseOfficeButton");
    	officeSelect.setRows(1);
    	officeSelect.setNullSelectionAllowed(true);
    	officeSelect.setContainerDataSource(MyUI.getCurrentUI().getOfficeContainer());
   		officeSelect.setConvertedValue(MyUI.getCurrentUI().getOffice().getId());
    	officeSelect.setItemCaptionMode(ItemCaptionMode.PROPERTY);
    	officeSelect.setItemCaptionPropertyId("name");
    	
    	officeSelect.addStyleName("small");
    	cl.addComponent(officeSelect,"officeChooser");
    	return cl;
	}

}

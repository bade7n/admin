package pro.deta.detatrak.controls;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.util.RightPaneView;
import pro.deta.detatrak.util.TopLevelMenuView;
import pro.deta.detatrak.view.Captioned;
import pro.deta.detatrak.view.ScheduleTabsView;
import ru.yar.vi.rm.data.OfficeDO;

import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Table;

@TopLevelMenuView(icon="icon-dashboard")
public class OfficeChooserView extends RightPaneView  implements Captioned {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8072346640237204386L;
	public static final String NAV_KEY = "/offices";
	private Table table = new Table();
	
    public OfficeChooserView() {
        table.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);
        table.addStyleName("office-choose");
        table.setSizeFull();
        table.setSelectable(true);

        // Define two columns for the built-in container
        table.addContainerProperty("name", String.class, null);
        table.setContainerDataSource(MyUI.getCurrentUI().getOfficeContainer());
        table.setConvertedValue(MyUI.getCurrentUI().getOffice().getId());
        table.setPageLength(0);
        table.setVisibleColumns("id","name","schedule","okato","serviceActionList","serviceRegionList");
        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
            	JPAContainerItem<OfficeDO> selectedOffice= (JPAContainerItem<OfficeDO>) itemClickEvent.getItem();
            	MyUI.getCurrentUI().setOffice(selectedOffice.getEntity());
                Navigator navigator = getUI().getNavigator();
                navigator.navigateTo(ScheduleTabsView.NAV_KEY);
            }
        });
        root.addComponent(table);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        table.setConvertedValue(MyUI.getCurrentUI().getOffice().getId());
    }

	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

    public String getCaption() {
    	return "Мой офис";
    }
}

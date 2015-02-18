package pro.deta.detatrak.controls.extra;

import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import pro.deta.detatrak.view.ExtraTabsView;
import ru.yar.vi.rm.data.AdvertisementDO;

import com.vaadin.data.fieldgroup.FieldGroup;

public class AdvertView extends JPAEntityViewBase<AdvertisementDO> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8149684523796848250L;
	public static final String NAV_KEY = "advertView";
    private ExtraTabsView extra = null;

    public AdvertView(ExtraTabsView extraTabsView) {
    	super(AdvertisementDO.class);
    	this.extra = extraTabsView;
    }

    @Override
	protected void initForm(FieldGroup binder,AdvertisementDO type) {
    	form.addComponent(ComponentsBuilder.createTextField("Название",binder, "name"));
    	form.addComponent(ComponentsBuilder.createTextField("Описание",binder, "description"));
        form.addComponent(ComponentsBuilder.createTextArea("HTML",binder, "html"));
        form.addComponent(ComponentsBuilder.createSaveCancelButtons(this));
	}
    
    @Override
    public void postSaveEntity(AdvertisementDO obj) {
    	extra.getAdvertContainer().refreshItem(itemId);
    }
    
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}

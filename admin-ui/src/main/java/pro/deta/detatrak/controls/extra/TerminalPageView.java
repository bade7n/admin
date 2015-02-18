package pro.deta.detatrak.controls.extra;

import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import pro.deta.detatrak.view.ExtraTabsView;
import ru.yar.vi.rm.data.TerminalPageDO;

import com.vaadin.data.fieldgroup.FieldGroup;

public class TerminalPageView extends JPAEntityViewBase<TerminalPageDO> {
	/**
	 * 
	 */
	public static final String NAV_KEY = "terminalPageView";

	private static final long serialVersionUID = -2909980719200560830L;

	private ExtraTabsView extra;

	public TerminalPageView(ExtraTabsView extraTabsView) {
		super(TerminalPageDO.class);
		this.extra = extraTabsView;
	}
    
	protected void initForm(FieldGroup binder,TerminalPageDO type) {
		binder.setBuffered(true);
    	form.addComponent(ComponentsBuilder.createTextField("Заголовок",binder, "name"));
        form.addComponent(ComponentsBuilder.createCKEditorTextField("Содержание", binder, "content"));
        form.addComponent(ComponentsBuilder.createSaveCancelButtons(this));
	}
    
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}

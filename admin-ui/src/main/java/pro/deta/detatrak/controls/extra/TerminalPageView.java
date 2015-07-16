package pro.deta.detatrak.controls.extra;

import pro.deta.detatrak.presenter.LayoutEntityViewBase;
import pro.deta.detatrak.view.layout.DetaFormLayout;
import pro.deta.detatrak.view.layout.FieldLayout;
import pro.deta.detatrak.view.layout.FieldLayout.FieldType;
import pro.deta.detatrak.view.layout.Layout;
import pro.deta.detatrak.view.layout.SaveCancelLayout;
import pro.deta.detatrak.view.layout.TabSheetLayout;
import ru.yar.vi.rm.data.TerminalPageDO;

public class TerminalPageView extends LayoutEntityViewBase<TerminalPageDO> {
	/**
	 * 
	 */
	public static final String NAV_KEY = "terminalPageView";

	private static final long serialVersionUID = -2909980719200560830L;

	public TerminalPageView() {
		super(TerminalPageDO.class);
	}
    
	@Override
	public Layout getFormDefinition() {
		
		TabSheetLayout l = new TabSheetLayout();
		l.addTab(new DetaFormLayout("Основные настройки",
				new FieldLayout("Заголовок", "name", FieldType.TEXTFIELD),
				new FieldLayout("Содержание", "content", FieldType.CKEDITOR),

				new SaveCancelLayout(this)
				));
		return l;
	}
	
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}

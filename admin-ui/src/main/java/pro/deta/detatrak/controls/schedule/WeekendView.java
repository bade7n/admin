package pro.deta.detatrak.controls.schedule;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.presenter.LayoutEntityViewBase;
import pro.deta.detatrak.view.layout.DetaFormLayout;
import pro.deta.detatrak.view.layout.FieldLayout;
import pro.deta.detatrak.view.layout.Layout;
import pro.deta.detatrak.view.layout.SaveCancelLayout;
import pro.deta.detatrak.view.layout.TabSheetLayout;
import pro.deta.detatrak.view.layout.ValuesContainer;
import ru.yar.vi.rm.data.WeekendDO;


public class WeekendView extends LayoutEntityViewBase<WeekendDO> {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3594074436756159006L;
	public static final String NAV_KEY = "weekendView";

    public WeekendView() {
    	super(WeekendDO.class);
    }

    
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}


	@Override
	public Layout getFormDefinition() {
		TabSheetLayout l = new TabSheetLayout();
		l.addTab(new DetaFormLayout("Основные настройки",
				new FieldLayout("Расписание", "schedule", FieldLayout.FieldType.SCHEDULE),
				new FieldLayout("Объекты", "objects", FieldLayout.FieldType.TWINCOLSELECT,new ValuesContainer<>(MyUI.getCurrentUI().getObjectContainer())),
				new FieldLayout("Дата начала действия", "start", FieldLayout.FieldType.DATEFIELD),
				new FieldLayout("Дата окончания действия", "end", FieldLayout.FieldType.DATEFIELD),
				new SaveCancelLayout(this)
		));
		return l;
	}

}

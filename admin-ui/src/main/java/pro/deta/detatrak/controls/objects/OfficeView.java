package pro.deta.detatrak.controls.objects;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.Audio;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.presenter.LayoutEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.view.layout.DetaFormLayout;
import pro.deta.detatrak.view.layout.FieldLayout;
import pro.deta.detatrak.view.layout.Layout;
import pro.deta.detatrak.view.layout.SaveCancelLayout;
import pro.deta.detatrak.view.layout.TabSheetLayout;
import pro.deta.detatrak.view.layout.ValuesContainer;
import ru.yar.vi.rm.data.ActionDO;
import ru.yar.vi.rm.data.OfficeDO;
import ru.yar.vi.rm.data.RegionDO;

public class OfficeView extends LayoutEntityViewBase<OfficeDO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -142895297402943269L;
	public static final String NAV_KEY = "officeView";

	public OfficeView() {
		super(OfficeDO.class);
	}

	public Layout getFormDefinition() {
		BeanContainer<Integer,RegionDO> regionContainer =  MyUI.createContainer(MyUI.getCurrentUI().getSite().getRegions(),RegionDO.class,"id");
		BeanContainer<Integer,ActionDO> actionContainer =  MyUI.createContainer(MyUI.getCurrentUI().getSite().getActions(),ActionDO.class,"id");

		TabSheetLayout l = new TabSheetLayout();
		l.addTab(new DetaFormLayout("Основные настройки",
				new FieldLayout("Название", "name", FieldLayout.FieldType.TEXTFIELD),
				new FieldLayout("Доступ", "security", FieldLayout.FieldType.ACCESSCOMBOBOX),
				new FieldLayout("Расписание", "schedule", FieldLayout.FieldType.TEXTFIELD),
				new FieldLayout("Использовать расписание офиса для фильтра предварительной записи", "overlapPeriods", FieldLayout.FieldType.CHECKBOX),
				new FieldLayout("Мин. разница между временем записи", "timeDiff", FieldLayout.FieldType.TEXTFIELD),
				new SaveCancelLayout(this)
				));

		l.addTab(new DetaFormLayout("Электронная очередь",
				new FieldLayout("Звук вызова следующего посетителя", "sound", FieldLayout.FieldType.SOUND_FILE,new Audio()) ,
				new FieldLayout("Общая очередь", "distinctQueue", FieldLayout.FieldType.CHECKBOX),
				new SaveCancelLayout(this)
				));
		l.addTab(new DetaFormLayout("Дополнительно",
				new FieldLayout("ОКАТО", "okato", FieldLayout.FieldType.TEXTFIELD),
				new FieldLayout("Регионы обслуживания офиса", "serviceRegionList", FieldLayout.FieldType.TWINCOLSELECT,new ValuesContainer<>(regionContainer)),
				new FieldLayout("Услуги", "serviceActionList", FieldLayout.FieldType.TWINCOLSELECT,new ValuesContainer<>(actionContainer)),
				new FieldLayout("Информация", "information", FieldLayout.FieldType.CKEDITOR),
				new SaveCancelLayout(this)
				));

		return l;
	}

	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

	@Override
	public OfficeDO save(OfficeDO obj) {
		super.preSaveEntity(obj);
		obj.setSite(MyUI.getCurrentUI().getSite());
		obj.getSite().getOffices().add(obj);
		MyUI.getCurrentUI().updateSite(JPAUtils.save(obj.getSite()));
		return obj;
	}

}




package pro.deta.detatrak.controls.service;

import pro.deta.detatrak.presenter.LayoutEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.view.layout.DetaFormLayout;
import pro.deta.detatrak.view.layout.EditableTableParameters;
import pro.deta.detatrak.view.layout.FieldLayout;
import pro.deta.detatrak.view.layout.Layout;
import pro.deta.detatrak.view.layout.SaveCancelLayout;
import pro.deta.detatrak.view.layout.TabSheetLayout;
import pro.deta.detatrak.view.layout.TableColumnInfo;
import pro.deta.detatrak.view.layout.ValuesContainer;
import pro.deta.detatrak.view.layout.FieldLayout.FieldType;
import ru.yar.vi.rm.data.CriteriaDO;
import ru.yar.vi.rm.data.CustomFieldDO;
import ru.yar.vi.rm.data.CustomerDO;

import com.vaadin.addon.jpacontainer.JPAContainer;

public class FieldView extends LayoutEntityViewBase<CustomFieldDO> {

    public static final String NAV_KEY = "fieldView";

    public FieldView() {
    	super(CustomFieldDO.class);
    	
    }
    
    @Override
	public Layout getFormDefinition() {
    	JPAContainer<CustomerDO> customerDataSource = JPAUtils.createCachingJPAContainer(CustomerDO.class);
    	JPAContainer<CriteriaDO> criteriaDataSource = JPAUtils.createCachingJPAContainer(CriteriaDO.class);
        
		TabSheetLayout l = new TabSheetLayout();
		l.addTab(new DetaFormLayout("Основные настройки",
					new FieldLayout("Название параметра", "name", FieldLayout.FieldType.TEXTFIELD),
					new FieldLayout("Тип клиента", "customerId", FieldLayout.FieldType.COMBOBOX_WITHNULL,new ValuesContainer<>(customerDataSource)),
					new FieldLayout("Обязательный", "required", FieldLayout.FieldType.CHECKBOX),
					new FieldLayout("Сообщение при ошибке заполнения", "requiredMsg", FieldLayout.FieldType.TEXTFIELD),
					new FieldLayout("Ограничение", "mask", FieldLayout.FieldType.TEXTFIELD),
					new FieldLayout("Сообщение при несовпадении", "maskMsg", FieldLayout.FieldType.TEXTFIELD),
					new FieldLayout("Пример", "example", FieldLayout.FieldType.TEXTFIELD),
					new FieldLayout("Поле", "field", FieldLayout.FieldType.TEXTFIELD),
					new SaveCancelLayout(this)
				));
		l.addTab(new DetaFormLayout("Дополнительные настройки",
				new FieldLayout("Показывать в отчётах", "reporting", FieldLayout.FieldType.CHECKBOX),
				new FieldLayout("Показывать на терминале", "onTerminal", FieldLayout.FieldType.CHECKBOX),
				new FieldLayout("Варианты", "criteria", FieldType.EDITABLE_LIST,
						new EditableTableParameters<CriteriaDO>(CriteriaDO.class, new TableColumnInfo[] {new TableColumnInfo("name","Название")}, 
								t0-> {return new CriteriaDO("Новый вариант");},"id")),
				new SaveCancelLayout(this)
			));
		return l;
	}

	@Override
	public String getNavKey() {
		return NAV_KEY;
	}


	

}

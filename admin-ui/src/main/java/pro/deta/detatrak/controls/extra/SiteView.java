package pro.deta.detatrak.controls.extra;



import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import com.vaadin.addon.jpacontainer.EntityContainer;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.ComboBox;

import pro.deta.detatrak.BaseTypeContainer;
import pro.deta.detatrak.presenter.LayoutEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.view.layout.DetaFormLayout;
import pro.deta.detatrak.view.layout.EditableTableParameters;
import pro.deta.detatrak.view.layout.FieldLayout;
import pro.deta.detatrak.view.layout.FieldLayout.FieldType;
import pro.deta.detatrak.view.layout.FormParameter;
import pro.deta.detatrak.view.layout.Layout;
import pro.deta.detatrak.view.layout.SaveCancelLayout;
import pro.deta.detatrak.view.layout.TabSheetLayout;
import pro.deta.detatrak.view.layout.TableColumnInfo;
import pro.deta.detatrak.view.layout.ValuesContainer;
import ru.yar.vi.rm.data.ActionDO;
import ru.yar.vi.rm.data.CustomerDO;
import ru.yar.vi.rm.data.OfficeDO;
import ru.yar.vi.rm.data.RegionDO;
import ru.yar.vi.rm.data.SiteDO;

public class SiteView extends LayoutEntityViewBase<SiteDO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3012193455613754605L;
	public static final String NAV_KEY = "siteView";

	public SiteView() {
		super(SiteDO.class);
	}


	@Override
	public Layout getFormDefinition() {
		EntityContainer<ActionDO> actionContainer = JPAUtils.createCachingJPAContainer(ActionDO.class);


		TabSheetLayout l = new TabSheetLayout();
		
		l.addTab(new DetaFormLayout("Основные настройки",
				new FieldLayout("Название", "name", FieldType.TEXTFIELD),
				new FieldLayout("Главный", "main", FieldType.CHECKBOX),
				new FieldLayout("URL Mapping", "urlMapping", FieldType.EDITABLE_LIST,
						new EditableTableParameters<BaseTypeContainer>(BaseTypeContainer.class, t0-> {return new BaseTypeContainer("New item");})),
				
				new FieldLayout("Типы клиентов", "customers", FieldType.EDITABLE_LIST,
						new EditableTableParameters<CustomerDO>(CustomerDO.class, new TableColumnInfo[] {new TableColumnInfo("name","Тип пользователя")}, 
								t0-> {return new CustomerDO("Новый тип пользователя");},t0-> {return t0.getId();})),
				new FieldLayout("Регионы", "regions", FieldType.EDITABLE_LIST,
						new EditableTableParameters<RegionDO>(RegionDO.class, 
								new TableColumnInfo[] {
										new TableColumnInfo("name","Регион"),
										new TableColumnInfo("defaultOffice","Офис по умолчанию",form ->{
											final List<OfficeDO> offices = ((SiteDO)form.getBean()).getOffices();
											ComboBox combo = new ComboBox();
											BeanItemContainer<OfficeDO> cont = new BeanItemContainer<>(OfficeDO.class);
											cont.addAll(offices);
											combo.setContainerDataSource(cont);
											combo.setItemCaptionMode(ItemCaptionMode.PROPERTY);
											combo.setItemCaptionPropertyId("name");
									        return combo;
										}) 
									}, 
								t0-> {
									RegionDO o = new RegionDO();
									o.setName("Район обслуживания");
									o.setSite((SiteDO)t0.getBean());
										return o;
									},
								t0-> {
									return t0.getId();
									}
								)),

				
				new SaveCancelLayout(this)
				));

		l.addTab(new DetaFormLayout("HTTP описание",
				new FieldLayout("HTTP Title", "httpTitle", FieldType.TEXTFIELD),
				new FieldLayout("HTTP Description", "httpDescription", FieldType.CKEDITOR),
				new FieldLayout("HTTP Keywords", "httpKeywords", FieldType.TEXTAREA),
				new FieldLayout("HTTP Template", "httpTemplate", FieldType.TEXTAREA),

				new SaveCancelLayout(this)
				));
		
		l.addTab(new DetaFormLayout("Информация",
				new FieldLayout("Описание", "description", FieldType.CKEDITOR),
				new FieldLayout("Информация", "info", FieldType.CKEDITOR),
				new FieldLayout("Вводный текст", "intro", FieldType.CKEDITOR),
				new FieldLayout("Текст по окончании процедуры записи", "finalText", FieldType.TEXTFIELD),

				new SaveCancelLayout(this)
				));

		l.addTab(new DetaFormLayout("Стили",
				new FieldLayout("Стиль основной", "styleCss", FieldType.TEXTAREA),
				new FieldLayout("Стиль терминала", "selfCss", FieldType.TEXTAREA),
				new FieldLayout("Стиль электронная очередь", "cfmCss", FieldType.TEXTAREA),
				new SaveCancelLayout(this)
				));

		
		l.addTab(new DetaFormLayout("Дополнительно",
				new FieldLayout("Услуги", "actions", FieldType.TWINCOLSELECT,new ValuesContainer<>(actionContainer)),
//				new FieldLayout("Офисы", "offices", FieldType.TWINCOLSELECT,new ValuesContainer<>(MyUI.getCurrentUI().getOfficeContainer())),
				new FieldLayout("Офисы", "offices", FieldType.EDITABLE_LIST,
						new EditableTableParameters<OfficeDO>(OfficeDO.class, new TableColumnInfo[] {new TableColumnInfo("name","Название офиса обслуживания")}, 
								t0-> {
									OfficeDO o = new OfficeDO();
									o.setName("Новый офис");
									o.setSite((SiteDO)t0.getBean());
										return o;
									},t0-> {return t0.getId();})),
				new SaveCancelLayout(this)
				));
		

		return l;

	}
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}

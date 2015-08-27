package pro.deta.detatrak.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.vaadin.openesignforms.ckeditor.CKEditorConfig;
import org.vaadin.openesignforms.ckeditor.CKEditorTextField;

import pro.deta.detatrak.BaseTypeContainer;
import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.listbuilder.ListBuilder;
import pro.deta.detatrak.util.MyTwinColSelectStringConverter;
import pro.deta.detatrak.util.ResourceProperties;
import pro.deta.detatrak.view.layout.EditableTableParameters;
import pro.deta.detatrak.view.layout.TableColumnInfo;
import ru.yar.vi.rm.data.Security;

import com.vaadin.addon.jpacontainer.EntityContainer;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.Container;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.Table.TableDragMode;

public class ComponentsBuilder {
	public static String width = "400px";
    private static final int ROWS_COMPONENT_HEIGHT = 5;

    public static <T> TwinColSelect createTwinColSelect(String caption,EntityContainer<T> objectContainer,FieldGroup binder,String field) {
        return createTwinColSelect(caption, objectContainer, binder, field, "name");
    }
    
    public static <T> TwinColSelect createTwinColSelect(String caption,EntityContainer<T> objectContainer,FieldGroup binder,String field,String valueCaptionField) {
    	TwinColSelect tcs = createTwinColSelectNoBind(caption, objectContainer, valueCaptionField);
        binder.bind(tcs, field);
        return tcs;
    }
    
    public static <T> TwinColSelect createTwinColSelectNoBind(String caption,EntityContainer<T> objectContainer,String valueCaptionField) {
    	TwinColSelect objectsSelect = new TwinColSelect(caption);
		objectsSelect.setImmediate(true);
        objectsSelect.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        objectsSelect.setItemCaptionPropertyId(valueCaptionField);
        objectsSelect.setContainerDataSource(objectContainer);
        objectsSelect.setWidth("100%");
        objectsSelect.setConverter(new MyTwinColSelectStringConverter<T>(objectContainer));
        return objectsSelect;
    }
    
    public static <T> CKEditorTextField createCKEditorTextField(String title,FieldGroup binder, String field) {
    	CKEditorTextField ck = createCKEditorTextFieldNoBind(title);
    	binder.bind(ck, field);
		return ck;
    }
    
    public static <T> CKEditorTextField createCKEditorTextFieldNoBind(String title) {
		CKEditorConfig config1 = new CKEditorConfig();
		config1.useCompactTags();
		config1.disableElementsPath();
		config1.setResizeDir(CKEditorConfig.RESIZE_DIR.HORIZONTAL);
		config1.disableSpellChecker();
		config1.setEnterMode("BR");
		config1.setShiftEnterMode("BR");
		config1.setHeight("300px");
    	CKEditorTextField ckEditorTextField1 = new CKEditorTextField(config1);
    	ckEditorTextField1.setCaption(title);
		ckEditorTextField1.setHeight("440px"); // account for 300px editor plus toolbars
		return ckEditorTextField1;
    }
    
    public static <T> ComboBox createComboBox(String caption, EntityContainer<T> container,FieldGroup binder,String field) {
    	return createComboBoxWithDataSource(caption, container, binder, field, "name");
    }

    public static <T> ComboBox createComboBoxWithDataSource(String caption, EntityContainer<T> container,FieldGroup binder,String field,String captionField) {
        ComboBox comboBox = createCustomComboBox(caption,container,binder,field,captionField,false);
		comboBox.setImmediate(true);
        comboBox.setNullSelectionAllowed(true);
        comboBox.setNullSelectionItemId(0);
        return comboBox;
    }
    
    public static <T> ComboBox createComboBoxWithDataSourceNoBind(String caption, Container container,String captionField) {
        ComboBox comboBox = createCustomComboBoxNoBind(caption,container,captionField,false);
		comboBox.setImmediate(true);
        return comboBox;
    }
    
    public static <T> ComboBox createComboBoxNullAllowedWithDataSourceNoBind(String caption, Container container,String captionField) {
        ComboBox comboBox = createCustomComboBoxNoBind(caption,container,captionField,true);
		comboBox.setImmediate(true);
        return comboBox;
    }
    
    public static ComboBox createAccessComboBox(FieldGroup binder, String field) {
    	ComboBox c = createAccessComboBoxNoBind("Доступ");
        binder.bind(c, field);
        return c;
    }
    
    public static ComboBox createAccessComboBoxNoBind(String caption) {
    	Container container = createContainerFromSecurity();
        ComboBox comboBox = new ComboBox(caption,container);
        comboBox.setWidth(width);
        comboBox.setItemCaptionPropertyId("code");
        comboBox.setTextInputAllowed(false);
        comboBox.setNullSelectionAllowed(false);
        return comboBox;
    }

    public static <T> ComboBox createCustomComboBox(String caption, Container container, FieldGroup binder, String field,String captionField,boolean nullAllowed) {
        ComboBox combo = createCustomComboBoxNoBind(caption, container, captionField,nullAllowed);
        binder.bind(combo, field);
        return combo;
    }
    public static <T> ComboBox createCustomComboBoxNoBind(String caption, Container container, String captionField,boolean nullAllowed) {
        ComboBox comboBox = new ComboBox(caption);
        comboBox.setContainerDataSource(container);
    	comboBox.setTextInputAllowed(false);
        if(!nullAllowed) {
        	comboBox.setNullSelectionAllowed(false);
        } else {
        	comboBox.setNullSelectionAllowed(true);
        	comboBox.setNullSelectionItemId(null);
        }
        comboBox.setWidth(width);
        comboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        comboBox.setItemCaptionPropertyId(captionField);
        comboBox.setConverter(new SingleSelectConverter(comboBox));
        return comboBox;
    }
    
    public static HorizontalLayout createSaveCancelButtons(final IAction iAction) {
        return new HorizontalLayout() {
            {
                Button saveButton = new Button("Сохранить");
                saveButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        iAction.save();
                    }
                });
                addComponent(saveButton);
                Button cancelButton = new Button("Отменить");
                cancelButton.addStyleName("std");
                cancelButton.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        iAction.cancel();
                    }
                });
                addComponent(cancelButton);
            }
        };
    }

	public static Component createDateField(String caption, FieldGroup binder,
			String field) {
		DateField mdf = createDateFieldNoBind(caption);
		binder.bind(mdf, field);
		return mdf;
	}
	
	public static DateField createDateFieldNoBind(String caption) {
		DateField mdf = new DateField(caption);
		Locale locale = new Locale("ru");
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG,locale);
		mdf.setLocale(locale);
		if(dateFormat instanceof SimpleDateFormat ) {
			mdf.setDateFormat(((SimpleDateFormat)dateFormat).toPattern());
		}
		return mdf;
	}

	public static TextField createTextField(String caption, FieldGroup binder,
			String field) {
		TextField tf = createTextFieldNoBind(caption,0);
		binder.bind(tf, field);
		return tf;
	}

	public static TextField createTextFieldNoBind(String caption,int index) {
		TextField tf = new TextField(caption);
		tf.setNullSettingAllowed(true);
		tf.setNullRepresentation("");
		tf.setImmediate(true);
		tf.setWidth(width);
		tf.setTabIndex(index);
		return tf;
	}
	
	public static Component createTextArea(String caption, FieldGroup binder,
			String field) {
		Field ta = createTextAreaNoBind(caption,0);
        binder.bind(ta, field);
        return ta;
	}
	
	public static Field createTextAreaNoBind(String caption,int tabIndex) {
		TextArea ta = new TextArea(caption);
		ta.setImmediate(true);
		ta.setTabIndex(tabIndex);
        return ta;
	}
	
	public static Component createCheckBox(String caption, FieldGroup binder,String field) {
		CheckBox cb = createCheckBoxNoBind(caption,0);
        binder.bind(cb, field);
		return cb;
	}
	public static CheckBox createCheckBoxNoBind(String caption,int tabIndex) {
        CheckBox cb = new CheckBox(caption);
		cb.setImmediate(true);
		cb.setTabIndex(tabIndex);
		return cb;
	}

	public static Component createOptionGroup(String string, FieldGroup binder,String field,Class<? extends Enum<?>> enumClass) {
		OptionGroup og = new OptionGroup(string,createContainerFromEnumClass(enumClass));
		og.setNullSelectionAllowed(false);
		og.setItemCaptionPropertyId(CAPTION_PROPERTY_NAME);
		og.setMultiSelect(true);
		binder.bind(og, field);
		return og;
	}
	
	public static Component createComboBox(String caption, FieldGroup binder,String field,Class<? extends Enum<?>> enumClass) {
		ComboBox comboBox = new ComboBox(caption);
        comboBox.setContainerDataSource(createContainerFromEnumClass(enumClass));
        comboBox.setTextInputAllowed(false);
        comboBox.setNullSelectionAllowed(false);
        comboBox.setWidth(width);
        comboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        comboBox.setItemCaptionPropertyId(CAPTION_PROPERTY_NAME);
		comboBox.setImmediate(true);
        binder.bind(comboBox, field);
		return comboBox;
	}

	public static String CAPTION_PROPERTY_NAME = "code";

	public static Container createContainerFromMap(Map<?, String> hashMap) {
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(CAPTION_PROPERTY_NAME, String.class, "");

		Iterator<?> iter = hashMap.keySet().iterator();
		while(iter.hasNext()) {
			Object itemId = iter.next();
			container.addItem(itemId);
			container.getItem(itemId).getItemProperty(CAPTION_PROPERTY_NAME).setValue(hashMap.get(itemId));
		}

		return container;
	}

	public static Container createContainerFromSecurity() {
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(CAPTION_PROPERTY_NAME, String.class, "");
		for (Security sec : Security.values()) {
			container.addItem(sec.getValue());
			container.getItem(sec.getValue()).getItemProperty(CAPTION_PROPERTY_NAME).setValue(sec.name());
		}
		return container;
	}

	
	public static Container createContainerFromEnumClass(Class<? extends Enum<?>> enumClass) {
		LinkedHashMap<Enum<?>, String> enumMap = new LinkedHashMap<Enum<?>, String>();
		ResourceProperties bundle = MyUI.getCurrentUI().getBundle();
		
		for (Object enumConstant : enumClass.getEnumConstants()) {
			enumMap.put((Enum<?>) enumConstant, enumConstant.toString() + " " + bundle.getString(enumClass.getName()+"."+enumConstant));
		}

		return createContainerFromMap(enumMap);
	}
	
	public static <T> ListBuilder createListBuilder(String caption,EntityContainer<T> container,List<Integer> objIdList) {
		ListBuilder listBuilder = new ListBuilder(caption);
        listBuilder.setImmediate(true);

        listBuilder.setContainerDataSource(container);
        listBuilder.setItemCaptionPropertyId("name");
        listBuilder.setValue(objIdList);
        return listBuilder;
	}

	public static EditableTable createEditableTable(String caption,List<?> values,EditableTableParameters p) {
		EditableTable table = new EditableTable();
		table.setOriginalList(values);
		table.setCaption(caption);
		table.initialize(p);
		return table;
	}
}

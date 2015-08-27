package pro.deta.detatrak.view.layout;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.AbstractMedia;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.common.EditableTable;
import pro.deta.detatrak.controls.objects.FileUploader;
import pro.deta.detatrak.controls.schedule.converter.ScheduleBlock;
import pro.deta.detatrak.controls.schedule.converter.ScheduleBuilder;
import pro.deta.detatrak.util.JPAUtils;
import ru.yar.vi.rm.data.FilestorageContentDO;
import ru.yar.vi.rm.data.FilestorageDO;


public class FieldLayout implements Layout<FormParameter<Object>> {
	private static final Logger logger = LoggerFactory.getLogger(FieldLayout.class);
	
	public enum FieldType {
		TEXTFIELD,CKEDITOR,TWINCOLSELECT,ACCESSCOMBOBOX,COMBOBOX,CHECKBOX, SOUND_FILE, TEXTAREA, DATEFIELD, COMBOBOX_WITHNULL, SCHEDULE, INTERNALTYPE_LIST
	}
	private String caption;
	private String field;
	private FieldType type;
	private ValuesContainer<? extends Object> valuesContainer;
	// hack for supporting file uploaders
	private FileUploader uploader;
	private AbstractMedia media;
	private ScheduleBlock scheduleBlock;
	/*
	 * Propertied for EditableTable
	 */
	private EditableTable table;
	private EditableTableParameters editableTableParameters;

	public FieldLayout(String caption,String field,FieldType type) {
		this.caption = caption;
		this.field = field;
		this.type = type;
	}

	public FieldLayout(String caption,String field,FieldType type,EditableTableParameters tableParameters) {
		this(caption,field,type);
		this.editableTableParameters = tableParameters;
	}
	
	public FieldLayout(String caption,String field,FieldType type,AbstractMedia media) {
		this(caption,field,type);
		this.media = media;
	}
	
	public FieldLayout(String caption,String field,FieldType type,ValuesContainer<? extends Object> valuesContainer) {
		this(caption,field,type);
		this.valuesContainer = valuesContainer;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	@Override
	public Component build(BuildLayoutParameter<FormParameter<Object>> param) throws LayoutDefinitionException {
		validate();
		@SuppressWarnings("rawtypes")
		Field c = null;
		int tabIndex = param.getData().getCurrentTabIndex();
		switch (type) {
		case TEXTFIELD:
			c = ComponentsBuilder.createTextFieldNoBind(getCaption(),tabIndex);
			break;
		case CKEDITOR:
			c = ComponentsBuilder.createCKEditorTextFieldNoBind(getCaption());
			break;
		case TWINCOLSELECT:
			c = ComponentsBuilder.createTwinColSelectNoBind(getCaption(), valuesContainer.getEntityContainer(),valuesContainer.getValueField());
			break;
		case ACCESSCOMBOBOX:
			c = ComponentsBuilder.createAccessComboBoxNoBind(caption);
			break;
		case COMBOBOX:
			c = ComponentsBuilder.createComboBoxWithDataSourceNoBind(caption, valuesContainer.getContainer(), valuesContainer.getValueField());
			break;
		case COMBOBOX_WITHNULL:
			c = ComponentsBuilder.createComboBoxNullAllowedWithDataSourceNoBind(caption, valuesContainer.getContainer(), valuesContainer.getValueField());
			break;
		case CHECKBOX:
			c = ComponentsBuilder.createCheckBoxNoBind(caption,tabIndex);
			break;
		case TEXTAREA:
			c = ComponentsBuilder.createTextAreaNoBind(caption,tabIndex);
			break;
		case DATEFIELD:
			c = ComponentsBuilder.createDateFieldNoBind(caption);
			break;
		case INTERNALTYPE_LIST:
			Object rawValue = param.getData().getBinder().getItemDataSource().getItemProperty(field).getValue();
			editableTableParameters.setTabIndex(tabIndex);
			if(rawValue instanceof List) {
				List<?> value1 = (List<?>) rawValue;
				table = ComponentsBuilder.createEditableTable(caption,value1,editableTableParameters);
				
				return table;
			} else {
				throw new LayoutDefinitionException("Property '"+field+"' for INTERNALTYPE_LIST expected to be java.util.List but '"+rawValue.getClass().getName()+"' found.");
			}
		case SCHEDULE:
			String value = (String) param.getData().getBinder().getItemDataSource().getItemProperty(field).getValue();
			scheduleBlock = ScheduleBuilder.getScheduleByString(value );
			return scheduleBlock.getScheduleElementsLayout();
		case SOUND_FILE:
			uploader = new FileUploader(caption,media);
			uploader.setImageSource((FilestorageDO) param.getData().getBinder().getItemDataSource().getItemProperty(field).getValue());
			return uploader;
		default:
			break;
		}
		if(c!= null) {
			param.getData().getBinder().bind(c, field);
		}
		return c;
	}

	public void save(BuildLayoutParameter<FormParameter<Object>> param) throws LayoutRuntimeException {
		try {
			if(type == FieldType.SOUND_FILE) {
				if(uploader.isUploaded()) {
					FilestorageDO fsdo = (FilestorageDO) param.getData().getBinder().getItemDataSource().getItemProperty(field).getValue();
					if(fsdo != null)
						uploader.populateFilestorage(fsdo);
					else {
						setProperty(param, uploader.getFilestorage());
					}
					fsdo = (FilestorageDO) param.getData().getBinder().getItemDataSource().getItemProperty(field).getValue();
					FilestorageContentDO content = JPAUtils.getEntityManager().merge(fsdo.getContent());
					fsdo.setContent(content);
					FilestorageDO fs = JPAUtils.getEntityManager().merge(fsdo);
					setProperty(param, fs);
				}
				uploader.clear();
			} else if(type == FieldType.INTERNALTYPE_LIST) {
				setProperty(param, table.getOriginalList());
			} else if(type == FieldType.SCHEDULE) {
				setProperty(param, ScheduleBuilder.getScheduleString(scheduleBlock));
			}
		} catch (IOException e) {
			logger.error("Error while saving {0} with parameters {1} ", this, param, e);
			throw new LayoutRuntimeException("Error while saving "+ param);
		}
	}

	@SuppressWarnings("unchecked")
	public void setProperty(BuildLayoutParameter<FormParameter<Object>> param,Object value) {
		param.getData().getBinder().getItemDataSource().getItemProperty(field).setValue(value);
	}

	public void cancel(BuildLayoutParameter<FormParameter<Object>> param) {
		if(type == FieldType.SOUND_FILE) {
			uploader.clear();
		}
	}


	private void validate() throws LayoutDefinitionException {
		if(type == FieldType.TWINCOLSELECT && valuesContainer == null) {
			throw new LayoutDefinitionException("Current field " + caption +" / " + field +" / " + type+ " should have valuesContainer filled.");
		}
	}

	@Override
	public String toString() {
		return "[FieldLayout for field="+field+ "]";
	}

	public FileUploader getUploader() {
		return uploader;
	}
}

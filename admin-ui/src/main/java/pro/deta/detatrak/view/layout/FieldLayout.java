package pro.deta.detatrak.view.layout;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.controls.objects.FileUploader;
import pro.deta.detatrak.util.JPAUtils;
import ru.yar.vi.rm.data.FilestorageContentDO;
import ru.yar.vi.rm.data.FilestorageDO;

import com.vaadin.ui.AbstractMedia;
import com.vaadin.ui.Audio;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;


public class FieldLayout implements Layout<FormParameter<Object>> {
	private static final Logger logger = LoggerFactory.getLogger(FieldLayout.class);
	
	public enum FieldType {
		TEXTFIELD,CKEDITOR,TWINCOLSELECT,ACCESSCOMBOBOX,COMBOBOX,CHECKBOX, SOUND_FILE
	}
	private String caption;
	private String field;
	private FieldType type;
	private ValuesContainer<? extends Object> valuesContainer;
	// hack for supporting file uploaders
	private FileUploader uploader;
	private AbstractMedia media;

	public FieldLayout(String caption,String field,FieldType type) {
		this.caption = caption;
		this.field = field;
		this.type = type;
	}

	public FieldLayout(String caption,String field,FieldType type,AbstractMedia media) {
		this.caption = caption;
		this.field = field;
		this.type = type;
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
		switch (type) {
		case TEXTFIELD:
			c = ComponentsBuilder.createTextFieldNoBind(getCaption());
			break;
		case CKEDITOR:
			c = ComponentsBuilder.createCKEditorTextFieldNoBind(getCaption());
			break;
		case TWINCOLSELECT:
			c = ComponentsBuilder.createTwinColSelectNoBind(getCaption(), valuesContainer.getContainer(),valuesContainer.getValueField());
			break;
		case ACCESSCOMBOBOX:
			c = ComponentsBuilder.createAccessComboBoxNoBind(caption);
			break;
		case COMBOBOX:
			c = ComponentsBuilder.createComboBoxWithDataSourceNoBind(caption, valuesContainer.getContainer(), valuesContainer.getValueField());
			break;
		case CHECKBOX:
			c = ComponentsBuilder.createCheckBoxNoBind(caption);
			break;
		case SOUND_FILE:
			uploader = new FileUploader(caption,media);
			uploader.setImageSource((FilestorageDO) param.getData().getBinder().getItemDataSource().getItemProperty(field).getValue());
			return uploader;
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

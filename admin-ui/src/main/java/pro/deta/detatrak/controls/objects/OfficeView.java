package pro.deta.detatrak.controls.objects;

import java.io.IOException;

import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import ru.yar.vi.rm.data.FilestorageContentDO;
import ru.yar.vi.rm.data.FilestorageDO;
import ru.yar.vi.rm.data.OfficeDO;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Audio;

public class OfficeView extends JPAEntityViewBase<OfficeDO> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -142895297402943269L;
	public static final String NAV_KEY = "officeView";
	private Audio sound = new Audio("Uploaded audio file");
	public FileUploader uploader = new FileUploader(sound);
	
    public OfficeView() {
    	super(OfficeDO.class);
    }


	@Override
	protected void initForm(FieldGroup binder,OfficeDO office) {
		
		form.addComponent(ComponentsBuilder.createTextField("Название", binder, "name"));
		form.addComponent(ComponentsBuilder.createAccessComboBox(binder, "security"));
		form.addComponent(ComponentsBuilder.createTextField("Расписание", binder, "schedule"));

		form.addComponent(ComponentsBuilder.createTextField("ОКАТО", binder, "okato"));
		form.addComponent(ComponentsBuilder.createTextField("Мин. разница между временем записи", binder, "timeDiff"));
		form.addComponent(ComponentsBuilder.createCheckBox("Использовать расписание офиса для фильтра предварительной записи", binder, "overlapPeriods"));
		form.addComponent(ComponentsBuilder.createCheckBox("Distinct queue", binder, "distinctQueue"));
		
		if(office.getSound() != null) {
			uploader.setImageSource(office.getSound());
		}
		uploader.addComponentTo(form);
        form.addComponent(ComponentsBuilder.createSaveCancelButtons(this));
	}

	
	@Override
	public void saveEntity(OfficeDO office) {
		try {
			if(uploader.isUploaded()) {
				if(office.getSound() != null)
					uploader.populateFilestorage(office.getSound());
				else {
					office.setSound(uploader.getFilestorage());
				}
				FilestorageContentDO content = JPAUtils.getEntityManager().merge(office.getSound().getContent());
				office.getSound().setContent(content);
				FilestorageDO fs = JPAUtils.getEntityManager().merge(office.getSound());
				office.setSound(fs);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		uploader.clear();
	}
	
	
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}
	
	public void postSaveEntity(OfficeDO obj) {
		uploader.clear();
	}

	public void postCancel() {
		uploader.clear();
	}


}




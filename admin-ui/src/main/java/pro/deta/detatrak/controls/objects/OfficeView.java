package pro.deta.detatrak.controls.objects;

import java.io.IOException;

import pro.deta.detatrak.presenter.LayoutEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.view.layout.DetaFormLayout;
import pro.deta.detatrak.view.layout.FieldLayout;
import pro.deta.detatrak.view.layout.Layout;
import pro.deta.detatrak.view.layout.SaveCancelLayout;
import pro.deta.detatrak.view.layout.TabSheetLayout;
import pro.deta.detatrak.view.layout.ValuesContainer;
import ru.yar.vi.rm.data.ActionDO;
import ru.yar.vi.rm.data.FilestorageContentDO;
import ru.yar.vi.rm.data.FilestorageDO;
import ru.yar.vi.rm.data.OfficeDO;
import ru.yar.vi.rm.data.RegionDO;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.ui.Audio;

public class OfficeView extends LayoutEntityViewBase<OfficeDO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -142895297402943269L;
	public static final String NAV_KEY = "officeView";
	FieldLayout uploaderFieldLayout;

	public OfficeView() {
		super(OfficeDO.class);
	}

	public Layout getFormDefinition() {
		JPAContainer<RegionDO> regionContainer = JPAUtils.createCachingJPAContainer(RegionDO.class);
		JPAContainer<ActionDO> actionContainer = JPAUtils.createCachingJPAContainer(ActionDO.class);

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
				new FieldLayout("Distinct queue", "distinctQueue", FieldLayout.FieldType.CHECKBOX),
				new SaveCancelLayout(this)
				));

		uploaderFieldLayout = new FieldLayout("Uploaded audio file", "sound", FieldLayout.FieldType.SOUND_FILE,new Audio());
		l.addTab(new DetaFormLayout("Дополнительно",
				uploaderFieldLayout ,
				new FieldLayout("ОКАТО", "okato", FieldLayout.FieldType.TEXTFIELD),
				new FieldLayout("Регионы обслуживания", "serviceRegionList", FieldLayout.FieldType.TWINCOLSELECT,new ValuesContainer<>(regionContainer)),
				new FieldLayout("Услуги", "serviceActionList", FieldLayout.FieldType.TWINCOLSELECT,new ValuesContainer<>(actionContainer)),
				new SaveCancelLayout(this)
				));

		return l;
	}

//	@Override
//	public void saveEntity(OfficeDO office) {
//		try {
//			if(uploaderFieldLayout != null) {
//				FileUploader uploader = uploaderFieldLayout.getUploader();
//				if(uploader.isUploaded()) {
//					if(office.getSound() != null)
//						uploader.populateFilestorage(office.getSound());
//					else {
//						office.setSound(uploader.getFilestorage());
//					}
//					FilestorageContentDO content = JPAUtils.getEntityManager().merge(office.getSound().getContent());
//					office.getSound().setContent(content);
//					FilestorageDO fs = JPAUtils.getEntityManager().merge(office.getSound());
//					office.setSound(fs);
//				}
//				uploader.clear();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}


	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

	public void postSaveEntity(OfficeDO obj) {
		if(uploaderFieldLayout != null) {
			FileUploader uploader = uploaderFieldLayout.getUploader();
			uploader.clear();
		}
	}

	public void postCancel() {
		if(uploaderFieldLayout != null) {
			FileUploader uploader = uploaderFieldLayout.getUploader();
			uploader.clear();
		}
	}


}




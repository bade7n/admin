package pro.deta.detatrak.controls.extra;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.listbuilder.ListBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.view.ExtraTabsView;
import ru.yar.vi.rm.data.ComplexReportDO;
import ru.yar.vi.rm.data.ReportObjectDO;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

public class ComplexReportView extends JPAEntityViewBase<ComplexReportDO> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4320175137995934514L;

	public static final String NAV_KEY = "complexReportView";

	ListBuilder listBuilder = null;
	private JPAContainer<ReportObjectDO> objectContainer = JPAUtils.createCachingJPAContainer(ReportObjectDO.class);
	private Upload logo = null;
	private Embedded image = new Embedded("Uploaded Image");
	private ByteArrayOutputStream baos = null;

	public MyImageSource imageSource;
	
    public ComplexReportView(ExtraTabsView extraTabsView) {
    	super(ComplexReportDO.class,extraTabsView.getComplexReportContainer());
    }

	@Override
	protected void initForm(FieldGroup binder,ComplexReportDO bean) {
		form.addComponent(ComponentsBuilder.createTextField("Название", binder, "name"));
		form.addComponent(ComponentsBuilder.createTextField("Title", binder, "title"));
		form.addComponent(ComponentsBuilder.createTextField("Бегущая строка", binder, "scrolling"));
		form.addComponent(ComponentsBuilder.createTextField("Скорость бегущей строки", binder, "scrollingSpeed"));
		form.addComponent(ComponentsBuilder.createTextField("Время обновления стилей", binder, "refreshTimeout"));
		form.addComponent(ComponentsBuilder.createTextField("Время акцента нового посетителя (для отчётов живой очереди)", binder, "blinkTime"));
		form.addComponent(ComponentsBuilder.createTextField("Количество акцентов нового посетителя (для отчётов живой очереди)", binder, "blinkNum"));
		form.addComponent(ComponentsBuilder.createTextArea("Css", binder, "css"));
		form.addComponent(ComponentsBuilder.createTwinColSelect("Объекты", objectContainer,binder,"objects" ));
		image.setVisible(false);
		baos = new ByteArrayOutputStream();
		if(bean.getLogo() != null) {
			try {
				baos.write(bean.getLogo());
			} catch (IOException e) {
				e.printStackTrace();
			}
	        imageSource = new MyImageSource(baos);
	        image.setSource(new StreamResource(imageSource,"logo.png"));
			image.setVisible(true);
		}
		ImageUploader receiver = new ImageUploader(baos);
		logo = new Upload("Логотип", receiver );
		logo.addSucceededListener(receiver);
		
		form.addComponent(logo);
		form.addComponent(image);
		form.addComponent(ComponentsBuilder.createSaveCancelButtons(this));
	}
	
	@Override
	public void saveEntity(ComplexReportDO obj) {
		listBuilder.commit();
		if(obj.getObjects()!= null)
			obj.getObjects().clear();
		else
			obj.setObjects(new ArrayList<ReportObjectDO>());
		for(Integer id: (List<Integer>)listBuilder.getValue()) {
			obj.getObjects().add(objectContainer.getItem(id).getEntity());
		}
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		if(imageSource  != null)
		try {
			IOUtils.copy(imageSource.getStream(), buffer);
			byte [] data = buffer.toByteArray();
			obj.setLogo(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	class ImageUploader implements Receiver, SucceededListener {
	    public ByteArrayOutputStream file;
	    
	    public ImageUploader(ByteArrayOutputStream baos) {
	    	this.file = baos;
	    }
	    
	    public OutputStream receiveUpload(String filename, String mimeType) {
	    	file.reset();
	        return file;
	    }

	    public void uploadSucceeded(SucceededEvent event) {
	        image.setVisible(true);
	        imageSource = new MyImageSource(file);
	        image.setSource(new StreamResource(imageSource,event.getFilename()));
	    }
	};
	
	class MyImageSource implements StreamResource.StreamSource {
		ByteArrayOutputStream imagebuffer = null;
		public MyImageSource(ByteArrayOutputStream baos) {
			this.imagebuffer = baos;
		}
		@Override
		public InputStream getStream() {
			return new ByteArrayInputStream(imagebuffer.toByteArray());
		}
	}
	
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}


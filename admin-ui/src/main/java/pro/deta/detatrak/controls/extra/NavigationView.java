package pro.deta.detatrak.controls.extra;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.common.ComponentsBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.view.ExtraTabsView;
import ru.yar.vi.rm.data.NavigationDO;
import ru.yar.vi.rm.data.TerminalLinkDO;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

public class NavigationView extends JPAEntityViewBase<NavigationDO> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4320175137995934514L;

	public static final String NAV_KEY = "navigationView";

	private ExtraTabsView extraTabsView;
	private JPAContainer<TerminalLinkDO> linkContainer = JPAUtils.createCachingJPAContainer(TerminalLinkDO.class);
	private Upload logo = null;
	private Embedded image = new Embedded("Uploaded Image");
	private ByteArrayOutputStream baos = null;

	public MyImageSource imageSource;
	
    public NavigationView(ExtraTabsView extraTabsView) {
    	super(NavigationDO.class);
    	this.extraTabsView = extraTabsView;
    }

	@Override
	protected void initForm(FieldGroup binder,NavigationDO bean) {
		form.addComponent(ComponentsBuilder.createTextField("Имя", binder, "name"));
		form.addComponent(ComponentsBuilder.createTextField("Заголовок", binder, "title"));
		form.addComponent(ComponentsBuilder.createTextField("Бегущая строка", binder, "scrolling"));
		form.addComponent(ComponentsBuilder.createTextField("Скорость бегущей строки", binder, "scrollingSpeed"));
		form.addComponent(ComponentsBuilder.createTextField("Время возврата на главную страницу в случае неактивности", binder, "idleSecs"));
		form.addComponent(ComponentsBuilder.createCKEditorTextField("Css", binder, "css"));
		form.addComponent(ComponentsBuilder.createComboBox("Статистика для офиса",MyUI.getCurrentUI().getOfficeContainer(), binder, "office"));
		
		form.addComponent(ComponentsBuilder.createComboBoxWithDataSource("Корень навигации", linkContainer, binder, "root","name"));

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
	public void saveEntity(NavigationDO obj) {
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

	@Override
	public void postSaveEntity(NavigationDO obj) {
		extraTabsView.getNavContainer().refreshItem(itemId);
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


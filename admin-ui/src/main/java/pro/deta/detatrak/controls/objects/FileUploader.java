package pro.deta.detatrak.controls.objects;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import ru.yar.vi.rm.data.FilestorageContentDO;
import ru.yar.vi.rm.data.FilestorageDO;

import com.vaadin.server.StreamResource;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.AbstractMedia;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

public class FileUploader implements Receiver, SucceededListener {
	private Upload upload = null;
	private AbstractMedia media;
	private ByteArrayOutputStream baos = new ByteArrayOutputStream();
	public MyImageSource imageSource;
	
    public FileUploader(AbstractMedia media) {
    	this.media = media;
    	upload = new Upload("", this);
    	upload.addSucceededListener(this);
    	media.setVisible(false);
    }
    
    public OutputStream receiveUpload(String filename, String mimeType) {
    	baos.reset();
        return baos;
    }

    public void uploadSucceeded(SucceededEvent event) {
        media.setVisible(true);
        imageSource = new MyImageSource(baos,event.getFilename(),event.getMIMEType());
        media.setSource(new StreamResource(imageSource,event.getFilename()));
    }
    
    class MyImageSource implements StreamResource.StreamSource {
    	private ByteArrayOutputStream imagebuffer = null;
    	private String mimeType;
    	private String fileName;
    	
    	public MyImageSource(ByteArrayOutputStream baos) {
    		this.imagebuffer = baos;
    	}
    	
    	public MyImageSource(ByteArrayOutputStream baos,String fileName,String mimeType) {
    		this.imagebuffer = baos;
    		this.fileName = fileName;
    		this.mimeType = mimeType;
    	}
    	@Override
    	public InputStream getStream() {
    		return new ByteArrayInputStream(imagebuffer.toByteArray());
    	}
    }
    
    public void addComponentTo(AbstractComponentContainer container) {
    	container.addComponent(upload);
    	container.addComponent(media);
    }

	public void setImageSource(ByteArrayOutputStream baos2, String name,
			String contentType) {
		imageSource = new MyImageSource(baos2,name,contentType);
		media.setSource(new StreamResource(imageSource,name));
		media.setVisible(true);
	}

	public void setImageSource(FilestorageDO fs) {
		try {
			baos.write(fs.getContent().getContent());
		} catch (IOException e) {
			e.printStackTrace();
		}
		imageSource = new MyImageSource(baos,fs.getName(),fs.getContentType());
		media.setSource(new StreamResource(imageSource,fs.getName()));
		media.setVisible(true);
	}

	
	public boolean isUploaded() {
		return imageSource != null;
	}

	public void populateFilestorage(FilestorageDO fs) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		IOUtils.copy(imageSource.getStream(), buffer);
		byte [] data = buffer.toByteArray();
		if(fs.getContent() != null) {
			fs.getContent().setContent(data);
		} else {
			fs.setContent(new FilestorageContentDO(data));
		}
		fs.setName(imageSource.fileName);
		fs.setContentType(imageSource.mimeType);
	}

	public FilestorageDO getFilestorage() throws IOException {
		FilestorageDO fs = new FilestorageDO();
		populateFilestorage(fs);
		return fs;
	}
	
	public void clear() {
		baos = new ByteArrayOutputStream();
		imageSource = null;
	}
}

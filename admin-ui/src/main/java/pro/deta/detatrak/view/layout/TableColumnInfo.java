package pro.deta.detatrak.view.layout;

import java.io.Serializable;
import java.util.function.Function;

import com.vaadin.ui.Field;

public class TableColumnInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5718052678059218374L;
	private String caption;
	private String name;
	private Integer width;
	private Float expandRatio;
	private Function<FormParameter<Object>, Field<?>> fieldInitializer;

	public TableColumnInfo(String name,String caption) {
		this.setCaption(caption);
		this.setName(name);
	}

	public TableColumnInfo(String name,String caption,Function<FormParameter<Object>, Field<?>> fieldInitializer) {
		this.setCaption(caption);
		this.setName(name);
		this.setFieldInitializer(fieldInitializer);
	}
	
	public TableColumnInfo(String name,String caption,Integer width) {
		this.setCaption(caption);
		this.setName(name);
		this.setWidth(width);
	}
	
	public TableColumnInfo(String name,String caption,Float expandRatio) {
		this.setCaption(caption);
		this.setName(name);
		this.setExpandRatio(expandRatio);
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Float getExpandRatio() {
		return expandRatio;
	}

	public void setExpandRatio(Float expandRatio) {
		this.expandRatio = expandRatio;
	}

	public Function<FormParameter<Object>, Field<?>> getFieldInitializer() {
		return fieldInitializer;
	}

	public void setFieldInitializer(Function<FormParameter<Object>, Field<?>> fieldInitializer) {
		this.fieldInitializer = fieldInitializer;
	}
}

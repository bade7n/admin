package pro.deta.detatrak.view.layout;

import com.vaadin.ui.Component;
import com.vaadin.ui.Table.ColumnGenerator;

public class TableColumnLayout implements Layout<TableParameter> {
	private String caption;
	private String name;
	private Integer width;
	private ColumnGenerator generator;
	private Float expandRatio;
	
	
	public TableColumnLayout(String name,String caption) {
		this.caption = caption;
		this.name = name;
	}
	
	public TableColumnLayout(String name,String caption,Integer width) {
		this.caption = caption;
		this.name = name;
		this.width = width;
	}
	
	public TableColumnLayout(String name,String caption,ColumnGenerator generator) {
		this.caption = caption;
		this.name = name;
		this.generator = generator;
	}
	
	public TableColumnLayout(String name,String caption,Float expandRatio) {
		this.caption = caption;
		this.name = name;
		this.expandRatio = expandRatio;
	}
	
	public TableColumnLayout(String name,String caption,ColumnGenerator generator,Float expandRatio) {
		this.caption = caption;
		this.name = name;
		this.generator = generator;
		this.expandRatio = expandRatio;
	}
	
	
	@Override
	public Component build(BuildLayoutParameter<TableParameter> param)
			throws LayoutDefinitionException {
		TableParameter tp = param.getData();
		tp.getTable().setColumnHeader(name, caption);
		
		if(width != null)
			tp.getTable().setColumnWidth(name, width);
		if(expandRatio != null)
			tp.getTable().setColumnExpandRatio(name, expandRatio);
		if(generator != null)
			tp.getTable().addGeneratedColumn(name,generator);
		
		return null;
	}

	public String getName() {
		return name;
	}

	public void save(BuildLayoutParameter<FormParameter<Object>> p) throws LayoutRuntimeException {
	}
	
	public void cancel(BuildLayoutParameter<FormParameter<Object>> p) throws LayoutRuntimeException {
	}
}

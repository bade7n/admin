package pro.deta.detatrak.view.layout;

import com.vaadin.ui.Component;
import com.vaadin.ui.Table.ColumnGenerator;

public class TableColumnLayout extends TableColumnInfo implements Layout<TableParameter> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8182945732470173803L;
	private ColumnGenerator generator;
	
	
	public TableColumnLayout(String name,String caption) {
		super(name, caption);
	}
	
	public TableColumnLayout(String name,String caption,Integer width) {
		super(name,caption,width);
	}
	
	public TableColumnLayout(String name,String caption,ColumnGenerator generator) {
		super(name,caption);
		this.generator = generator;
	}
	
	public TableColumnLayout(String name,String caption,Float expandRatio) {
		super(name,caption,expandRatio);
	}
	
	public TableColumnLayout(String name,String caption,ColumnGenerator generator,Float expandRatio) {
		super(name,caption,expandRatio);
		this.generator = generator;
	}


	@Override
	public Component build(BuildLayoutParameter<TableParameter> param)
			throws LayoutDefinitionException {
		TableParameter tp = param.getData();
		tp.getTable().setColumnHeader(getName(), getCaption());
		
		if(getWidth() != null)
			tp.getTable().setColumnWidth(getName(), getWidth());
		if(getExpandRatio() != null)
			tp.getTable().setColumnExpandRatio(getName(), getExpandRatio());
		if(generator != null)
			tp.getTable().addGeneratedColumn(getName(),generator);
		
		return null;
	}

	public void save(BuildLayoutParameter<FormParameter<Object>> p) throws LayoutRuntimeException {
	}
	
	public void cancel(BuildLayoutParameter<FormParameter<Object>> p) throws LayoutRuntimeException {
	}
}

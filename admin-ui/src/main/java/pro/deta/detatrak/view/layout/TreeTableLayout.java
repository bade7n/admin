package pro.deta.detatrak.view.layout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.LoggerFactory;

import pro.deta.detatrak.common.TableBuilder;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import ru.yar.vi.rm.data.HierarchyDO;

import com.vaadin.addon.jpacontainer.EntityContainer;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.AbstractSelect.AbstractSelectTargetDetails;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.AbstractSelect.VerticalLocationIs;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.TreeTable;

public class TreeTableLayout<E extends HierarchyDO<E>> extends TableLayout {
	private JPAEntityViewBase<E> component;
	private PopupView view;
	private EntityContainer<E> entityContainer;
	private Handler actionHandler;

	
	public TreeTableLayout(EntityContainer<E> container, String caption,
			String editItemNavigationKey, TableColumnLayout... columns) {
		super(container, caption, editItemNavigationKey, columns);
		this.entityContainer = container;
	}
	
	@Override
	protected Button addPlusButton(ComponentContainer componentContainer) {
		Button addNew = new Button("Добавить");
		addNew.addClickListener(event -> {
				entityContainer.addItem();
			}
		);
		return addNew;
	}
	
	public void initializeLayout(ComponentContainer verticalLayout) {
		verticalLayout.addComponent(view);
	}

	
	protected Table getTableInstance() {
		return new TreeTable() {
			private static final long serialVersionUID = 2689155068351476684L;
			
			private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			@Override
			protected String formatPropertyValue(Object rowId, Object colId,
					Property<?> property) {
				try {
					Object v = property.getValue();
					if (v instanceof Date) {
						Date dateValue = (Date) v;
						return sdf.format(dateValue);
					}
				} catch(Exception e) {
					LoggerFactory.getLogger(TableBuilder.class).error("Error while formatting property " + colId +" on " + rowId, e);
					return "RowId: " + rowId +" ColId: " + colId;
				}
				return super.formatPropertyValue(rowId, colId, property);

			}
		};
	}
	
	protected void addItemClickListener(ComponentContainer componentContainer, Table table) {
		table.addItemClickListener(event -> {
				if(event.isDoubleClick()) {
					Notification.show("Clicked!", ""+event.getItemId(),Type.TRAY_NOTIFICATION);
					final EntityItem<E> item = (EntityItem<E>) table.getContainerDataSource().getItem(event.getItemId());
					component.setItem(item, entityContainer, view);
					view.setPopupVisible(true);
				}
				if(event.getButton() == MouseButton.RIGHT) {
					table.select(event.getItemId());
				}
			}
		);
	}

	@Override
	public void addServiceColumn(ComponentContainer componentContainer,Table ttable) {
		TreeTable table = (TreeTable) ttable;
		table.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		table.setItemCaptionPropertyId("label");
		table.setImmediate(true);
		table.setSortContainerPropertyId("sorter");
		table.setSelectable(true);
		table.setCollapsed(1, true);

		table.setDragMode(TableDragMode.ROW);
		
		if(actionHandler != null)
			table.addActionHandler(actionHandler);

		
		table.setDropHandler(new DropHandler() {

			@Override
			public AcceptCriterion getAcceptCriterion() {
				return new Not(VerticalLocationIs.MIDDLE);
			}

			@Override
			public void drop(DragAndDropEvent event) {
				// Wrapper for the object that is dragged
				DataBoundTransferable t = (DataBoundTransferable)
						event.getTransferable();

				AbstractSelectTargetDetails target =
						(AbstractSelectTargetDetails) event.getTargetDetails();

				// Get ids of the dragged item and the target item
				Object sourceItemId = t.getData("itemId");
				Object targetItemId = target.getItemIdOver();

				// Check that the target is not in the subtree of
				// the dragged item itself
				for (Object itemId = targetItemId; itemId != null; itemId = table.getParent(itemId))
					if (itemId == sourceItemId)
						return;

				// On which side of the target was the item dropped 
				VerticalDropLocation location = target.getDropLocation();

				JPAContainer<E> container =	(JPAContainer<E>) table.getContainerDataSource();

				// Do some hassle with the example data representation

				EntityItem<E> beanItem = container.getItem(sourceItemId);

				E bean = beanItem.getEntity();
				EntityItem<E> targetItem = container.getItem(targetItemId);

				// Drop right on an item -> make it a child
				if (location == VerticalDropLocation.MIDDLE) {
					targetItem.getEntity().getChilds().remove(bean);
					targetItem.getEntity().getChilds().add(bean);
					bean.setParent(targetItem.getEntity());
					JPAUtils.save(targetItem.getEntity());
					// Drop at the top of a subtree -> make it previous
				} else if (location == VerticalDropLocation.TOP) {
					E parent = targetItem.getEntity().getParent();
					if(parent != null) {
						List<E> childs = parent.getChilds();
						childs.remove(bean);
						int index = childs.indexOf(targetItem.getEntity());
						if(index >= 0) {
							childs.add(index,bean);
						} else {
							childs.add(bean);
						}
						bean.setParent(parent);
						JPAUtils.save(parent);
					} else {
						if(bean.getParent() != null) {
							bean.getParent().getChilds().remove(bean);
							JPAUtils.save(bean.getParent());
						}
						bean.setParent(null);
					}
				}

				// Drop below another item -> make it next 
				else if (location == VerticalDropLocation.BOTTOM) {
					targetItem.getEntity().getChilds().remove(bean);
					targetItem.getEntity().getChilds().add(bean);
					bean.setParent(targetItem.getEntity());
					JPAUtils.save(targetItem.getEntity());
				}
				JPAUtils.save(bean);
				container.refresh();

				//		        ttable.setItemCaption(beanItem, bean.getLabel());
			}
		});
	}

	public void setAdditionalComponents(PopupView view,
			JPAEntityViewBase<E> navigationLink) {
		this.view = view;
		this.component = navigationLink;
	}

	public Handler getActionHandler() {
		return actionHandler;
	}

	public void setActionHandler(Handler actionHandler) {
		this.actionHandler = actionHandler;
	}
}

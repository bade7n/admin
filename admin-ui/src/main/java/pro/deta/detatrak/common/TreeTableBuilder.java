package pro.deta.detatrak.common;

import java.util.List;

import pro.deta.detatrak.presenter.JPAEntityViewBase;
import pro.deta.detatrak.util.JPAUtils;
import ru.yar.vi.rm.data.HierarchyDO;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
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
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;

public class TreeTableBuilder<E extends HierarchyDO<E>> extends TableBuilder {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1315068038375524149L;
	TreeTable ttable = null;
	private PopupView view;
	private JPAEntityViewBase<E> component;
	private Handler actionHandler;
	
	public TreeTableBuilder(PopupView view,JPAEntityViewBase<E> comp) {
		this.view = view;
		this.component = comp;
	}
	
	
	@Override
	protected Table createTableInstance() {
		ttable = new TreeTable();
		return ttable;
	}
	
	@Override
	protected void addPlusButton(VerticalLayout layout) {
		Button addNew = new Button("Добавить");
		addNew.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				container.addItem();
			}
		});
		layout.addComponent(addNew);
    	layout.addComponent(view);
	}
	
	
	public void setActionHandler(Handler handle) {
		this.actionHandler = handle;
	}
	
	@Override
	public void addServiceColumn() {
		ttable.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		ttable.setItemCaptionPropertyId("label");
		ttable.setImmediate(true);
		ttable.setSortContainerPropertyId("sorter");
		ttable.setSelectable(true);
		ttable.setCollapsed(1, true);

		//		ttable.addContainerProperty("Label", String.class, "");
		ttable.setDragMode(TableDragMode.ROW);
		ttable.addItemClickListener(new ItemClickListener() {

			@Override
			public void itemClick(ItemClickEvent event) {
				if(event.isDoubleClick()) {
					Notification.show("Clicked!", ""+event.getItemId(),Type.TRAY_NOTIFICATION);
					JPAContainer<E> container =	(JPAContainer<E>) ttable.getContainerDataSource();
					final EntityItem<E> item = container.getItem(event.getItemId());
					component.setItem(item, container, view);
					view.setPopupVisible(true);
				}
				if(event.getButton() == MouseButton.RIGHT) {
					ttable.select(event.getItemId());
				}
			}
		});
		
		if(actionHandler != null)
			ttable.addActionHandler(actionHandler);

		
		ttable.setDropHandler(new DropHandler() {

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
				for (Object itemId = targetItemId; itemId != null; itemId = ttable.getParent(itemId))
					if (itemId == sourceItemId)
						return;

				// On which side of the target was the item dropped 
				VerticalDropLocation location = target.getDropLocation();

				JPAContainer<E> container =	(JPAContainer<E>) ttable.getContainerDataSource();

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
}

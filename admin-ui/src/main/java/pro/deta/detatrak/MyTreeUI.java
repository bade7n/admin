package pro.deta.detatrak;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pro.deta.detatrak.confirmdialog.ConfirmDialog;
import pro.deta.detatrak.controls.extra.TerminalLinkView;
import pro.deta.detatrak.util.JPAUtils;
import ru.yar.vi.rm.TimeFormatConverter;
import ru.yar.vi.rm.data.ConfigDO;
import ru.yar.vi.rm.data.SMSDO;
import ru.yar.vi.rm.data.StoredRecordDO;
import ru.yar.vi.rm.data.TerminalLinkDO;
import ru.yar.vi.rm.data.UserDO;
import ru.yar.vi.rm.model.NumberWrapper;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.event.Action;
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
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.PopupView.Content;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.TableDragMode;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;


@Theme("mytheme")
@SuppressWarnings("serial")
public class MyTreeUI extends MyUI {
	private JPAContainer<TerminalLinkDO> items;
	PopupView view = new PopupView(null,null);
	TerminalLinkView v1 = null;
	private Button addNew = new Button("Add New Item");
	JPAContainer<StoredRecordDO> recordContainer = null;
	DateField df = new DateField("Choose date:");
	ComboBox objectChooser = new ComboBox("Choose object:");
	Button bu = new Button("Загрузить");

	protected void postInit() {
//		v1 = new TerminalLinkView();
		user = new UserDO();
		user.setName("admin");
		user.setDescription("Администратор");
		buildOfficeChooser();
		root.removeAllComponents();
		VerticalLayout vl = new VerticalLayout();
		generate3(vl);
		root.addComponent(vl);
	}

	static final Action ACTION_SEND_SMS_1 = new Action("Send sms");
    static final Action[] ACTIONS3 = new Action[] { ACTION_SEND_SMS_1 };
    
	private void generate3(final Layout root) {
		recordContainer = JPAUtils.createCachingJPAContainer(StoredRecordDO.class);
		objectChooser.setContainerDataSource(this.getObjectContainer());
		objectChooser.setItemCaptionPropertyId("name");
		recordContainer.addNestedContainerProperty("action.name");

		HorizontalLayout hl = new HorizontalLayout();
		hl.setSizeFull();
		hl.addComponent(df);
		hl.addComponent(objectChooser);
		hl.addComponent(bu);
		
		root.addComponent(hl);
		final Table table=  new Table();
		table.setContainerDataSource(recordContainer);
		table.setColumnHeader("time", "Время");
		table.addGeneratedColumn("time", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				JPAContainerItem<StoredRecordDO> rec1 = (JPAContainerItem<StoredRecordDO>) source.getContainerDataSource().getItem(itemId);
				StoredRecordDO rec = rec1.getEntity();
				Calendar cal = Calendar.getInstance();
				cal.setTime(rec.getDay());
				cal.set(Calendar.HOUR_OF_DAY, rec.getHour());
				cal.set(Calendar.MINUTE, rec.getStart());
				return new TimeFormatConverter().getFormat().format(cal.getTime());
			}
		});
		table.setColumnHeader("name", "Фамилия Имя Отчество / Наименование юридического лица");
		table.setColumnHeader("action.name", "Услуга");
		table.setColumnHeader("phone", "Телефон");
		
		table.setVisibleColumns("time","name","action.name","phone");

		
		table.setCaption(bundle.getString("label.operator"));
        table.setVisible(false);
		table.addItemClickListener(new ItemClickListener() {

			@Override
			public void itemClick(ItemClickEvent event) {
				if(event.getButton() == MouseButton.RIGHT) {
					table.select(event.getItemId());
				}
			}
		});

        
		bu.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				recordContainer.removeAllContainerFilters();
				table.setVisible(true);
				Date dt = new Date();
				if(df.getValue() != null)
					dt = df.getValue();
				Filter filter = new Compare.Equal("day", dt);
				recordContainer.addContainerFilter(filter);
				if(objectChooser.getValue() != null) {
					Filter filter1 = new Compare.Equal("object", objectChooser.getValue());
					recordContainer.addContainerFilter(filter1);
				}
				recordContainer.sort(new Object[] {"hour","start"}, new boolean[] {true,true});
				root.addComponent(table);
			}
		});
		
		final TextArea ta = new TextArea("Message text");
		final TextField tf = new TextField("Number");
		final Button send = new Button("Send");
		final PopupView sendSmsView = new PopupView(new Content() {
			
			@Override
			public Component getPopupComponent() {
				VerticalLayout vl = new VerticalLayout();
				vl.addComponent(ta);
				vl.addComponent(tf);
				vl.addComponent(send);
				return vl;
			}
			
			@Override
			public String getMinimizedValueAsHTML() {
				return "";
			}
		});
		final SMSDO sm = new SMSDO();
		sm.setSmsType(new NumberWrapper(getConfig("sms.type")).getInteger());
		sm.setCoding(new NumberWrapper(getConfig("sms.coding")).getInteger());
		sm.setCharset(getConfig("sms.charset"));
		sm.setMomt(getConfig("sms.momt"));
		sm.setSmscId(getConfig("sms.smsc_id"));
		sm.setSender(getConfig("sms.sender"));
		
		send.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				SMSDO sms = new SMSDO();
				sms.setMsgdata(ta.getValue());
				sms.setReceiver(tf.getValue());
				sms.setTime(System.currentTimeMillis());
				
				sms.setSmsType(sm.getSmsType());
				sms.setCoding(sm.getCoding());
				sms.setCharset(sm.getCharset());
				sms.setMomt(sm.getMomt());
				sms.setSmscId(sm.getSmscId());
				sms.setSender(sm.getSender());
				JPAUtils.getEntityManager().getTransaction().begin();
				JPAUtils.getEntityManager().persist(sms);
				JPAUtils.getEntityManager().getTransaction().commit();
				sendSmsView.setPopupVisible(false);
			}
		});
		
		sendSmsView.setPopupVisible(false);
		hl.addComponent(sendSmsView);
		
		table.addActionHandler(new Action.Handler() {
			   public Action[] getActions(Object target, Object sender) {
			      return ACTIONS3;
			   }

			   public void handleAction(Action action, Object sender,final Object target) {
				   JPAContainerItem<StoredRecordDO> rec1 = (JPAContainerItem<StoredRecordDO>) recordContainer.getItem(target);
				   ta.setValue(rec1.getEntity().getName());
				   tf.setValue(rec1.getEntity().getPhone());
				   sendSmsView.setPopupVisible(true);
			   }
			});

	}
		
	
	private String getConfig(String string) {
		return ((ConfigDO) JPAUtils.getEntityManager().createQuery("from ConfigDO where name = :name").setParameter("name", string).getSingleResult()).getValue();
	}

	static final Action ACTION_DELETE = new Action("Delete");
    static final Action[] ACTIONS = new Action[] { ACTION_DELETE };

	private void generate2(HorizontalLayout root) {
		items = JPAUtils.createCachingJPAContainer(TerminalLinkDO.class);
		items.setParentProperty("parent");
		root.addComponent(addNew);
		addNew.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				TerminalLinkDO entity = new TerminalLinkDO();
				entity.setName("New navigation");
				items.addEntity(entity );
			}
		});


		
		final TreeTable ttable = new TreeTable("My TreeTable",items);
		ttable.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		ttable.setItemCaptionPropertyId("label");
		ttable.setWidth("500px");
		ttable.setImmediate(true);
		ttable.setColumnHeader("label", "Label");
		ttable.setColumnHeader("id", "Identifier");
		ttable.setVisibleColumns(new String[] {"label","id"});
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
					JPAContainer<TerminalLinkDO> container =
							(JPAContainer<TerminalLinkDO>) ttable.getContainerDataSource();
					final EntityItem<TerminalLinkDO> item = container.getItem(event.getItemId());
					v1.setItem(item, container,view);
					view.setPopupVisible(true);
				}
				if(event.getButton() == MouseButton.RIGHT) {
					ttable.select(event.getItemId());
				}
			}
		});
		
		ttable.addActionHandler(new Action.Handler() {
			   public Action[] getActions(Object target, Object sender) {
			      return ACTIONS;
			   }

			   public void handleAction(Action action, Object sender,final Object target) {
				   ConfirmDialog.show(MyUI.getCurrent(), "Подтверждение", "Вы уверены?",
							"Да", "Нет", new ConfirmDialog.Listener() {

						public void onClose(ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								EntityItem<TerminalLinkDO> trg = items.getItem(target);
								trg.getEntity().getParent().getChilds().remove(trg.getEntity());
								items.removeItem(target);
								items.commit();
								items.refresh();
							} else {
								// User did not confirm
								//                    	                    feedback(dialog.isConfirmed());
							}
						}
					});
//				   JPAUtils.remove(items.getItem(target).getEntity());
//				   items.removeItem(target);
//				   items.commit();
//				   items.refresh();
			   }
			});
		
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

				JPAContainer<TerminalLinkDO> container =
						(JPAContainer<TerminalLinkDO>) ttable.getContainerDataSource();

				// Do some hassle with the example data representation

				EntityItem<TerminalLinkDO> beanItem = container.getItem(sourceItemId);

				TerminalLinkDO bean = beanItem.getEntity();
				EntityItem<TerminalLinkDO> targetItem = container.getItem(targetItemId);

				// Drop right on an item -> make it a child
				if (location == VerticalDropLocation.MIDDLE) {
					targetItem.getEntity().getChilds().remove(bean);
					targetItem.getEntity().getChilds().add(bean);
					bean.setParent(targetItem.getEntity());
					JPAUtils.save(targetItem.getEntity());
					// Drop at the top of a subtree -> make it previous
				} else if (location == VerticalDropLocation.TOP) {
					TerminalLinkDO parent = targetItem.getEntity().getParent();
					if(parent != null) {
						List<TerminalLinkDO> childs = parent.getChilds();
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
		root.addComponent(ttable);
		initPopup();
	}

	private void initPopup() {
		root.addComponent(view);
		view.setContent(new Content() {
			
			@Override
			public Component getPopupComponent() {
				return v1;
			}
			
			@Override
			public String getMinimizedValueAsHTML() {
				return "";
			}
		});
	}
	
}


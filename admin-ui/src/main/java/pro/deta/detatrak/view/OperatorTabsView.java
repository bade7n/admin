package pro.deta.detatrak.view;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.presenter.TabViewPresenter;
import pro.deta.detatrak.util.DETAHelper;
import pro.deta.detatrak.util.DataUtil;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.util.RightPaneTabsView;
import pro.deta.detatrak.util.TopLevelMenuView;
import pro.deta.security.SecurityElement;
import ru.yar.vi.rm.DateTimeFormatConverter;
import ru.yar.vi.rm.data.ConfigDO;
import ru.yar.vi.rm.data.NotificationDO;
import ru.yar.vi.rm.data.NotificationEvent;
import ru.yar.vi.rm.data.NotificationSMSConnectorDO;
import ru.yar.vi.rm.data.RecordHistoryDO;
import ru.yar.vi.rm.data.SMSDO;
import ru.yar.vi.rm.data.StoredRecordDO;
import ru.yar.vi.template.MiniTemplator;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.addon.jpacontainer.util.DefaultQueryModifierDelegate;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.event.Action;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.PopupView.Content;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@TopLevelMenuView(icon="icon-eye")
public class OperatorTabsView extends RightPaneTabsView  implements Captioned,Initializable,Restrictable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1286752618922726883L;
	public static final String NAV_KEY = "/oper"; 
    private JPAContainer<StoredRecordDO> recordContainer;
	static final Action ACTION_SEND_SMS_1 = new Action(Messages.getString("OperatorTabsView.1")); 
	static final Action ACTION_REMOVE = new Action(Messages.getString("OperatorTabsView.5")); 
    static final Action[] ACTIONS3 = new Action[] { ACTION_SEND_SMS_1,ACTION_REMOVE };
	StoredRecordDO currentEditingRecord;

    
	private DateField df;
	private ComboBox objectChooser ;
	private TextField name;
	private CheckBox lookInArchives;
	private CheckBox lookInCancelled;
	private Button bu;
	Table table;
	VerticalLayout vl;
	
	/*Контролы попапа для отсылки смс*/
	ComboBox template;
	TextArea ta; 
	TextField tf; 
	Button send; 
	VerticalLayout popupLayout;
	PopupView sendSmsView; 
    
	@Override
	public void initTabs(TabSheet tabs) {
		vl = new VerticalLayout();
		popupLayout = new VerticalLayout();
		sendSmsView = new PopupView(new Content() {
			
			@Override
			public Component getPopupComponent() {
				return popupLayout;
			}
			
			@Override
			public String getMinimizedValueAsHTML() {
				return Messages.getString("OperatorTabsView.21"); 
			}
		});
	    
		df = new DateField(Messages.getString("OperatorTabsView.2")); 
		objectChooser = new ComboBox(Messages.getString("OperatorTabsView.3")); 
		objectChooser.setWidth(90f, Unit.PERCENTAGE);
		bu = new Button(Messages.getString("OperatorTabsView.4")); 
		bu.addStyleName("small"); 
		name = new TextField(Messages.getString("OperatorTabsView.6")); 
		name.setWidth(90f, Unit.PERCENTAGE);
		lookInArchives = new CheckBox(Messages.getString("OperatorTabsView.7")); 
		lookInCancelled = new CheckBox(Messages.getString("OperatorTabsView.8")); 

		ta = new TextArea(Messages.getString("OperatorTabsView.15"));
		List<SecurityElement> secList = DataUtil.getRestrictions(MyUI.getCurrentUI().getUser());
		if(!DataUtil.matchElement(secList, SecurityElement.OPER_SMS))
			ta.setEnabled(false);
		tf = new TextField(Messages.getString("OperatorTabsView.16")); 
		send = new Button(Messages.getString("OperatorTabsView.17")); 

		template = new ComboBox(Messages.getString("OperatorTabsView.14"));
		template.setNullSelectionAllowed(false);
		template.setImmediate(true);
		template.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		template.setItemCaptionPropertyId("name");

		template.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				MiniTemplator.TemplateSpecification spec = new MiniTemplator.TemplateSpecification();
				spec.templateFileName = getConfig("template.sms.key");
				NotificationDO notify = (NotificationDO) template.getValue();
				if(notify != null) {
					spec.templateText = notify.getTemplate();
					String out = DETAHelper.getTemplate(currentEditingRecord, spec);
					ta.setValue(out);
				}
			}
		});
		
		table=  new Table();
		recordContainer = JPAUtils.createCachingJPAContainer(StoredRecordDO.class);
		recordContainer.addNestedContainerProperty("action.name"); 
		recordContainer.addNestedContainerProperty("object.name"); 
		tabs.addTab(createRepListTable(), Messages.getString("OperatorTabsView.11")); 
	}

	private Component createRepListTable() {
		GridLayout cssLayout = new GridLayout(3,2);
		cssLayout.addStyleName("tabsheet-content"); 
		cssLayout.setWidth("100%"); 
		cssLayout.addComponent(name);
		cssLayout.addComponent(objectChooser);
		cssLayout.addComponent(bu);
		cssLayout.setComponentAlignment(bu, Alignment.BOTTOM_LEFT);
		cssLayout.addComponent(df);
		VerticalLayout verticalLayout = new VerticalLayout(lookInArchives,lookInCancelled);
		cssLayout.addComponent(verticalLayout);
		
		popupLayout.addComponent(template);
		popupLayout.addComponent(ta);
		popupLayout.addComponent(tf);
		popupLayout.addComponent(send);
		
		objectChooser.setContainerDataSource(MyUI.getCurrentUI().getObjectContainer());
		objectChooser.setItemCaptionPropertyId("name");
		
		sendSmsView.setPopupVisible(false);
		sendSmsView.setHideOnMouseOut(false);
		
		cssLayout.addComponent(sendSmsView);
//		vl.setExpandRatio(sendSmsView, 1f);
		vl.addComponent(cssLayout);
		vl.setSizeFull();
		table.setSizeFull();
		table.setVisible(false);
		table.setContainerDataSource(recordContainer);
		table.setColumnHeader("time", Messages.getString("OperatorTabsView.23"));  //$NON-NLS-2$
		table.addGeneratedColumn("time", new Table.ColumnGenerator() { 
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				JPAContainerItem<StoredRecordDO> rec1 = (JPAContainerItem<StoredRecordDO>) source.getContainerDataSource().getItem(itemId);
				StoredRecordDO rec = rec1.getEntity();
				Calendar cal = Calendar.getInstance();
				cal.setTime(rec.getDay());
				cal.set(Calendar.HOUR_OF_DAY, rec.getHour());
				cal.set(Calendar.MINUTE, rec.getStart());
				return new DateTimeFormatConverter().getFormat().format(cal.getTime());
			}
		});
		table.setColumnHeader("name", Messages.getString("OperatorTabsView.26"));  //$NON-NLS-2$
		table.setColumnHeader("action.name", Messages.getString("OperatorTabsView.28"));  //$NON-NLS-2$
		table.setColumnHeader("object.name", Messages.getString("OperatorTabsView.30"));  //$NON-NLS-2$
		table.setColumnHeader("status", Messages.getString("OperatorTabsView.32"));  //$NON-NLS-2$
		table.setColumnHeader("phone", Messages.getString("OperatorTabsView.34"));  //$NON-NLS-2$
		table.setVisibleColumns("time","object.name","name","action.name","status","phone");  //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		table.addItemClickListener(new ItemClickListener() {

			@Override
			public void itemClick(ItemClickEvent event) {
				if(event.getButton() == MouseButton.RIGHT) {
					table.select(event.getItemId());
				}
			}
		});
		table.addActionHandler(new Action.Handler() {
			   public Action[] getActions(Object target, Object sender) {
			      return ACTIONS3;
			   }

			   public void handleAction(Action action, Object sender,final Object target) {
				   JPAContainerItem<StoredRecordDO> rec1 = (JPAContainerItem<StoredRecordDO>) recordContainer.getItem(target);
				   currentEditingRecord = rec1.getEntity();
				   if(action == ACTION_SEND_SMS_1) {
					   NotificationDO notif = null;
					   final BeanItemContainer<NotificationDO> c = new BeanItemContainer<>( NotificationDO.class );
					   if (currentEditingRecord.getAction().getNotifications() != null) {
						   for (Iterator<NotificationDO> i = currentEditingRecord.getAction().getNotifications().iterator(); i.hasNext();) {
							   NotificationDO notify = i.next();
							   if(notify.getEvent() == NotificationEvent.OPERATOR) {
								   c.addItem(notify);
								   if(notif == null)
									   notif = notify;
							   }
						   }
					   }
					   if(notif != null) {
						   template.setContainerDataSource(c);
						   template.select(notif);
						   MiniTemplator.TemplateSpecification spec = new MiniTemplator.TemplateSpecification();
						   spec.templateFileName = getConfig("template.sms.key");
						   spec.templateText = notif.getTemplate();
						   String out = DETAHelper.getTemplate(currentEditingRecord, spec);
						   ta.setValue(out);
						   String phone = currentEditingRecord.getPhone();
						   if(phone != null && phone.startsWith(Messages.getString("OperatorTabsView.42"))) 
							   phone = phone.replaceFirst(Messages.getString("OperatorTabsView.43"), Messages.getString("OperatorTabsView.44"));  //$NON-NLS-2$
						   tf.setValue(phone);
						   sendSmsView.setPopupVisible(true);
					   }
				   } else if(action == ACTION_REMOVE) {
					   currentEditingRecord.setStatus("D:"+MyUI.getCurrentUI().getUser().getName()+"/"+new Date().getTime());
					   currentEditingRecord.setUpdateDate(new Date());
					   currentEditingRecord.getRecordHistory().add(new RecordHistoryDO(MyUI.getCurrentUI().getUser().getName(), MyUI.getCurrentUI().getRemoteHost(), "D"));
					   JPAUtils.getEntityManager().getTransaction().begin();
					   JPAUtils.getEntityManager().persist(currentEditingRecord);
					   JPAUtils.getEntityManager().getTransaction().commit();
					   recordContainer.refreshItem(target);
				   }
			   }
			});

        bu.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				updateRecordContinerFilters();
			}
		});
		
		
		send.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				NotificationDO notify = (NotificationDO) template.getValue();
				if(notify != null) {
					if(notify.getConnector() instanceof NotificationSMSConnectorDO) {
						NotificationSMSConnectorDO conn = (NotificationSMSConnectorDO) notify.getConnector();
						SMSDO sms = new SMSDO();
						try {
							sms.setMsgdata(URLEncoder.encode(ta.getValue(),conn.getSmsCharset()));
						} catch (UnsupportedEncodingException e) {
							sms.setMsgdata(ta.getValue());
						}
						sms.setReceiver(tf.getValue());
						sms.setTime(System.currentTimeMillis());
						
						sms.setSmsType(conn.getSmsType().getValue());
						sms.setCoding(conn.getSmsEncoding().getValue());
						sms.setCharset(conn.getSmsCharset());
						sms.setMomt(conn.getMomt());
						sms.setSmscId(conn.getSmscId());
						sms.setSender(conn.getSender());
						JPAUtils.getEntityManager().getTransaction().begin();
						JPAUtils.getEntityManager().persist(sms);
						JPAUtils.getEntityManager().getTransaction().commit();
					}
				}
				sendSmsView.setPopupVisible(false);
			}
		});
		vl.setSizeFull();
		return vl;
    }
	
	protected void updateRecordContinerFilters() {
		recordContainer.removeAllContainerFilters();
		table.setVisible(true);
		if(df.getValue() != null) {
			recordContainer.addContainerFilter(new Compare.Equal("day", df.getValue())); 
		}
		recordContainer.getEntityProvider().setQueryModifierDelegate(
				new DefaultQueryModifierDelegate () {
					@Override
					public void filtersWillBeAdded(
							CriteriaBuilder criteriaBuilder,
							CriteriaQuery<?> query,
							List<Predicate> predicates) {
						Root<?> fromRecord = query.getRoots().iterator().next();

						if(name.getValue() != null ) {
							Path<String> exp = fromRecord.<String>get("name"); 
							predicates.add(criteriaBuilder.like(criteriaBuilder.lower(exp), "%"+name.getValue().toLowerCase()+"%"));  //$NON-NLS-2$
						}

						if(!lookInCancelled.getValue()) {
							Path<String> exp = fromRecord.<String>get("status"); 
							predicates.add(criteriaBuilder.notLike(exp, "D%")); 
						}
					}
				});
		if(!lookInArchives.getValue() && df.getValue() == null) {
			recordContainer.addContainerFilter(new Compare.GreaterOrEqual("day",new Date())); 
		}

		if(objectChooser.getValue() != null) {
			Filter filter1 = new Compare.Equal("object", objectChooser.getValue()); 
			recordContainer.addContainerFilter(filter1);
		}

		recordContainer.sort(new Object[] {"day","hour","start"}, new boolean[] {true,true,true});  //$NON-NLS-2$ //$NON-NLS-3$
		
		if(vl.getComponentIndex(table) < 0) {
			vl.addComponent(table);
			vl.setExpandRatio(table, 1000);
		}
	}

	private String getConfig(String key,String defaultValue) {
		try {
			return getConfig(key);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	private String getConfig(String string) {
		return ((ConfigDO) JPAUtils.getEntityManager().createQuery("from ConfigDO where name = :name").setParameter("name", string).getSingleResult()).getValue();  //$NON-NLS-2$
	}

	@Override
	public SecurityElement getRestriction() {
		return SecurityElement.OPER_ACCESS;
	}

	@Override
	public void init(TabViewPresenter presenter) {
		presenter.getNavigator().addView(NAV_KEY, this);
	}

	@Override
	public void changeView(ViewChangeEvent viewChangeEvent) {
	}

	@Override
	public String getCaption() {
		return bundle.getString("label.operator"); 
	}
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}

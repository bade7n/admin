package pro.deta.detatrak.view;

import java.util.List;

import pro.deta.detatrak.MyUI;
import pro.deta.detatrak.common.TableBuilder;
import pro.deta.detatrak.common.TreeTableBuilder;
import pro.deta.detatrak.confirmdialog.ConfirmDialog;
import pro.deta.detatrak.controls.extra.AdvertView;
import pro.deta.detatrak.controls.extra.ComplexReportView;
import pro.deta.detatrak.controls.extra.ConfigurationView;
import pro.deta.detatrak.controls.extra.NavigationView;
import pro.deta.detatrak.controls.extra.ObjectTypeItemView;
import pro.deta.detatrak.controls.extra.ObjectTypeView;
import pro.deta.detatrak.controls.extra.ReportObjectView;
import pro.deta.detatrak.controls.extra.SiteView;
import pro.deta.detatrak.controls.extra.TerminalLinkView;
import pro.deta.detatrak.controls.extra.TerminalPageView;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.util.RightPaneTabsView;
import pro.deta.detatrak.util.TopLevelMenuView;
import pro.deta.security.SecurityElement;
import ru.yar.vi.rm.data.AdvertisementDO;
import ru.yar.vi.rm.data.ComplexReportDO;
import ru.yar.vi.rm.data.ConfigDO;
import ru.yar.vi.rm.data.NavigationDO;
import ru.yar.vi.rm.data.ObjectTypeDO;
import ru.yar.vi.rm.data.ObjectTypeItemDO;
import ru.yar.vi.rm.data.ReportObjectDO;
import ru.yar.vi.rm.data.SiteDO;
import ru.yar.vi.rm.data.TerminalLinkDO;
import ru.yar.vi.rm.data.TerminalPageDO;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.event.Action;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Link;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.PopupView.Content;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;

@TopLevelMenuView(icon="icon-extra")
public class ExtraTabsView extends RightPaneTabsView  implements Captioned,Initializable,Restrictable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1286752618922726883L;
	public static final String NAV_KEY = "/extra";
    private JPAContainer<ObjectTypeDO> objectTypeContainer;
    private JPAContainer<ObjectTypeItemDO> objectTypeItemContainer;
    private JPAContainer<ConfigDO> configContainer;
    private JPAContainer<SiteDO> siteContainer;
    private JPAContainer<ReportObjectDO> reportObjectContainer;
    private JPAContainer<ComplexReportDO> complexReportContainer;
    private JPAContainer<AdvertisementDO> advertContainer;
    private JPAContainer<NavigationDO> navContainer;
    private JPAContainer<TerminalLinkDO> navLinkContainer;
    private JPAContainer<TerminalPageDO> pageContainer;

	@Override
	public void initTabs(TabSheet tabs) {	
    	objectTypeContainer = JPAUtils.createCachingJPAContainer(ObjectTypeDO.class);
    	objectTypeItemContainer = JPAUtils.createCachingJPAContainer(ObjectTypeItemDO.class);
    	configContainer = JPAUtils.createCachingJPAContainer(ConfigDO.class);
    	siteContainer = JPAUtils.createCachingJPAContainer(SiteDO.class);
    	objectTypeItemContainer.addNestedContainerProperty("type.name");
    	objectTypeItemContainer.addNestedContainerProperty("type.type");
    	reportObjectContainer = JPAUtils.createCachingJPAContainer(ReportObjectDO.class);
    	complexReportContainer = JPAUtils.createCachingJPAContainer(ComplexReportDO.class);
    	reportObjectContainer.addNestedContainerProperty("office.name");
    	reportObjectContainer.addNestedContainerProperty("object.name");
    	reportObjectContainer.addNestedContainerProperty("adv.name");
    	advertContainer = JPAUtils.createCachingJPAContainer(AdvertisementDO.class);
    	navContainer = JPAUtils.createCachingJPAContainer(NavigationDO.class);
    	navLinkContainer = JPAUtils.createCachingJPAContainer(TerminalLinkDO.class);
    	navLinkContainer.setParentProperty("parent");
    	pageContainer = JPAUtils.createCachingJPAContainer(TerminalPageDO.class);
    	
        addForInitialization(this);
        
        addTab(createObjectTypesTable());
        addTab(createObjectTypeItemsTable());
        addTab(createConfigurationsTable());
        addTab(createSitesTable());
        addTab(createComplexReportTable());
        addTab(createReportObjectTable());
        addTab(createAdvertTable());
        addTab(createNavigationTable());
        addTab(createNavigationLinkTable());
        addTab(createTerminalPageTable());
        
    }

	
    private TableBuilder createConfigurationsTable() {
    	ConfigurationView configurationView = new ConfigurationView();
    	TableBuilder panel =
                new TableBuilder()
                        .addColumn("name", "Имя параметра")
                        .addColumn("value", "Значение")
                        .setEditItemKey(configurationView.getNavKey())
                        .setBeanContainer(configContainer);
        panel.setCaption("Конфигурация");
//        configurationView.setTableBuilder(panel);
        addForInitialization(configurationView);
        return panel;
    }

	
	private TableBuilder createTerminalPageTable() {
		TerminalPageView view = new TerminalPageView(this);
    	TableBuilder panel = new TableBuilder()
    	.addColumn("name", "Название",0.3f)
    	.addColumn("link", "", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				Link l = new Link("Ссылка на терминал",new ExternalResource("/rm/self/terminal.do?pageId="+itemId));
				l.setTargetName("_blank");
				return l;
			}
		})
    	.setEditItemKey(view.getNavKey())
    	.setBeanContainer(pageContainer);
    	panel.setCaption("Страницы термнала");
    	addForInitialization(view);
    	return panel;
	}

	static final Action ACTION_DELETE = new Action("Delete");
	

    private TableBuilder createNavigationLinkTable() {
    	PopupView view = new PopupView(null,null);
    	final TerminalLinkView v1 = new TerminalLinkView(navLinkContainer);
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
    	
    	TreeTableBuilder panel = new TreeTableBuilder<TerminalLinkDO>(view,v1);
    	panel.addColumn("name", "Название",0.7f);
    	panel.addColumn("id", "Идентификатор",0.3f);
		panel.setEditItemKey(v1.getNavKey());
    	panel.setBeanContainer(navLinkContainer);
    	panel.setActionHandler(new Action.Handler() {
			   public Action[] getActions(Object target, Object sender) {
			      return new Action[] { ACTION_DELETE };
			   }

			   public void handleAction(Action action, Object sender,final Object target) {
				   ConfirmDialog.show(MyUI.getCurrent(), "Подтверждение", "Вы уверены?",
							"Да", "Нет", new ConfirmDialog.Listener() {

						public void onClose(ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								EntityItem<TerminalLinkDO> trg = navLinkContainer.getItem(target);
								trg.getEntity().getParent().getChilds().remove(trg.getEntity());
								navLinkContainer.removeItem(target);
								navLinkContainer.commit();
								navLinkContainer.refresh();
							} else {
							}
						}
					});
			   }
			});
    	addForInitialization(v1);
    	panel.setCaption("Навигация - меню");
    	return panel;
	}
    
    private TableBuilder createNavigationTable() {
    	NavigationView view = new NavigationView(this);
    	TableBuilder panel = new TableBuilder()
    	.addColumn("title", "Название",0.3f)
		.addColumn("link", "", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				Link l = new Link("Ссылка на терминал",new ExternalResource("/rm/self/terminal.do?navId="+itemId));
				l.setTargetName("_blank");
				return l;
			}
		})
    	.setEditItemKey(view.getNavKey())
    	.setBeanContainer(navContainer);
    	panel.setCaption("Навигация");
    	addForInitialization(view);
    	return panel;
	}

	private TableBuilder createReportObjectTable() {
		ReportObjectView view = new ReportObjectView(this);
    	TableBuilder panel = new TableBuilder()
    	.addColumn("office.name", "Офис")
    	.addColumn("object.name", "Объект")
    	.addColumn("adv.name", "Объявление")
    	.addColumn("type", "Тип")
    	.setEditItemKey(view.getNavKey())
    	.setBeanContainer(reportObjectContainer);
    	panel.setCaption("Объекты отчёта");
    	addForInitialization(view);
    	return panel;
	}

	private TableBuilder createComplexReportTable() {
		ComplexReportView view = new ComplexReportView(this);
    	TableBuilder panel = new TableBuilder()
    	.addColumn("name", "Название",0.3f)
    	.addColumn("objects", "Объекты", 0.7f, new Table.ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				List<ReportObjectDO> objects = (List<ReportObjectDO>) source.getContainerProperty(itemId, "objects").getValue();
				String value = "";
				for (ReportObjectDO c : objects) {
					switch (c.getType()) {
					case ADV_TYPE:
						value += "ADV: " + c.getAdv().getName() +"\n";
						break;
					case OBJECT_TYPE:
						value +=  c.getObject().getName() +"\n";
						break;
					case OFFICE_TYPE:
						value += c.getOffice().getName() +"\n";
						break;
					default:
						break;
					}
				}
				TextArea ta = new TextArea();
				ta.setRows(objects.size() == 0 ? 1 : objects.size());
				ta.setWordwrap(false);
				ta.setValue(value);
				ta.setSizeFull();
				ta.setReadOnly(true);
				return ta;
			}
		})
		.addColumn("link", "Ссылка на отчёт", new Table.ColumnGenerator() {
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				Link l = new Link("Ссылка на отчёт",new ExternalResource("/rm/self/report.do?reportId="+itemId));
				l.setTargetName("_blank");
				return l;
			}
		})
    	.setEditItemKey(view.getNavKey())
    	.setBeanContainer(complexReportContainer);
    	panel.setCaption("Отчёты");
    	addForInitialization(view);
    	return panel;
	}

	private TableBuilder createObjectTypeItemsTable() {
		ObjectTypeItemView view = new ObjectTypeItemView(this);
    	TableBuilder panel = new TableBuilder()
    	.addColumn("type.name", "Тип объекта")
    	.addColumn("type.type", "Код")
    	.addColumn("required", "Обязательный")
    	.setEditItemKey(view.getNavKey())
    	.setBeanContainer(objectTypeItemContainer);
    	panel.setCaption("Типы объектов");
    	addForInitialization(view);
    	return panel;
    }

	@Override
    public void changeView(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }

    private TableBuilder createObjectTypesTable() {
    	ObjectTypeView view = new ObjectTypeView();
    	TableBuilder panel = new TableBuilder()
                .addColumn("name", "Имя типа объекта")
                .addColumn("type", "Код типа объекта")
                .setEditItemKey(view.getNavKey())
                .setBeanContainer(objectTypeContainer);
        panel.setCaption("Типы объектов");
        addForInitialization(view);
        return panel;
    }

    private TableBuilder createSitesTable() {
    	SiteView view = new SiteView();
    	TableBuilder panel =
                new TableBuilder()
                        .addColumn("name", "Название")
                        .addColumn("description", "Описание")
                        .setEditItemKey(view.getNavKey())
                        .setBeanContainer(siteContainer);
        panel.setCaption("Площадки");
        addForInitialization(view);
        return panel;
    }

    private TableBuilder createAdvertTable() {
    	AdvertView view = new AdvertView(this);
    	TableBuilder panel =
                new TableBuilder()
                        .addColumn("name", "Название")
                        .addColumn("description", "Описание")
                        .setEditItemKey(view.getNavKey())
                        .setBeanContainer(advertContainer);
        panel.setCaption("Объявления");
        addForInitialization(view);
        return panel;
    }
	@Override
	public String getCaption() {
		return bundle.getString("label.extra");
	}

	public JPAContainer<ObjectTypeDO> getObjectTypeContainer() {
		return objectTypeContainer;
	}

	public JPAContainer<ObjectTypeItemDO> getObjectTypeItemContainer() {
		return objectTypeItemContainer;
	}

	public JPAContainer<ConfigDO> getConfigContainer() {
		return configContainer;
	}

	public JPAContainer<SiteDO> getSiteContainer() {
		return siteContainer;
	}

	public JPAContainer<NavigationDO> getNavContainer() {
		return navContainer;
	}

	@Override
	public SecurityElement getRestriction() {
		return SecurityElement.EXTRA_TAB;
	}

	public JPAContainer<AdvertisementDO> getAdvertContainer() {
		return advertContainer;
	}


    public JPAContainer<ReportObjectDO> getReportObjectContainer() {
		return reportObjectContainer;
	}

	public JPAContainer<ComplexReportDO> getComplexReportContainer() {
		return complexReportContainer;
	}

	@Override
	public String getNavKey() {
		return NAV_KEY;
	}
}

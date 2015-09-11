package pro.deta.detatrak.view;

import java.util.List;

import pro.deta.detatrak.MyUI;
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
import pro.deta.detatrak.util.NewRightPaneTabsView;
import pro.deta.detatrak.util.TopLevelMenuView;
import pro.deta.detatrak.view.layout.FormParameter;
import pro.deta.detatrak.view.layout.Layout;
import pro.deta.detatrak.view.layout.TabSheetLayout;
import pro.deta.detatrak.view.layout.TableColumnLayout;
import pro.deta.detatrak.view.layout.TableLayout;
import pro.deta.detatrak.view.layout.TreeTableLayout;
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
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;

@TopLevelMenuView(icon="icon-extra")
public class ExtraTabsView extends NewRightPaneTabsView  implements Captioned,Initializable,Restrictable{

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
	public Layout getLayoutDefinition() {
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

		TabSheetLayout tsl = new TabSheetLayout();


//		ObjectTypeView objectType = new ObjectTypeView();
//		tsl.addTab(new TableLayout(objectTypeContainer,bundle.getString("label.objectType"), objectType.getNavKey(),
//				new TableColumnLayout("name",bundle.getString("label.name")),
//				new TableColumnLayout("type",bundle.getString("label.type"))
//				));


		ObjectTypeItemView objectTypeItem = new ObjectTypeItemView();
		tsl.addTab(new TableLayout(objectTypeItemContainer, bundle.getString("label.objectTypeItem"),objectTypeItem.getNavKey(), 
				new TableColumnLayout("type.name",bundle.getString("label.type.name")),
				new TableColumnLayout("type.type",bundle.getString("label.type.code")),
				new TableColumnLayout("required",bundle.getString("label.required"))
				));

		ConfigurationView configuration = new ConfigurationView();
		tsl.addTab(new TableLayout(configContainer, bundle.getString("label.configuration"),configuration.getNavKey(), 
				new TableColumnLayout("name",bundle.getString("label.name")),
				new TableColumnLayout("value",bundle.getString("label.value"))
				));

		SiteView site = new SiteView();
		tsl.addTab(new TableLayout(siteContainer, bundle.getString("label.site"),site.getNavKey(), 
				new TableColumnLayout("name",bundle.getString("label.name")),
				new TableColumnLayout("description",bundle.getString("label.description"))
				));

		ComplexReportView complexReport = new ComplexReportView();
		tsl.addTab(new TableLayout(complexReportContainer, bundle.getString("label.complexReport"),complexReport.getNavKey(), 
				new TableColumnLayout("name",bundle.getString("label.name"),0.3f),
				new TableColumnLayout("objects",bundle.getString("label.objects"),new Table.ColumnGenerator() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 4355046341072124674L;

					@SuppressWarnings("unchecked")
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
				},0.7f),
				new TableColumnLayout("link",bundle.getString("label.reportLink"),new Table.ColumnGenerator() {
					/**
					 * 
					 */
					private static final long serialVersionUID = -3592369916305235539L;

					@Override
					public Object generateCell(Table source, Object itemId, Object columnId) {
						Link l = new Link("Ссылка на отчёт",new ExternalResource("/rm/self/report.do?reportId="+itemId));
						l.setTargetName("_blank");
						return l;
					}
				})
		));

		ReportObjectView reportObject = new ReportObjectView();
		tsl.addTab(new TableLayout(reportObjectContainer, bundle.getString("label.reportObject"),reportObject.getNavKey(), 
				new TableColumnLayout("office.name",bundle.getString("label.office")),
				new TableColumnLayout("object.name",bundle.getString("label.object")),
				new TableColumnLayout("adv.name",bundle.getString("label.adv")),
				new TableColumnLayout("type",bundle.getString("label.type"))
				));
		

		AdvertView advert = new AdvertView();
		tsl.addTab(new TableLayout(advertContainer, bundle.getString("label.adv"),advert.getNavKey(), 
				new TableColumnLayout("name",bundle.getString("label.name")),
				new TableColumnLayout("description",bundle.getString("label.description"))
				));

				
		NavigationView navigation = new NavigationView();
		tsl.addTab(new TableLayout(navContainer, bundle.getString("label.navigation"),navigation.getNavKey(), 
				new TableColumnLayout("title",bundle.getString("label.title"),0.3f),
				new TableColumnLayout("link",bundle.getString("label.link"),new Table.ColumnGenerator() {
					private static final long serialVersionUID = -190760629159210981L;

					@Override
					public Object generateCell(Table source, Object itemId, Object columnId) {
						Link l = new Link("Ссылка на терминал",new ExternalResource("/rm/self/terminal.do?navId="+itemId));
						l.setTargetName("_blank");
						return l;
					}
				})
		));
		
		
		TerminalLinkView navigationLink = new TerminalLinkView();
		tsl.addTab(createNavigationLinkTable(navigationLink));
		
		TerminalPageView terminalPage = new TerminalPageView();
		tsl.addTab(new TableLayout(pageContainer, bundle.getString("label.terminalPage"),configuration.getNavKey(), 
				new TableColumnLayout("name",bundle.getString("label.name"),0.3f),
				new TableColumnLayout("link",bundle.getString("label.link"),new Table.ColumnGenerator() {
					/**
					 * 
					 */
					private static final long serialVersionUID = -6597981813395434865L;

					@Override
					public Object generateCell(Table source, Object itemId, Object columnId) {
						Link l = new Link("Ссылка на терминал",new ExternalResource("/rm/self/terminal.do?pageId="+itemId));
						l.setTargetName("_blank");
						return l;
					}
				})
		));

		addForInitialization(this);

//		addForInitialization(objectType,objectTypeContainer);
		addForInitialization(objectTypeItem,objectTypeItemContainer);
		addForInitialization(configuration,configContainer);
		addForInitialization(site,siteContainer);
		addForInitialization(complexReport,complexReportContainer);
		addForInitialization(reportObject,reportObjectContainer);
		addForInitialization(advert,advertContainer);
		addForInitialization(navigation,navContainer);
		addForInitialization(navigationLink,navLinkContainer);
		addForInitialization(terminalPage,pageContainer);
		return tsl;
	}



	


	static final Action ACTION_DELETE = new Action("Delete");

	@SuppressWarnings("serial")
	private Layout<FormParameter<Object>> createNavigationLinkTable(
			TerminalLinkView navigationLink) {
		
		PopupView view = new PopupView(null,null);
		view.setContent(new Content() {
			public Component getPopupComponent() {
				return navigationLink;
			}
			public String getMinimizedValueAsHTML() {
				return "";
			}
		});

		
		TreeTableLayout<TerminalLinkDO> layout = new TreeTableLayout<TerminalLinkDO>(navLinkContainer, bundle.getString("label.navigationLink"),navigationLink.getNavKey(), 
				new TableColumnLayout("name",bundle.getString("label.name"),0.7f),
				new TableColumnLayout("id",bundle.getString("label.id"),0.3f)
		);
		layout.setAdditionalComponents(view,navigationLink);
		layout.setActionHandler(new Action.Handler() {
			public Action[] getActions(Object target, Object sender) {
				return new Action[] { ACTION_DELETE };
			}

			@SuppressWarnings("serial")
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
		return layout;
	}




	@Override
	public void changeView(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
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

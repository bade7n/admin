package pro.deta.detatrak;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Or;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.UI;

import pro.deta.detatrak.common.MyConverterFactory;
import pro.deta.detatrak.controls.OfficeChooserView;
import pro.deta.detatrak.presenter.TabViewPresenter;
import pro.deta.detatrak.util.DataUtil;
import pro.deta.detatrak.util.JPAUtils;
import pro.deta.detatrak.util.MyVerticalLayout;
import pro.deta.detatrak.util.MyViewDisplay;
import pro.deta.detatrak.util.ResourceProperties;
import pro.deta.detatrak.util.TopLevelMenuView;
import pro.deta.detatrak.view.AdminTabsView;
import pro.deta.detatrak.view.Captioned;
import pro.deta.detatrak.view.ExtraTabsView;
import pro.deta.detatrak.view.Initializable;
import pro.deta.detatrak.view.LoginView;
import pro.deta.detatrak.view.ObjectsTabsView;
import pro.deta.detatrak.view.OperatorTabsView;
import pro.deta.detatrak.view.Restrictable;
import pro.deta.detatrak.view.ScheduleTabsView;
import pro.deta.detatrak.view.ServiceTabsView;
import pro.deta.security.SecurityElement;
import ru.yar.vi.rm.data.ObjectDO;
import ru.yar.vi.rm.data.OfficeDO;
import ru.yar.vi.rm.data.SiteDO;
import ru.yar.vi.rm.data.UserDO;

@Theme("mytheme")
@SuppressWarnings("serial")
public class MyUI extends UI {
	public static final Logger logger = LoggerFactory.getLogger(MyUI.class);
	private static List<Class<? extends View>> topLevelMenus = null;
	
	ResourceProperties bundle = null;
	
	HorizontalLayout root = new HorizontalLayout();
	CssLayout content = new CssLayout();
	CssLayout menu = new CssLayout();

	/* Office Chooser container */
	private JPAContainer<ObjectDO> objectContainer = null;
	private JPAContainer<UserDO> userContainer = null;
	private JPAContainer<SiteDO> siteContainer = null;


	protected UserDO user = null;
	private SiteDO site = null;	
	private OfficeDO office = null;	
	private MyViewDisplay viewDisplay = null;
	private String remoteHost;
	

	@Override
	protected void init(VaadinRequest request) {//new java.io.File(((java.net.URLClassLoader) (Thread.currentThread().getContextClassLoader())).getURLs()[0].getPath()+"/resource.properties").exists()
		//bundle = Utf8ResourceBundle.getBundle("resource");Thread.currentThread().getContextClassLoader().getResource("resource")
		bundle = new ResourceProperties(ResourceBundle.getBundle("resource"));
		remoteHost = request.getRemoteHost();
		userContainer = JPAUtils.createJPAContainer(UserDO.class);
		objectContainer = JPAUtils.createCachingJPAContainer(ObjectDO.class);
		siteContainer = JPAUtils.createJPAContainer(SiteDO.class);
		initResources();
		VaadinSession.getCurrent().setConverterFactory(new MyConverterFactory());
		root.setSizeFull();
		setContent(root);
		postInit();
	}

	public String getRemoteHost() {
		return remoteHost;
	}

	protected void postInit() {
		root.addComponent(new LoginView());
	}

	private void initResources() {
		topLevelMenus = new ArrayList<Class<? extends View>>() {{
			add(OfficeChooserView.class);
			add(ScheduleTabsView.class);
			add(ObjectsTabsView.class);
			add(ServiceTabsView.class);
			add(ExtraTabsView.class);
			add(OperatorTabsView.class);
			add(AdminTabsView.class);
		}};
	}

//	public JPAContainer<OfficeDO> getOfficeContainer() {
//		return officeContainer;
//	}
	
	private BeanContainer<Integer, OfficeDO> officeContainer;
	
	public BeanContainer<Integer, OfficeDO> createOfficeContainer() {
    	List<OfficeDO> offices = MyUI.getCurrentUI().getSite().getOffices();
    	return createContainer(offices, OfficeDO.class, "id");
	}
	
	 public static <T> BeanContainer<Integer, T> createContainer(List<T> list,Class<T> cl, String idColumn) {
    	BeanContainer<Integer, T> ontainer = new BeanContainer<>(cl);
    	ontainer.setBeanIdProperty(idColumn);
    	ontainer.addAll(list);
    	return ontainer;
	}

	
	public UserDO getUser() {
		return user;
	}

	public void setUser(UserDO user) {
		this.user = user;
	}

	public ResourceProperties getBundle() {
		return bundle;
	}
	
	public void buildOfficeChooser() {
		root.removeAllComponents();
		buildMenu();        		
	}


	
	protected void buildMenu() {
		this.viewDisplay = new MyViewDisplay(content);
		setNavigator(new Navigator(this, viewDisplay));
		root.addStyleName("main-view");

		root.addComponent(new MyVerticalLayout("sidebar",menu));
		MyVerticalLayout l = new MyVerticalLayout("", content);
		l.setSizeFull();
		root.addComponent(l);
		
		root.setExpandRatio(l, 1);
		content.setSizeFull();

		menu.removeAllComponents();

		for (Class<?> class1 : topLevelMenus) {
			initializeTopLevelMenu(class1);
		}
		

		menu.addStyleName("menu");
		menu.setHeight("100%");
		menu.getComponent(0).addStyleName("selected");
		getNavigator().addView(OfficeChooserView.NAV_KEY, new OfficeChooserView());
		getNavigator().navigateTo(OfficeChooserView.NAV_KEY);
	}

	public void initializeTopLevelMenu(Class<?> class1) {
		TopLevelMenuView topLevelAnnotation = class1.getAnnotation(TopLevelMenuView.class);
		if(topLevelAnnotation != null) {
			String icon = topLevelAnnotation.icon();
//				final String nav = topLevelAnnotation.view();
			if (View.class.isAssignableFrom(class1) && Captioned.class.isAssignableFrom(class1)) {
				try {
					View v = (View) class1.newInstance();
					Captioned capt = (Captioned) v;
					final String nav = capt.getNavKey();
					if(Restrictable.class.isInstance(v)) {
						Restrictable init = (Restrictable) v;
						List<SecurityElement> secList = DataUtil.getRestrictions(user);
						if(!DataUtil.matchElement(secList, init.getRestriction()))
							return;
					}
						
					
					if(Initializable.class.isInstance(v)) {
						Initializable init = (Initializable) v;
						TabViewPresenter presenter = new TabViewPresenter(getNavigator(), nav);
						init.init(presenter);
					}

					Button b = new NativeButton(capt.getCaption());
					b.addStyleName(icon);
					b.addClickListener(new Button.ClickListener() {
						@Override
						public void buttonClick(Button.ClickEvent event) {
							clearMenuSelection();
							event.getButton().addStyleName("selected");
							if (!getNavigator().getState().equals(nav))
								getNavigator().navigateTo(nav);
						}
					});
					menu.addComponent(b);
				} catch (InstantiationException | IllegalAccessException e) {
					logger.error("Error occured while constructing class " + class1,e);
				}
			} else {
				logger.error("Error occured while constructing class " + class1 +" annotation " + TopLevelMenuView.class + "is not available.");
			}
		}
	}

	private void clearMenuSelection() {
		for (Iterator<Component> it = menu.getComponentIterator(); it.hasNext(); ) {
			Component next = it.next();
			if (next instanceof NativeButton) {
				next.removeStyleName("selected");
			} else if (next instanceof DragAndDropWrapper) {
				((DragAndDropWrapper) next).iterator().next()
				.removeStyleName("selected");
			}
		}
	}

	public void updateContainerForSite() {
		setOfficeContainer(createOfficeContainer());
		OfficeDO office = officeContainer.getItem(officeContainer.firstItemId()).getBean();
		setOffice(office);
		updateContainerForObjects();
	}
	
	public void updateContainerForObjects() {
    	objectContainer.removeAllContainerFilters();
    	Filter[] filters = new Filter[site.getOffices().size()];
    	int i =0;
    	for (OfficeDO of : site.getOffices()) {
    		filters[i++] = new Compare.Equal("office", of);
		}
		objectContainer.addContainerFilter(new Or(filters));
    	if(office != null)
    		objectContainer.addContainerFilter(new Compare.Equal("office", office));
	}
	
	public void setOffice(OfficeDO office) {
		this.office = office;
	}

	public OfficeDO getOffice() {
		return office;
	}

	public JPAContainer<ObjectDO> getObjectContainer() {
		return objectContainer;
	}

	public static MyUI getCurrentUI() {
		return (MyUI) UI.getCurrent();
	}

	public void setBundle(ResourceProperties bundle) {
		this.bundle = bundle;
	}

	public MyViewDisplay getViewDisplay() {
		return viewDisplay;
	}

	public void setViewDisplay(MyViewDisplay viewDisplay) {
		this.viewDisplay = viewDisplay;
	}
	public JPAContainer<UserDO> getUserContainer() {
		return userContainer;
	}

	public JPAContainer<SiteDO> getSiteContainer() {
		return siteContainer;
	}

	public void setSiteContainer(JPAContainer<SiteDO> siteContainer) {
		this.siteContainer = siteContainer;
	}

	public SiteDO getSite() {
		return site;
	}

	public void setSite(SiteDO site) {
		this.site = site;
	}

	public BeanContainer<Integer, OfficeDO> getOfficeContainer() {
		return officeContainer;
	}

	public void setOfficeContainer(BeanContainer<Integer, OfficeDO> officeContainer) {
		this.officeContainer = officeContainer;
	}

}

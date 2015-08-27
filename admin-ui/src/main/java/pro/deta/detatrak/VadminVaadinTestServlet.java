package pro.deta.detatrak;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

@WebServlet(initParams={
		@WebInitParam(name="UI",value="pro.deta.detatrak.MyTestUI"),
		@WebInitParam(name="legacyPropertyToString",value="false"),
		@WebInitParam(name="widgetset",value="pro.deta.detatrak.MyAppWidgetSet"),
		@WebInitParam(name="productionMode",value="false"),
},urlPatterns="/test/*",loadOnStartup=0)
public class VadminVaadinTestServlet extends VadminVaadinServlet {

}

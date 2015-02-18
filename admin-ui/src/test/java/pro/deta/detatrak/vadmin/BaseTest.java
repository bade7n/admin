package pro.deta.detatrak.vadmin;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.After;
import org.junit.Before;

import pro.deta.detatrak.LazyHibernateEntityManagerProvider;
import pro.deta.detatrak.dao.EMDAO;

public abstract class BaseTest {
	protected EntityManager em;
	private LazyHibernateEntityManagerProvider lhemp = LazyHibernateEntityManagerProvider.getInstance();

	public void before() {
		em = EMDAO.getInstance().createEntityManager();
		lhemp.setCurrentEntityManager(em);
	}

	@Before
	public void beforeEach() {
		before();
	}

	public void after() {
		lhemp.setCurrentEntityManager(null);
	}
	
	@After
	public void afterEach() {
		after();
	}


	public String format(Object o) {
		return ToStringBuilder.reflectionToString(o);
	}
}

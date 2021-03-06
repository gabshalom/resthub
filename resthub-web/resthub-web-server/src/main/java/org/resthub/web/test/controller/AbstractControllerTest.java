package org.resthub.web.test.controller;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.resthub.core.test.AbstractTransactionAwareTest;
import org.resthub.core.util.ClassUtils;
import org.resthub.core.util.MetamodelUtils;
import org.resthub.web.controller.GenericController;

import com.sun.jersey.api.NotFoundException;

/**
 * The class is intended to test your controller by directly calling its Java interface.
 * For controller integration test with embeded servlet container, see also AbstractControllerWebTest  
 */
public abstract class AbstractControllerTest <T, ID extends Serializable, C extends GenericController<T, ID>> extends AbstractTransactionAwareTest {


		/**
		 * The controller to test
		 */
		protected C controller;

		protected ID id;

		@PersistenceContext
		private EntityManager em;

		/**
		 * Injection of controller.
		 */
		public void setController(C controller) {
			this.controller = controller;
		}

		/**
		 * Automatically retrieve ID from entity instance.
		 * 
		 * @param obj
		 *            The object from whom we need primary key
		 * @return The corresponding primary key.
		 */
		@SuppressWarnings({ "rawtypes", "unchecked" })
		protected ID getIdFromEntity(T obj) {
			MetamodelUtils utils = new MetamodelUtils<T, ID>(
					(Class<T>) ClassUtils.getGenericTypeFromBean(this.controller),
					em.getMetamodel());
			return (ID) utils.getIdFromEntity(obj);
		}

		@SuppressWarnings("unchecked")
		protected T createTestRessource() throws Exception {
			return (T) ClassUtils.getGenericTypeFromBean(this.controller)
					.newInstance();
		}

		@Before
		public void setUp() throws Exception {
			T resource = controller.create(this.createTestRessource());
			this.id = getIdFromEntity(resource);
		}

		@After
		public void tearDown() throws Exception {
			// Don't use deleteAll because it does not acheive cascade delete
			for (T resource : controller.findAll()) {
				controller.delete(getIdFromEntity(resource));
			}

		}

		@Test
		public void testCreate() throws Exception {
			T resource = controller.create(this.createTestRessource());

			T foundResource = controller.findById(getIdFromEntity(resource));
			Assert.assertNotNull("Resource not created!", foundResource);
		}
		
		@Test
		public abstract void testUpdate();

		@Test(expected=NotFoundException.class)
		public void testDelete() throws Exception {
			T resource = controller.findById(this.id);
			controller.delete(getIdFromEntity(resource));

			controller.findById(this.id);
		}

		@Test(expected=NotFoundException.class)
		public void testDeleteById() throws Exception {
			T resource = controller.findById(this.id);
			controller.delete(getIdFromEntity(resource));
			controller.findById(this.id);
		}

		@Test
		public void testFindById() throws Exception {
			T resource = controller.findById(this.id);
			Assert.assertNotNull("Resource should not be null!", resource);
			Assert.assertEquals("Resource id and resourceId should be equals!",
					this.id, this.getIdFromEntity(resource));
		}

		@Test
		public void testFindAll() throws Exception {
			List<T> resourceList = controller.findAll();
			Assert.assertTrue("No resources found!", resourceList.size() >= 1);
		}

}

package co.com.psl.googlevehicletracking.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This class create a unique Spring Application Context
 * 
 * @author Alejandro
 *
 */
public class SpringAppContextSingleton {

	/**
	 * ClassPath Application Context
	 */
	private ClassPathXmlApplicationContext cac;
	private static SpringAppContextSingleton sacs;

	/**
	 * Private Constructor
	 */
	private SpringAppContextSingleton() {
		cac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
	}

	/**
	 * Return the bean indicated
	 * 
	 * @param beanName
	 *            name of the bean to inject
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public <T> T getBean(String beanName) {
		return (T) cac.getBean(beanName);
	}

	/**
	 * eturn the bean indicated
	 * 
	 * @param beanName
	 *            beanName name of the bean to inject
	 * @param objects
	 *            parameters for initialized the bean
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getBean(String beanName, Object[] objects) {
		return (T) cac.getBean(beanName, objects);
	}

	/**
	 * Get's a unique instance of the SpringAppContextSingleton class
	 * 
	 * @return SpringAppContextSingleton
	 */
	public static SpringAppContextSingleton getSpringAppContextSingleton() {
		if (sacs == null) {
			sacs = new SpringAppContextSingleton();
		}

		return sacs;
	}

	/**
	 * Close the ClasspathApplicationContext to avoid resource leaking
	 */
	public void closeApplicationContext() {
		cac.close();
	}

}

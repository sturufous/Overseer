package ca.bc.gov.tno.overseer;

import java.util.Optional;
import java.util.Properties;
import javax.annotation.PreDestroy;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

/**
 * Development configuration for Hibernate data access. The instantiation of this class is managed automatically by Spring's <code>profiles</code>
 * feature. On invocation, Jorel2 obtains the data-source profile name from the command line and sets this as the active profile. If the profile
 * name matches the one in the @Profile annotation below ('dev') an object of this class will be created and added to the Spring context.
 * 
 * @author Stuart Morse
 * @version 0.0.1
 */

@Component
final class DevDataSourceConfig extends OverseerRoot {
	
	/** Cached SessionFactory used to create a new session for each Jorel2Runnable thread */
	private SessionFactory sessionFactory = null;
		
	/**
	 * This method initializes the Hibernate framework for use throughout the execution of this Jorel2 invocation. It creates a properties object
	 * containing the connection parameters, and adds this to the Hibernate <code>Configuration</code> object. All Hibernate entities are then 
	 * added to the configuration and a <code>ServiceRegistry</code> object is constructed. Finally the thread-safe <code>SessionFactory</code>
	 * is built using the compiled configuration information.
	 * 
	 * @return An Optional object which either contains a SessionFactory or is Empty().
	 */
	public SessionFactory getSessionFactory() {
		
		
		if (sessionFactory == null) {
			try {
				logger.debug("Getting development Hibernate session factory.");
							
				Properties settings = populateSettings("vongole.tno.gov.bc.ca", "1521", "tnotst02", "tno", "tn29tst", "org.hibernate.dialect.Oracle12cDialect");
		        Configuration config = registerEntities();
		        config.setProperties(settings);
		        
		        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
		        
		        sessionFactory = config.buildSessionFactory(serviceRegistry);
		     } catch (HibernateException  e) {
		    	 logger.error("Getting the development Hibernate session factory. Going offline.", e);
		     }
		}
		
        return sessionFactory;
	}
	
	/**
	 * Ensure a clean shutdown of the level 2 cache.
	 */
	@PreDestroy
	private void shutDown() {
		
		sessionFactory.close();
	}
	
	/**
	 * Creates a java.util.Properties object containing all the information necessary to establish a connection with the database.
	 * 
	 * @param systemName The name of the system hosting the TNO database.
	 * @param port The port on which the database is listening for connections.
	 * @param sid The system id of the database.
	 * @param userId The id of the user used to sign in to the database.
	 * @param userPw The password of the user used to sign in to the database.
	 * @param dialect The Hibernate dialect to use in communication with the database.
	 * @return a Properties object containing the data passed in the parameters keyed by org.hibernate.cfg.Enviroment values.
	 */
	
	protected Properties populateSettings(String systemName, String port, String sid, String userId, String userPw, String dialect) {
		
		Properties settings = new Properties();
		
        settings.put(Environment.DRIVER, "oracle.jdbc.OracleDriver");
        settings.put(Environment.URL, "jdbc:oracle:thin:@" + systemName + ":" + port + ":" + sid);
        settings.put(Environment.USER, userId);
        settings.put(Environment.PASS, userPw);
        settings.put(Environment.DIALECT, dialect);
        settings.put("checkoutTimeout", DB_CONNECTION_TIMEOUT);
        //settings.put(AvailableSettings.CONNECTION_HANDLING, PhysicalConnectionHandlingMode.IMMEDIATE_ACQUISITION_AND_HOLD);
        //settings.put(AvailableSettings.RELEASE_CONNECTIONS, ConnectionReleaseMode.ON_CLOSE);
        //settings.put(Environment.SHOW_SQL, "true");
        
        return settings;
	}
	
	/**
	 * Creates a org.hibernate.cfg.Configuration object and registers all the Hibernate entity classes used by Jorel2 with it.
	 * 
	 * @return A configuration object populated with all the annotated entity classes used by Jorel2.
	 */
	protected Configuration registerEntities() {
        
		Configuration config = new Configuration();

        // Register all Hibernate classes used in Jorel2
        config.addAnnotatedClass(Jorel2HostsDao.class);
        
        return config;
	}
}

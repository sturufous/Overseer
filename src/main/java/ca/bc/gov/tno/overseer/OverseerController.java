package ca.bc.gov.tno.overseer;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OverseerController {
	 
	Map<String, MBeanServerConnection> mbeanConnections = new ConcurrentHashMap<>();
	
	@Inject
	DevDataSourceConfig config;
	
	/* @PostConstruct
	private void init() {
		try {
			Runtime rt = Runtime.getRuntime();
			String url = "http://localhost:8080/overseer/serverlist";
			rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
			
			// Mac
			//Runtime rt = Runtime.getRuntime();
			//String url = "http://stackoverflow.com";
			//rt.exec("open " + url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} */
	
	@CrossOrigin(origins = {"*"})
    @RequestMapping(value = "/api/hosts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Jorel2HostsDto>> hosts() {
    	SessionFactory sessionFactory = config.getSessionFactory();
    	Session session = sessionFactory.openSession();
    	List<Jorel2HostsDto> hostList = new ArrayList<>();
		ResponseEntity<List<Jorel2HostsDto>> output = null; 
 	    HttpHeaders responseHeaders = new HttpHeaders();
 	    
 	    responseHeaders.set("Content-Type", "application/json");
    	
    	List<Jorel2HostsDao> results = Jorel2HostsDao.getJorel2Hosts(session);
    	
    	for(Jorel2HostsDao hostDao : results) {
    		Jorel2HostsDto hostDto = new Jorel2HostsDto();
    		hostDto.setHostUrl("host=" + hostDao.getHostIp() + "&port=" + hostDao.getPort());
    		hostDto.setGraph(hostDao.getGraph());
    		hostDto.setSeriesOffset(-1);
    		hostDto.setLineColor(hostDao.getLineColor());
    		hostList.add(hostDto);
    	}
    	
 	    output = new ResponseEntity<List<Jorel2HostsDto>>(hostList, responseHeaders, HttpStatus.CREATED);

		return output;
	}
	
	@CrossOrigin(origins = {"*"})
    @RequestMapping(value = "/api/lastduration", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<PollingResponse> polling(@RequestParam String host, @RequestParam String port) {
		
		ResponseEntity<PollingResponse> output = null; 
		MBeanServerConnection mbsc = null;
 		PollingResponse response = new PollingResponse();
 	    HttpHeaders responseHeaders = new HttpHeaders();
		
	    try {
	 	    responseHeaders.set("Content-Type", "application/json");
	 	    
	 	    mbsc = getBeanServerConnection(host, port);
	 	    
	 	    ObjectName objName = new ObjectName("Jorel2Instance:name=jorel2Mbean");
	 		
	 		String[] attributes = {"LastDuration", "AppConnectionStatusStr", "AppDatabaseProfileName", "AppInstanceName", "AppInstanceRunTime",
	 				"AppStartTime", "ThreadCompleteCount", "ThreadMaxDurationSeconds", "ThreadMinDurationSeconds","BuildNumber", "ActiveThreads", 
	 				"ServerDetails", "EventTypesHandled", "ServerUser", "DatabaseInterruptions"};
	 			    
	 		AttributeList attrs = mbsc.getAttributes(objName, attributes);
	 		
	 		Attribute duration = (Attribute) attrs.get(0);
	 		response.setDuration((Long) ((Attribute) attrs.get(0)).getValue());
	 		response.setConnectionStatus((String) ((Attribute) attrs.get(1)).getValue());
	 		response.setDbProfileName((String) ((Attribute) attrs.get(2)).getValue());
	 		response.setInstanceName((String) ((Attribute) attrs.get(3)).getValue());
	 		response.setInstanceRunTime((String) ((Attribute) attrs.get(4)).getValue());
	 		response.setStartTime((String) ((Attribute) attrs.get(5)).getValue());
	 		response.setThreadsCompleted((Long) ((Attribute) attrs.get(6)).getValue());
	 		response.setMaxDuration((Long) ((Attribute) attrs.get(7)).getValue());
	 		response.setMinDuration((Long) ((Attribute) attrs.get(8)).getValue());
	 		response.setBuildNumber((String) ((Attribute) attrs.get(9)).getValue());
	 		response.setActiveThreads((int) ((Attribute) attrs.get(10)).getValue());
	 		response.setServerDetails((String) ((Attribute) attrs.get(11)).getValue());
	 		response.setEventTypesHandled((String) ((Attribute) attrs.get(12)).getValue());
	 		response.setServerUser((String) ((Attribute) attrs.get(13)).getValue());
	 		response.setDatabaseInterruptions((String[][]) ((Attribute) attrs.get(14)).getValue());
	 		response.setJorelHostIp(host);
	 		
	 	    output = new ResponseEntity<PollingResponse>(response, responseHeaders, HttpStatus.CREATED);
	    } catch (Exception e) {
	    	System.out.println(e);
			mbeanConnections.remove(host);
	    	response.setError(e.toString());
	    	response.setSuccess(false);
	    	response.setEventTypesHandled("Unable to connect to server at: " + host);
	 	    output = new ResponseEntity<PollingResponse>(response, responseHeaders, HttpStatus.CREATED);
	    }
		
		return output;
	}
	
	@CrossOrigin(origins = {"*"})
    @RequestMapping(value = "/api/storage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<StorageResponse> storage(@RequestParam String host, @RequestParam String port) {
		
		ResponseEntity<StorageResponse> output = null; 
		MBeanServerConnection mbsc = null;
		
	    try {
	 	    HttpHeaders responseHeaders = new HttpHeaders();
	 	    responseHeaders.set("Content-Type", "application/json");
	 	    
	 	    mbsc = getBeanServerConnection(host, port);
	 	    
	 	    ObjectName objName = new ObjectName("Jorel2Instance:name=jorel2Mbean");
	 		
	 		String[] attributes = {"StorageArchiveTo", "StorageAvHost", "StorageBinaryRoot", "StorageCaptureDir", "StorageFtpRoot", 
	 				"StorageImportFileHours", "StorageMaxCdSize", "StorageProcessedLoc", "StorageWwwRoot", "FtpHostName", "FtpSecure",
	 				"FtpType", "FtpUserName"};
	 			    
	 		AttributeList attrs = mbsc.getAttributes(objName, attributes);
	 		
	 		Attribute duration = (Attribute) attrs.get(0);
	 		StorageResponse response = new StorageResponse();
	 		response.setArchiveTo((String) ((Attribute) attrs.get(0)).getValue());
	 		response.setAvHost((String) ((Attribute) attrs.get(1)).getValue());
	 		response.setBinaryRoot((String) ((Attribute) attrs.get(2)).getValue());
	 		response.setCaptureDir((String) ((Attribute) attrs.get(3)).getValue());
	 		response.setFtpRoot((String) ((Attribute) attrs.get(4)).getValue());
	 		response.setImportFileHours((String) ((Attribute) attrs.get(5)).getValue());
	 		response.setMaxCdSize((String) ((Attribute) attrs.get(6)).getValue());
	 		response.setProcessedLoc((String) ((Attribute) attrs.get(7)).getValue());
	 		response.setWwwRoot((String) ((Attribute) attrs.get(8)).getValue());
	 		response.setFtpHost((String) ((Attribute) attrs.get(9)).getValue());
	 		response.setFtpSecure((String) ((Attribute) attrs.get(10)).getValue());
	 		response.setFtpType((String) ((Attribute) attrs.get(11)).getValue());
	 		response.setFtpUser((String) ((Attribute) attrs.get(12)).getValue());
	 		
	 	    output = new ResponseEntity<StorageResponse>(response, responseHeaders, HttpStatus.CREATED);
	
	    } catch (Exception e) {
			mbeanConnections.remove(host);
	    	System.out.println(e);
	    }
		
		return output;
	}
	
	@CrossOrigin(origins = {"*"})
    @RequestMapping(value = "/api/mail", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<MailResponse> mail(@RequestParam String host, @RequestParam String port) {
		
		ResponseEntity<MailResponse> output = null; 
		MBeanServerConnection mbsc = null;
		
	    try {
	 	    HttpHeaders responseHeaders = new HttpHeaders();
	 	    responseHeaders.set("Content-Type", "application/json");
	 	    
	 	    mbsc = getBeanServerConnection(host, port);
	 	    
	 	    ObjectName objName = new ObjectName("Jorel2Instance:name=jorel2Mbean");
	 		
	 		String[] attributes = {"MailFromAddress", "MailHostAddress", "MailPortNumber", "MailToAddress"};
	 			    
	 		AttributeList attrs = mbsc.getAttributes(objName, attributes);
	 		
	 		Attribute duration = (Attribute) attrs.get(0);
	 		MailResponse response = new MailResponse();
	 		response.setFromAddress((String) ((Attribute) attrs.get(0)).getValue());
	 		response.setHostAddress((String) ((Attribute) attrs.get(1)).getValue());
	 		response.setPortNumber((String) ((Attribute) attrs.get(2)).getValue());
	 		response.setToAddress((String) ((Attribute) attrs.get(3)).getValue());
	 		
	 	    output = new ResponseEntity<MailResponse>(response, responseHeaders, HttpStatus.CREATED);
	
	    } catch (Exception e) {
			mbeanConnections.remove(host);
	    	System.out.println(e);
	    }
		
		return output;
	}
	
	@CrossOrigin(origins = {"*"})
    @RequestMapping(value = "/api/stop", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> stop(@RequestParam String host, @RequestParam String port) {
		
		ResponseEntity<String> output = null; 
		MBeanServerConnection mbsc = null;
		
	    try {
	 	    HttpHeaders responseHeaders = new HttpHeaders();
	 	    responseHeaders.set("Content-Type", "application/json");
	 	    
	 	    mbsc = getBeanServerConnection(host, port);
	 	    
	 	    ObjectName objName = new ObjectName("Jorel2Instance:name=jorel2Mbean");
	 	    mbsc.invoke(objName, "stop", null, null);
	 			 		
	 	    output = new ResponseEntity<String>("{value: \"Done.\"}", responseHeaders, HttpStatus.CREATED);
	
	    } catch (Exception e) {
			mbeanConnections.remove(host);
	    	System.out.println(e);
	    }
		
		return output;
	}
		
	private MBeanServerConnection getBeanServerConnection(String host, String port) {
		
		MBeanServerConnection mbsc = null;
		JMXServiceURL url = null;
		JMXConnector connection = null;
		
		try {
	 	    if(!mbeanConnections.containsKey(host)) {
		 		url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi");
		 		connection = JMXConnectorFactory.connect(url);
		 		mbsc = connection.getMBeanServerConnection();
		 		mbeanConnections.put(host, mbsc);
	 	    } else {
	 	    	mbsc = mbeanConnections.get(host);
	 	    }
		} catch (Exception e) {
			mbeanConnections.remove(host);
			System.out.println("Exception getting mbean server: " + e);
		}
 	    
 	    return mbsc;
	}
}
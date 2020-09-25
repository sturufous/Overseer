package ca.bc.gov.tno.overseer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

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
public class HelloController {
	 
	Map<String, MBeanServerConnection> mbeanConnections = new ConcurrentHashMap<>();
	
	@CrossOrigin(origins = {"*"})
    @RequestMapping(value = "/lastduration", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<OverseerResponse> hello(@RequestParam String host, @RequestParam String port) {
		
		ResponseEntity<OverseerResponse> output = null; 
		JMXServiceURL url = null;
		JMXConnector connection = null;
		MBeanServerConnection mbsc = null;
		
	    try {
	 	    HttpHeaders responseHeaders = new HttpHeaders();
	 	    responseHeaders.set("Content-Type", "application/json");
	 	    
	 	    if(!mbeanConnections.containsKey(host)) {
		 		url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi");
		 		connection = JMXConnectorFactory.connect(url);
		 		mbsc = connection.getMBeanServerConnection();
		 		mbeanConnections.put(host, mbsc);
	 	    } else {
	 	    	mbsc = mbeanConnections.get(host);
	 	    }
	 	    
	 	    ObjectName objName = new ObjectName("Jorel2Instance:name=jorel2Mbean");
	 		
	 		String[] attributes = {"LastDuration", "AppConnectionStatusStr", "AppDatabaseProfileName", "AppInstanceName", "AppInstanceRunTime",
	 				"AppStartTime", "ThreadCompleteCount", "ThreadMaxDurationSeconds", "ThreadMinDurationSeconds","BuildNumber", "ActiveThreads", 
	 				"ServerDetails", "EventTypesHandled"};
	 			    
	 		AttributeList attrs = mbsc.getAttributes(objName, attributes);
	 		
	 		Attribute duration = (Attribute) attrs.get(0);
	 		OverseerResponse response = new OverseerResponse();
	 		response.setDuration((Long) ((Attribute) attrs.get(0)).getValue());
	 		response.setConnectionStatus((String) ((Attribute) attrs.get(1)).getValue());
	 		response.setDbProfileName((String) ((Attribute) attrs.get(2)).getValue());
	 		response.setInstanceName((String) ((Attribute) attrs.get(3)).getValue());
	 		response.setInstanceRunTime((String) ((Attribute) attrs.get(4)).getValue());
	 		response.setStartTime((String) ((Attribute) attrs.get(5)).getValue());
	 		response.setThreadsCompleted((Integer) ((Attribute) attrs.get(6)).getValue());
	 		response.setMaxDuration((Long) ((Attribute) attrs.get(7)).getValue());
	 		response.setMinDuration((Long) ((Attribute) attrs.get(8)).getValue());
	 		response.setBuildNumber((String) ((Attribute) attrs.get(9)).getValue());
	 		response.setActiveThreads((int) ((Attribute) attrs.get(10)).getValue());
	 		response.setServerDetails((String) ((Attribute) attrs.get(11)).getValue());
	 		response.setEventTypesHandled((String) ((Attribute) attrs.get(12)).getValue());
	 		
	 	    output = new ResponseEntity<OverseerResponse>(response, responseHeaders, HttpStatus.CREATED);
	
	    } catch (Exception e) {
	    	System.out.println(e);
	    }
		
		return output;
	}
}
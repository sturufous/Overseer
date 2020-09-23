package ca.bc.gov.tno.overseer;

import java.util.Date;

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
	
	@CrossOrigin(origins = {"*"})
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Tester> hello(@RequestParam String testvar) {
    	//HISeries chart = new HISeries();
 	    HttpHeaders responseHeaders = new HttpHeaders();
 	    responseHeaders.set("Content-Type", "application/json");
 	    Tester tester = new Tester();
 	    tester.setDuration(Long.valueOf((long) (Math.random() * 1000)));
    	return new ResponseEntity<Tester>(tester, responseHeaders, HttpStatus.CREATED);
    }
}

class Tester {
	Long duration = 0L;
	Long timestamp = new Date().getTime();
	Boolean success = true;
	String error = "No Error.";
	
	public Long getDuration() {
		return duration;
	}
	
	public void setDuration(Long value) {
		this.duration = value;
	}
	
	public Long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Long value) {
		this.timestamp = value;
	}
	
	public Boolean getSuccess() {
		return success;
	}
	
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
	public String getError() {
		return error;
	}
	
	public void setError(String error) {
		this.error = error;
	}
}

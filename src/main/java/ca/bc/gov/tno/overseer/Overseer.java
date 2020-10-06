package ca.bc.gov.tno.overseer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import ca.bc.gov.tno.overseer.OverseerConfiguration;

@SpringBootApplication
public class Overseer {
    public static void main(String[] args) {
    	
		//AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(); 
        //ConfigurableEnvironment env = ctx.getEnvironment(); 
    	//env.setActiveProfiles("dev");
        //ctx.register(OverseerConfiguration.class);
        //ctx.refresh();
    	
        SpringApplication.run(Overseer.class, args);
    }
}
package ca.bc.gov.tno.overseer;


//import org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.Resource;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

//import org.apache.tomcat.util.file.ConfigurationSource.Resource; 

@Configuration
@ComponentScan("ca.bc.gov.tno.overseer")
public class OverseerConfiguration implements WebMvcConfigurer {
	
	/**
	 * Necessary to support deep links from Angular application. Any path beginning with <code>overseer</code> will be 
	 * re-written to <code>/static/index.html</code>.
	 */
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/overseer/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource requestedResource = (Resource) location.createRelative(resourcePath);
 
                        return requestedResource.exists() && requestedResource.isReadable() ? requestedResource
                                : new ClassPathResource("/static/index.html");
                    }
                });
    }
}


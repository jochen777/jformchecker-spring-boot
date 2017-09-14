package de.jformchecker;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import de.jformchecker.themes.BasicBootstrapFormBuilder;

@Configuration
//@ConditionalOnClass(FC.class)
@EnableConfigurationProperties(JFormCheckerProperties.class)
public class JFormCheckerAutoconfiguration extends WebMvcConfigurerAdapter{
 
	@Autowired
	MessageSource messageSource;

	
    @Autowired
    private JFormCheckerProperties jFormCheckerProperties;
 
    @Bean
    //@ConditionalOnMissingBean
    public FormCheckerConfig getConfig() {
    	System.err.println("I was called");
        return new FormCheckerConfig(key -> messageSource.getMessage(key,  null, LocaleContextHolder.getLocale()), new BasicBootstrapFormBuilder());
    }
 
    @Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolver) {
		argumentResolver.add(new FCFormArgumentResolver(getConfig(), messageSource));
	}

}


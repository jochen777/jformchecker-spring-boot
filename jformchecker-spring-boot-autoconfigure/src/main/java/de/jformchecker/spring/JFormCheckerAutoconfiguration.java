package de.jformchecker.spring;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import de.jformchecker.FormCheckerConfig;
import de.jformchecker.themes.BasicBootstrapFormBuilder;

@Configuration
//@ConditionalOnClass(FC.class)
@EnableConfigurationProperties(JFormCheckerProperties.class)
public class JFormCheckerAutoconfiguration extends WebMvcConfigurerAdapter implements ApplicationContextAware{
 
	@Autowired
	MessageSource messageSource;
	 
	
	private ApplicationContext applicationContext;
	
    @Autowired
    private JFormCheckerProperties jFormCheckerProperties;
 
    @Bean
    @ConditionalOnMissingBean
    public FormCheckerConfig getConfig() {
        return new FormCheckerConfig(key -> messageSource.getMessage(key,  null, LocaleContextHolder.getLocale()), new BasicBootstrapFormBuilder());
    }
 
    @Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolver) {
		argumentResolver.add(new FCFormArgumentResolver(getConfig(), messageSource,applicationContext ));
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}


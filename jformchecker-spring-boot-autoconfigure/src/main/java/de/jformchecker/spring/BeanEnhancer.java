package de.jformchecker.spring;

import org.springframework.web.context.request.NativeWebRequest;

import de.jformchecker.FormCheckerForm;

// this enables a spring-bean to enhance a form
public interface BeanEnhancer {
	public FormCheckerForm enhanceForm(FormCheckerForm form, NativeWebRequest request) ;
}

package de.jformchecker.spring;

import de.jformchecker.FormCheckerForm;

// this enables a spring-bean to enhance a form
public interface BeanEnhancer {
	public FormCheckerForm enhanceForm(FormCheckerForm form) ;
}

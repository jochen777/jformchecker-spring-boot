package de.jformchecker.spring;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import de.jformchecker.FormChecker;
import de.jformchecker.FormCheckerConfig;
import de.jformchecker.FormCheckerForm;
import de.jformchecker.adapter.FC;
import de.jformchecker.adapter.FCForm;

public class FCFormArgumentResolver implements HandlerMethodArgumentResolver{

	
	FormCheckerConfig formcheckerConfig;
	MessageSource messageSource;
	ApplicationContext applicationContext;
	
	public FCFormArgumentResolver(FormCheckerConfig formcheckerConfig, MessageSource messageSource, ApplicationContext applicationContext) {
		this.formcheckerConfig = formcheckerConfig;
		this.messageSource = messageSource;
		this.applicationContext = applicationContext;
	}

	@Override
	public Object resolveArgument(MethodParameter methodParam, ModelAndViewContainer mavContainer, NativeWebRequest request,
			WebDataBinderFactory binderFactory) throws Exception {
		
		String parameterName = getParameterName(methodParam);
		
		// RFE: allow id as a param
		String id = "id";
		
		methodParam.increaseNestingLevel();
		Class<?> typeOfBean = methodParam.getNestedParameterType();
		methodParam.decreaseNestingLevel();
		
		FCForm<?> f = (FCForm<?>)BeanUtils.instantiateClass(methodParam.getParameterType());
		Object bean = BeanUtils.instantiateClass(typeOfBean);
		
		f.setModel(bean);
		
		FormCheckerForm form = de.jformchecker.utils.BeanUtils.fromBeanWithMessage(bean, 
				k -> messageSource.getMessage(k, null, LocaleContextHolder.getLocale()));
		
		// is there a form-enhancer?
		if ( methodParam.getParameterAnnotation(FormEnhancer.class) != null) {
			Object annotatedObjectTarget = applicationContext.getBean(methodParam.getParameterAnnotation(FormEnhancer.class).value());
			if (annotatedObjectTarget instanceof BeanEnhancer) {
				BeanEnhancer be = (BeanEnhancer)annotatedObjectTarget;
				form = be.enhanceForm(form, request); 
			}
		}
//		System.err.println("super:" + methodParam.getParameterAnnotation(FormEnhancer.class).value());
//		
		
		FC fc = FC.simple(formcheckerConfig, (HttpServletRequest)request.getNativeRequest(), form);
		f.setFC(fc);
		return f;
	}

	private String getParameterName(MethodParameter methodParam) {
		String parameterName = ModelFactory.getNameForParameter(methodParam);
		return parameterName;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(FCForm.class);
	}
	
	protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
		((ExtendedServletRequestDataBinder) binder).bind((HttpServletRequest)request.getNativeRequest());
	}

	
	private void getInfosAboutArgument(MethodParameter methodParam) {
		
		System.err.println(methodParam.getContainingClass());
		System.err.println(methodParam.getGenericParameterType());
		System.err.println(methodParam.getDeclaringClass());
		System.err.println(methodParam.getNestedGenericParameterType());
		System.err.println(methodParam.getNestedParameterType());
		System.err.println(methodParam.getParameterType());
		System.err.println();
		
		System.err.println(methodParam.getNestingLevel());
		System.err.println("-------------------");
		//methodParam.decreaseNestingLevel();
		methodParam.increaseNestingLevel();
		
		System.err.println(methodParam.getContainingClass());
		System.err.println(methodParam.getGenericParameterType());
		System.err.println(methodParam.getDeclaringClass());
		System.err.println(methodParam.getNestedGenericParameterType());
		System.err.println(methodParam.getNestedParameterType());
		
		System.err.println(methodParam.getParameterType());
//		Object bean = BeanUtils.instantiate(f.getType());

	}

}

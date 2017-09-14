package de.jformchecker;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
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

import de.jformchecker.adapter.FC;
import de.jformchecker.adapter.FCForm;

public class FCFormArgumentResolver implements HandlerMethodArgumentResolver{

	
	FormCheckerConfig formcheckerConfig;
	MessageSource messageSource;
	
	public FCFormArgumentResolver(FormCheckerConfig formcheckerConfig, MessageSource messageSource) {
		this.formcheckerConfig = formcheckerConfig;
		this.messageSource = messageSource;
	}

	@Override
	public Object resolveArgument(MethodParameter methodParam, ModelAndViewContainer mavContainer, NativeWebRequest request,
			WebDataBinderFactory binderFactory) throws Exception {
		
		String parameterName = ModelFactory.getNameForParameter(methodParam);
		
		String id = "id";
		methodParam.increaseNestingLevel();
		Class<?> typeOfBean = methodParam.getNestedParameterType();
		methodParam.decreaseNestingLevel();
		
		FCForm<?> f = (FCForm<?>)BeanUtils.instantiateClass(methodParam.getParameterType());
		Object bean = BeanUtils.instantiateClass(typeOfBean);
		WebDataBinder binder = binderFactory.createBinder(request, bean, parameterName);
		bindRequestParameters(binder, request);

		
		f.setModel(bean);
		FC fc = FC.simpleFromBean(formcheckerConfig, bean, de.jformchecker.utils.BeanUtils.fromBeanWithMessage(bean, 
				k -> messageSource.getMessage(k, null, LocaleContextHolder.getLocale())), 
				request.getParameter(FormChecker.SUBMIT_KEY));
		f.setFC(fc);
		return f;
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

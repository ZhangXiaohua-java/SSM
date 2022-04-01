package cloud.huel.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

/**
 * @author 张晓华
 * @version 1.0
 * 这个类用来替代web.xml中的配置信息,即当前类是web.xml的配置类
 */
public class WebInit extends AbstractAnnotationConfigDispatcherServletInitializer {

	/**
	 * 指定Spring的配置类
	 * @return
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[]{SpringConfig.class};
	}

	/**
	 * 指定Spring MVC的配置类
	 * @return
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[]{MvcConfig.class};
	}

	/**
	 * 指定DispatcherServlet的映射路径,即url-pattern
	 * @return
	 */
	@Override
	protected String[] getServletMappings() {
		return new String[]{"/"};
	}

	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("utf-8");
		characterEncodingFilter.setForceResponseEncoding(true);
		HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();
		return new  Filter []{characterEncodingFilter,hiddenHttpMethodFilter};
	}

}

package cloud.huel.config;

import cloud.huel.interceptor.TestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

/**
 * @author 张晓华
 * @version 1.0
 * 这个类代替Spring MVC的配置文件
 * Spring MVC需要配置的文件内容
 * 		1: 注解驱动   2: view-controller  3: 开启default-servlet-handler
 * 		4: 视图解析器  5:异常处理器  6: 配置拦截器   7: 注解扫描器  8: 文件上传解析器
 *
 */
//1开启注解驱动
@EnableWebMvc
//7开启注解扫描器
@ComponentScan(basePackages = "cloud.huel")
//标识当前类为配置类
@Configuration
public class MvcConfig implements WebMvcConfigurer {

	/**
	 * 3开启默认的servlet处理静态资源的访问即default-servlet-handler,Tomcat提供的Servlet
	 * @param configurer
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	/**
	 * 6配置拦截器
	 * @param registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		HandlerInterceptor interceptor = new TestInterceptor();
		//设置拦截路径和排除的拦截路径
		registry.addInterceptor(interceptor).addPathPatterns("/**").excludePathPatterns("/");
	}

	/**
	 * 5配置异常处理器
	 * @param resolvers
	 */
	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
		SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
		Properties properties = new Properties();
		//key为异常的全限定类名,value是发生指定异常后的要跳转的视图名
		properties.setProperty("java.lang.NullPointerException","error");
		properties.setProperty("java.lang.ArithmeticException","error");
		resolver.setExceptionMappings(properties);
		//设置异常对象,通过这个异常对象可以在异常页面中获取异常信息,这个对象的名字就是key
		resolver.setExceptionAttribute("ex");
		resolvers.add(resolver);
	}

	/**
	 * 2 配置view-controller
	 * @param registry
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
	}
	/**
	 * 8 配置文件上传解析器
	 */

	@Bean
	public MultipartResolver multipartResolver(){
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		return multipartResolver;
	}

	/** 配置Thymeleaf视图解析器
	 * 1 生成模板配置解析器
	 * 2 生成模板引擎,并为模板引擎注入模板配置解析器依赖
	 * 3 生成视图解析器并为其注入模板引擎依赖
	 */
//	生成模板配置解析器
	@Bean
	public ITemplateResolver iTemplateResolver(){
		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
		ServletContextTemplateResolver servletContextTemplateResolver = new ServletContextTemplateResolver(webApplicationContext.getServletContext());
		servletContextTemplateResolver.setPrefix("/WEB-INF/templates/");
		servletContextTemplateResolver.setSuffix(".html");
		servletContextTemplateResolver.setCharacterEncoding("UTF-8");
		servletContextTemplateResolver.setTemplateMode(TemplateMode.HTML);
		return servletContextTemplateResolver;
	}
//	生成模板引擎,Spring MVC会自动为其注入依赖
	@Bean
	public SpringTemplateEngine springTemplateEngine(ITemplateResolver resolver){
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(resolver);
		return templateEngine;
	}
//	生成视图解析器
	@Bean
	public ViewResolver viewResolver(SpringTemplateEngine templateEngine){
		ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
		thymeleafViewResolver.setCharacterEncoding("UTF-8");
		thymeleafViewResolver.setTemplateEngine(templateEngine);
		return thymeleafViewResolver;
	}


}

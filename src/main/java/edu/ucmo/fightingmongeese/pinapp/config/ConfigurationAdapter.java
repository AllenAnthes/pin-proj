package edu.ucmo.fightingmongeese.pinapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
public class ConfigurationAdapter extends WebMvcConfigurerAdapter {

    private RequestInterceptor requestInterceptor;

    @Autowired
    public ConfigurationAdapter(RequestInterceptor requestInterceptor) {
        this.requestInterceptor = requestInterceptor;
    }

    /**
     * Configuration needed to add our custom interceptor to catch incoming
     * requests to be logged.
     *
     * @param registry Spring registry for interceptors
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor)
                .addPathPatterns("/api/**");
    }
//    Method doesn't appear to be necessary anymore?
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(31556926);
//        registry.addResourceHandler("/img/**").addResourceLocations("/img/").setCachePeriod(31556926);
//        registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(31556926);
//    }
//

    /**
     * Configuration to enable the default handler.  Used for mapping static content like bootstrap
     * to the default location. (e.g. '/webjars/bootstrap/etc')
     * <p>
     * TODO: This should be unnecessary in production.  Only used for demo frontend
     *
     * @param configurer
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }


}

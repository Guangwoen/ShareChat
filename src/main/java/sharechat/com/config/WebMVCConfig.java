package sharechat.com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sharechat.com.interceptor.TokenInterceptor;
import sharechat.com.repository.UserRepository;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    private final UserRepository userRepository;

    public WebMVCConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 拦截器注册
     * */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor(userRepository))
                .addPathPatterns("/**") // 为所有请求进行拦截
                .excludePathPatterns("/api/user/**"); // 其中排除login请求的拦截
    }

    /**
     * 解决跨域问题
     * */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        return corsConfiguration;
    }
}

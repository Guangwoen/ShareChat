package sharechat.com.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sharechat.com.interceptor.TokenInterceptor;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor())
                .addPathPatterns("/**") // 为所有请求进行拦截
                .excludePathPatterns("/login"); // 其中排除login请求的拦截
    }
}

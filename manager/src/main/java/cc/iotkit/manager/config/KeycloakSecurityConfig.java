package cc.iotkit.manager.config;


import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.management.HttpSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@KeycloakConfiguration
public class KeycloakSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    @Value("${app.systemRole}")
    private String systemRole;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        SimpleAuthorityMapper grantedAuthorityMapper = new SimpleAuthorityMapper();
        grantedAuthorityMapper.setPrefix("ROLE_");

        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(grantedAuthorityMapper);
        auth.authenticationProvider(keycloakAuthenticationProvider);

    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
//        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
        return new NullAuthenticatedSessionStrategy();
    }

    @Bean
    @Override
    @ConditionalOnMissingBean(HttpSessionManager.class)
    protected HttpSessionManager httpSessionManager() {
        return new HttpSessionManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
                .authorizeRequests()
                .antMatchers("/*.html", "/favicon.ico", "/v2/api-docs", "/webjars/**", "/swagger-resources/**", "/*.js").permitAll()
                .antMatchers("/api/**").hasRole("iot_client_user")
                .antMatchers("/aligenieDevice/invoke/**").hasRole("iot_client_user")
                //客户端用户写权限
                .antMatchers("/space/addSpace/**").hasRole("iot_client_user")
                .antMatchers("/space/saveSpace/**").hasRole("iot_client_user")
                .antMatchers("/space/delSpace/**").hasRole("iot_client_user")
                .antMatchers("/space/saveHome/**").hasRole("iot_client_user")

                .antMatchers(HttpMethod.DELETE).hasRole("iot_write")
                .antMatchers(HttpMethod.PUT).hasRole("iot_write")
                .antMatchers("/**/save*/**").hasRole("iot_write")
                .antMatchers("/**/del*/**").hasRole("iot_write")
                .antMatchers("/**/add*/**").hasRole("iot_write")
                .antMatchers("/**/clear*/**").hasRole("iot_write")
                .antMatchers("/**/set*/**").hasRole("iot_write")
                .antMatchers("/**").hasAnyRole(systemRole)
                .and().csrf().disable();
    }
}
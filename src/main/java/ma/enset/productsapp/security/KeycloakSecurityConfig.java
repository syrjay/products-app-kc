package ma.enset.productsapp.security;

// import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

// @Configuration
@KeycloakConfiguration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class KeycloakSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

  @Override
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
  }

  // gestion des user
  @Autowired
   public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
       KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
       keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
       auth.authenticationProvider(keycloakAuthenticationProvider);
   }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    super.configure(http);
    // pour accéser à /product il faut s'authentifier d'abord
    http.authorizeRequests().antMatchers("/products/**", "/suppliers/**").authenticated();
  }

  /* @Bean
  public KeycloakSpringBootConfigResolver springBootConfigResolver() {
    return new KeycloakSpringBootConfigResolver();
  } */

  @Bean
  public KeycloakRestTemplate keycloakRestTemplate(KeycloakClientRequestFactory keycloakClientRequestFactory) {
    return new KeycloakRestTemplate(keycloakClientRequestFactory);
  }
  
}

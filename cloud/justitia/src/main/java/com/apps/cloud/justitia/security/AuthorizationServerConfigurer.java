package com.apps.cloud.justitia.security;

import com.apps.cloud.justitia.data.repository.AccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.Arrays;

//      http://localhost:8002/oauth/authorize?response_type=code&client_id=sdapplication&scope=read
//      POST ONLY:  http://localhost:8002/oauth/token?client_id=sdapplication&client_secret=sdapplication_secret&grant_type=authorization_code&code=AUTHORIZATION_CODE
//      https://www.digitalocean.com/community/tutorials/oauth-2-ru

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppClientDetailsService clientDetailsService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Value("${jwt.certificate.store.key-store}")
    private Resource keystore;

    @Value("${jwt.certificate.store.key-store-password}")
    private String keystorePassword;

    @Value("${jwt.certificate.key.key-alias}")
    private String keyAlias;

    @Value("${jwt.certificate.key.key-password}")
    private String keyPassword;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.allowFormAuthenticationForClients()
                .passwordEncoder(passwordEncoder); // for client secret
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
//        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(domainEnhancer(), accessTokenConverter()));

        endpoints.tokenStore(tokenStore())
//                .requestFactory(requestFactory())
                .tokenEnhancer(tokenEnhancerChain)
                .approvalStore(approvalStore())
                .userDetailsService(userDetailsService) // for refresh-token grant
                .authenticationManager(authenticationManager); // for resource-owner-password grant
    }
//
//    private OAuth2RequestFactory requestFactory() {
//    }
//
//    private Object domainEnhancer() {
//    }

    @Bean
    @Primary
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    @Bean
    public JdbcApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    @Bean
    public CustomJwtTokenStore tokenStore() {
        CustomJwtTokenStore tokenStore = new CustomJwtTokenStore(accessTokenConverter(), accessTokenRepository);
        tokenStore.setApprovalStore(approvalStore()); // refresh token only if approval still exists
        return tokenStore;
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(keystore, keystorePassword.toCharArray());
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(keyAlias, keyPassword.toCharArray());
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair); //sign with private key.
        return converter;
    }

}

package jy.learning.bootrestapi.configs;

import jy.learning.bootrestapi.accounts.Account;
import jy.learning.bootrestapi.accounts.AccountRepository;
import jy.learning.bootrestapi.accounts.AccountRole;
import jy.learning.bootrestapi.accounts.AccountService;
import jy.learning.bootrestapi.common.AppProperties;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Autowired
            AccountService accountService;

            @Autowired
            AppProperties appProperties;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Account admin = Account.builder()
                        .email(appProperties.getAdminUsername())
                        .password(appProperties.getAdminPassword())
                        .roles(AccountRole.makeSetRoles(AccountRole.ADMIN, AccountRole.USER))
                        .build();

                accountService.saveAccount(admin);

                Account user = Account.builder()
                        .email(appProperties.getUserUsername())
                        .password(appProperties.getUserPassword())
                        .roles(AccountRole.makeSetRoles(AccountRole.USER))
                        .build();

                accountService.saveAccount(user);
            }
        };
    }
}

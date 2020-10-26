package jy.learning.bootrestapi.configs;

import jy.learning.bootrestapi.accounts.Account;
import jy.learning.bootrestapi.accounts.AccountRole;
import jy.learning.bootrestapi.accounts.AccountService;
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

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Set<AccountRole> roles = new HashSet<>();
                roles.add(AccountRole.ADMIN);
                roles.add(AccountRole.USER);

                Account account = Account.builder()
                        .email("joonyeop@email.com")
                        .password("1234")
                        .roles(roles)
                        .build();

                accountService.saveAccount(account);
            }
        };
    }
}

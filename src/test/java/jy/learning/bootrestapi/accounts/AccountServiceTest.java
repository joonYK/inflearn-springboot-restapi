package jy.learning.bootrestapi.accounts;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    public void findByUsername() {
        //given
        Set<AccountRole> roles = new HashSet<>();
        roles.add(AccountRole.ADMIN);
        roles.add(AccountRole.USER);

        String password = "1234";
        String username = "joonyeop@email.com";

        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(roles)
                .build();
        this.accountRepository.save(account);

        //when
        UserDetailsService userDetailsService = (UserDetailsService)accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        //then
        assertThat(userDetails.getPassword()).isEqualTo(password);
        assertThat(userDetails.getUsername()).isEqualTo(username);
    }
}
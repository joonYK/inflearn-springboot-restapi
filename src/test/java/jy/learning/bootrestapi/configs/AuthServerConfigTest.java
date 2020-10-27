package jy.learning.bootrestapi.configs;

import jy.learning.bootrestapi.accounts.Account;
import jy.learning.bootrestapi.accounts.AccountRole;
import jy.learning.bootrestapi.accounts.AccountService;
import jy.learning.bootrestapi.common.BaseControllerTest;
import jy.learning.bootrestapi.common.TestDescription;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Test
    @TestDescription("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception {
        //Given
        String username = "joonyeop@email.com";
        String password = "1234";
        String clientId = "myApp";
        String clientSecret = "pass";

        //when & then
        this.mockMvc.perform(post("/oauth/token")
                    .with(httpBasic(clientId, clientSecret))
                    .param("username", username)
                    .param("password", password)
                    .param("grant_type", "password")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("access_token").isNotEmpty());
    }

}
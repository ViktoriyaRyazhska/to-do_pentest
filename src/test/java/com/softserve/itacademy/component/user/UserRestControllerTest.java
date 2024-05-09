package com.softserve.itacademy.component.user;

import com.softserve.itacademy.config.WithMockCustomUser;
import com.softserve.itacademy.model.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @WithMockCustomUser(email = "admin@example.com", role = UserRole.ADMIN)
    @Test
    public void shouldReturnUserListForAdmin() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(jsonPath("$").isArray())
                .andDo(print());
    }

    @WithMockCustomUser(email = "user@example.com", role = UserRole.USER)
    @Test
    public void shouldReturn403ForNotAdmin() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    public void shouldReturn401ForNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

}
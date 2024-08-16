package article.controller;

import article.model.User;
import article.model.dto.ArticleDto;
import article.model.dto.UserRole;
import article.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class StatisticControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testGetStatistic() throws Exception {
        // Регистрация админа
        User user = User.builder()
                .username("admin")
                .role(UserRole.ROLE_ADMIN)
                .password(passwordEncoder.encode("password"))
                .build();
        userRepository.save(user);


        // Админ создает статьи
        LocalDateTime FirstArticleDate = LocalDateTime.now();
        ArticleDto articleDto = ArticleDto.builder()
                .title("title")
                .author("author")
                .content("content")
                .publishDate(FirstArticleDate)
                .build();

        String articleJson = mapper.writeValueAsString(articleDto);

        mockMvc.perform(post("/api/articles/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(articleJson)
                        .locale(Locale.ENGLISH)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .with(org.springframework.security.test.web.servlet.request
                                .SecurityMockMvcRequestPostProcessors.httpBasic("admin",
                                        "password")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(articleDto.getTitle())))
                .andExpect(jsonPath("$.author", is(articleDto.getAuthor())));

        LocalDateTime SecondArticleDate = LocalDateTime.now().minusDays(2);
        ArticleDto articleDto2 = ArticleDto.builder()
                .title("title")
                .author("author")
                .content("content")
                .publishDate(SecondArticleDate)
                .build();

        String articleJson2 = mapper.writeValueAsString(articleDto2);

        mockMvc.perform(post("/api/articles/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(articleJson2)
                        .locale(Locale.ENGLISH)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .with(org.springframework.security.test.web.servlet.request
                                .SecurityMockMvcRequestPostProcessors.httpBasic("admin",
                                        "password")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(articleDto.getTitle())))
                .andExpect(jsonPath("$.author", is(articleDto.getAuthor())));

        //Получение количества статей по датам
        mockMvc.perform(get("/api/admin/statistics")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .with(org.springframework.security.test.web.servlet.request
                                .SecurityMockMvcRequestPostProcessors.httpBasic("admin",
                                        "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + LocalDate.from(FirstArticleDate), is(1), Integer.class))
                .andExpect(jsonPath("$."+ LocalDate.from(SecondArticleDate), is(1), Integer.class));
    }

    @Test
    public void testGetStatisticWhenNotByAdminThenThrownException() throws Exception {
        // Регистрация пользователя
        String userJson = "{\"username\":\"testuser\", \"password\":\"password\"}";

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk());

        // Авторизованный пользователь создает статью
        ArticleDto articleDto = ArticleDto.builder()
                .title("title")
                .author("author")
                .content("content")
                .publishDate(LocalDateTime.now())
                .build();

        String articleJson = mapper.writeValueAsString(articleDto);

        mockMvc.perform(post("/api/articles/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(articleJson)
                        .locale(Locale.ENGLISH)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .with(org.springframework.security.test.web.servlet.request
                                .SecurityMockMvcRequestPostProcessors.httpBasic("testuser",
                                        "password")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(articleDto.getTitle())))
                .andExpect(jsonPath("$.author", is(articleDto.getAuthor())));

        //Получение количества статей по датам
        mockMvc.perform(get("/api/admin/statistics")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .with(org.springframework.security.test.web.servlet.request
                                .SecurityMockMvcRequestPostProcessors.httpBasic("testuser",
                                        "password")))
                .andExpect(status().isForbidden());
    }
}

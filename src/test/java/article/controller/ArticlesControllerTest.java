package article.controller;

import article.model.dto.ArticleDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ArticlesControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testCreateArticle() throws Exception {
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
    }

    @Test
    public void testCreateArticleByNotAuthorizedUser() throws Exception {

        // Неавторизованный пользователь создает статью
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
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetArticlesList() throws Exception {
        // Регистрация пользователя
        String userJson = "{\"username\":\"testuser\", \"password\":\"password\"}";

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk());


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


        ArticleDto articleDto2 = ArticleDto.builder()
                .title("title")
                .author("author")
                .content("content")
                .publishDate(LocalDateTime.now().minusDays(2))
                .build();

        String articleJson2 = mapper.writeValueAsString(articleDto2);

        mockMvc.perform(post("/api/articles/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(articleJson2)
                        .locale(Locale.ENGLISH)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .with(org.springframework.security.test.web.servlet.request
                                .SecurityMockMvcRequestPostProcessors.httpBasic("testuser",
                                        "password")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(articleDto.getTitle())))
                .andExpect(jsonPath("$.author", is(articleDto.getAuthor())));

        // Получаем список статей
        mockMvc.perform(get("/api/articles/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(org.springframework.security.test.web.servlet.request
                                .SecurityMockMvcRequestPostProcessors.httpBasic("testuser",
                                        "password")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].title", is(articleDto.getTitle())))
                .andExpect(jsonPath("$.[0].author", is(articleDto.getAuthor())))
                .andExpect(jsonPath("$.[1].title", is(articleDto2.getTitle())))
                .andExpect(jsonPath("$.[1].author", is(articleDto2.getAuthor())));
    }
}

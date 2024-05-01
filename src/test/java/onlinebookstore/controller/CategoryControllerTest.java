package onlinebookstore.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import lombok.SneakyThrows;
import onlinebookstore.dto.category.CategoryDto;
import onlinebookstore.dto.category.CategoryRequestDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    protected static MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext webApplicationContext,
                          @Autowired DataSource dataSource) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        tearDown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/add-three-books-to-books-with-categories-table.sql"));
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        tearDown(dataSource);
    }

    @SneakyThrows
    static void tearDown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/remove-all-from-books-with-categories-table.sql")
            );
        }
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("""
            Find all categories from Category Table""
            """)
    void findAll() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto[] actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CategoryDto[].class);
        Assertions.assertEquals(3, actual.length);
    }

    @SneakyThrows
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    @Sql(scripts = {
            "classpath:database/book/remove-all-from-books-with-categories-table.sql",
            "classpath:database/book/add-three-books-to-books-with-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_Category_Success() throws Exception {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Test Category");
        requestDto.setDescription("Test Description");

        CategoryDto expected = new CategoryDto();
        expected.setName("Test Category");
        expected.setDescription("Test Description");

        String request = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/categories")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), CategoryDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @SneakyThrows
    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("""
            Find category by id from Category Table""
            """)
    void findCategory_ById_Success() throws Exception {
        CategoryDto expected = new CategoryDto();
        expected.setId(1L);
        expected.setName("Romance");
        MvcResult result = mockMvc.perform(get("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), CategoryDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @SneakyThrows
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    @Sql(scripts = {
            "classpath:database/book/remove-all-from-books-with-categories-table.sql",
            "classpath:database/book/add-three-books-to-books-with-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_Category_Success() throws Exception {
        Long id = 1L;
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("QQQ");
        CategoryDto expected = new CategoryDto();
        expected.setId(id);
        expected.setName("QQQ");
        String request = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(put("/categories/1")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), CategoryDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(expected, actual);
    }
}

package onlinebookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;
import lombok.SneakyThrows;
import onlinebookstore.dto.book.BookDto;
import onlinebookstore.dto.book.CreateBookRequestDto;
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
class BookControllerTest {
    private static final String ADD_BOOKS_WITH_CATEGORIES_SQL =
            "database/book/add-three-books-to-books-with-categories-table.sql";
    private static final String REMOVE_BOOKS_WITH_CATEGORIES_SQL =
            "database/book/remove-all-from-books-with-categories-table.sql";
    private static final String DELETE_BOOK_WHERE_TITLE_QQQ_SQL =
            "classpath:database/book/delete-book-where-title-qqq.sql";
    private static final String CLASS_PATH = "classpath:";
    protected static MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext webApplicationContext,
            @Autowired DataSource dataSource
    ) throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        tearDown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(ADD_BOOKS_WITH_CATEGORIES_SQL));
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
                    new ClassPathResource(REMOVE_BOOKS_WITH_CATEGORIES_SQL)
            );
        }
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("""
            Find all books from Book Table""
            """)
    void findAll() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto[] bookDtos = mapper.readValue(mvcResult.getResponse().getContentAsString(), BookDto[].class);
        Assertions.assertEquals(3, bookDtos.length);
    }

    @SneakyThrows
    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("""
            Find book by id from Book Table""
            """)
    void findBook_ById_Success() throws Exception {
        BookDto expected = new BookDto();
        expected.setId(1L);
        expected.setTitle("AAA");
        expected.setAuthor("aaa");
        expected.setIsbn("1");
        expected.setPrice(BigDecimal.valueOf(100));
        expected.setCategories(Set.of(2L, 3L));
        MvcResult result = mockMvc.perform(get("/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = mapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @SneakyThrows
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    @Sql(scripts = {
            CLASS_PATH + REMOVE_BOOKS_WITH_CATEGORIES_SQL,
            CLASS_PATH + ADD_BOOKS_WITH_CATEGORIES_SQL
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_Book_Success() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("QQQ");
        requestDto.setAuthor("qqq");
        requestDto.setIsbn("444");
        requestDto.setPrice(BigDecimal.valueOf(400));

        BookDto expected = new BookDto();
        expected.setId(3L);
        expected.setTitle(requestDto.getTitle());
        expected.setAuthor(requestDto.getAuthor());
        expected.setIsbn(requestDto.getIsbn());
        expected.setPrice(requestDto.getPrice());
        expected.setCategories(new HashSet<>());

        String request = mapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/books/3")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        BookDto actual = mapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("""
            Save Book
            """)
    @Sql(scripts = {
            DELETE_BOOK_WHERE_TITLE_QQQ_SQL
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_Book_Success() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("QQQ");
        requestDto.setAuthor("qqq");
        requestDto.setIsbn("444");
        requestDto.setPrice(BigDecimal.valueOf(400));
        BookDto expected = new BookDto();
        expected.setTitle(requestDto.getTitle());
        expected.setAuthor(requestDto.getAuthor());
        expected.setIsbn(requestDto.getIsbn());
        expected.setPrice(requestDto.getPrice());

        String request = mapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(
                        post("/books")
                                .content(request)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        BookDto actual = mapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("""
            Search books with params
            """)
    void search_booksWithParams_Success() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/search?titles=AAA,BBB&authors=aaa,bbb")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        BookDto[] bookDtos = mapper.readValue(result.getResponse().getContentAsString(), BookDto[].class);
        Assertions.assertNotNull(bookDtos);
        Assertions.assertEquals(2, bookDtos.length);
    }
}

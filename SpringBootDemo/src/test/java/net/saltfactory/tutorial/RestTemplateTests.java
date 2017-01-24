package net.saltfactory.tutorial;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestTemplateTests {

    Logger logger = Logger.getLogger(this.getClass());

    @Value("${local.server.port}")
    int port;

    @MockBean
    ArticlesService articlesService;

    private String baseUrl;
    RestTemplate restTemplate;

    @Before
    public void setUp() {
        restTemplate = new RestTemplate();
        baseUrl = "http://localhost:" +  String.valueOf(port);
    }

    private String jsonStringFromObject(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    @MockBean
    private FixturesProperty fixturesProperty;

    @Test
    public void testGetArticles() {
        List<Article> articles = fixturesProperty.getArticles();
        assertThat(articles.size(), is(3));
    }

    @Test
    public void testIndex() throws Exception {

        URI uri = URI.create(baseUrl+ "/api/articles");
        String responseString = restTemplate.getForObject(uri, String.class);

        // 컨트롤러 결과를 로깅
        logger.info(responseString);

        // 컨트롤러 결과를 확인하기 위한 데이터 가져오기
        List<Article> articles = articlesService.getArticles();
        String jsonString = jsonStringFromObject(articles);

        // 컨트롤러의 결과와 JSON 문자열로 비교
        assertThat(responseString, is(equalTo(jsonString)));
    }


    @Test
    public void testIndex1() throws Exception {

        URI uri = URI.create(baseUrl+ "/api/articles");
//  String responseString = restTemplate.getForObject(uri, String.class);
        List<Article> resultArticles = Arrays.asList(restTemplate.getForObject(uri, Article[].class));

// 컨트롤러 결과를 로깅
//  logger.info(responseString);

// 컨트롤러 결과를 확인하기 위한 데이터 가져오기
        List<Article> articles = articlesService.getArticles();
//  String jsonString = jsonStringFromObject(articles);

// 컨트롤러의 결과와 JSON 문자열로 비교
//  assertThat(responseString, is(equalTo(jsonString)));
        assertThat(resultArticles.size(), is(equalTo(articles.size())));
        assertThat(resultArticles.get(0).getId(), is(equalTo(articles.get(0).getId())));
    }


    @Test
    public void testCreate() throws Exception {

        URI uri = URI.create(baseUrl + "/api/articles");

        Article article = new Article();
        article.setTitle("testing create article");
        article.setContent("test content");

        Comment comment = new Comment();
        comment.setContent("test comment1");
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);

        article.setComments(comments);

        Article resultArticle = restTemplate.postForObject(uri, article, Article.class);

        assertThat(resultArticle.getTitle(), is(equalTo(article.getTitle())));


//        String responseString = restTemplate.postForObject(uri, article, String.class);
//        String jsonString = jsonStringFromObject(article);
//
//        assertThat(responseString, is(equalTo(jsonString)));
//        logger.info(responseString);
    }

}

package net.saltfactory.tutorial;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(locations = {"classpath:fixtures.yml"}, prefix = "fixtures")
public class FixturesProperty {
    private List<Article> articles = new ArrayList<>();

    public List<Article> getArticles() {
        return articles;
    }

}

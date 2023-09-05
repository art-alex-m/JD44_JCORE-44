package ru.netology;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.fluent.Content;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import ru.netology.cat.Fact;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class Main {

    public static void main(String[] args) throws IOException {

        String catFactsUrl = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
        ObjectMapper mapper = new ObjectMapper();

        System.out.println("Решение с большим количеством разнообразных настроек");
        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setRedirectsEnabled(false)
                        .setConnectionRequestTimeout(5, TimeUnit.SECONDS)
                        .setResponseTimeout(30, TimeUnit.SECONDS)
                        .build())
                .build()
        ) {
            ClassicHttpRequest request = ClassicRequestBuilder.get().setUri(catFactsUrl).build();
            AtomicReference<List<Fact>> facts1 = new AtomicReference<>();

            httpClient.execute(request, response -> {
                final HttpEntity entity = response.getEntity();
                facts1.set(mapper.readValue(entity.getContent(), new TypeReference<>() {}));
                EntityUtils.consume(entity);
                return true;
            });

            facts1.get().stream()
                    .filter(Fact::hasUpvotes)
                    .forEach(System.out::println);
        }

        System.out.println("Решение с меньшим количеством кода");
        Content content = Request.get(catFactsUrl).execute().returnContent();
        List<Fact> facts2 = mapper.readValue(content.asStream(), new TypeReference<>() {});
        facts2.stream()
                .filter(Fact::hasUpvotes)
                .forEach(System.out::println);

        System.out.println("Решение использует обращение сразу на url из библиотеки jackson");
        List<Fact> facts3 = mapper.readValue(new URL(catFactsUrl), new TypeReference<>() {});
        facts3.stream()
                .filter(Fact::hasUpvotes)
                .forEach(System.out::println);
    }
}
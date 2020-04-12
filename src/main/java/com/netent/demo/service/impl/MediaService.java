package com.netent.demo.service.impl;

import com.google.gson.Gson;
import com.netent.demo.Exceptions.ElasticSearchFailure;
import com.netent.demo.Exceptions.NoMediaPostMatchWithIsbnException;
import com.netent.demo.Exceptions.NoSuchBookFoundException;
import com.netent.demo.Exceptions.UnableToProcessQueryException;
import com.netent.demo.model.MediaPostEntity;
import com.netent.demo.repo.repoManager.BookRepoService;
import com.netent.demo.service.MediaServiceInterface;
import com.netent.demo.utils.Constants;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.params.Parameters;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.search.MatchQuery;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class MediaService implements MediaServiceInterface {


  @Autowired
  private JestClient client;

  @Autowired
  private BookRepoService bookRepoService;

  private Gson gson = new Gson();

  @Value("${media.post.url}")
  private String mediaUrl;

  @Override
  public List<String> getMediaPostsByIsbn(String isbn)
      throws NoSuchBookFoundException, NoMediaPostMatchWithIsbnException,
      UnableToProcessQueryException {

    return findByTitle(bookRepoService.getBookByIsbn(isbn).getTitle());
  }

  private List<String> findByTitle(String query)
      throws NoMediaPostMatchWithIsbnException, UnableToProcessQueryException {
    query = query.toLowerCase();
    BoolQueryBuilder finalQuery = QueryBuilders.boolQuery();
    QueryBuilder locationMatch = QueryBuilders
        .multiMatchQuery(query, Constants.MEDIA_TITLE, Constants.MEDIA_BODY)
        .type(MatchQuery.Type.PHRASE_PREFIX);

    finalQuery.filter(locationMatch);
    SearchResult result = getSearchResultFromES(finalQuery);
    List<String> title = processSearchResult(result);
    if (title.isEmpty()) {
      throw new NoMediaPostMatchWithIsbnException();
    }
    return title;
  }

  private SearchResult getSearchResultFromES(QueryBuilder queryBuilder)
      throws UnableToProcessQueryException {
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(queryBuilder);
    String str = Constants.MEDIA_TITLE;
    searchSourceBuilder.fetchSource(str, null);
    Search userListSearch = new Search.Builder(searchSourceBuilder.toString())
        .addIndex(Constants.ES_INDEX)
        .addType(Constants.ES_TYPE)
        .setParameter(Parameters.SIZE, 15)
        .build();

    try {
      SearchResult result = client.execute(userListSearch);
      return result;
    } catch (IOException e) {
      //Log it:
      throw new UnableToProcessQueryException();
    }
  }

  private List<String> processSearchResult(SearchResult result) {
    List<String> postResults = new ArrayList<>();
    List<SearchResult.Hit<Map, Void>> hits = result.getHits(Map.class);
    for (SearchResult.Hit<Map, Void> hit : hits) {
      Map<String, String> source = hit.source;
      postResults.add(source.get(Constants.TITLE));
    }
    return postResults;
  }

  @Scheduled(fixedDelay = 10000000)
  private void getUpdateOfPostFromThirdParty() {
    System.out.println("Scheduled at " + System.currentTimeMillis());

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(mediaUrl);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(new MediaType("application", "json"));
    HttpEntity<String> entity = new HttpEntity<String>(headers);

    try {
      ResponseEntity<List<MediaPostEntity>> rateResponse =
          restTemplate().exchange(builder.build().toUriString(), HttpMethod.GET, entity,
              new ParameterizedTypeReference<List<MediaPostEntity>>() {
              });
      List<MediaPostEntity> posts = rateResponse.getBody();

      for (MediaPostEntity post : posts) {
        save(post);
      }
    } catch (Exception e) {

    }
  }

  private void save(MediaPostEntity mediaPostEntity) throws ElasticSearchFailure, IOException {
    JestResult result = null;
    Index index = new Index.Builder(mediaPostEntity)
        .index(Constants.ES_INDEX)
        .type(Constants.ES_TYPE)
        .id(mediaPostEntity.getTitle())
        .build();

    result = client.execute(index);
    if (! result.isSucceeded()) {
      throw new ElasticSearchFailure();
    }
  }

  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate();
  }


  /*
      Unit Test Case Verification
   */
  public void addMediaPosts(MediaPostEntity mediaPostEntity) throws ElasticSearchFailure,IOException
  {
    save(mediaPostEntity);
  }



}

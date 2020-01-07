package com.brand.log.impl;

import com.brand.log.util.DateFormatV1;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class EsOprationImpl {

    @Autowired
    RestHighLevelClient getRHLClient;

    @Autowired
    DateFormatV1 dateFormat;

    //写入数据
    public void add(String tag, String msg, String key, String request_id){
        Date date = new Date();

        try
        {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            builder.field("@timestamp", dateFormat.getISO8601Timestamp(date, "GMT"));
            builder.field("tag", tag);
            builder.field("value", msg);
            builder.field("unique_id", key);
            builder.field("request_id", request_id);
            builder.endObject();
            IndexRequest request = new IndexRequest(key + "." + dateFormat.getDate("yyyy-MM-dd", "GMT")).source(builder);
            getRHLClient.index(request, RequestOptions.DEFAULT);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void search(){

        try{

            RestHighLevelClient client;

            client = new RestHighLevelClient(RestClient.builder(new HttpHost("172.18.12.214", 9201, "http")));

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            QueryBuilder query = QueryBuilders.termQuery("type", "user");

            QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("content", "地方");

            sourceBuilder.query(query).query(matchQueryBuilder);

            sourceBuilder.from(0);

            sourceBuilder.size(5);

            SearchRequest searchRequest = new SearchRequest();

            searchRequest.source(sourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            RestStatus status = searchResponse.status();

            SearchHits hits = searchResponse.getHits();

            TotalHits totalHits = hits.getTotalHits();

            long numHits = totalHits.value;

            SearchHit[] searchHits = hits.getHits();

            for (SearchHit hit : searchHits) {
                // String index = hit.getIndex();
                String id = hit.getId();

                log.info(id);

                float score = hit.getScore();

                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            }
        }catch(IOException e){
            e.printStackTrace();
        }

    }


}

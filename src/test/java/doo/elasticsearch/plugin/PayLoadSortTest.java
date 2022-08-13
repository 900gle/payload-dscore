package doo.elasticsearch.plugin;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.test.ESSingleNodeTestCase;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class PayLoadSortTest extends ESSingleNodeTestCase {

    @Test
    public void indexApi() {

        IndexRequest idxRequest =
                new IndexRequest()
                        .index("doo")
                        .id("bbongdoo")
                        .source(newSource());

        IndexResponse idxResponse = node().client().index(idxRequest).actionGet();
        assertEquals("doo", idxResponse.getIndex());
        assertEquals(RestStatus.CREATED, idxResponse.status());
        assertEquals("bbongdoo", idxResponse.getId());
        assertEquals(1L, idxResponse.getPrimaryTerm());
        assertEquals(0L, idxResponse.getSeqNo());
        assertEquals(1L, idxResponse.getVersion());

        ReplicationResponse.ShardInfo shardInfo = idxResponse.getShardInfo();
        assertEquals(0, shardInfo.getFailed());
        assertEquals(1, shardInfo.getSuccessful());
        assertEquals(1, shardInfo.getTotal());
    }

    private Map<String, String> newSource() {
        Map<String, String> source = new HashMap<>();
        source.put("firstName", "Doo");
        source.put("lastName", "LEE");
        return source;
    }


    @Test
    public void getApi() {
        GetResponse response =
                node()
                        .client()
                        .prepareGet()
                        .setIndex("doo")
                        .setId("bbongdoo")
                        .execute()
                        .actionGet();

        assertEquals("doo", response.getIndex());
        assertEquals("bbongdoo", response.getId());

        Map<String, Object> source = response.getSourceAsMap();
        assertEquals("Doo", source.get("firstName"));
        assertEquals("LEE", source.get("lastName"));
    }

    @Test
    public void searchApi() {
        SearchResponse response =
                node()
                        .client()
                        .prepareSearch("doo")
                        .setQuery(QueryBuilders.termQuery("lastName", "LEE"))
                        .get();

        SearchHits hits = response.getHits();
        assertEquals(2L, hits.getTotalHits().value);
        assertEquals("Doo", hits.getHits()[0].getId());
        assertEquals("LEE", hits.getHits()[1].getId());
    }

}

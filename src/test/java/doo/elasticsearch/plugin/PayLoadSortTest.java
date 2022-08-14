package doo.elasticsearch.plugin;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.ScriptSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.test.ESSingleNodeTestCase;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.elasticsearch.action.support.WriteRequest.RefreshPolicy.IMMEDIATE;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.*;
import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.hasId;

public class PayLoadSortTest extends ESSingleNodeTestCase {

    private void setup() throws IOException {
        try {
            client().admin().indices().prepareDelete("test").execute().actionGet();
        } catch (Exception e) {
        }

        client().admin().indices().prepareCreate("test")
                .addMapping("type1", jsonBuilder().startObject()
                        .startObject("type1")
                        .startObject("properties")
                        .startObject("field1")
                        .field("type", "text")
//                        .field("term_vector", "with_positions_payloads")
//                        .field("analyzer", "payload_delimiter")
                        .endObject()
                        .endObject()
                        .endObject()
                        .endObject())
                .setSettings(Settings.builder()
//                        .putList("index.analysis.analyzer.payload_delimiter.filter", "delimited_payload")
//                        .put("index.analysis.analyzer.payload_delimiter.encoding", "int")
//                        .put("index.analysis.analyzer.payload_delimiter.delimiter", "|")
//                        .put("index.analysis.analyzer.payload_delimiter.tokenizer", "standard")
                ).execute().actionGet();

        client().prepareIndex("test", "type1", "1").setSource("field1", "the|0").setRefreshPolicy(IMMEDIATE).execute().actionGet();
        client().prepareIndex("test", "type1", "2").setSource("field1", "the|0 test|0").setRefreshPolicy(IMMEDIATE).execute().actionGet();
        client().prepareIndex("test", "type1", "3").setSource("field1", "brown|0 fox|10 test|21 fox|100").setRefreshPolicy(IMMEDIATE).execute().actionGet();
        client().prepareIndex("test", "type1", "4").setSource("field1", "brown|10 fox|98").setRefreshPolicy(IMMEDIATE).execute().actionGet();
        client().prepareIndex("test", "type1", "5").setSource("field1", "brown|20 fox|99 test|23").setRefreshPolicy(IMMEDIATE).execute().actionGet();
        client().prepareIndex("test", "type1", "6").setSource("field1", "fox|100").setRefreshPolicy(IMMEDIATE).execute().actionGet();
        client().prepareIndex("test", "type1", "7").setSource("field1", "fox|100 fox|100 fox|0 test|0 fox|0").setRefreshPolicy(IMMEDIATE).execute().actionGet();
    }


    public void testPayloadAscSort() throws Exception {

        this.setup();

//        Map<String,Object> params = new HashMap<>();
//        params.put("field","field1");
//        params.put("value","brown");
//
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        boolQueryBuilder.must(QueryBuilders.termQuery("field1", "brown"));
//
//        final Script script = new Script(ScriptType.INLINE, "sort_script", "payload_sort", params);
//        SearchResponse searchResponse =
//                client().prepareSearch().setQuery(boolQueryBuilder)
//                        .addSort(SortBuilders.scriptSort(script, ScriptSortBuilder.ScriptSortType.NUMBER).order(SortOrder.ASC))
//                        .execute().actionGet();
//
//        assertSortValues(searchResponse,
//                new Object[] {
//                        0.0
//                },
//                new Object[] {
//                        10.0
//                },
//                new Object[] {
//                        20.0
//                });
//
//        assertFirstHit(searchResponse, hasId("3"));
    }

//    public void testPlayloadDescSort() throws Exception {
//
//        this.setup();
//
//        Map<String,Object> params = new HashMap<>();
//        params.put("field","field1");
//        params.put("value","fox");
//
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        boolQueryBuilder.must(QueryBuilders.termQuery("field1", "fox"));
//
//        final Script script = new Script(ScriptType.INLINE, "sort_script", "payload_sort", params);
//        SearchResponse searchResponse =
//                client().prepareSearch().setQuery(boolQueryBuilder).setSize(2)
//                        .addSort(SortBuilders.scriptSort(script, ScriptSortBuilder.ScriptSortType.NUMBER).order(SortOrder.DESC))
//                        .execute().actionGet();
//
//        assertSortValues(searchResponse,
//                new Object[] {
//                        200.0
//                },
//                new Object[] {
//                        110.0
//                });
//
//        assertFirstHit(searchResponse, hasId("7"));
//    }

//    @Override
//    protected Collection<Class<? extends Plugin>> getPlugins() {
//        List<Class<? extends Plugin>> classpathPlugins = new ArrayList<>();
//        classpathPlugins.add(DooPayloadScoreQueryPlugin.class);
//        classpathPlugins.add(CommonAnalysisPlugin.class);
//        return classpathPlugins;
//    }
}

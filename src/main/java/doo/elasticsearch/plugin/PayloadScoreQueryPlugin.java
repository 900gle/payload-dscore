package doo.elasticsearch.plugin;

import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.SearchPlugin;

import java.util.Collections;
import java.util.List;

public class PayloadScoreQueryPlugin extends Plugin implements SearchPlugin {
    @Override
    public List<QuerySpec<?>> getQueries() {
        return Collections.singletonList(
            new QuerySpec<>(PayloadScoreQueryBuilder.NAME, PayloadScoreQueryBuilder::new, PayloadScoreQueryBuilder::fromXContent)
        );
    }
}
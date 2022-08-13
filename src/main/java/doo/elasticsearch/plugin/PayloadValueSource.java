package doo.elasticsearch.plugin;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.DoubleValues;
import org.apache.lucene.search.DoubleValuesSource;
import org.apache.lucene.search.IndexSearcher;

import java.io.IOException;

public class PayloadValueSource extends DoubleValuesSource {

    private final String field;
    private final String value;
    public PayloadValueSource(String field, String value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public DoubleValues getValues(LeafReaderContext context, DoubleValues doubleValues) throws IOException {
        return doubleValues;
    }

    @Override
    public boolean needsScores() {
        return true;
    }

    @Override
    public DoubleValuesSource rewrite(IndexSearcher indexSearcher) throws IOException {
        return this;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public boolean isCacheable(LeafReaderContext leafReaderContext) {
        return false;
    }
}

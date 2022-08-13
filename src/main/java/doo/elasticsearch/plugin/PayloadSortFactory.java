package doo.elasticsearch.plugin;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.script.DocReader;
import org.elasticsearch.script.NumberSortScript;
import org.elasticsearch.search.lookup.SearchLookup;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

public class PayloadSortFactory implements NumberSortScript.LeafFactory {

    private final Map<String, Object> params;
    private final SearchLookup lookup;
    private final String field;
    private final String value;
    private final static String FIELD_NAME = "field";
    private final static String FIELD_VALUE = "value";
    private final PayloadValueSource payloadValueSource;

    public PayloadSortFactory(Map<String,Object> params, SearchLookup lookup) {
        if (params.containsKey(FIELD_NAME) == false) {
            throw new IllegalArgumentException(
                    "Missing parameter ["+FIELD_NAME+"]");
        }
        if (params.containsKey(FIELD_VALUE) == false) {
            throw new IllegalArgumentException(
                    "Missing parameter ["+FIELD_VALUE+"]");
        }
        this.params = params;
        this.lookup = lookup;
        field = params.get(FIELD_NAME).toString();
        value = params.get(FIELD_VALUE).toString();
        payloadValueSource = new PayloadValueSource(this.field, this.value);
    }

    @Override
    public NumberSortScript newInstance(DocReader docReader) throws IOException {
        return null;
    }


    //    @Override
//    public NumberSortScript newInstance(DocReader docReader) throws IOException {
//
//
//
//        PostingsEnum postings = context.reader().postings(new Term(field, value), PostingsEnum.ALL);
//
//        if (postings == null) {
//            return new NumberSortScript(params, lookup, context) {
//                @Override
//                public double execute() {
//                    return 0.0d;
//                }
//            };
//        }
//
//        return new NumberSortScript(params, lookup, context) {
//            int currentDocid = -1;
//            int beforDocid = -1;
//            Map<Integer, Double> map = new HashMap<>();
//
//            @Override
//            public void setDocument(int docid) {
//                if (postings.docID() < docid) {
//                    try {
//                        postings.advance(docid);
//                    } catch (IOException e) {
//                        throw new IllegalStateException("Can't advance to doc using " + e);
//                    }
//                }
//                currentDocid = docid;
//            }
//
//            @Override
//            public double execute() {
//                if(postings.docID() != currentDocid) {
//                    return 0.0d;
//                }
//                /***
//                 * 같은 docid가 nextDoc() 으로 부터 전달 받았을 경우에는 "postings.nextPosion()" 을 먼저 호출 하지 않는다. (이건 이유를 모르겠다, lucene 버그 인지 어떤것인지 가끔 docid가 nextDoc으로 부터 1,2,3,4,5,6,7,7,8... 처럼 다시 불려 지는 경우가 있다)
//                 */
//                beforDocid = currentDocid;
//                double sum_payload = 0.0f;
//                try {
//                    int freq  = postings.freq();
//                    if(freq > 0 && map.get(currentDocid) != null) {
//                        return map.get(currentDocid);
//                    }
//                    for(int i = 0; i < freq; i ++)
//                    {
//                        postings.nextPosition();
//                        BytesRef payload = postings.getPayload();
//                        if(payload != null) {
//                            sum_payload += ByteBuffer.wrap(payload.bytes, payload.offset, payload.length)
//                                    .order(ByteOrder.BIG_ENDIAN).getFloat();
//                        }
//                    }
//                    map.put(currentDocid,sum_payload);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return (map.get(currentDocid) == null) ? 0.0d : map.get(currentDocid);
//            }
//        };
//    }



    @Override
    public boolean needs_score() {
        return true;
    }
}

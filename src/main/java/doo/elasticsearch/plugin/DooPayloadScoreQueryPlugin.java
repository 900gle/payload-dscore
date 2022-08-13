package doo.elasticsearch.plugin;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.SearchPlugin;
import org.elasticsearch.script.NumberSortScript;
import org.elasticsearch.script.ScriptContext;
import org.elasticsearch.script.ScriptEngine;

import java.util.*;

public class DooPayloadScoreQueryPlugin extends Plugin implements SearchPlugin {
    @Override
    public List<QuerySpec<?>> getQueries() {
        return Collections.singletonList(
            new QuerySpec<>(DooPayloadScoreQueryBuilder.NAME, DooPayloadScoreQueryBuilder::new, DooPayloadScoreQueryBuilder::fromXContent)
        );
    }






    public ScriptEngine getScriptEngine(Settings settings, Collection<ScriptContext<?>> contexts) {
        return new SortScriptEngine();
    }

    private static class SortScriptEngine implements ScriptEngine {
        private final String _PAYLOAD_SORT_SOURCE_VALUE = "payload_sort";

        @Override
        public String getType() {
            return "sort_script";
        }

        @Override
        public <T> T compile(String scriptName, String scriptSource, ScriptContext<T> context, Map<String, String> params) {

            if (!context.equals(NumberSortScript.CONTEXT)) {
                throw new IllegalArgumentException(getType()
                        + " scripts cannot be used for context ["
                        + context.name + "]");
            }

            if(_PAYLOAD_SORT_SOURCE_VALUE.equals(scriptSource)) {
                NumberSortScript.Factory factory = PayloadSortFactory::new;
                return context.factoryClazz.cast(factory);
            }

            throw new IllegalArgumentException("Unknown script name " + scriptSource);
        }

        @Override
        public void close() {
        }

        @Override
        public Set<ScriptContext<?>> getSupportedContexts() {
            return Collections.singleton(NumberSortScript.CONTEXT);
        }
    }

}
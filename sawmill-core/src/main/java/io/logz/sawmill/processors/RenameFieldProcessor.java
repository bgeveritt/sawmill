package io.logz.sawmill.processors;

import io.logz.sawmill.Doc;
import io.logz.sawmill.ProcessResult;
import io.logz.sawmill.Processor;
import io.logz.sawmill.annotations.ProcessorProvider;
import io.logz.sawmill.utilities.JsonUtils;

@ProcessorProvider(type = "rename", factory = RenameFieldProcessor.Factory.class)
public class RenameFieldProcessor implements Processor {
    private final String from;
    private final String to;

    public RenameFieldProcessor(String from, String to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public ProcessResult process(Doc doc) {
        if (!doc.hasField(from)) {
            return ProcessResult.failure(String.format("failed to rename field [%s] to [%s]", from, to));
        }
        Object fieldValue = doc.getField(from);
        doc.removeField(from);
        doc.addField(to, fieldValue);

        return ProcessResult.success();
    }

    public static class Factory implements Processor.Factory {
        public Factory() {
        }

        @Override
        public Processor create(String config) {
            RenameFieldProcessor.Configuration renameFieldConfig = JsonUtils.fromJsonString(RenameFieldProcessor.Configuration.class, config);

            return new RenameFieldProcessor(renameFieldConfig.getFrom(), renameFieldConfig.getTo());
        }
    }

    public static class Configuration implements Processor.Configuration {
        private String from;
        private String to;

        public Configuration() { }

        public Configuration(String from, String to) {
            this.from = from;
            this.to = to;
        }

        public String getFrom() { return from; }

        public String getTo() { return to; }
    }
}

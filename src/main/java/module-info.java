module org.slbtty.yapyz {

    requires java.desktop;

    requires org.apache.lucene.core;
    requires org.apache.lucene.queryparser;
    requires org.tinylog.api;

    requires kernal;
    requires io;
    requires commons;

    requires com.formdev.flatlaf;

    requires jdk.unsupported;

    exports org.slbtty.yapyz;
}
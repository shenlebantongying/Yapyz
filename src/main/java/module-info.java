module org.slbtty.yapyz {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.lucene.core;
    requires org.apache.lucene.demo;
    requires org.apache.lucene.queryparser;
    requires org.tinylog.api;

    // implementation 'com.itextpdf:kernel:7.2.3'
    // itext7 didn't give a proper name for module
    // it is called "automatic module" btw
    requires kernel;

    opens org.slbtty.yapyz to javafx.fxml;
    exports org.slbtty.yapyz;
}
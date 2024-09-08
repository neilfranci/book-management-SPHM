module com.bgsix {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens com.bgsix to javafx.fxml;

    exports com.bgsix;
}

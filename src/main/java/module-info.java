module org.bankapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires spring.jdbc;
    requires com.microsoft.sqlserver.jdbc;
    requires java.sql;
    exports org.bankapplication;
    opens org.bankapplication.models to javafx.base;
    exports org.bankapplication.controllers to javafx.fxml;
    opens org.bankapplication.controllers to javafx.fxml;
}
package lk.ijse.dep10;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import lk.ijse.dep10.db.DBConnector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class AppInitializer extends Application {

    public static void main(String[] args) {


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("your connection is going to close");
                if (DBConnector.getInstance().getConnection() != null &&

                        !DBConnector.getInstance().getConnection().isClosed()) {
                    DBConnector.getInstance().getConnection().close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        generateSchemaIfNotExists();
    }

    public void generateSchemaIfNotExists() {
        try {
            Connection connection = DBConnector.getInstance().getConnection();
            HashSet<String> tblList = new HashSet<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("show tables");
            while (resultSet.next()) {
                tblList.add(resultSet.getString(1));
            }
            System.out.println(tblList);
            Boolean exist = tblList.containsAll(Set.of("Attendence", "User", "Picture", "Student"));

            if (!exist) {

                InputStream resourceAsStream = getClass().getResourceAsStream("/schema.sql");

                BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");

                }
                br.close();
                Statement statementTable = connection.createStatement();
                statementTable.execute(sb.toString());
            }


        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error in schema.sql").showAndWait();
            throw new RuntimeException(e);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Problem with mysql").showAndWait();
            throw new RuntimeException(e);
        }
    }
}

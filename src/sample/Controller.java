package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.UUID;
import java.lang.Math;

import static jdk.internal.org.objectweb.asm.TypeReference.CAST;

public class Controller implements Initializable {
    final String hostname = "employeedb.cnfxx7covndy.us-east-1.rds.amazonaws.com";
    final String dbName = "employeedb";
    final String port = "3306";
    final String username = "admin";
    final String password = "Powerkill11";
    final String AWS_URL = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + username + "&password=" + password;
    @FXML
    Button LoadButt;
    @FXML
    Button RunButt;
    @FXML
    TextField Mintext;
    @FXML
    TextField Maxtext;
    @FXML
    private Label Num1;
    @FXML
    ListView TableViewNums;

    private void runNum(int min, int max, String url) {
        double randomDouble = Math.random();
        randomDouble = randomDouble * max + min;
        int Num = (int) randomDouble;
        Num1.setText(String.valueOf(Num));

        try {

            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            try {
                stmt.execute("CREATE TABLE Num2DB (" +
                        "Num VARCHAR(36) )");

                System.out.println("TABLE CREATED");
            } catch (Exception ex) {
                System.out.println("TABLE ALREADY EXISTS, NOT CREATED");
            }
            String potato = String.valueOf(Num);
            String sql = "INSERT INTO Num2DB VALUES" +
                    "('" + potato + "')";
            System.out.println("L");
            stmt.executeUpdate(sql);
            System.out.println("Table fill");
            stmt.close();
            conn.close();
        } catch (Exception ex) {
            String msg = ex.getMessage();
            System.out.println(msg);
        }
        Mintext.clear();
        Maxtext.clear();
    }
    private void runLoad(String URL) {
        try {
            Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement();
            String sqlStatement = "SELECT Num FROM Num2DB";
            ResultSet result = stmt.executeQuery(sqlStatement);
            ObservableList<NumDB> dbEmployeeList = FXCollections.observableArrayList();
            while (result.next()) {
                NumDB Rand = new NumDB();
                Rand.potato = result.getString("Num");
                dbEmployeeList.add(Rand);
            }
                TableViewNums.setItems(dbEmployeeList);

            System.out.println("DATA LOADED");
            stmt.close();
            conn.close();
        }
        catch (Exception ex)
        {
            String msg = ex.getMessage();
            System.out.println("DATA NOT LOADED");
            System.out.println(msg);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        RunButt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                runNum(Integer.parseInt(Mintext.getText()), Integer.parseInt(Maxtext.getText()), AWS_URL);
            }
        });
        LoadButt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {runLoad(AWS_URL);
            }
        });
    }
}

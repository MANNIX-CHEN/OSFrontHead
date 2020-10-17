package sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import javafx.collections.ObservableList;

public class Controller {

    @FXML
    private Button mButton;
    @FXML
    private Label mLabel;
    @FXML
    private ComboBox comboBox_API;

    Integer ClickCount=0;


    private ObservableList<String> apiList=FXCollections.observableArrayList();
    public void Init(){

    }


}
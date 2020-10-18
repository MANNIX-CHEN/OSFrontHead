package sample;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class Controller {

    @FXML
    private Button onButton;
    @FXML
    private TableView<String> openFilesTable;

    ReadDiskFile rd;


    private ObservableList<String> openFilesList=FXCollections.observableArrayList();
    public void Init(){
        rd =  new ReadDiskFile();
        initTable();//初始化表格
    }

    private void initTable() {
        for (String[] curFiles:
             rd.openFilesInfo) {
            for (String curInfo:
                 curFiles) {
                openFilesList.add(curInfo);
            }
        }
        openFilesTable.setItems(openFilesList);
        System.out.println(openFilesList);
    }

    @FXML//事件监听器
    public void onButtonClick(ActionEvent event){
        System.out.println(77777);
    }

}
package sample;

import com.sun.prism.Image;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import javax.swing.text.html.ImageView;

public class Controller {

    /*以下成员分别定义对应的主页面组件*/
    @FXML
    private AnchorPane filePane;
    @FXML
    private Button parentCat;
    @FXML
    private TableView<DataOfTable> openFilesTable;
    @FXML
    private TreeView<String> filesCatalogue;
    @FXML
    private GridPane diskStatus;
    @FXML
    private TableView fatTable;
    @FXML
    private PieChart pieChart;
    @FXML
    private ImageView test;

    ReadDiskFile rd;//读取diskfile的文件信息


    /*以下为直接渲染到程序的成员*/
    private ObservableList<DataOfTable> openFilesData = FXCollections.observableArrayList();
    private ObservableList<DataOfFat> fatData = FXCollections.observableArrayList();
    //已经打开文件表格data（需要与对应的的table区别开）
    TreeItem<String> root = new TreeItem<>("爹目录");

    public void Init(){
        rd = new ReadDiskFile();
        initFileGUI();//初始化文件管理主界面
        initcatalogue();//初始化目录结构
        intDiskPane();//初始化磁盘分配情况页面
        initTable();//初始化表格
    }

    private void initFileGUI() {
        parentCat.getStyleClass().add("parentCat");
    }

    private void intDiskPane() {
        double testwidth;
        /*以下内容为初始化fat表*/
        ObservableList<TableColumn<DataOfFat, ?>> observableList = fatTable.getColumns();

        for (int i = 0 ; i < 4 ; i++){
            TableColumn curBlockCol = observableList.get(2*i);
            TableColumn curValCol = observableList.get(2*i+1);

            /*块列*/
            curBlockCol.setText("块");
            curBlockCol.setCellValueFactory(new PropertyValueFactory("block"+i));
            curBlockCol.getStyleClass().add("blockCol");

            /*值列*/
            curValCol.setText("值");
            curValCol.setCellValueFactory(new PropertyValueFactory("val"+i));
        }//初始化fatTable的需要用到的东西


        for (int i = 0 ; i < 32 ; i++ ){
            fatData.add(new DataOfFat(
                    new int[]{4*i,4*i+1,4*i+2,4*i+3},
                    new int[]{-1,-1,-1,-1}));
        }
        fatTable.setItems(fatData);



        /*以下内容为初始化饼图*/
        PieChart.Data used = new PieChart.Data("used",2);
        PieChart.Data unused = new PieChart.Data("unused",128-used.getPieValue());
        pieChart.getData().add(used);
        pieChart.getData().add(unused);
    }

    private void initcatalogue() {
        filesCatalogue.setRoot(root);
        root.getChildren().add(new TreeItem<>("son1"));
        root.getChildren().add(new TreeItem<>("son2"));
    }

    private void initTable() {

        ObservableList<TableColumn<DataOfTable, ?>> observableList = openFilesTable.getColumns();
        observableList.get(0).setCellValueFactory(new PropertyValueFactory("fileName"));
        observableList.get(1).setCellValueFactory(new PropertyValueFactory("filePath"));
        observableList.get(2).setCellValueFactory(new PropertyValueFactory("firstBlock"));
        observableList.get(3).setCellValueFactory(new PropertyValueFactory("fileLength"));
        observableList.get(4).setCellValueFactory(new PropertyValueFactory("operateType"));
        //设定需要添加的信息是什么

        for (String[] curFileInfo:
             rd.openFilesInfo) {
            openFilesData.add(new DataOfTable(curFileInfo));
        }
        //将需要放在表格的信息放在data里头

        openFilesTable.setItems(openFilesData);
        //setItem将data的数据放到表格中

    }

    @FXML//事件监听器
    public void onButtonClick(ActionEvent event){
        System.out.println(77777);
    }


    public void panePress(MouseEvent mouseEvent) {
        System.out.println(5555);
    }
}
package sample;

import com.sun.prism.Image;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.Optional;

import javax.swing.text.html.ImageView;

public class Controller {

    /*���³�Ա�ֱ����Ӧ����ҳ�����*/
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
    @FXML
    private Label fileLabel;
    //@FXML
    private ContextMenu fileContextMenu;
    private ContextMenu fileContextMenu2;
    private MenuItem newFile,newFolder,openFile,delFile,fileData,renameFile;
    @FXML
    private FlowPane fileFlowPane;
    
    ReadDiskFile rd;//��ȡdiskfile���ļ���Ϣ


    /*����Ϊֱ����Ⱦ������ĳ�Ա*/
    private ObservableList<DataOfTable> openFilesData = FXCollections.observableArrayList();
    private ObservableList<DataOfFat> fatData = FXCollections.observableArrayList();
    //�Ѿ����ļ����data����Ҫ���Ӧ�ĵ�table���𿪣�
    TreeItem<String> root = new TreeItem<>("��Ŀ¼");
    

    public void Init(){
        rd = new ReadDiskFile();
        initContextMenu();//��ʼ���Ҽ������˵�
        setOnActionMenuItem();//�����˵�����ʵ��
        initFileGUI();//��ʼ���ļ�����������
        initcatalogue();//��ʼ��Ŀ¼�ṹ
        intDiskPane();//��ʼ�����̷������ҳ��
        initTable();//��ʼ�����
    }
    
    public void initContextMenu() {
    	fileContextMenu=new ContextMenu();
    	newFile = new MenuItem("�½��ļ�");
    	newFolder = new MenuItem("�½��ļ���");
    	fileContextMenu.getItems().addAll(newFile,newFolder);
    	fileFlowPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
			if (event.getButton() == MouseButton.SECONDARY && !fileContextMenu2.isShowing()) {
				fileContextMenu.show(fileFlowPane, event.getScreenX(), event.getScreenY());
			} else {
				fileContextMenu.hide();
			}
		});
    	
    	fileContextMenu2=new ContextMenu();
    	openFile = new MenuItem("��");
    	delFile = new MenuItem("ɾ��");
    	fileData = new MenuItem("����");
    	renameFile = new MenuItem("������");
    	fileContextMenu2.getItems().addAll(openFile,delFile,fileData,renameFile);
    	parentCat.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
			if (event.getButton() == MouseButton.SECONDARY && !fileContextMenu.isShowing()) {
				fileContextMenu2.show(parentCat, event.getScreenX(), event.getScreenY());
			} else {
				fileContextMenu2.hide();
			}
		});
    }
    
    /*�����˵�����ʵ��*/
    public void setOnActionMenuItem() {
    	newFile.setOnAction(ActionEvent -> {
			//System.out.println("�½��ļ�");
    		TextInputDialog dialog=new TextInputDialog();
    		dialog.setTitle("�½��ļ�");
    		dialog.setHeaderText("�½��ļ�");
    		dialog.setContentText("�������ļ���:");
    		Optional<String> result = dialog.showAndWait();
    		if (result.isPresent()){
    		    System.out.println("�ļ���Ϊ: " + result.get());
    		}
		});
    	
    	newFolder.setOnAction(ActionEvent -> {
			//System.out.println("�½��ļ���");
    		TextInputDialog dialog=new TextInputDialog();
    		dialog.setTitle("�½��ļ���");
    		dialog.setHeaderText("�½��ļ���");
    		dialog.setContentText("�������ļ�����:");
    		Optional<String> result = dialog.showAndWait();
    		if (result.isPresent()){
    		    System.out.println("�ļ�����Ϊ: " + result.get());
    		}
		});
    }

    private void initFileGUI() {
        parentCat.getStyleClass().add("parentCat");
    }

    private void intDiskPane() {
        double testwidth;
        /*��������Ϊ��ʼ��fat��*/
        ObservableList<TableColumn<DataOfFat, ?>> observableList = fatTable.getColumns();

        for (int i = 0 ; i < 4 ; i++){
            TableColumn curBlockCol = observableList.get(2*i);
            TableColumn curValCol = observableList.get(2*i+1);

            /*����*/
            curBlockCol.setText("��");
            curBlockCol.setCellValueFactory(new PropertyValueFactory("block"+i));
            curBlockCol.getStyleClass().add("blockCol");

            /*ֵ��*/
            curValCol.setText("ֵ");
            curValCol.setCellValueFactory(new PropertyValueFactory("val"+i));
        }//��ʼ��fatTable����Ҫ�õ��Ķ���


        for (int i = 0 ; i < 32 ; i++ ){
            fatData.add(new DataOfFat(
                    new int[]{4*i,4*i+1,4*i+2,4*i+3},
                    new int[]{-1,-1,-1,-1}));
        }
        fatTable.setItems(fatData);



        /*��������Ϊ��ʼ����ͼ*/
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
        //�趨��Ҫ��ӵ���Ϣ��ʲô

        for (String[] curFileInfo:
             rd.openFilesInfo) {
            openFilesData.add(new DataOfTable(curFileInfo));
        }
        //����Ҫ���ڱ�����Ϣ����data��ͷ

        openFilesTable.setItems(openFilesData);
        //setItem��data�����ݷŵ������

    }

    @FXML//�¼�������
    public void onButtonClick(ActionEvent event){
        System.out.println(77777);
    }

    @FXML
    public void panePress(MouseEvent mouseEvent) {
    	System.out.println(66666);
    }
    
    /*@FXML
    public void paneSecondary(MouseEvent mouseEvent) {
        if(mouseEvent.getButton()==MouseButton.SECONDARY) {
        	System.out.println("�Ҽ�����հ׵ط�");
        }
    }*/
    
    @FXML
    public void onButtonClickTwice(MouseEvent event) {
    	if(event.getClickCount()==2) {
    		System.out.println("˫�������ļ����У�");
    	}
    }
    
    @FXML 
    public void onButtonSecondary(MouseEvent event) {
    	if(event.getButton()==MouseButton.SECONDARY) {
    		System.out.println("�Ҽ�����ļ����У�");
    	}
    }
    
}
package sample;

import FrontHead.content.CatEntry;
import FrontHead.content.Catalogue;
import FrontHead.content.File;
import FrontHead.content.component.FileCom;
import FrontHead.content.component.FilePaneCom;
import FrontHead.content.component.CatCom;
import FrontHead.tableData.DataOfFat;
import FrontHead.tableData.DataOfTable;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.awt.*;
import java.util.Optional;
import java.util.Vector;

import javax.swing.text.html.ImageView;
import de.jensd.fx.glyphs.fontawesome.*;
import javafx.scene.text.Font;

public class Controller {

    /*���³�Ա�ֱ����Ӧ��FXML�е����*/
    @FXML
    private Button parentCat;
    @FXML
    private TableView<DataOfTable> openFilesTable;
    @FXML
    private TreeView<String> filesCatView;
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
    private FlowPane filePane;
    @FXML
    private TextField curAbsPath;
    
    ReadDiskFile rd;//��ȡdiskfile���ļ���Ϣ


    /*����Ϊ��Ⱦ������Ҫʹ�õ�����س�Ա*/
    private Catalogue rootCat ;
    private Catalogue curCat ;//��ǰĿ¼
    FilePaneCom[] curFPaneComs;


    /*����Ϊ�Ѿ����ļ����data����Ҫ���Ӧ�ĵ�table���𿪣�*/
    private ObservableList<DataOfTable> openFilesData = FXCollections.observableArrayList();
    private ObservableList<DataOfFat> fatData = FXCollections.observableArrayList();

    //TreeItem<String> root = new TreeItem<>("C:");


    public void setCurCat(Catalogue curCat) {
        this.curCat = curCat;
    }

    public void Init(){
        rd = new ReadDiskFile();
        initContextMenu();//��ʼ���Ҽ������˵�
        setOnActionMenuItem();//�����˵�����ʵ��
        initcatalogue();//��ʼ��Ŀ¼�ṹ
        //updateFilePane();//ˢ�� FilePane
        intDiskPane();//��ʼ�����̷������ҳ��
        initTable();//��ʼ�����
    }
    
    public void initContextMenu() {
    	fileContextMenu=new ContextMenu();
    	newFile = new MenuItem("�½��ļ�");
    	newFolder = new MenuItem("�½��ļ���");
    	fileContextMenu.getItems().addAll(newFile,newFolder);
    	filePane.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {

			if (event.getButton() == MouseButton.SECONDARY
                    && !fileContextMenu2.isShowing()) {
				fileContextMenu.show(filePane, event.getScreenX(), event.getScreenY());
			} else {
				fileContextMenu.hide();
			}
		});

    	/*�ļ��Ҽ��˵�*/
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
    		    curCat.addFileEntry(result.get());
    		    updateFilePane();
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
    		    curCat.addCatEntry(result.get());
    		    updateFilePane();//���Ŀ¼����½���
    		}
		});
    }

    public void updateFilePane() {
        /*����filePane����clear���������Ȼ�����curCat������
        * */
        //System.out.println(curCat.getEntries()[0]);
        curAbsPath.setText(curCat.getAbsPath());
        filePane.getChildren().clear();//����������
        Vector<CatEntry> curCatEntries  = curCat.getEntries();

        /*����forѭ�������� ���ļ��У�Ȼ�����ļ�*/
        for (CatEntry cat :
             curCatEntries) {
            if (cat.toString().matches("(.*)Catalogue(.*)")){
                //������ʽƥ�� catalogue
                CatCom addCom = new CatCom(cat.getName(), this, (Catalogue) cat);
                filePane.getChildren().add(addCom);
            }
        }
        for (CatEntry file :
                curCatEntries) {
            if (file.toString().matches("(.*)File(.*)")){
                //������ʽƥ�� File
                FileCom addCom = new FileCom(file.getName(), (File) file,this);
                filePane.getChildren().add(addCom);
            }
        }
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
        rootCat = new Catalogue("C:");
        curCat = rootCat;
        filesCatView.setRoot(rootCat.getFxTreeItem());
        filesCatView.getSelectionModel().selectedIndexProperty().addListener(
                (observableValue, number, t1) -> {
                    //System.out.println("info is " + observableValue +" @@@ " + number+" @@@ " +t1);
                }
        );

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
    }
    
/*    @FXML
    public void paneSecondary(MouseEvent mouseEvent) {
        if(mouseEvent.getButton()==MouseButton.SECONDARY) {
        	System.out.println("�Ҽ�����հ׵ط�");
        	updateFilePane();
        }
    }*/
    
    @FXML
    public void onButtonClickTwice(MouseEvent event) {
    	if(event.getClickCount() == 2) {
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
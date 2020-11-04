package sample;

import BackGround.Server;
import FrontHead.content.CatEntry;
import FrontHead.content.Catalogue;
import FrontHead.content.VirtualFile;
import FrontHead.content.component.FileCom;
import FrontHead.content.component.FilePaneCom;
import FrontHead.content.component.CatCom;
import FrontHead.content.component.ParentCatCom;
import FrontHead.tableData.DataOfFat;
import FrontHead.tableData.DataOfOpenFiles;
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

import java.io.IOException;
import java.util.Optional;
import java.util.Vector;

import javax.swing.text.html.ImageView;

public class Controller {

    /*以下成员分别定义对应的FXML中的组件*/
    @FXML
    private Button parentCat;
    @FXML
    private TableView<DataOfOpenFiles> openFilesTable;
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
    
    ReadDiskFile rd;//读取diskfile的文件信息
    Server server;


    /*以下为渲染程序需要使用到的相关成员*/
    private Catalogue rootCat ;
    private Catalogue curCat ;//当前目录
    FilePaneCom[] curFPaneComs;
    Vector<VirtualFile> curOpenFiles;
    int[] fat;


    /*以下为已经打开文件表格data（需要与对应的的table区别开）*/
    private ObservableList<DataOfOpenFiles> openFilesData = FXCollections.observableArrayList();
    private ObservableList<DataOfFat> fatData = FXCollections.observableArrayList();

    //TreeItem<String> root = new TreeItem<>("C:");


    /*getter&setter*/

    public Server getServer() {
        return server;
    }

    public Catalogue getCurCat() {
        return curCat;
    }

    public void setCurCat(Catalogue curCat) {
        this.curCat = curCat;
    }

    public Vector<VirtualFile> getCurOpenFiles() {
        return curOpenFiles;
    }

    public void Init() throws IOException {
        server = new Server(this);

        initContextMenu();//初始化右键下拉菜单
        setOnActionMenuItem();//下拉菜单功能实现
        initcatalogue();//初始化目录结构
        initOpenFilesTable();//初始化表格,初始化curOpenFiles
        updateFilePane();//初始刷新 FilePane
        intDiskPane();//初始化磁盘分配情况页面

    }
    
    public void initContextMenu() {
    	fileContextMenu=new ContextMenu();
    	newFile = new MenuItem("新建文件");
    	newFolder = new MenuItem("新建文件夹");
    	fileContextMenu.getItems().addAll(newFile,newFolder);
    	filePane.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {

			if (event.getButton() == MouseButton.SECONDARY
                    && !fileContextMenu2.isShowing()) {
				fileContextMenu.show(filePane, event.getScreenX(), event.getScreenY());
			} else {
				fileContextMenu.hide();
			}
		});

    	/*文件右键菜单*/
    	fileContextMenu2=new ContextMenu();
    	openFile = new MenuItem("打开");
    	delFile = new MenuItem("删除");
    	fileData = new MenuItem("属性");
    	renameFile = new MenuItem("重命名");
    	fileContextMenu2.getItems().addAll(openFile,delFile,fileData,renameFile);
    	parentCat.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
			if (event.getButton() == MouseButton.SECONDARY && !fileContextMenu.isShowing()) {
				fileContextMenu2.show(parentCat, event.getScreenX(), event.getScreenY());
			} else {
				fileContextMenu2.hide();
			}
		});
    }
    
    /*下拉菜单功能实现*/
    public void setOnActionMenuItem() {
    	newFile.setOnAction(ActionEvent -> {
			//System.out.println("新建文件");
    		TextInputDialog dialog=new TextInputDialog();
    		dialog.setTitle("新建文件");
    		dialog.setHeaderText("新建文件");
    		dialog.setContentText("请输入文件名:");
    		Optional<String> result = dialog.showAndWait();
    		if (result.isPresent()){
                try {
                    curCat.addFileEntry(new VirtualFile(result.get(),curCat,curCat.getAbsPath(),curCat.getServer()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                updateFilePane();
    		}
		});
    	
    	newFolder.setOnAction(ActionEvent -> {
			//System.out.println("新建文件夹");
    		TextInputDialog dialog=new TextInputDialog();
    		dialog.setTitle("新建文件夹");
    		dialog.setHeaderText("新建文件夹");
    		dialog.setContentText("请输入文件夹名:");
    		Optional<String> result = dialog.showAndWait();
    		if (result.isPresent()){ //新建文件夹事件
                try {
                    curCat.addCatEntry(new Catalogue(result.get(),curCat));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                updateFilePane();//添加目录后更新界面
    		}
		});
    }


    public void updateFilePane() {
        /*更新filePane，先clear所有组件，然后根据curCat添加组件
        * */
        curAbsPath.setText(curCat.getAbsPath());
        filePane.getChildren().clear();//清空所有组件

        if ((curCat != rootCat)) {
            filePane.getChildren().add(new ParentCatCom("...", this));
        }


        Vector<CatEntry> curCatEntries  = curCat.getEntries();
        /*两个for循环添加组件 先文件夹，然后再文件*/
        for (CatEntry cat :
             curCatEntries) {
            if (cat.toString().matches("(.*)Catalogue(.*)")){
                //正则表达式匹配 catalogue
                CatCom addCom = new CatCom(cat.getName(), this, (Catalogue) cat);
                filePane.getChildren().add(addCom);
            }
        }
        for (CatEntry file :
                curCatEntries) {
            if (file.toString().matches("(.*)File(.*)")){
                //正则表达式匹配 File
                FileCom addCom = new FileCom(file.getName(), (VirtualFile) file,this);
                filePane.getChildren().add(addCom);
            }
        }
        updateOpenFilesTable();
    }



    public void updateFilePane(Catalogue nextCurCat){

        setCurCat(nextCurCat);
        curCat.updateTreeView();
        updateFilePane();
    }
    public void closeFile(VirtualFile file){
        getCurOpenFiles().remove(file);
        updateOpenFilesTable();
    }
    public void openFile(VirtualFile file){
        getCurOpenFiles().add(file);
        updateOpenFilesTable();
    }

    private void updateOpenFilesTable() {
        openFilesData.clear();
        for (VirtualFile addFile:
             curOpenFiles) {
            openFilesData.add(new DataOfOpenFiles(addFile));
        }
        openFilesTable.setItems(openFilesData);
    }
    private void intDiskPane() {
        double testwidth;
        fat = new int[128];
        fat[0] = -1; fat[1] = -1;

        /*以下内容初始化fat表*/
        ObservableList<TableColumn<DataOfFat, ?>> observableList = fatTable.getColumns();

        for (int i = 0 ; i < 4 ; i++){
            TableColumn curBlockCol = observableList.get(2*i);
            TableColumn curValCol = observableList.get(2*i+1);

            /*块列*/
            curBlockCol.setText("块");
            curBlockCol.setCellValueFactory(new PropertyValueFactory("block"+i));
            curBlockCol.getStyleClass().add("blockCol");//加上左边界，加强观感

            /*值列*/
            curValCol.setText("值");
            curValCol.setCellValueFactory(new PropertyValueFactory("val"+i));
        }//初始化fatTable的需要用到的东西

        /*以下内容为初始化饼图*/
        PieChart.Data used = new PieChart.Data("used",12);
        PieChart.Data unused = new PieChart.Data("unused",128-used.getPieValue());
        pieChart.getData().add(used);
        pieChart.getData().add(unused);

        updateFat();
    }
    public void updateFat(){

        /*以下为刷新fat表*/
        for (int i = 0 ; i < 32 ; i++ ){
            fatData.add(new DataOfFat(
                    new int[]{4*i,4*i+1,4*i+2,4*i+3},
                    new int[]{fat[4*i] , fat[4*i+1],
                            fat[4*i+2] , fat[4*i+3] }));
        }
        int numOfused = 0;
        for (int curVal :
             fat) {
            if (curVal == -1)
                numOfused++;
        }
        fatTable.setItems(fatData);

        /*以下内容为刷新pie图*/
        PieChart.Data used = new PieChart.Data("used",numOfused);
        pieChart.getData().set(0,used);
        pieChart.getData().set(1,new PieChart.Data("unused",128-used.getPieValue()));

    }

    private void initcatalogue() throws IOException {
        rootCat = new Catalogue("C:" , getServer());
        curCat = rootCat;
        filesCatView.setRoot(rootCat.getFxTreeItem());
        filesCatView.getSelectionModel().selectedIndexProperty().addListener(
                (observableValue, number, t1) -> {
                    //System.out.println("info is " + observableValue +" @@@ " + number+" @@@ " +t1);
                }
        );

    }

    private void initOpenFilesTable() {
        curOpenFiles = new Vector<VirtualFile>(5);

        ObservableList<TableColumn<DataOfOpenFiles, ?>> observableList = openFilesTable.getColumns();
        observableList.get(0).setCellValueFactory(new PropertyValueFactory("fileName"));
        observableList.get(1).setCellValueFactory(new PropertyValueFactory("filePath"));
        observableList.get(2).setCellValueFactory(new PropertyValueFactory("firstBlock"));
        observableList.get(3).setCellValueFactory(new PropertyValueFactory("fileLength"));
        observableList.get(4).setCellValueFactory(new PropertyValueFactory("operateType"));
        //设定需要添加的信息是什么


//        for (String[] curFileInfo:
//             rd.openFilesInfo) {
//            openFilesData.add(new DataOfOpenFiles(curFileInfo));
//        }
//        //将需要放在表格的信息放在data里头
//
//        openFilesTable.setItems(openFilesData);
//        //setItem将data的数据放到表格中

    }

    @FXML//事件监听器
    public void onButtonClick(ActionEvent event){
        System.out.println(77777);
    }

    @FXML
    public void panePress(MouseEvent mouseEvent) {
    }
    
/*    @FXML
    public void paneSecondary(MouseEvent mouseEvent) {
        if(mouseEvent.getButton()==MouseButton.SECONDARY) {
        	System.out.println("右键点击空白地方");
        	updateFilePane();
        }
    }*/
    
    @FXML
    public void onButtonClickTwice(MouseEvent event) {
    	if(event.getClickCount() == 2) {
    		System.out.println("双击进入文件（夹）");
    	}
    }
    
    @FXML 
    public void onButtonSecondary(MouseEvent event) {
    	if(event.getButton()==MouseButton.SECONDARY) {
    		System.out.println("右键点击文件（夹）");
    	}
    }

    public void formattingDisk(ActionEvent event) throws IOException {
        /* 格式化磁盘，用于测试
        *  可以在菜单栏 -> Help -> format中直接调用
        * */
        server.formatting();
        System.out.println("format!!!!");
    }
    public void showMeFat(ActionEvent event) throws IOException {
        server.showMeFat();
    }
    public void findNextBlock(ActionEvent event) throws IOException {
        System.out.println("next block is " + server.findNextFreeBlock());
    }
}
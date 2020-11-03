package BackGround;

import FrontHead.content.Catalogue;
import FrontHead.content.VirtualFile;
import sample.Controller;

import java.io.*;
import java.util.Vector;

public class Server {
    Controller controller;
    private File diskFile;
    private int[] fatTable;
    private String bufferIn;  		//读缓冲
    private String bufferOut;  		//写缓冲

    private Vector<byte[]> curCatalogue;
    //private String[] curCatalogue;

    private final static int ONE_BLOCK_LENGTH = 64;
    public final static int SAVED_SUCESS = 0;
    public final static int ADD_SUCESS = 0;
    public final static int CAT_IS_FULL = 1;
    public final static int NO_MORE_DISK_SPACE = 2;
    public final static int FILE_EXISTED = 3;


    public Server () throws IOException {
        init();
    }
    public Server(Controller controller) throws IOException {
        setController(controller);
        init();
        //readFile();
    }
    public void setController(Controller controller) {
        this.controller = controller;
    }
    private void init() throws IOException {
        fatTable = new int[128];
        curCatalogue = new Vector<>(8);
        int ftLength = fatTable.length;
        this.diskFile = new File("src/BackGround/diskFile");
        if(diskFile.exists()) {
            InputStream inFat = new FileInputStream(diskFile);
            byte[] inBytes = new byte[ftLength];
            inFat.read(inBytes);
            for(int i=0; i<ftLength; i++) {
                fatTable[i] = (int)inBytes[i];
            }

            for(int i=0; i<8; i++) {
                curCatalogue.add(new byte[8]);
                inFat.read(curCatalogue.get(i),0,8);
            }
            inFat.close();
        }else {
            fatTable[0] = -1;
            fatTable[1] = -1;
            fatTable[2] = -1;//第三个盘块放根目录
            //System.out.println("fl is"+fatTable.length);
            for(int i=3; i<ftLength; i++) {
                fatTable[i] = 0;
            }
            diskFile.createNewFile();
            OutputStream outFat = new FileOutputStream(diskFile);

            for(int i=0; i<ftLength; i++) {
                int temp = fatTable[i];
                outFat.write(temp);
            }
            initRootCat(outFat);

            //创建好64*128字节的文件
            byte[] areaByte = new byte[64*128-192];
            outFat.write(areaByte);
            outFat.close();
        }
        //初始化fatTable
    }

    private void readCat(){

    }
    private void initRootCat(OutputStream outFat) throws IOException {
        for (int i = 0 ; i < 8 ; i++ ){
            curCatalogue.add(new byte[]{'$','0','0','0','0','0','0','0'});
            outFat.write(curCatalogue.get(0));
        }
        //初始化diskFile的时候添加根目录

    }
    private void initRootCat(String name) {

    }
    public int findNextFreeBlock() {
        int blockNum = -1;
        for(int i=3; i<fatTable.length; i++) {
            if(fatTable[i] == 0) {
                System.out.println("i is " + i);
                return i;
            }
        }
        return blockNum;
    }

    public int addFile(VirtualFile file) throws IOException {


        int curBlock = findNextFreeBlock();//当前所在的盘块号
        //int needBlock = text.length()/64+1;
        int haveBlock = 0;

        file.setFirstBlock(curBlock);
        //curFile.setFileLength(needBlock);
        file.setAttribute("1");//文件属性怎么规定
        {
            //将文件属性的内容写到curCatalogue
        }
        int i = 0;

//        for (i = 0; i < 8; i++) {
//            System.out.println(curCatalogue[i].substring(7, 8));
//            if ("0".equals(curCatalogue[i].substring(7, 8))) {
//                freeCatalogue = i;
//                break;
//            }
//        }
        if(i == 8) {
            System.out.println("当前目录已满");
            return CAT_IS_FULL;
        }//判断当前目录项是否还有空间

        for(i=0; i<fatTable.length; i++) {
            if(fatTable[i] == 0) {
                haveBlock++;
            }
        }
//        if(haveBlock < needBlock) {
//            System.out.println("磁盘容量不足");
//            return NO_MORE_DISK_SPACE;
//        }//判断是否还有足够的空闲块

//        for (i = 0; i < 8; i++) {
//            if (fileName.equals(curCatalogue[i].substring(0, 2))) {
//                System.out.println("文件已存在");
//                return FILE_EXISTED;
//            }
//        }

        return ADD_SUCESS;
    }
    public String readFile (int firstBlock){
        String text = new String();
        /*步骤
        * 1. 根据传入firstBlock查询fat表
        * 2. 根据fat表读取text，伪代码如下:
        * int curBlock = firstBlock
        * while(curBlock != -1){
        *   String termText = getDiskText (curBlock)
        *   text += termText
        *   curBlock = fatTable[curBlock]
        * }
        * 3. 返回text值
        * */
        return text;
    }
    public void addCat (Catalogue catalogue){
        /*需要完成的功能
        * 1. server根据传入的catalougue ，获取对应的控制信息（目录项需要的控制信息有1.目录名2.目录属性3.起始盘块号
        * 目录名和目录属性 getName 和 getBlock 即可，目录属性查阅指导书在这个方法可以自己计算），拼接得到
        * 将登记目录项的信息以byte[ ]的形式写入diskFile（写到程序当前的目录的位置）
        * 2. 更新fat表
        * 3. 初始化新增的文件夹在diskFile的目录项（就是写入8个空目录项，即"$0000000"）到catalougue的diskFile区域
        * ************ #1步 和 #3 步写到diskFile的位置应该是不一致的，需要加以区分 **********
        *  */

        /*
        * cat目录项信息 (null表示未使用，填写0)
        * 内容目录   : | 目  录  名 |  null  | 属性 | 起始盘块号 |  null |
        * byteskey  : | [0][1][2] | [3][4] | [5] |    [6]    |  [7]  |
        * */
        byte[] entryBytes;
        System.out.println();

    }


    public void showMeFat(){
        /*在控制台直接输出 4*32 fat表*/
        System.out.println("输出FAT表");
        for (int i = 0; i < 4; i++) {
            System.out.print("#"+i*32+" ~ #"+i*32+31+" : ");
            for (int j = 0; j < 32; j++) {
                System.out.print(fatTable[i*32+j]);
            }
            System.out.println();
        }

    }
    public void delCat (Catalogue catalogue){
        /*
        * 1. 获取父目录catalougue.getParent()
        * 2. 获取父目录在diskFile的信息删除对应的目录项
        * （可以使用正则表达式匹配目录名的初始位置，并计算目录项的起始，并清空）
        * 3. 获取起始盘块号码catalougue.getFirstBlock()
        * 4. 将fat表对应的val置0（表示该盘块空闲）
        * */
    }
    public void changeCat (Catalogue catalogue){
        /*
        * 这个函数其实就只有更改目录名的功能
        * 实现方法1：直接先调用一次 delCat，再调用一次addCat
        *（还需要更新上一层Catalogue的firstBlick）
        *
        * 实现方法2：一般只有变更文件名的需求
        * 所以找到对应的目录项，将对应的目录名字的部分修改即可
        * */
    }
    public void delFile(VirtualFile file){
        /*
         * 这个方法和delCat结构是差不多的，可以互相参照
         * 1. 获取父录file.getCatalogue()
         * 2. 获取父目录在diskFile的信息删除对应的目录项
         * （可以使用正则表达式匹配目录名的初始位置，并计算目录项的起始，并清空）
         * 3. 获取起始盘块号码file.getFirstBlock()
         * 4. 将fat表对应的val置0（表示该盘块空闲）
         * */
    }
    public void changeFileATTR (VirtualFile file){
        /* 这个方法需要与saveFile区分开，一个是修改文件的内容，一个是修改文件的属性
         * 同理这个方法和changeCat结构是差不多的，可以互相参照
         *
         * 实现方法1：直接先调用一次 delFile，再调用一次addFile
         *（还需要更新file的firstBlock）
         *
         * 实现方法2：先依旧按更该文件名的功能来做，后续可能有更多的需求
         * 所以找到对应的目录项，将对应的目录名字的部分修改即可
         * */
    }
    public int saveFile(VirtualFile file) throws IOException {
        byte[] curByte = null;//多次写入磁盘文件的byte数组
        String text = file.getLatestText();
        int curBlock = file.getFirstBlock();
        int needBlock = text.length()/64+1;
        RandomAccessFile rdf = new RandomAccessFile(diskFile,"rw");

        System.out.println("curblock is " + curBlock);
        int lastFatNum = curBlock;//记录上一次的盘块号
        int textNum = 0;//第几个text剪切段，因为存在缓冲区，text可能要分多次写入磁盘
        int freeCatalogue;//第几个目录项时空闲 的

        rdf.seek((long)curBlock*64);
        if(textNum*64+64 < text.length()) {
            bufferIn = text.substring(textNum*64, textNum*64+64);//0=64?0-63?
            textNum++;
        }else {
            bufferIn = text.substring(textNum*64, text.length());
        }
        rdf.write(bufferIn.getBytes());
        fatTable[curBlock] = -1; //先将fatTable中该项标为已用
        needBlock--;

        while(needBlock > 0) {
            curBlock = findNextFreeBlock();
            fatTable[lastFatNum] = curBlock;
            fatTable[curBlock] = -1;
            rdf.seek((long)curBlock*64);
            if(textNum*64+64 > text.length()) {
                bufferIn = text.substring(textNum*64, textNum*64+64);//0=64?0-63?
                textNum++;
            }else {
                bufferIn = text.substring(textNum*64, text.length());
            }
            rdf.write(bufferIn.getBytes());
            needBlock--;
        }
        //将更新后的fat表写进磁盘
        rdf.seek(0);
        for(int i=0; i<fatTable.length; i++) {
            int temp = fatTable[i];
            rdf.write(temp);
        }

        return SAVED_SUCESS;
    }

}

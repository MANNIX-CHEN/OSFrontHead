import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class MainForBack {



    public static void main(String[] args) throws IOException {
        Vector<Integer> vector = new Vector<>(8);
        Vector<byte[]> bytes = new Vector<>(8);

        File testFile = new File("src/BackGround/testFile");
        testFile.createNewFile();

        OutputStream outTest = new FileOutputStream(testFile);
        String a = "127";
        for (int i = 0; i < 8; i++) {
            bytes.add(new byte[]{'0','1',50,51,52,53,54,55});
            outTest.write(bytes.get(i));
        }

        //bytes.set(0,new Byte[]{});


    }
}

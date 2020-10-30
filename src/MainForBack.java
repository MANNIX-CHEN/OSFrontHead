import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class MainForBack {



    public static void main(String[] args) {
        Vector<Integer> vector = new Vector<>(8);
        int data [] = new int[]{0,1,2,3,4,5};

        for (int a:
             data) {
            vector.add(a);
        }

        System.out.println("#4 is "+vector.get(4));
        vector.remove(4);
        System.out.println("#4 is "+vector.get(4));
    }

}

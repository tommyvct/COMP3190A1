import java.util.ArrayList;


/**
 * A1Q2
 */
public class A1Q2 {

    public static void main(String[] args) {
        ArrayList<Integer> test = new ArrayList<Integer>();
        test.add(1);
        test.add(2);
        test.add(3);
        test.add(4);
        test.add(5);

        System.out.println(test);
        
        System.out.println(manipulate(test));
        
        System.out.println(test);
        

    }

    public static ArrayList<Integer> manipulate(ArrayList<Integer> tryThis)
    {
        tryThis.set(2, 6);

        System.out.println(tryThis);

        return tryThis;
    }
}
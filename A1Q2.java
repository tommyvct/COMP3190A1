

/**
 * A1Q2
 */
public class A1Q2 {

    public static void main(String[] args) 
    {
        
        for (int i = 1; i <= 9; i++) 
        {
            System.out.println(i);

            for (int j = 1; j <= 9; j++) 
            {
                for (int k = 1; k <= 9; k++) 
                {
                    if
                    (
                        j + k == i ||
                        j - k == i ||
                        k - j == i ||
                        j * k == i ||
                        (double) j / (double) k == (double) i || 
                        (double) k / (double) j == (double) i
                    )
                    {
                        System.out.println("" + j + "" + k);
                    }
        
                }
            }


            System.out.println();
            System.out.println();
            System.out.println();
        }

    }

}
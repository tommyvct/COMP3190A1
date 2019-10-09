import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

/**
 * A1Q1
 */
public class A1Q1 {

    public static void main(String[] args) 
    {
        Scanner in = new Scanner(System.in);
        String initialState = in.nextLine();
        in.close();

        ArrayList<AztecNode> al = new ArrayList<AztecNode>();

        // add number to the nodes
        for (char c : initialState.toCharArray()) 
        {
            int value = Character.getNumericValue(c);
            al.add(new AztecNode(value, 0, null, null));
        }

        int levelCounter = 1;
        int levelIterator = levelCounter;

        // add level to the nodes
        for (AztecNode n : al) 
        {
            n.setLevel(levelCounter);

            if (--levelIterator == 0)
            {
                levelCounter++;
                levelIterator = levelCounter;
            }
        }

        // link children to the nodes
        for (int i = 0; i < al.size(); i++)
        {
            int leftChildIndex = i + al.get(i).getLevel();
            int rightChildIndex = leftChildIndex + 1;

            if (leftChildIndex >= al.size())
            {
                continue;
            }
            else
            {
                al.get(i).setLeftChild(al.get(leftChildIndex));
                al.get(i).setRightChild(al.get(rightChildIndex));
            }
        }
        
                
    }
    
    public static void depthFirstSearch(ArrayList<AztecNode> original) 
    {
        ArrayList<AztecNode> searchThis = deepCopy(original);
        Stack<AztecNode> dfsStack = new Stack<AztecNode>();

        
    }

    public static ArrayList<AztecNode> deepCopy(ArrayList<AztecNode> original)
    {
        ArrayList<AztecNode> ret = new ArrayList<AztecNode>();
        
        for (AztecNode n : original) 
        {
            ret.add(n);
        }

        return ret;
    }
}

/**
 * AztecNode
 */
class AztecNode 
{
    int node;
    int level;
    AztecNode leftChild;
    AztecNode rightChild;

    public AztecNode(int node, int level, AztecNode leftChild, AztecNode rightChild)
    {
        this.node = node;
        this.level = level;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public int getNode() 
    {
        return node;
    }

    public void setNode(int node) 
    {
        this.node = node;
    }

    public AztecNode getLeftChild() 
    {
        return leftChild;
    }

    public void setLeftChild(AztecNode leftChild) 
    {
        this.leftChild = leftChild;
    }

    public AztecNode getRightChild() 
    {
        return rightChild;
    }

    public void setRightChild(AztecNode rightChild) 
    {
        this.rightChild = rightChild;
    }

    public int getLevel() 
    {
        return level;
    }

    public void setLevel(int level) 
    {
        this.level = level;
    }

}

// class AztecTree
// {
//     private AztecTree parent;
//     private int node;
//     private AztecTree leftChild;
//     private AztecTree rightChild;

//     private AztecTree(          AztecTree parent, 
//                                    int node, 
//                      AztecTree leftChild, AztecTree rightChild)
//     {
//         this.parent = parent;
//         this.node = node;
//         this.leftChild = leftChild;
//         this.rightChild = rightChild;
//     }

//     private AztecTree(AztecTree parent, int node, boolean isLeftChild, int[] initialState)
//     {
//         this.parent = parent;
//         this.node = node;

//         if (initialState.length == 3)
//         {
//             this.leftChild = new AztecTree(this, initialState[1], null, null);
//             this.rightChild = new AztecTree(this, initialState[2], null, null);
//         }
//         else
//         {
//             this.leftChild = new AztecTree(this, initialState[1], true, Arrays.copyOfRange(initialState, 3, initialState.length));
//             this.rightChild = new AztecTree(this, initialState[2], false, Arrays.copyOfRange(initialState, 3, initialState.length));
//         }
//     }

//     public AztecTree(String initialState)
//     {
//         int[] temp = new int[(initialState.trim().length() + 1) / 2];
//         int counter = 0;

//         for (char c : initialState.toCharArray()) 
//         {
//             temp[counter++] = Character.getNumericValue(c);
//         }

//         this.parent = null;
//         this.node = temp[0];
//         if (temp.length == 3)
//         {
//             this.leftChild = new AztecTree(this, temp[1], null, null);
//             this.rightChild = new AztecTree(this, temp[2], null, null);
//         }
//         else
//         {
//             this.leftChild = new AztecTree(this, temp[1], true, Arrays.copyOfRange(temp, 3, temp.length));
//             this.rightChild = new AztecTree(this, temp[2], false, Arrays.copyOfRange(temp, 3, temp.length));
//         }
//     }
// }
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

/**
 * A1Q1
 */
public class A1Q1 
{
    public static void main(String[] args) 
    {
        Scanner in = new Scanner(System.in);
        String initialState = in.nextLine();
        in.close();

        AztecState original = new AztecState(initialState);
        AztecState dfs = original.deepCopy();
        AztecState bfs = original.deepCopy();
        AztecState ids = original.deepCopy();
        AztecState informed = original.deepCopy();

        dfs.depthFirstSearch();
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

    public AztecNode
    (
        int node, int level, 
        AztecNode leftChild, AztecNode rightChild
    )
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
    
    // children info is lost during deep copy
    public AztecNode deepCopy()
    {
        return new AztecNode(this.node, this.level, null, null);
    }
}

/**
 * AztecState
 */
class AztecState 
{
    private ArrayList<AztecNode> state;
    static int permutation = 0;

    // private constructor for deep copy
    private AztecState(AztecState toCopy)
    {
        this.state = new ArrayList<AztecNode>();

        for (AztecNode n : toCopy.state) 
        {
            this.state.add(n.deepCopy());
        }

        // link children to the nodes
        for (int i = 0; i < this.state.size(); i++)
        {
            int leftChildIndex = i + this.state.get(i).getLevel();
            int rightChildIndex = leftChildIndex + 1;

            if (leftChildIndex >= state.size())
            {
                continue;
            }
            else
            {
                state.get(i).setLeftChild(state.get(leftChildIndex));
                state.get(i).setRightChild(state.get(rightChildIndex));
            }
        }
        
    }

    public AztecState(String initialState)
    {
        this.state = new ArrayList<AztecNode>();
        
        // add number to the nodes
        for (char c : initialState.toCharArray()) 
        {
            if (c == ',' || c == ' ')
            {
                continue;
            }
            int value = Character.getNumericValue(c);
            state.add(new AztecNode(value, 0, null, null));
        }

        int levelCounter = 1;
        int levelIterator = levelCounter;

        // add level to the nodes
        for (AztecNode n : state) 
        {
            n.setLevel(levelCounter);

            if (--levelIterator == 0)
            {
                levelCounter++;
                levelIterator = levelCounter;
            }
        }

        // link children to the nodes
        for (int i = 0; i < state.size(); i++)
        {
            int leftChildIndex = i + state.get(i).getLevel();
            int rightChildIndex = leftChildIndex + 1;

            if (leftChildIndex >= state.size())
            {
                continue;
            }
            else
            {
                state.get(i).setLeftChild(state.get(leftChildIndex));
                state.get(i).setRightChild(state.get(rightChildIndex));
            }
        }
    }
    
    @Override
    public String toString() 
    {
        String ret = "";
        
        for (AztecNode n : this.state) 
        {
            ret += n.node;
            ret += ",";
            // if (n == this.state.get(this.state.size() - 1));
            // {
            //     break;
            // }
        }

        return ret;
    }

    public AztecNode get(int i)
    {
        return this.state.get(i);
    }

    private boolean isValid()
    {
        for (AztecNode node : this.state) 
        {
            AztecNode leftChild = node.getLeftChild();
            AztecNode rightChild = node.getRightChild();

            if (leftChild == null || rightChild == null)  // reached end
            {
                continue;
            }
            else if  // have 0 
            (
                node.getNode() == 0 ||
                leftChild.getNode() == 0 ||
                rightChild.getNode() == 0
            )
            {
                continue;
            }
            else if (leftChild.getNode() == rightChild.getNode())
            {
                return false;
            }
            else if   // neither one of + - * /
            (!(
                leftChild.getNode() + rightChild.getNode() == node.getNode() ||
                leftChild.getNode() - rightChild.getNode() == node.getNode() ||
                leftChild.getNode() * rightChild.getNode() == node.getNode() ||
                leftChild.getNode() / rightChild.getNode() == node.getNode()
            ))
            {
                return false;
            }
            else
            {
                continue;
            }
        }
        
        return true;
    }
        
    public AztecState deepCopy()
    {
        return new AztecState(this);
    }

    public void depthFirstSearch()
    {
        Stack<AztecState> open = new Stack<AztecState>();
        Stack<AztecState> closed = new Stack<AztecState>();

        open.push(this);

        do 
        {
            AztecState popped = open.pop();
            ArrayList<AztecState> toPush = validate(permute(popped));
            AztecState possibleAnswer = findAnswer(toPush);

            if (possibleAnswer == null)
            {
                for (AztecState s : toPush) 
                {
                    open.push(s);
                }
            }
            else
            {
                System.out.print("Solution found:" + possibleAnswer.toString());
                System.out.println();
                System.out.println(permutation + "generated.");
                return;
            }
        } 
        while (!open.isEmpty());
    }

    private ArrayList<AztecState> permute(AztecState toPermute)
    {
        ArrayList<AztecState> ret = new ArrayList<AztecState>();

        for (int i = 0; i < toPermute.state.size(); i++)
        // for (AztecNode var : toPermute.state) 
        {
            if (toPermute.state.get(i).node == 0)
            {
                for (int j = 1; j <= 9; j++) 
                {
                    AztecState temp = toPermute.deepCopy();  // make a deep copy
                    temp.get(i).setNode(j);             // permute 0 to others
                    ret.add(temp);                     // add to return list
                    permutation++;
                }
            }
        }

        if (ret.isEmpty())
        {
            return null;
        }
        else
        {
            return ret;
        }
    }

    private ArrayList<AztecState> validate(ArrayList<AztecState> toValidate)
    {
        ArrayList<AztecState> toRemove = new ArrayList<AztecState>();

        for (AztecState s : toValidate) 
        {
            if (!s.isValid())
            {
                toRemove.add(s);
            }
        }

        toValidate.removeAll(toRemove);
        return toValidate;
    }

    private boolean haveZero()
    {
        for (AztecNode n : this.state) 
        {
            if (n.node == 0)
            {
                return false;
            }
        }
        
        return true;
    }

    // since everything is validated, there are only states with 0 or answers
    private AztecState findAnswer(ArrayList<AztecState> toFind)
    {
        AztecState ret = null;
        
        if (toFind.isEmpty())
        {
            return null;
        }

        for (AztecState s : toFind) 
        {
            if (s.haveZero())
            {
                continue;
            }
            else
            {
                ret = s;
                break;
            }
        }

        return ret;
    }
}
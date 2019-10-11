import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.LinkedList;

/**
 * University of Manitoba COMP3190 Fall 2019 Assignment 1 Question 1 <p>
 * Aztec math puzzle solver using 
 * <b>Depth First Search</b>, <b>Breadth First Search</b>,
 * <b>Iterative Deepening Search</b> and <b>Informed search</b>.
 * 
 * @author Tommy Wu (7852291)
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
        System.out.println();
        bfs.breadthFirstSearch();
        System.out.println();
        ids.iterativeDeepeningSearch(100);
        System.out.println();
        informed.informedSearch();
    }
}

/**
 * Data structure for nodes in the puzzle.
 * 
 * @author Tommy Wu (7852291)
 */
class AztecNode 
{
    private int node;
    private int level;
    private AztecNode leftChild;
    private AztecNode rightChild;

    /**
     * Constructor for {@code AztecNode}
     * 
     * @param node        value of the node
     * @param level       which level the {@code AztecNode} belongs to
     * @param leftChild   reference to the left child
     * @param rightChild  reference to the right child
     */
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

    /**
     * Getter for member {@code Node}
     * 
     * @return member {@code Node}
     */
    public int getNode() 
    {
        return node;
    }

    /**
     * Setter for member {@code Node}
     */
    public void setNode(int node) 
    {
        this.node = node;
    }

    /**
     * Getter for member {@code leftChild}
     * 
     * @return member {@code leftChild}
     */
    public AztecNode getLeftChild() 
    {
        return leftChild;
    }

    /**
     * Setter for member {@code leftChild}
     */
    public void setLeftChild(AztecNode leftChild) 
    {
        this.leftChild = leftChild;
    }

    /**
     * Getter for member {@code rightChild}
     * 
     * @return member {@code rightChild}
     */
    public AztecNode getRightChild() 
    {
        return rightChild;
    }

    /**
     * Setter for member {@code rightChild}
     */
    public void setRightChild(AztecNode rightChild) 
    {
        this.rightChild = rightChild;
    }

    /**
     * Getter for member {@code level}
     * 
     * @return member {@code level}
     */
    public int getLevel() 
    {
        return level;
    }

    /**
     * Setter for member {@code level}
     */
    public void setLevel(int level) 
    {
        this.level = level;
    }
    
    // children info is lost during deep copy
    
    /**
     * A deep copy function for {@code AztecNode} <p>
     * It is very important that <b>references to children</b> is <b>lost</b>
     * during deep copy since the reference to children will stop making sense 
     * once the {@code AztecNode} leaves its {@code Collection}.
     * 
     * @return a deep copy of current {@code AztecNode}
     */
    public AztecNode deepCopy()
    {
        return new AztecNode(this.node, this.level, null, null);
    }

    @Override
    public String toString() 
    {
        return "" + this.node;
    }
}

/**
 * Date structure for holding the state of the puzzle.<p>
 * Implemented using {@code ArrayList<AztecNode>}
 * 
 * @author Tommy Wu (7852291)
 */
class AztecState 
{
    private ArrayList<AztecNode> state;
    private static int permutation = 0;     // counter for counting permutations

    /**
     * private constructor for deep copy
     * 
     * @param toCopy source to do deep copy
     */
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

    /**
     * Initiate a puzzle.
     * 
     * @param initialState {@code String} containing the initial state
     */
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
            ret += n.getNode();
            ret += ",";
            // if (n == this.state.get(this.state.size() - 1));
            // {
            //     break;
            // }
        }

        return ret;
    }

    /**
     * Getter for a {@code AztecNode} with given index
     * 
     * @param i index of a {@code AztecNode}
     * @return the {@code AztecNode} at given index
     */
    public AztecNode get(int i)
    {
        return this.state.get(i);
    }

    /**
     * Tell if current state is valid. <p>
     * A state is valid if:
     * <ul>
     *  <li>Left child is same as right child</li>
     *  <li>If both children are defined(NOT 0), left child <b>add</b>, 
     *      <b>subtract</b>, <b>multiply</b> OR <b>divide</b> right child in any
     *       order equals to the parent</li>
     * 
     * </ul>
     * 
     * @return {@code true} if current state satisfy ALL above conditions, 
     * @{code false} if not.
     */
    public boolean isValid()
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
                // add
                leftChild.getNode() + rightChild.getNode() == node.getNode() ||
                // subtract
                leftChild.getNode() - rightChild.getNode() == node.getNode() ||
                rightChild.getNode() - leftChild.getNode() == node.getNode() ||
                // multiply
                leftChild.getNode() * rightChild.getNode() == node.getNode() ||
                // divide
                (double) leftChild.getNode() / (double) rightChild.getNode() == 
                (double) node.getNode() || 
                (double) rightChild.getNode() / (double) leftChild.getNode() == 
                (double) node.getNode()
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

    /**
     * A deep copy function for {@code AztecState} <p>
     * 
     * @return a deep copy of current {@code AztecState}
     */
    public AztecState deepCopy()
    {
        return new AztecState(this);
    }

    /**
     * Do a Depth First Search against current {@code AztecState}
     */
    public void depthFirstSearch()
    {
        Stack<AztecState> open = new Stack<AztecState>();
        permutation = 0;
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
                System.out.print("DFS Solution found: ");
                System.out.print(possibleAnswer.toString());
                System.out.println();
                System.out.println(permutation + " permutations generated.");
                return;
            }
        } 
        while (!open.isEmpty());

        System.out.println("No solution.");
    }

    /**
     * Do a Breadth First Search against current {@code AztecState}
     */
    public void breadthFirstSearch()
    {
        Queue<AztecState> open = new LinkedList<AztecState>();
        permutation = 0;
        open.add(this);

        do 
        {
            AztecState popped = open.remove();
            ArrayList<AztecState> toPush = validate(permute(popped));
            AztecState possibleAnswer = findAnswer(toPush);

            if (possibleAnswer == null)
            {
                for (AztecState s : toPush) 
                {
                    open.add(s);
                }
            }
            else
            {
                System.out.print("BFS Solution found: ");
                System.out.print(possibleAnswer.toString());
                System.out.println();
                System.out.println(permutation + " permutations generated.");
                return;
            }
        } 
        while (!open.isEmpty());

        System.out.println("No solution.");
    }
    
    /**
     * Do a Iterative Deepening Search against current {@code AztecState}
     */
    public void iterativeDeepeningSearch(int maxDepth)
    {
        Stack<AztecState> open = new Stack<AztecState>();
        permutation = 0;

        for (int i = 0; i < maxDepth; i++)  // depth counter
        {
            int depth = 0;
            do 
            {
                if (open.isEmpty())
                {
                    open.push(this);
                }
                AztecState popped = open.pop();
                ArrayList<AztecState> toPush = validate(permute(popped));
                AztecState possibleAnswer = findAnswer(toPush);

                depth++;

                if (depth >= i)
                {
                    System.out.print("    ");
                    System.out.println("finished search with depth limit " + i);
                    break;  // do while loop
                }

                if (possibleAnswer == null)
                {
                    for (AztecState s : toPush) 
                    {
                        open.push(s);
                    }
                }
                else
                {
                    System.out.print("IDS Solution found: ");
                    System.out.print(possibleAnswer.toString());
                    System.out.println();
                    System.out.println(permutation +" permutations generated.");
                    return;
                }
            } 
            while (!open.isEmpty());
        }
    }

    /**
     * Permute all possible permutations against given {@code AztecState} by
     * replacing every undefined node into 1 to 9.
     * 
     * @param toPermute {@code AztecState} to start with
     * @return {@code ArrayList<AztecState>} containing all permutations
     */
    private ArrayList<AztecState> permute(AztecState toPermute)
    {
        ArrayList<AztecState> ret = new ArrayList<AztecState>();
        AztecState backup = toPermute.deepCopy();  // make a deep copy

        // for each node
        for (int i = 0; i < backup.state.size(); i++)
        {
            // if this node is 0
            if (toPermute.state.get(i).getNode() == 0)
            {
                for (int j = 1; j <= 9; j++) 
                {
                    backup.get(i).setNode(j);             // permute 0 to others
                    AztecState temp = backup.deepCopy();
                    ret.add(temp);                     // add to return list
                    permutation++;
                }

                break;
            }
        }

        return ret;
    }

    /**
     * Validate every given {@code AztecState} in the given {@code Collection} 
     * and remove everything that is NOT valid.
     * 
     * @param toValidate {@code Collection} to be validated
     * @return A {@code Collection} of valid {@code AztecStates}
     */
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

    /**
     * Tell if the current {@code AztecState} contains undefined node.
     * 
     * @return {@code true} if the current {@code AztecState} contains undefined
     * node, {@code false} if not.
     */
    private boolean haveZero()
    {
        for (AztecNode n : this.state) 
        {
            if (n.getNode() == 0)
            {
                return true;
            }
        }
        
        return false;
    }

    // since everything is validated, there are only states with 0 or answers
    /**
     * Find the answer from given {@code Collection} of {@code AztecState}
     * 
     * @param toFind A validated {@code Collection} of {@code AztecState}
     * @return reference to the first found solution of current puzzle
     */
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

    // tuples[root][indexPermutation][0 for left child, 1 for right child]

    /**
     * A static method to retrieve all valid children for given parent.<p>
     * Using prepared hard-coded data to speed up search speed.
     * 
     * @param parent value of parent
     * @return all valid children 
     */
    private static int[][] validTuples(int parent)
    {
        // crazy hard coding
        final int[][][] tuples = 
        {
            {  // 1
                {1,2},{2,1},{2,3},{3,2},{3,4},{4,3},{4,5},{5,4},{5,6},{6,5},
                {6,7},{7,6},{7,8},{8,7},{8,9},{9,8}
            },
            
            {  // 2                
                {1,2},{1,3},{2,1},{2,4},{3,1},{3,5},{3,6},{4,2},{4,6},{4,8},
                {5,3},{5,7},{6,3},{6,4},{6,8},{7,5},{7,9},{8,4},{8,6},{9,7}
            },

            {  // 3
                {1,2},{1,3},{1,4},{2,1},{2,5},{2,6},{3,1},{3,6},{3,9},{4,1},
                {4,7},{5,2},{5,8},{6,2},{6,3},{6,9},{7,4},{8,5},{9,3},{9,6}
            },
            
            {  // 4
                {1,3},{1,4},{1,5},{2,6},{2,8},{3,1},{3,7},{4,1},{4,8},{5,1},
                {5,9},{6,2},{7,3},{8,2},{8,4},{9,5}
            },
            
            {  // 5
                {1,4},{1,5},{1,6},{2,3},{2,7},{3,2},{3,8},{4,1},{4,9},{5,1},
                {6,1},{7,2},{8,3},{9,4}
            },
            
            {  // 6
                {1,5},{1,6},{1,7},{2,3},{2,4},{2,8},{3,2},{3,9},{4,2},{5,1},
                {6,1},{7,1},{8,2},{9,3}
            },
            
            {  // 7
                {1,6},{1,7},{1,8},{2,5},{2,9},{3,4},{4,3},{5,2},{6,1},{7,1},
                {8,1},{9,2}
            },
            
            {  // 8
                {1,7},{1,8},{1,9},{2,4},{2,6},{3,5},{4,2},{5,3},{6,2},{7,1},
                {8,1},{9,1}
            },
            
            {  // 9
                {1,8},{1,9},{2,7},{3,6},{4,5},{5,4},{6,3},{7,2},{8,1},{9,1}
            }
            
        };
    
        return tuples[parent - 1];
    }

    /**
     * Do a Informed Search against current {@code AztecState}
     */
    public void informedSearch()
    {
        Stack<AztecState> open = new Stack<AztecState>();

        open.push(this);

        do 
        {
            AztecState popped = open.pop();
            ArrayList<AztecState> toPush = validate(informedPermute(popped));
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
                System.out.print("Informed DFS Solution found: ");
                System.out.print(possibleAnswer.toString());
                System.out.println();
                System.out.println(permutation + " permutations generated.");
                return;
            }
        } 
        while (!open.isEmpty());

        System.out.println("No solution.");
    }

    /**
     * Permute all possible permutations against given {@code AztecState} by
     * look up hard-coded data.
     * 
     * @param toPermute {@code AztecState} to start with
     * @return {@code ArrayList<AztecState>} containing all permutations
     */
    private ArrayList<AztecState> informedPermute(AztecState toPermute)
    {
        ArrayList<AztecState> ret = new ArrayList<AztecState>();
        AztecState backup = toPermute.deepCopy();

        // for each node
        for (int i = 0; i < backup.state.size(); i++)
        // for (AztecNode n :backup.state)
        {
            AztecNode n = backup.state.get(i);
            AztecNode leftChild = n.getLeftChild();
            AztecNode rightChild = n.getRightChild();

            if (n.getNode() == 0)
            {
                continue;
            }
            else if (leftChild == null || rightChild == null)
            {
                continue;
            }
            // found the node with 0 in right child
            else if (leftChild.getNode() != 0 && rightChild.getNode() == 0)
            {
                int[][] tuples = validTuples(n.getNode());

                for (int[] t : tuples) 
                {
                    if (t[0] == leftChild.getNode())
                    {
                        // set the right child, leave the left child alone
                        backup.state.get(i).getRightChild().setNode(t[1]);
                        AztecState toAdd = backup.deepCopy();
                        ret.add(toAdd);
                        permutation++;
                    }
                }

                break;
            }
            // found the node with 0 in left child
            else if (leftChild.getNode() == 0 && rightChild.getNode() != 0)
            {
                int[][] tuples = validTuples(n.getNode());

                for (int[] t : tuples) 
                {
                    if (t[1] == rightChild.getNode())
                    {
                        // set the left child, leave the right child alone
                        backup.state.get(i).getLeftChild().setNode(t[0]);
                        AztecState toAdd = backup.deepCopy();
                        ret.add(toAdd);
                        permutation++;
                    }
                }
                
                break;
            }
            // found the node with 0 in both children 
            else if (leftChild.getNode() == 0 && rightChild.getNode() == 0)
            {
                int[][] tuples = validTuples(n.getNode());

                for (int[] t : tuples) 
                {
                    backup.state.get(i).getLeftChild().setNode(t[0]);
                    backup.state.get(i).getRightChild().setNode(t[1]);
                    AztecState toAdd = backup.deepCopy();
                    ret.add(toAdd);
                    permutation++;
                }

                break;
            }
            else
            {
                continue;
            }
        }

        return ret;
    }
}


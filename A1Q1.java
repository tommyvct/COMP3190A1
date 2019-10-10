import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.LinkedList;

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
        // AztecState informed = original.deepCopy();

        dfs.depthFirstSearch();
        bfs.breadthFirstSearch();
        ids.iterativeDeepeningSearch(100);

        // AztecState test = new AztecState("3,9,6,1,9,3,7,4,0,8");
        // System.out.println(test.isValid());
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

    @Override
    public String toString() 
    {
        return "" + this.node;
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
                leftChild.getNode() + rightChild.getNode() == node.getNode() ||
                leftChild.getNode() - rightChild.getNode() == node.getNode() ||
                rightChild.getNode() - leftChild.getNode() == node.getNode() ||
                leftChild.getNode() * rightChild.getNode() == node.getNode() ||
                (double) leftChild.getNode() / (double) rightChild.getNode() == (double) node.getNode() || 
                (double) rightChild.getNode() / (double) leftChild.getNode() == (double) node.getNode()
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
                System.out.print("DFS Solution found: " + possibleAnswer.toString());
                System.out.println();
                System.out.println(permutation + " permutations generated.");
                return;
            }
        } 
        while (!open.isEmpty());
    }

    public void breadthFirstSearch()
    {
        Queue<AztecState> open = new LinkedList<AztecState>();
        // Queue<AztecState> closed = new LinkedList<AztecState>();

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
                System.out.print("BFS Solution found: " + possibleAnswer.toString());
                System.out.println();
                System.out.println(permutation + " permutations generated.");
                return;
            }
        } 
        while (!open.isEmpty());
    }

    public void iterativeDeepeningSearch(int maxDepth)
    {
        Stack<AztecState> open = new Stack<AztecState>();

        // open.push(this);

        for (int i = 0; i < maxDepth; i++) 
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
                    System.out.println("finished search with depth limit " + i);
                    break;
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
                    System.out.print("IDS Solution found: " + possibleAnswer.toString());
                    System.out.println();
                    System.out.println(permutation + " permutations generated.");
                    return;
                }
            } 
            while (!open.isEmpty());
        }
    }

    private ArrayList<AztecState> permute(AztecState toPermute)
    {
        ArrayList<AztecState> ret = new ArrayList<AztecState>();
        AztecState temp1 = toPermute.deepCopy();  // make a deep copy

        // for each node
        for (int i = 0; i < temp1.state.size(); i++)
        {
            // if this node is 0
            if (toPermute.state.get(i).node == 0)
            {
                for (int j = 1; j <= 9; j++) 
                {
                    temp1.get(i).setNode(j);             // permute 0 to others
                    AztecState temp2 = temp1.deepCopy();
                    ret.add(temp2);                     // add to return list
                    permutation++;
                }

                break;
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
                return true;
            }
        }
        
        return false;
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

    private static int[] validTuples(int forNumber)
    {
        
        int[][][] tuples = 
        {
            {  // 1
            {1,2},
            {2,1},
            {2,3},
            {3,2},
            {3,4},
            {4,3},
            {4,5},
            {5,4},
            {5,6},
            {6,5},
            {6,7},
            {7,6},
            {7,8},
            {8,7},
            {8,9},
            {9,8},
            },
            
              
            
            
            {  // 2
                
            {1,2},
            {1,3},
            {2,1},
            {2,4},
            {3,1},
            {3,5},
            {3,6},
            {4,2},
            {4,6},
            {4,8},
            {5,3},
            {5,7},
            {6,3},
            {6,4},
            {6,8},
            {7,5},
            {7,9},
            {8,4},
            {8,6},
            {9,7}
            },
            
            
            {    //3
            {1,2},
            {1,3},
            {1,4},
            {2,1},
            {2,5},
            {2,6},
            {3,1},
            {3,6},
            {3,9},
            {4,1},
            {4,7},
            {5,2},
            {5,8},
            {6,2},
            {6,3},
            {6,9},
            {7,4},
            {8,5},
            {9,3},
            {9,6}
            },
            
            
            
            {   //4
            {1,3},
            {1,4},
            {1,5},
            {2,6},
            {2,8},
            {3,1},
            {3,7},
            {4,1},
            {4,8},
            {5,1},
            {5,9},
            {6,2},
            {7,3},
            {8,2},
            {8,4},
            {9,5}
            },
            
            
            {    //5
            {1,4},
            {1,5},
            {1,6},
            {2,3},
            {2,7},
            {3,2},
            {3,8},
            {4,1},
            {4,9},
            {5,1},
            {6,1},
            {7,2},
            {8,3},
            {9,4}
            },
            
            
            
            {    //6
            {1,5},
            {1,6},
            {1,7},
            {2,3},
            {2,4},
            {2,8},
            {3,2},
            {3,9},
            {4,2},
            {5,1},
            {6,1},
            {7,1},
            {8,2},
            {9,3}
            },
            
            
            {    // 7
            {1,6},
            {1,7},
            {1,8},
            {2,5},
            {2,9},
            {3,4},
            {4,3},
            {5,2},
            {6,1},
            {7,1},
            {8,1},
            {9,2}
            },
            
            
            
            
            {   // 8
            {1,7},
            {1,8},
            {1,9},
            {2,4},
            {2,6},
            {3,5},
            {4,2},
            {5,3},
            {6,2},
            {7,1},
            {8,1},
            {9,1}
            },
            
            
            {     // 9
            {1,8},
            {1,9},
            {2,7},
            {3,6},
            {4,5},
            {5,4},
            {6,3},
            {7,2},
            {8,1},
            {9,1}
            }
            
            };
    
        return null;
    }

}


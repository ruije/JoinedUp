import java.util.*;

/**
 * Tree implementation
 */
public class Tree<T> {
    public List<Tree<T>> children = new ArrayList<Tree<T>>();
    public Tree<T> parent = null;
    public T data = null;

    public Tree(T data) {
        this.data = data;
    }

    public Tree(T data, Tree<T> parent) {
        this.data = data;
        this.parent = parent;
    }

    public List<Tree<T>> getChildren() {
        return children;
    }

    public Tree<T> getParent() {
        return parent;
    }

    public void addChild(T data) {
        Tree<T> child = new Tree<T>(data);
        //child.setParent(this);
        this.parent = parent;
        this.children.add(child);
    }

    public void addChild(Tree<T> child) {
        //child.setParent(this);
        this.parent = parent;
        this.children.add(child);
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isRoot() {
        return (this.parent == null);
    }

    public boolean isLeaf() {
        if(this.children.size() == 0) 
            return true;
        else 
            return false;
    }

    public void removeParent() {
        this.parent = null;
    }

    /** A method to find the size of a tree.
     *
     * @return count is the int that counts how many nodes are in the tree.
     */
    public int size() {
        int count = 1;
        if(children.isEmpty()){
            
            return 1;
        }
        for(Tree<T> leaf: children){
            count+= leaf.size();
        }
        return count;
    }

    /** A method that finds if a certain value is in the tree.
     *
     * @param value is the value being searched for.
     * @return value when at root, match when found in children, else null.
     */
    public Tree<T> find(T value) {
        if (data.equals(value)) {
            return this;
        }
        for (Tree<T> child : children) {
            Tree<T> match = child.find(value);
            if (match != null) {
                return match;
            }
        }
        return null;
    }

    
        public Tree<T> remove(T value) {
            if (data.equals(value)) {
                return this;
            }
            for (Tree<T> child : children) {
                Tree<T> match = child.find(value);
                if (match != null) {
                    child.removeParent();
                }
            }
            return null;
        }

    /** A method that converts the tree to string.
     *
     * @return the tree as a string.
     */
    public String toString() {
        if(children.isEmpty()) {
            return data.toString();
        }
        return data.toString() + ' ' + children.toString();
    }

    public String findParents(String value) {
        if (data.equals(value)) {
            return data.toString();
        }
        for (Tree<T> child : children) {
            String match = child.findParents(value);
            if (match != null) {
                return  match + " " + child.findParents(child.data.toString());
            }
        }
        return null;
    }

    /** A helper method for testing (used by main).  Searches tree for
     *  the given target and adds white space separated children to
     *  the tree matching target if there is one.
     *
     * @param target the root value to seach for.
     * @param children a white space separated list of children to add
     * to the tree whose value matches target.
     */
    public static void addChildren(String target, String children, Tree tree) {
        @SuppressWarnings("unchecked")
        Tree<String> parent = tree.find(target);
        if(parent != null) {
            for(String child : children.split(" ")) {
                parent.addChild(new Tree<String>(child));
            }
        }
    }

}

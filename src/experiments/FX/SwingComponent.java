
package experiments.FX;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * 
 */
public class SwingComponent extends JPanel{
    public SwingComponent(){
        super.setLayout(new BorderLayout(5,5));
        //setBackground(Color.CYAN);
        JTree jTree = new JTree();
        jTree.setDragEnabled(true);
        DefaultMutableTreeNode      root = new DefaultMutableTreeNode("JTree");
        DefaultMutableTreeNode      parent;

        parent = new DefaultMutableTreeNode("I am");
        root.add(parent);
        parent.add(new DefaultMutableTreeNode("a Swing tree!"));
        parent.add(new DefaultMutableTreeNode("on the right side..."));


        parent = new DefaultMutableTreeNode("Animals");
        root.add(parent);
        parent.add(new DefaultMutableTreeNode("cat"));
        parent.add(new DefaultMutableTreeNode("dog"));
        parent.add(new DefaultMutableTreeNode("dolphin"));
        parent.add(new DefaultMutableTreeNode("snake"));

        parent = new DefaultMutableTreeNode("Activities");
        root.add(parent);
        parent.add(new DefaultMutableTreeNode("coding"));
        parent.add(new DefaultMutableTreeNode("sleeping"));
        parent.add(new DefaultMutableTreeNode("debugging"));
        jTree.setModel(new DefaultTreeModel(root));
        
        JTextField jTextField = new JTextField("Type something here...");
        
        super.add(BorderLayout.CENTER,new JScrollPane(jTree));
        super.add(BorderLayout.SOUTH,jTextField);
 
    }
}

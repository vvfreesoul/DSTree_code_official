package cn.edu.fudan.cs.dstree;

import de.ruedigermoeller.serialization.FSTObjectInput;
import de.ruedigermoeller.serialization.FSTObjectOutput;
import org.junit.Test;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TestSerialize {
    @Test
    public void testSave1() throws Exception {
        String fileName = "2.dat";
        TreeNode root = getTreeNode();

        FSTObjectOutput out = new FSTObjectOutput(new FileOutputStream(fileName));
        out.writeObject(root);
        out.close(); // required !

        FSTObjectInput in = new FSTObjectInput(new FileInputStream(fileName));
        TreeNode newRoot = (TreeNode) in.readObject(TreeNode.class);
        in.close();
        int idx  = newRoot.left.left.left.i;
        System.out.println("idx = " + idx);
    }

    @Test
    public void testSave() throws IOException, ClassNotFoundException {
        TreeNode root = getTreeNode();

        String fileName = "1.dat";
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(root);
        fos.close();

        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ios = new ObjectInputStream(fis);
        TreeNode newRoot = (TreeNode) ios.readObject();
        int idx  = newRoot.left.left.left.i;
//        System.out.println("name = " + name);
        System.out.println("idx = " + idx);
    }

    private TreeNode getTreeNode() {
        int i = 0;
        TreeNode root = new TreeNode(i++, null);
        List<TreeNode> queue = new ArrayList<TreeNode>();
        queue.add(root);
        while (i < 1000 * 10)
        {
            TreeNode parent = queue.remove(0);
            TreeNode left = new TreeNode(i++,parent);
            TreeNode right = new TreeNode(i++,parent);
            parent.left = left;
            parent.right = right;
            queue.add(left);
            queue.add(right);
        }
        System.out.println("i = " + i);
        return root;
    }
}

package cn.edu.fudan.cs.dstree;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: wangyang
 * Date: 13-11-17
 * Time: 下午9:29
 * To change this template use File | Settings | File Templates.
 */
public class TreeNode implements Serializable {
    int i;             //4
    TreeNode parent;    //4
    TreeNode left;      //4
    TreeNode right;     //4

    boolean b = false;

    public String getName()
    {
        return i + "";
    }

    public TreeNode(int i, TreeNode parent) {
        this.i = i;
        this.parent = parent;
    }
}

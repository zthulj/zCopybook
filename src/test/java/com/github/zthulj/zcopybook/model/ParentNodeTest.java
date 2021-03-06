package com.github.zthulj.zcopybook.model;

import com.github.zthulj.zcopybook.factory.NodeFactory;
import org.junit.Assert;
import org.junit.Test;

public class ParentNodeTest {

    @Test
    public void addParentNode_shouldAddAParentNodeToChilds(){
        ParentNode<String> root = NodeFactory.createRootNode();
        root.addChild(NodeFactory.createParentNode(root,0),"parentName");
        Assert.assertEquals(true,root.getChilds().get("parentName").isParent());
    }

    @Test
    public void addValueNode_shouldAddAValueNodeToChilds(){
        ParentNode<String> root = NodeFactory.createRootNode();
        root.addChild(NodeFactory.createValueNode(root,  Coordinates.create(0,1)),"child");
        Assert.assertEquals(false,root.getChilds().get("child").isParent());
    }

    @Test
    public void isParent_rootNode_shouldReturnTrue(){
        ParentNode<String> root = NodeFactory.createRootNode();
        Node<String> parent = NodeFactory.createParentNode(root, 0);
        Assert.assertEquals(true, parent.isParent());
    }

    @Test
    public void isParent_parentNode_shouldReturnTrue(){
        Node<String> root = NodeFactory.createRootNode();
        Assert.assertEquals(true, root.isParent());
    }
}

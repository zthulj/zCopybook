package com.zthulj.zcopybook.converter;

import com.zthulj.zcopybook.factory.NodeFactory;
import com.zthulj.zcopybook.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ZLoaderTest {

    ZLoader converter = new ZLoader();

    @Test(expected = IllegalArgumentException.class)
    public void convert_nullString_shouldThrowIllegalArgExc() {
        String s = null;
        converter.load(s);
    }

    @Test(expected = IllegalArgumentException.class)
    public void convert_nullFile_shouldThrowIllegalArgExc() throws IOException {
        File f = null;
        converter.load(f);
    }

    @Test
    public void convert_emptyString_shouldReturnEmptyModel() {
        Node node = converter.load("");
        Assert.assertEquals(NodeFactory.createRootNode(), node);
    }

    @Test
    public void convert_emptyFile_shouldReturnEmptyModel() throws IOException {
        Node node = converter.load(fileFromResource("copybook/empty.cbl"));
        Assert.assertEquals(NodeFactory.createRootNode(), node);
    }

    @Test
    public void convert_aSingleParent_shouldReturnASingleParentModel() throws IOException {
        Node node = converter.load(fileFromResource("copybook/singleParent.cbl"));

        ParentNode expected = NodeFactory.createRootNode();
        expected.addChild(NodeFactory.createParentNode(expected, 1), "CLIENT");

        Assert.assertEquals(expected, node);
    }

    @Test
    public void convert_aSingleParentWithComments_shouldReturnASingleParentModel() throws IOException {
        Node node = converter.load(fileFromResource("copybook/singleParentWithComments.cbl"));

        ParentNode expected = NodeFactory.createRootNode();
        expected.addChild(NodeFactory.createParentNode(expected, 1), "CLIENT");

        Assert.assertEquals(expected, node);
    }


    @Test
    public void convert_SingleParentOneChildValue_shouldReturnASingleParentModelWithAChild() throws IOException {
        Node node = converter.load(fileFromResource("copybook/singleParentOneChildValue.cbl"));

        ParentNode expected = NodeFactory.createRootNode();
        ParentNode parent = NodeFactory.createParentNode(expected, 1);
        expected.addChild(parent, "CLIENT");
        parent.addChild(NodeFactory.createValueNode(parent, Coordinates.create(0, 17)), "CLIENT-NAME");

        Assert.assertEquals(expected, node);
    }

    @Test
    public void convert_ManyParentManyChilds_shouldReturnAManyParentWithManyChildModel() throws IOException {
        Node node = converter.load(fileFromResource("copybook/manyParentManyChilds.cbl"));

        ParentNode rootExpected = NodeFactory.createRootNode();
        ParentNode firstParent = NodeFactory.createParentNode(rootExpected, 1);

        rootExpected.addChild(firstParent, "CLIENT");

        ParentNode commonParent = NodeFactory.createParentNode(firstParent, 3);
        firstParent.addChild(commonParent, "CLIENT-COMMON-INFOS");


        commonParent.addChild(NodeFactory.createValueNode(commonParent, Coordinates.create(0, 17)), "FIRSTNAME");
        commonParent.addChild(NodeFactory.createValueNode(commonParent, Coordinates.create(18, 29)), "LASTNAME");

        ParentNode advancedParent = NodeFactory.createParentNode(firstParent, 3);
        firstParent.addChild(advancedParent, "CLIENT-ADVANCED-INFOS");
        advancedParent.addChild(NodeFactory.createValueNode(advancedParent, Coordinates.create(30, 31)), "GENDER");
        advancedParent.addChild(NodeFactory.createValueNode(advancedParent, Coordinates.create(32, 34)), "AGE");

        Assert.assertEquals(rootExpected, node);

    }

    @Test
    public void convert_ParentChildsWith88Level_ShouldIgnore88Level() throws IOException {
        Node node = converter.load(fileFromResource("copybook/parentManyChildWith88Level.cbl"));
        ParentNode rootExpected = NodeFactory.createRootNode();
        ParentNode firstParent = NodeFactory.createParentNode(rootExpected, 1);

        rootExpected.addChild(firstParent, "CLIENT");

        ParentNode commonParent =NodeFactory.createParentNode(firstParent,3);
        firstParent.addChild(commonParent,"CLIENT-COMMON-INFOS");

        commonParent.addChild(NodeFactory.createValueNode(firstParent,Coordinates.create(0, 17)),"FIRSTNAME");
        commonParent.addChild(NodeFactory.createValueNode(firstParent,Coordinates.create(18, 29)),"LASTNAME");
        commonParent.addChild(NodeFactory.createValueNode(firstParent,Coordinates.create(30, 30)),"GENDER");

        Assert.assertEquals(rootExpected, node);

    }

    @Test
    public void convert_ParentWithOccurs_shouldReturnModelWithOccurs() throws IOException {
        Node node = converter.load(fileFromResource("copybook/oneParentWithOccurs.cbl"));
        ParentNode rootExpected = NodeFactory.createRootNode();
        ParentNode firstParent = NodeFactory.createParentNode(rootExpected, 1);

        rootExpected.addChild(firstParent, "CLIENT");
        ParentArrayNode parentArray = NodeFactory.createParentNodeArray(firstParent,3,3);
        firstParent.addChild(parentArray,"CLIENT-COMMON-INFOS");

        parentArray.addChild(NodeFactory.createValueNode(parentArray,Coordinates.create(0, 17)),"FIRSTNAME");
        parentArray.addChild(NodeFactory.createValueNode(parentArray,Coordinates.create(18, 29)),"LASTNAME");
        parentArray.duplicateOccurs(30);
        Assert.assertEquals(node, rootExpected);
    }

    @Test
    public void convert_NodeWithAParentAndSomeValues_shouldReturnModelOK() throws IOException {
        Node node = converter.load(fileFromResource("copybook/aParentAndSomeChildsAtSameLevel.cbl"));

        ParentNode rootExpected = NodeFactory.createRootNode();
        ParentNode firstParent = NodeFactory.createParentNode(rootExpected, 1);

        rootExpected.addChild(firstParent, "CLIENT");
        ParentNode secondParent = NodeFactory.createParentNode(firstParent,3);
                firstParent.addChild(secondParent,"CLIENT-COMMON-INFOS");

        secondParent.addChild(NodeFactory.createValueNode(secondParent, Coordinates.create(0, 17)),"FIRSTNAME");
        secondParent.addChild(NodeFactory.createValueNode(secondParent, Coordinates.create(18, 29)), "LASTNAME");
        firstParent.addChild(NodeFactory.createValueNode(firstParent,Coordinates.create(30, 47)),"SOMETHING");
        firstParent.addChild(NodeFactory.createValueNode(firstParent,Coordinates.create(48, 65)),"ELSE");

        Assert.assertEquals(rootExpected, node);


    }

    @Test
    public void convert_NodeWithAParentArrayAndSomeValuesAtSameLevel_shouldReturnModelOK() throws IOException {
        Node node = converter.load(fileFromResource("copybook/aParentOccursAndSomeChildsAtSameLevel.cbl"));

        ParentNode rootExpected = NodeFactory.createRootNode();
        ParentNode firstParent = NodeFactory.createParentNode(rootExpected, 1);

        rootExpected.addChild(firstParent, "CLIENT");
        ParentArrayNode secondParent = NodeFactory.createParentNodeArray(firstParent,3,2);
        firstParent.addChild(secondParent,"CLIENT-COMMON-INFOS");

        secondParent.addChild(NodeFactory.createValueNode(secondParent,Coordinates.create(0, 17)),"FIRSTNAME");
        secondParent.addChild(NodeFactory.createValueNode(secondParent, Coordinates.create(18, 29)), "LASTNAME");

        secondParent.duplicateOccurs(30);

        firstParent.addChild(NodeFactory.createValueNode(firstParent,Coordinates.create(60, 77)),"SOMETHING");
        firstParent.addChild(NodeFactory.createValueNode(firstParent, Coordinates.create(78, 95)),"ELSE");

        Assert.assertEquals(rootExpected, node);
    }

    @Test
    public void convert_NodeWithRedefineField_ShouldIgnoreRedefine() throws IOException {
        Node node = converter.load(fileFromResource("copybook/aNodeWithARedefine.cbl"));

        ParentNode rootExpected = NodeFactory.createRootNode();
        ParentNode firstParent = NodeFactory.createParentNode(rootExpected, 1);

        rootExpected.addChild(firstParent, "CLIENT");
        ParentNode secondParent = NodeFactory.createParentNode(firstParent,3);
        firstParent.addChild(secondParent,"CLIENT-COMMON-INFOS");

        secondParent.addChild(NodeFactory.createValueNode(secondParent,Coordinates.create(0, 17)),"FIRSTNAME");
        secondParent.addChild(NodeFactory.createValueNode(secondParent,Coordinates.create(18, 29)),"LASTNAME");
        firstParent.addChild(NodeFactory.createValueNode(firstParent, Coordinates.create(30, 47)),"SOMETHING");
        firstParent.addChild(NodeFactory.createValueNode(firstParent,Coordinates.create(48, 65)),"ELSE" );

        Assert.assertEquals(rootExpected, node);

    }

    @Test
    public void convert_NodeWithRedefineStruct_ShouldIgnoreRedefine() throws IOException {
        Node node = converter.load(fileFromResource("copybook/aNodeWithARedefineStruct.cbl"));

        ParentNode rootExpected = NodeFactory.createRootNode();
        ParentNode firstParent = NodeFactory.createParentNode(rootExpected, 1);

        rootExpected.addChild(firstParent, "CLIENT");
        ParentNode secondParent = NodeFactory.createParentNode(firstParent,3);
        firstParent.addChild(secondParent,"CLIENT-COMMON-INFOS");

        secondParent.addChild(NodeFactory.createValueNode(secondParent,Coordinates.create(0, 17)),"FIRSTNAME");
        secondParent.addChild(NodeFactory.createValueNode(secondParent,Coordinates.create(18, 29)),"LASTNAME");
        firstParent.addChild(NodeFactory.createValueNode(firstParent, Coordinates.create(30, 47)),"SOMETHING");
        firstParent.addChild(NodeFactory.createValueNode(firstParent,Coordinates.create(48, 65)),"ELSE" );

        Assert.assertEquals(rootExpected, node);
    }

    @Test
    public void convert_NodeWithSignedValue_ShouldDetectSigned() throws IOException {
        Node node = converter.load(fileFromResource("copybook/simplecopybook.cbl"));

        ParentNode parent = (ParentNode) ((ParentNode) node).getChilds().get("CLIENT");
        ValueNode value = (ValueNode) parent.getChilds().get("SIGNEDINT");

        Assert.assertEquals(ValueNode.ValueType.SIGNED_INT, value.getValueType());

    }

    @Test
    public void convert_NodeWithSignedFloatValue_ShouldDetectSigned() throws IOException {
        Node node = converter.load(fileFromResource("copybook/simplecopybook.cbl"));

        ParentNode parent = (ParentNode) ((ParentNode) node).getChilds().get("CLIENT");
        ValueNode value = (ValueNode) parent.getChilds().get("SIGNEDFLOAT");

        Assert.assertEquals(ValueNode.ValueType.SIGNED_FLOAT, value.getValueType());

    }

    @Test
    public void convert_NodeWithPicX_ShouldDetectDefault() throws IOException {
        Node node = converter.load(fileFromResource("copybook/simplecopybook.cbl"));

        ParentNode parent = (ParentNode) ((ParentNode) node).getChilds().get("CLIENT");
        ValueNode value = (ValueNode) parent.getChilds().get("PIC-X");

        Assert.assertEquals(ValueNode.ValueType.STRING, value.getValueType());
    }

    @Test
    public void convert_duplicateKey_ShouldRenameKeys() throws IOException {
        Node node = converter.load(fileFromResource("copybook/simplecopybook.cbl"));

        ParentNode parent = (ParentNode) ((ParentNode) node).getChilds().get("CLIENT");
        ValueNode value = (ValueNode) parent.getChilds().get("DUPLICATE2");
        ValueNode value2 = (ValueNode) parent.getChilds().get("DUPLICATE3");

        Assert.assertNotNull(value);
        Assert.assertNotNull(value2);

    }


    private File fileFromResource(String path) {
        return new File(getClass().getClassLoader().getResource(path).getFile());
    }
}

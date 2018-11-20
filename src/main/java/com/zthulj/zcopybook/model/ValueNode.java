package com.zthulj.zcopybook.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zthulj.zcopybook.factory.NodeFactory;
import com.zthulj.zcopybook.serializer.ValueNodeSerializer;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JsonSerialize(using = ValueNodeSerializer.class)
@Getter
@EqualsAndHashCode(callSuper=true)
@ToString
public final class ValueNode<T> extends Node<T> {
    
	private static final long serialVersionUID = -3833993476849963456L;
	
	@Setter
	private T value;
    private Coordinates coordinates;
    private ValueType valueType;

    public static enum ValueType{
        STRING, SIGNED_INT, SIGNED_FLOAT
    }

    public ValueNode(ParentNode<T> parent,Coordinates coordinates, ValueType valueType){
        super(parent, false);
        this.coordinates = coordinates;
        this.valueType = valueType;
    }

    @Override
    public int copyInto(ParentNode<T> destination, int cursorPosition, String name) {
        Coordinates nextCoords = calculateCoordinates(this, cursorPosition);
        ValueNode<T> valueNode = NodeFactory.createValueNode(destination,nextCoords,this.valueType);
        destination.addChild(valueNode,name);
        cursorPosition += nextCoords.getSize();
        return cursorPosition;
    }

    private Coordinates calculateCoordinates(ValueNode<T> value, int nextStart) {
        return Coordinates.create(nextStart, nextStart + value.getCoordinates().getSize() - 1);
    }
}

package com.github.zthulj.zcopybook.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zthulj.zcopybook.model.Coordinates;
import com.github.zthulj.zcopybook.model.ValueNode;
import com.github.zthulj.zcopybook.model.ZCopyBook;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class ZConverter {
    private final ZCopyBook<String> copyBook;
    private ObjectMapper mapperJson = new ObjectMapper();

    /**
     * Convert a positional line to a json
     *
     * @param positionalLine the line to convert
     * @return the converted json
     * @throws JsonProcessingException
     */
    public String convertToJson(final String positionalLine) throws JsonProcessingException {

        if(null == positionalLine)
            throw new IllegalArgumentException("positionalLine can't be null");

        if(positionalLine.length() != copyBook.getWaitedLength())
            throw new IllegalArgumentException("Waited length : " + copyBook.getWaitedLength() + ", actual positionnalLine length : "+ positionalLine.length());

        for(ValueNode<String> valueNode : copyBook.getValueNodes()){
            Coordinates c = valueNode.getCoordinates();
            valueNode.setValue(positionalLine.substring(c.getStart(), c.getEnd()));
        }
        return mapperJson.writeValueAsString(copyBook.getRootNode());
    }
}

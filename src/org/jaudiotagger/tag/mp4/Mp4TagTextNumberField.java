/*
 * Entagged Audio Tag library
 * Copyright (c) 2003-2005 Rapha�l Slinckx <raphael@slinckx.net>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *  
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jaudiotagger.tag.mp4;

import java.io.UnsupportedEncodingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.nio.ByteBuffer;

import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.audio.mp4.util.Mp4BoxHeader;
import org.jaudiotagger.tag.mp4.Mp4TagTextField;

/**
 * Represents simple text field that contains an array of number,
 *
 * But reads the data content as an arry of 16 bit unsigned numbers
 */
public class Mp4TagTextNumberField extends Mp4TagTextField
{
    public static final int NUMBER_LENGTH = 2;

    //Holds the numbers decoded
    protected List <Short> numbers;

    public Mp4TagTextNumberField(String id, String n)
    {
        super(id, n);
    }

    public Mp4TagTextNumberField(String id, ByteBuffer data) throws UnsupportedEncodingException
    {
        super(id, data);
    }

    /**
     * Recreate the raw data content from the list of numbers
     *
     * @return
     */
    protected byte[] getDataBytes()
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for(Short number:numbers)
        {
            try
            {
                baos.write(Utils.getShortSizeBigEndian(number));
            }
            catch(IOException e)
            {
                //This should never happen because we are not writing to file at this point.
                throw new RuntimeException(e);
            }
        }
        return baos.toByteArray();
    }

    /**
     *
     * @return type numeric
     */
    protected Mp4FieldType getFieldType()
    {
        return Mp4FieldType.NUMERIC;
    }

    protected void build(ByteBuffer data) throws UnsupportedEncodingException
    {
        //Data actually contains a 'Data' Box so process data using this
        Mp4BoxHeader header  = new Mp4BoxHeader(data);
        Mp4DataBox   databox = new Mp4DataBox(header,data);
        dataSize = header.getDataLength();
        content  = databox.getContent();
        numbers  = databox.getNumbers();
    }

    /**
     *
     * @return the individual numbers making up this field
     */
    public List<Short> getNumbers()
    {
        return numbers;
    }
}

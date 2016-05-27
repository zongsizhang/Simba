/*
 *  Copyright 2016 by Simba Project                                   
 *                                                                            
 *  Licensed under the Apache License, Version 2.0 (the "License");           
 *  you may not use this file except in compliance with the License.          
 *  You may obtain a copy of the License at                                   
 *                                                                            
 *    http://www.apache.org/licenses/LICENSE-2.0                              
 *                                                                            
 *  Unless required by applicable law or agreed to in writing, software       
 *  distributed under the License is distributed on an "AS IS" BASIS,         
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
 *  See the License for the specific language governing permissions and       
 *  limitations under the License.                                            
 */

package org.apache.spark.sql.catalyst.expressions;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.apache.spark.sql.spatial.*;

import java.io.*;

/**
 * Created by dong on 3/24/16.
 */
public class KryoShapeSerializer {
    static private Kryo kryo = new Kryo();

    static {
        kryo.register(Shape.class);
        kryo.register(Point.class);
        kryo.register(MBR.class);
        kryo.register(Polygon.class);
        kryo.register(Circle.class);
    }

    //TODO Find all kryo classes needed to be registered, change the code to a kryo serilized version
    public static Shape deserialize(byte[] data) {
        if (data == null) return null;
        try{
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInput in = new ObjectInputStream(bis);
            Object o = in.readObject();
            return (Shape) o;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
//        ByteArrayInputStream in = new ByteArrayInputStream(data);
//        Input input = new Input(in);
//        return kryo.readObject(input, Shape.class);
    }

    public static byte[] serialize(Shape o) {
        try{
            ByteOutputStream bos = new ByteOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(o);
            return bos.toByteArray();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
        return null;
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        Output output = new Output(out);
//        kryo.writeObject(output, o);
//        output.flush();
//        return out.toByteArray();
    }
}

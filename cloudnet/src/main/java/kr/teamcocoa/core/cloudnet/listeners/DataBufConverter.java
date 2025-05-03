package kr.teamcocoa.core.cloudnet.listeners;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import eu.cloudnetservice.driver.network.buffer.DataBuf;
import eu.cloudnetservice.driver.network.buffer.DataBufFactory;

public class DataBufConverter {
    
    public static DataBuf objectToDataBuf(Object object) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(stream);
            output.writeObject(object);

            return DataBufFactory.defaultFactory().fromBytes(stream.toByteArray());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object byteArrayToObject(byte[] bytes) {
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            ObjectInputStream input = new ObjectInputStream(stream);
            return input.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




}

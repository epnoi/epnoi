package org.epnoi.model;

import lombok.Data;

import java.io.*;

@Data
public class Event {

    private byte[] bytes;

	private Event(byte[] bytes){
        this.bytes = bytes;
    }

    public String toString(){
        try {
            return new String(bytes,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            return new String(bytes);
        }
    }

    public static <T> Event from( T resource ){
        try {
            if (resource instanceof String){
                return new Event(((String) resource).getBytes());
            } else if (resource instanceof byte[]){
                return new Event((byte[])resource);
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(resource);
            return new Event(bos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T to(Class<T> classType){
        try {
            if (classType.equals(String.class)){
               return (T) toString();
            }
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInput in = new ObjectInputStream(bis);
            Object data = in.readObject();
            return (T) data;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

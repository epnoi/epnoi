package org.epnoi.model;

import java.io.*;
import java.net.URI;

public class Event {

    private byte[] bytes;

	private Event() {
	}

    public Event setBytes(byte[] bytes){
        this.bytes = bytes;
        return this;
    }

    public String toString(){
        try {
            return new String(bytes,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            return new String(bytes);
        }
    }

    public byte[] toBytes(){
        return bytes;
    }

    public URI toURI(){
        return (URI) toObject();
    }

    public Object toObject(){
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
           throw new RuntimeException(e);
        }
    }

    public static class Builder{

        private final Event event;

        public Builder(){
            this.event = new Event();
        }

        public Event fromBytes(byte[] bytes){
            return event.setBytes(bytes);
        }

        public Event fromString(String text){
            try {
                return this.event.setBytes(text.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                return this.event.setBytes(text.getBytes());
            }
        }

        public Event fromURI(URI uri){
            return fromObject(uri);
        }


        public Event fromObject(Object uri){
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                ObjectOutput out = new ObjectOutputStream(bos);
                out.writeObject(uri);
                return this.event.setBytes(bos.toByteArray());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }


}

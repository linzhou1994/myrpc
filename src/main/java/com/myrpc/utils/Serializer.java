package com.myrpc.utils;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @作者 bg389315
 * @时间 2019/9/18
 * @描述
 */
public class Serializer {
    public static byte[] serialize(Object obj) throws IOException {
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(obj);
            }
            return b.toByteArray();
        }
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try(ByteArrayInputStream b = new ByteArrayInputStream(bytes)){
            try(ObjectInputStream o = new ObjectInputStream(b)){
                return o.readObject();
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        List<String> list = new ArrayList<>();
        list.add("123");
        list.add("1234");
        list.add("1235");
        list.add("1236");
        byte[] bytes = serialize(list);
        System.out.println(bytes);
        System.out.println(deserialize(bytes).toString());
    }
}

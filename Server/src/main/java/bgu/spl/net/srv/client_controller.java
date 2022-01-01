package bgu.spl.net.srv;


import bgu.spl.net.api.Connections;
import bgu.spl.net.message.Message;
import com.sun.tools.javac.util.Pair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class client_controller<T extends Serializable> implements Connections<String> {

    private int connection_ID;
   private Map<String, Pair<Integer,String>> clients_map;//username and pair of <ID,password>
    private Map<Integer, ConnectionHandler<String>> connectionHandlerList;//username and his specefic connection handler

    public client_controller()
    {
        connection_ID=1;
        this.clients_map=new HashMap<String, Pair<Integer,String>>();
        this.connectionHandlerList=new HashMap<Integer,ConnectionHandler<String>>();
    }



    @Override
    public boolean send(int connId, String  msg) {
        this.connectionHandlerList.get(connId).send(msg);

        return false;
    }

    @Override
    public void broadcast(String msg) {

    }

    @Override
    public void disconnect(int connId) {

    }

    public void pre_procces(T msg,ConnectionHandler<String> ch)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(msg);
            out.flush();
            byte[] msg_in_bytes = bos.toByteArray();
            this.dirty_work(msg_in_bytes,ch);
            bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }





        }

    private void dirty_work(byte[] msg_in_bytes,ConnectionHandler<String> ch)
    {

        switch (Byte.toString(msg_in_bytes[0]))
        {
            case ("00000001"):// case of register
            {   int current_byte=1;
                int username_length=0;
                int password_length=0;
                int birthday_length=0;
                while(Byte.toString(msg_in_bytes[current_byte])!="00000000")// while we havent reached the delimeter 0
                {
                    current_byte++;
                    username_length++;

                }
                current_byte++;
                while(Byte.toString(msg_in_bytes[current_byte])!="00000000")// while we havent reached the delimeter 0
                {
                    current_byte++;
                    password_length++;

                }
                current_byte++;
                while(Byte.toString(msg_in_bytes[current_byte])!="00000000")// while we havent reached the delimeter 0
                {
                    current_byte++;
                    birthday_length++;
                }

                //we have to know the size of every string , i did byte[] and not list because there is tostring method to byte[]

                byte[] username_in_bytes=new byte[username_length];
                byte[] password_in_bytes=new byte[password_length];
                byte[] birthday_in_bytes=new byte[birthday_length];
                current_byte=1;


                //now we loop again to save values to a mathcing array
                while(Byte.toString(msg_in_bytes[current_byte])!="00000000")// while we havent reached the delimeter 0
                {
                    username_in_bytes[current_byte]=msg_in_bytes[current_byte];
                    current_byte++;
                }
                current_byte++;

                while(Byte.toString(msg_in_bytes[current_byte])!="00000000")// while we havent reached the delimeter 0
                {
                    password_in_bytes[current_byte]=msg_in_bytes[current_byte];
                    current_byte++;
                }
                current_byte++;

                while(Byte.toString(msg_in_bytes[current_byte])!="00000000")// while we havent reached the delimeter 0
                {
                    birthday_in_bytes[current_byte]=msg_in_bytes[current_byte];
                    current_byte++;
                }

                String username = new String(username_in_bytes, StandardCharsets.UTF_8);
                String password = new String(password_in_bytes, StandardCharsets.UTF_8);
                String birthday = new String(birthday_in_bytes, StandardCharsets.UTF_8);

                if(!this.clients_map.containsKey(username))
                {
                    Pair<Integer,String> pair=new Pair<>(connection_ID,password);
                    this.clients_map.put(username,pair);
                 this.connectionHandlerList.put(connection_ID,ch);
                 connection_ID++;
                    this.send(connection_ID--,"username "+username+" created");// we need to implement this shit somewhere

                }
                else
                {

                    this.send(this.clients_map.get(username).fst,"another user with same name already exists");// we need to implement this shit somewhere
                }
                }

            }
        }



    }








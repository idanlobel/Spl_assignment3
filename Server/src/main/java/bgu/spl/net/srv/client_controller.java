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
import java.util.concurrent.ConcurrentHashMap;

public class client_controller<T extends Serializable> implements Connections<String> {

    private int connection_ID;
    private ConcurrentHashMap<User, Integer> dic_of_users;


    private Map<String, Pair<Integer, String>> clients_map;//username and pair of <ID,password>
    private ConcurrentHashMap<Integer, ConnectionHandler<String>> connectionHandlerList;//username and his specefic connection handler
    private List<String> logged_in_clients;//list of usernames that are logged in
    private Map<String, List<String>> list_of_followers;

    public client_controller() {
        connection_ID = 1;
        this.dic_of_users = new ConcurrentHashMap<>();
        this.clients_map = new HashMap<String, Pair<Integer, String>>();
        this.connectionHandlerList = new ConcurrentHashMap<Integer, ConnectionHandler<String>>();
        logged_in_clients = new ArrayList<>();
        this.list_of_followers = new HashMap<String, List<String>>();

    }


    @Override
    public boolean send(int connId, String msg) {
        this.connectionHandlerList.get(connId).send(msg);

        return false;
    }

    @Override
    public void broadcast(String msg) {

    }

    @Override
    public void disconnect(int connId) {

    }

    public void pre_procces(T msg, ConnectionHandler<String> ch) {
        // this shit converts generics type (T) to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(msg);
            out.flush();
            byte[] msg_in_bytes = bos.toByteArray();
            this.dirty_work(msg_in_bytes, ch);
            bos.close();

            } catch (IOException ex) {
                // ignore close exception
            }
    }
    }

    private void dirty_work(byte[] msg_in_bytes, ConnectionHandler<String> ch) {
        //this first byte is 00000000
        switch (Byte.toString(msg_in_bytes[1])) {
            case ("00000001"):// case of register
            {
                int current_byte = 2;
                int username_length = 0;
                int password_length = 0;
                int birthday_length = 0;
                while (Byte.toString(msg_in_bytes[current_byte]) != "00000000")// while we havent reached the delimeter 0
                {
                    current_byte++;
                    username_length++;

                }
                current_byte++;
                while (Byte.toString(msg_in_bytes[current_byte]) != "00000000")// while we havent reached the delimeter 0
                {
                    current_byte++;
                    password_length++;

                }
                current_byte++;
                while (Byte.toString(msg_in_bytes[current_byte]) != "00000000")// while we havent reached the delimeter 0
                {
                    current_byte++;
                    birthday_length++;
                }

                //we have to know the size of every string , i did byte[] and not list because there is tostring method to byte[]

                byte[] username_in_bytes = new byte[username_length];
                int usernameindex = 0;
                byte[] password_in_bytes = new byte[password_length];
                int passwordindex = 0;
                byte[] birthday_in_bytes = new byte[birthday_length];
                int birthdayindex = 0;
                current_byte = 2;


                //now we loop again to save values to a mathcing array
                while (Byte.toString(msg_in_bytes[current_byte]) != "00000000")// while we havent reached the delimeter 0
                {
                    username_in_bytes[usernameindex] = msg_in_bytes[current_byte];
                    usernameindex++;
                    current_byte++;
                }
                current_byte++;

                while (Byte.toString(msg_in_bytes[current_byte]) != "00000000")// while we havent reached the delimeter 0
                {
                    password_in_bytes[passwordindex] = msg_in_bytes[current_byte];
                    passwordindex++;
                    current_byte++;
                }
                current_byte++;

                while (Byte.toString(msg_in_bytes[current_byte]) != "00000000")// while we havent reached the delimeter 0
                {
                    birthday_in_bytes[birthdayindex] = msg_in_bytes[current_byte];
                    birthdayindex++;
                    current_byte++;
                }

                String username = new String(username_in_bytes, StandardCharsets.UTF_8);
                String password = new String(password_in_bytes, StandardCharsets.UTF_8);
                String birthday = new String(birthday_in_bytes, StandardCharsets.UTF_8);

                User new_user = new User(username, password, birthday);

                if (!another_user_with_same_name(username)) {
                    this.dic_of_users.put(new_user, connection_ID);
                    this.connectionHandlerList.put(connection_ID, ch);
                    connection_ID++;
                    this.send(connection_ID--, "username " + username + " created");// we need to implement this shit somewhere
                } else {
                    // not sure which connection, i did using the last user connnection hander

                    this.send(getConnection_ID(ch), "another user with same name already exists");// we need to implement this shit somewhere
                }


            }
            case ("00000010")://login
            {// have no clue what is captcha
                int current_byte = 2;
                int username_length = 0;
                int password_length = 0;
                while (Byte.toString(msg_in_bytes[current_byte]) != "00000000")// while we havent reached the delimeter 0
                {
                    current_byte++;
                    username_length++;
                }
                current_byte++;
                while (Byte.toString(msg_in_bytes[current_byte]) != "00000000")// while we havent reached the delimeter 0
                {
                    current_byte++;
                    password_length++;

                }
                current_byte++;


                byte[] username_in_bytes = new byte[username_length];
                int usernameindex = 0;
                byte[] password_in_bytes = new byte[password_length];
                int passwordindex = 0;
                current_byte = 2;

                //now we loop again to save values to a mathcing array
                while (Byte.toString(msg_in_bytes[current_byte]) != "00000000")// while we havent reached the delimeter 0
                {
                    username_in_bytes[usernameindex] = msg_in_bytes[current_byte];
                    usernameindex++;
                    current_byte++;
                }
                current_byte++;

                while (Byte.toString(msg_in_bytes[current_byte]) != "00000000")// while we havent reached the delimeter 0
                {
                    password_in_bytes[passwordindex] = msg_in_bytes[current_byte];
                    passwordindex++;
                    current_byte++;
                }
                current_byte++;


                String username = new String(username_in_bytes, StandardCharsets.UTF_8);
                String password = new String(password_in_bytes, StandardCharsets.UTF_8);


                Integer current_ch_ID = 0;
                for (Integer key : this.connectionHandlerList.keySet()) {// get the id of current ch
                    if (this.connectionHandlerList.get(key).equals(ch)) {
                        current_ch_ID = key;
                        break;
                    }

                }
                if (!this.logged_in_clients.contains(username)) {
                    if (this.clients_map.containsKey(username)) {
                        this.logged_in_clients.add(username);
                        this.send(current_ch_ID, username + " successfully logged in");
                    } else {
                        this.send(current_ch_ID, "Error!! " + username + " doesn't  exists");
                    }


                    this.connectionHandlerList.put(connection_ID, ch);
                    connection_ID++;


                } else {

                    this.send(current_ch_ID, "Error!! " + username + " is already logged in");
                }

            }
            case ("00000011")://logout
            {


                Integer current_ch_ID = 0;
                for (Integer key : this.connectionHandlerList.keySet()) {// get the id of current ch
                    if (this.connectionHandlerList.get(key).equals(ch)) {
                        current_ch_ID = key;
                        break;
                    }

                }
                String username = "";
                for (String user : this.clients_map.keySet()) {
                    if (this.clients_map.get(user).fst == current_ch_ID) {
                        username = user;
                        break;
                    }
                }

                if (this.logged_in_clients.contains(username)) {
                    this.logged_in_clients.remove(username);
                    this.send(current_ch_ID, username + " logged out successfully");

                } else {
                    this.send(current_ch_ID, "Error!! " + username + " is not logged in");
                }

            }
            case ("00000100")://follow
            {
                Integer current_ch_ID = 0;
                for (Integer key : this.connectionHandlerList.keySet()) {// get the id of current ch
                    if (this.connectionHandlerList.get(key).equals(ch)) {
                        current_ch_ID = key;
                        break;
                    }

                }
                String username = "";
                for (String user : this.clients_map.keySet()) {
                    if (this.clients_map.get(user).fst == current_ch_ID) {
                        username = user;
                        break;
                    }
                }

                if (check_logged_in(username)) {


                    int current_byte = 2;
                    int follow_him = 0;
                    while (Byte.toString(msg_in_bytes[current_byte]) != "00000000")// while we havent reached the delimeter 0
                    {
                        current_byte++;
                        follow_him++;
                    }
                    current_byte++;


                    byte[] follow_him_in_bytes = new byte[follow_him];
                    int follow_him_index = 0;

                    current_byte = 2;

                    //now we loop again to save values to a mathcing array
                    while (Byte.toString(msg_in_bytes[current_byte]) != "00000000")// while we havent reached the delimeter 0
                    {
                        follow_him_in_bytes[follow_him_index] = msg_in_bytes[current_byte];
                        follow_him_index++;
                        current_byte++;
                    }


                    String follow_him_string = new String(follow_him_in_bytes, StandardCharsets.UTF_8);

                    if (this.list_of_followers.get(username).contains(follow_him_string)) {
                        this.list_of_followers.get(username).remove(follow_him_string);
                        this.send(current_ch_ID, "successfully unfollowed :" + follow_him_string);
                    } else {
                        this.list_of_followers.get(username).add(follow_him_string);
                        this.send(current_ch_ID, "successfully followed :" + follow_him_string);
                    }

                } else {
                    this.send(current_ch_ID, "Error!! " + username + " is not logged in");
                }


            }


        }
    }

    private boolean check_logged_in(String username) {
        return this.logged_in_clients.contains(username);
    }

    private boolean another_user_with_same_name(String username) {
        for (User user : this.dic_of_users.keySet()) {
            if (user.getUsername().equals(username)) {
                return true;
            }

        }
        return false;
    }

    private int getConnection_ID(ConnectionHandler<String> ch) {

        for (Integer id : this.connectionHandlerList.keySet()) {
            if (this.connectionHandlerList.get(id).equals(ch)) {
                return id;
            }

        }
        return this.connection_ID;
    }



    }








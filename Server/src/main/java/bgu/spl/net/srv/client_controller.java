package bgu.spl.net.srv;


import bgu.spl.net.api.Connections;
import bgu.spl.net.message.Message;
import com.sun.tools.javac.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class client_controller<T> implements Connections<T> {

   private Map<Integer, Pair<String,String>> clients_map;//id and pair of <username,password>
    private List<ConnectionHandler<T>> connectionHandlerList;
    public client_controller()
    {
        this.clients_map=new HashMap<Integer, Pair<String,String>>();
        this.connectionHandlerList=new ArrayList<ConnectionHandler<T>>();
    }



    @Override
    public boolean send(int connId, T msg) {
        return false;
    }

    @Override
    public void broadcast(T msg) {

    }

    @Override
    public void disconnect(int connId) {

    }



}

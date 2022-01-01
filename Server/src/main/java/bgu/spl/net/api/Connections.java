package bgu.spl.net.api;

import java.io.Serializable;

public interface Connections<T extends Serializable>  {

    boolean send(int connId, T msg);

    void broadcast(T msg);


    void disconnect(int connId);

}

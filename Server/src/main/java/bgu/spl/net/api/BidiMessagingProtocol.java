package bgu.spl.net.api;

import java.io.Serializable;

public interface BidiMessagingProtocol<T> extends Serializable {

    void start(int connectionId, Connections connections);

    T process(T message);

    boolean shouldTerminate();

}

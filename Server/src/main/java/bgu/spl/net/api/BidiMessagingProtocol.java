package bgu.spl.net.api;

public interface BidiMessagingProtocol<T> {

    void start(int connectionId, Connections connections);

    T process(T message);

    boolean shouldTerminate();

}

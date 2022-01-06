package bgu.spl.net.srv;

import java.util.function.Supplier;

public class BaseServerImpl extends BaseServer{

    public BaseServerImpl(int port, Supplier protocolFactory, Supplier encdecFactory) {
        super(port, protocolFactory, encdecFactory);
    }

    @Override
    protected void execute(BlockingConnectionHandler handler) {
        new Thread(handler).start();
    }
}

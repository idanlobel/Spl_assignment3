package bgu.spl.net.impl.bguServer;

import bgu.spl.net.api.*;
import bgu.spl.net.srv.BaseServerImpl;
import bgu.spl.net.srv.Reactor;

public class TPCServer {
    Database db=Database.getInstance();
    ConnectionsImpl connections= ConnectionsImpl.getInstance();
    public static void main(String[] args){
        Database db=Database.getInstance();
        Connections connections= ConnectionsImpl.getInstance();
        int port;
        int numOfThreads;
        if (args.length==0){
            port = 7777;
            numOfThreads=5;

        }
        else {
            port = Integer.parseInt(args[0]);
        }
        BaseServerImpl server = new BaseServerImpl(port
                ,()-> {return new MessageProtocolImpl<>();}
                ,()-> {return new MessageEncoderDecoderImpl<>();});
        server.serve();
    }
}

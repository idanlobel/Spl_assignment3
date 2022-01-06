package bgu.spl.net.impl.bguServer;

import bgu.spl.net.api.*;
import bgu.spl.net.srv.Reactor;

public class ReactorServer {
    public static void Main(String[] args){
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
            numOfThreads = Integer.parseInt(args[1]);
        }
        Reactor server = new Reactor(numOfThreads
                ,port
                ,()-> {return new MessageProtocolImpl<>();}
                ,()-> {return new MessageEncoderDecoderImpl<>();});
        server.serve();
    }
}

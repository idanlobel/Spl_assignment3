package bgu.spl.net.api;

import bgu.spl.net.message.Message;

public class MessageProtocolImpl<T> implements BidiMessagingProtocol<Message> {
    private boolean shouldTerminate;
    private int connId;
    private ConnectionsImpl connections;
    private Database db;
    private Client client;



    public MessageProtocolImpl()
    {
        db=Database.getInstance();
        connections=ConnectionsImpl.getInstance();
        this.start(db.getConnId(),connections);
    }

    /**
     * Used to initiate the current client protocol with it's personal connection ID and the connections implementation
     **/
    @Override
    public void start(int connectionId, Connections connections) {

        this.connId = connectionId;
        this.db = Database.getInstance();
        this.connections = (ConnectionsImpl) connections;
        this.client=null;
        this.shouldTerminate=false;
    }

    @Override
    public Message process(Message message) {
        ClientMessage clientMessage = (ClientMessage)message;
        int op = clientMessage.getOp();
        switch (op){
            case 1:
                return register(clientMessage);
            case 2:
                return login(clientMessage);
            case 3:
                return logout(clientMessage);
            case 4:
                return db.follow(clientMessage, client);
            case 5:
                return db.post(clientMessage, client);
            case 6:
                return db.PM(clientMessage, client);
            case 7:
                //logstat - check if the response is null, otherwise it's an error
                return db.logstat(clientMessage, client);
            case 8:
                //stat - check if the response is null, otherwise it's an error
                return db.stat(clientMessage, client);
            case 12:
                return db.block(clientMessage, client);

        }
        return null;
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }

    public ServerResponse register (ClientMessage message) {
        ServerResponse response = db.register(message, connId);

        //Only after successful register, save the current client, and save the client in the hashmap in connections
        if (response.getFirstOP()==(short)10) {
            client = db.getUser(message.getUsername());
            connections.addIdAndUsername(connId,client.getUsername());
        }
        return response;
    }

    public ServerResponse login(ClientMessage message) {
        ServerResponse response = db.login(message);

        //If a client registered in the past, and now tries login without register, we need to save the client in the protocol
        if(client==null && response.getFirstOP()==(short)10)
            client = db.getUser(message.getUsername());

        //For successful login, need to save in the hashmap in connections
        if (response.getFirstOP()==(short)10)
            connections.addIdAndUsername(connId,client.getUsername());

        return response;
    }

    public ServerResponse logout (ClientMessage message) {
        ServerResponse response= db.logout(message, client.getUsername());
        if(response.getFirstOP()==10) {
            this.shouldTerminate=true;
            connections.disconnect(connId);
        }
        return response;
    }
}

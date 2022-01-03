package bgu.spl.net.message;

//maybe do abstract class of message and for every message kind (like register, login,follow/unfollow ... we will create special message that extends this abstract class.


public abstract class  Message {
    private short opcode;
    public Message(short opcode)
    {
        this.opcode=opcode;
    }

    public Message() {

    }
}

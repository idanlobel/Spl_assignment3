package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.io.*;
import java.nio.ByteBuffer;

public class BGS_Encoder_Decoder<T extends Serializable> implements MessageEncoderDecoder<T>  {

    private final ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
    private byte[] objectBytes = null;
    private int objectBytesIndex = 0;

    @Override
    public T decodeNextByte(byte nextByte) {
        if (objectBytes == null) { //indicates that we are still reading the length
            lengthBuffer.put(nextByte);
            if (!lengthBuffer.hasRemaining()) { //we read 4 bytes and therefore can take the length
                lengthBuffer.flip();
                objectBytes = new byte[lengthBuffer.getInt()];
                objectBytesIndex = 0;
                lengthBuffer.clear();
            }
        } else {
            objectBytes[objectBytesIndex] = nextByte;
            if (++objectBytesIndex == objectBytes.length) {
                T result = deserializeObject();
                objectBytes = null;
                return result;
            }
        }

        return null;
    }

    @Override
    public byte[] encode(Serializable message) {
        return serializeObject(message);
    }

    private T deserializeObject() {
        try {
            ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(objectBytes));
            return (T) in.readObject();
        } catch (Exception ex) {
            throw new IllegalArgumentException("cannot desrialize object", ex);
        }

    }

    private byte[] serializeObject(Serializable message) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();// defalut size is 32

            //placeholder for the object size
            for (int i = 0; i < 4; i++) {
                bytes.write(0);// i think every time we do encode we only encode 4 bytes of our object/message
            }

            ObjectOutput out = new ObjectOutputStream(bytes);
            out.writeObject(message);
            out.flush();
            byte[] result = bytes.toByteArray();

            //now write the object size
            ByteBuffer.wrap(result).putInt(result.length - 4);
            return result;

        } catch (Exception ex) {
            throw new IllegalArgumentException("cannot serialize object", ex);
        }
    }

}

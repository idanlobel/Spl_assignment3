/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.net.srv;

import bgu.spl.net.api.Connections;

import java.io.Closeable;
import java.io.Serializable;

/**
 * The ConnectionHandler interface for Message of type T
 */
public interface ConnectionHandler<T extends Serializable> extends Closeable ,Connections<T> {

     Void send(T msg);

}

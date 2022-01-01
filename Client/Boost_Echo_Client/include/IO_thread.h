//
// Created by bayan kassem on 01/01/2022.
//

#ifndef BOOST_ECHO_CLIENT_IO_THREAD_H
#define BOOST_ECHO_CLIENT_IO_THREAD_H


#include "connectionHandler.h"

class IO_thread {
private:
    ConnectionHandler c_handler;

public:
    IO_thread(ConnectionHandler ch);
    void Run();

};


#endif //BOOST_ECHO_CLIENT_IO_THREAD_H

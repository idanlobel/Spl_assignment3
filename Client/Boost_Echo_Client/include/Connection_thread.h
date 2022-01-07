//
// Created by bayan kassem on 01/01/2022.
//

#ifndef BOOST_ECHO_CLIENT_CONNECTION_THREAD_H
#define BOOST_ECHO_CLIENT_CONNECTION_THREAD_H


#include "connectionHandler.h"

class Connection_thread {

    private:
     ConnectionHandler c_handler;

    public:
    Connection_thread(const ConnectionHandler &ch,bool shouldTerminate);
    void Run();
    short bytesToShort(char* bytesArr);
};

#endif //BOOST_ECHO_CLIENT_CONNECTION_THREAD_H

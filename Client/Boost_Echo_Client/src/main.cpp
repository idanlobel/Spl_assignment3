//
// Created by bayan kassem on 31/12/2021.
//


#include <iostream>
#include <fstream>
#include <sstream>
#include <cstring>
#include <bitset>
#include <vector>
#include "../include/connectionHandler.h"
#include "../include/Connection_thread.h"
#include "../include/IO_thread.h"
#include <thread>




int main(int argc, char** argv){

    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host=argv[1];
    int port=std::stoi(argv[2]);

    ConnectionHandler c_handler(host, port);// enter port


   // c_handler.connect();

    IO_thread io_thread= IO_thread(c_handler);
    Connection_thread con_thread= Connection_thread(c_handler);


    std::thread th_io = std::thread(&IO_thread::Run,io_thread);
    std::thread th_co(&Connection_thread::Run,con_thread);
    th_co.join();
    th_io.detach();

    /*Connection_thread* con_thread=new Connection_thread(c_handler);
    IO_thread* io_thread=new IO_thread(c_handler);


    std::thread th_io(&IO_thread::Run,io_thread);
    std::thread th_co(&Connection_thread::Run,con_thread);*/




//register okay passw 12-12-1999



/*

  std::thread keyBoardThread = std::thread(keyBoardTask(connectionHandler));
    std::thread socketThread = std::thread(socketTask(connectionHandler));
    socketThread.join();
    keyBoardThread.detach();
*/


}
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


using namespace std;



int main(int argc, char** argv){



    string readline;
    std::cin>>readline;
    int index = 0;
    string command;
    string host=argv[0];
    int port=std::stoi(argv[1]);

     const ConnectionHandler& c_handler=ConnectionHandler(host, port);// enter port

    Connection_thread* con_thread=new Connection_thread(c_handler);
    IO_thread* io_thread=new IO_thread(c_handler);

    std::thread th_co(con_thread->Run(),);
    std::thread th_io(io_thread->Run(),);

    th_io.join();
    th_co.join();




    while(!std::equal(readline.begin(), readline.end(), "terminate")) // not sure which command terimantes all
    {
        while (readline.at(index) != ' ') {
            command += readline.at(index);
            index++;
        }
        if (std::equal(command.begin(), command.end(), "REGISTER"))
        {
            string username;
            string password;
            string birthday;
            while (readline.at(index) != ' ') {
                username += readline.at(index);
                index++;
            }


            while (readline.at(index) != ' ') {
                password += readline.at(index);
                index++;
            }



            while (readline.at(index) != ' ') {
                birthday += readline.at(index);
                index++;
            }
          string op_code="00000001";

            string line_to_send=op_code+username+"0"+password+"0"+birthday+"0";
            c_handler.sendLine(line_to_send);




        }
    }


}
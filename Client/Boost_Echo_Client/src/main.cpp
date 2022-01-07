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

using namespace std;



int main(int argc, char** argv){

    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    string host=argv[1];
    int port=std::stoi(argv[2]);

    ConnectionHandler c_handler(host, port);// enter port
    //c_handler.connect();

    std::thread con_thread=std::thread(Connection_thread(c_handler));
    std::thread io_thread=std::thread(IO_thread(c_handler));
    io_thread.join();
    con_thread.join();



/*

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
*/


}
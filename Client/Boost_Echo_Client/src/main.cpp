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


using namespace std;



int main(int argc, char** argv){
    string readline;
    std::cin>>readline;
    int index = 0;
    string command;
    ConnectionHandler* c_handler=new ConnectionHandler("127.001.001.001",123);// enter port
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
            std::vector<char> username_bytes(username.begin(), username.end());

            while (readline.at(index) != ' ') {
                password += readline.at(index);
                index++;
            }
            std::vector<char> password_bytes(password.begin(), password.end());


            while (readline.at(index) != ' ') {
                birthday += readline.at(index);
                index++;
            }
            std::vector<char> birthday_bytes(birthday.begin(), birthday.end());
            string op_code="00000001";
            std::vector<char> op_code_bytes(op_code.begin(), op_code.end());


            int index=0;
            char encoded_command[36];


            for(index;index<op_code_bytes.size();index++)
            {
                encoded_command[index]=op_code_bytes[index];
            }

            for(index;index<username_bytes.size();index++)
            {
                encoded_command[index]=username_bytes[index];
            }
            encoded_command[index]=0;
            index++;
            for(index;index<password_bytes.size();index++)
            {
                encoded_command[index]=password_bytes[index];
            }

            encoded_command[index]=0;
            index++;
            for(index;index<birthday_bytes.size();index++)
            {
                encoded_command[index]=birthday_bytes[index];
            }
            encoded_command[index]=0;

            c_handler->sendBytes(encoded_command,0);








        }
    }


}
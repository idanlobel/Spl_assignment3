//
// Created by bayan kassem on 01/01/2022.
//

#include "../include/Connection_thread.h"

Connection_thread::Connection_thread(ConnectionHandler& ch): c_handler(ch) {

}

short Connection_thread::bytesToShort(char *bytesArr)
{
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

void Connection_thread::Run() {

    this->c_handler.connect();// maybe put this in main

    std::string answer;


    while(1) {
        if (!this->c_handler.getLine(answer)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }

        //do we get string from sever after all this process? if yes then good
        //first word is eaither notfication, ack or error
        //if it is printied with 0 at end then we should do asnwer.length-1 look at echoclient notes // A C string must end with a 0 char delimiter.  When we filled the answer buffer from the socket
        //		// we filled up to the \n char - we must make sure now that a 0 char is also present. So we truncate last character.
        std::string first_word;
        int index=0;
        int linesize=static_cast<int>(answer.size());
        while(index <linesize && answer.at(index)!=' ')
        {
            first_word+=answer.at(index);
            index++;
        }
        while(index <linesize && answer.at(index)==' ')
        {
            index++;
        }
        if(std::equal(first_word.begin(), first_word.end(), "NOTIFICATION") | std::equal(first_word.begin(), first_word.end(), "notification") )
        {
            std::string PM_public;
            std::string posting_user;
            std::string content;
            while(index <linesize && answer.at(index)!=' ')
            {
                PM_public+=answer.at(index);
                index++;
            }
            while(index <linesize && answer.at(index)==' ')
            {
                index++;
            }
            while(index <linesize && answer.at(index)!=' ')
            {
                posting_user+=answer.at(index);
                index++;
            }
            while(index <linesize && answer.at(index)==' ')
            {
                index++;
            }
            while(index <linesize && answer.at(index)!=' ')
            {
                content+=answer.at(index);
                index++;
            }

            std::cout<<first_word<<" "<<PM_public<<" "<<posting_user<<" "<<content<<std::endl;
        }
        else
        {
            if(std::equal(first_word.begin(), first_word.end(), "ACK") | std::equal(first_word.begin(), first_word.end(), "ack") )
            {
                std::string message_opcode;
                std::string optional="";

                while(index <linesize && answer.at(index)!=' ')
                {
                    message_opcode+=answer.at(index);
                    index++;
                }
                while(index <linesize && answer.at(index)==' ')
                {
                    index++;
                }
                while(index <linesize && answer.at(index)!=' ')
                {
                    optional+=answer.at(index);
                    index++;
                }
                if(optional!="")
                {

                    std::string space_delimiter = " ";
                    std::vector<std::string> words{};

                    size_t pos = 0;
                    while ((pos = optional.find(space_delimiter)) != std::string::npos) {
                        words.push_back(optional.substr(0, pos));
                        optional.erase(0, pos + space_delimiter.length());
                    }
                    std::cout<<first_word<<" "<<message_opcode<<" "<<optional<<std::endl;
                }
                else
                {
                    std::cout<<first_word<<" "<<message_opcode<<std::endl;
                }

            }
            else//it is error
            {
                std::string message_opcode;

                while(index <linesize && answer.at(index)!=' ')
                {
                    message_opcode+=answer.at(index);
                    index++;
                }
                std::cout<<first_word<<" "<<message_opcode<<std::endl;

            }
        }



    }




}

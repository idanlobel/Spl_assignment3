//
// Created by bayan kassem on 01/01/2022.
//

#include "../include/Connection_thread.h"

Connection_thread::Connection_thread(const ConnectionHandler& ch): c_handler(ch) {

}

void Connection_thread::Run() {
    std::string answer;

    while(1) {
        if (!this->c_handler.getLine(answer)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
    }
    int len=0;
    len=answer.length();

    answer.resize(len-1);



}

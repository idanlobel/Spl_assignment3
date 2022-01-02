//
// Created by bayan kassem on 01/01/2022.
//

#include "../include/IO_thread.h"

IO_thread::IO_thread(const ConnectionHandler& ch): c_handler(ch) {

}

void IO_thread::Run() {
    while (1) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);

        if (!this->c_handler.sendLine(line)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }


    }



    }



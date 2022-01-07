//
// Created by bayan kassem on 01/01/2022.
//

#include "../include/IO_thread.h"



IO_thread::IO_thread(ConnectionHandler& ch): c_handler(ch),shouldTerminate(false) {


}

void IO_thread::shortToBytes(short num, char *bytesArr) {

    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);

}


void IO_thread::Run() {
    //this->c_handler.connect();
    char arr[2];
    while (1) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        std::string command;
        int index=0;

        int linesize=static_cast<int>(line.size());
        while (index <linesize && line.at(index) != ' ') {
            command += line.at(index);
            index++;
        }

        while(index <linesize && line.at(index)==' ')
        {
            index++;
        }
        if(std::equal(command.begin(), command.end(), "REGISTER") |std::equal(command.begin(), command.end(), "register") )
        {
            std::string username;
            std::string password;
            std::string birthday;
            while (index <linesize && line.at(index) != ' ') {
                username += line.at(index);
                index++;
            }

            while(index <linesize && line.at(index)==' ')
            {
                index++;
            }


            while (index <linesize && line.at(index) != ' ') {
                password += line.at(index);
                index++;
            }
            while(index <linesize && line.at(index)==' ')
            {
                index++;
            }


            while (index <linesize && line.at(index) != ' ') {
                birthday += line.at(index);
                index++;
            }

            std::cout<<"i am hererere"<<std::endl;

            this->shortToBytes(1, arr);
            this->c_handler.sendBytes(arr, 2);
            std::cout<<"i am hererere"<<std::endl;

            std::string send_me=username+"0"+password+"0"+birthday+"0";
            this->c_handler.sendLine(send_me);

        }
        else
        {
            if(std::equal(command.begin(), command.end(), "LOGIN") | std::equal(command.begin(), command.end(), "login"))
            {
                std::string username;
                std::string password;
                while (index <linesize && line.at(index) != ' ') {
                    username += line.at(index);
                    index++;
                }

                while(index <linesize && line.at(index)==' ')
                {
                    index++;
                }


                while (index <linesize && line.at(index) != ' ') {
                    password += line.at(index);
                    index++;
                }
                //current captcha is always 1 (the last byte in send_me)

                this->shortToBytes(2, arr);
                this->c_handler.sendBytes(arr, 2);
                std::string send_me=username+"0"+password+"0"+"1";
                this->c_handler.sendLine(send_me);


            }
            else
            {
                if(std::equal(command.begin(), command.end(), "LOGOUT") | std::equal(command.begin(), command.end(), "logout"))
                {
                    this->shortToBytes(3, arr);
                    this->c_handler.sendBytes(arr, 2);

                }
                else
                {
                    if(std::equal(command.begin(), command.end(), "FOLLOW")|std::equal(command.begin(), command.end(), "follow"))
                    {
                        std::string follow_or_unfollow;
                        std::string username;
                        while (index <linesize && line.at(index) != ' ') {
                            follow_or_unfollow += line.at(index);
                            index++;
                        }

                        while(index <linesize && line.at(index)==' ')
                        {
                            index++;
                        }


                        while (index <linesize && line.at(index) != ' ') {
                            username += line.at(index);
                            index++;
                        }

                        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
                        this->shortToBytes(10, arr);
                        this->c_handler.sendBytes(arr, 2);
                        this->shortToBytes(4, arr);
                        this->c_handler.sendBytes(arr, 2);

                        std::string send_me=follow_or_unfollow+username+"0";
                        this->c_handler.sendLine(send_me);
                    }
                    else
                    {
                        if(std::equal(command.begin(), command.end(), "POST")| std::equal(command.begin(), command.end(), "post"))
                        {
                            std::string content;

                            while (index <linesize && line.at(index) != ' ') {
                                content += line.at(index);
                                index++;
                            }

                            this->shortToBytes(5, arr);
                            this->c_handler.sendBytes(arr, 2);
                            std::string send_me=content+"0";
                            this->c_handler.sendLine(send_me);

                        }
                        else
                        {
                            if(std::equal(command.begin(), command.end(), "PM")|std::equal(command.begin(), command.end(), "pm"))
                            {

                                std::string username;
                                std::string content;
                                while (index <linesize && line.at(index) != ' ') {
                                    username += line.at(index);
                                    index++;
                                }

                                while(index <linesize && line.at(index)==' ')
                                {
                                    index++;
                                }


                                while (index <linesize && line.at(index) != ' ') {
                                    content += line.at(index);
                                    index++;
                                }

                                std::time_t rawtime;
                                std::tm* timeinfo;
                                char buffer [80];

                                std::time(&rawtime);
                                timeinfo = std::localtime(&rawtime);

                                std::strftime(buffer,80,"%d-%m-%Y-%H-%M",timeinfo);
                                std::puts(buffer);
                                std::string time=buffer;
                                this->shortToBytes(6, arr);
                                this->c_handler.sendBytes(arr, 2);
                                std::string send_me=username+"0"+content+"0"+time+"0";
                                this->c_handler.sendLine(send_me);
                            }
                            else
                            {
                                if(std::equal(command.begin(), command.end(), "LOGSTAT")|std::equal(command.begin(), command.end(), "logstat"))
                                {

                                    this->shortToBytes(7, arr);
                                    this->c_handler.sendBytes(arr, 2);

                                }
                                else
                                {
                                    if(std::equal(command.begin(), command.end(), "STAT")|std::equal(command.begin(), command.end(), "stat"))
                                    {
                                        std::string send_me;
                                        std::string checkme=command.substr(index);
                                        std::string listofusers;

                                        std::stringstream test(checkme);
                                        std::string segment;
                                        std::vector<std::string> seglist;

                                        while(std::getline(test, segment, ' '))
                                        {
                                            seglist.push_back(segment);
                                        }

                                        for(std::string username:seglist)
                                        {
                                            send_me+=username+",";

                                        }

                                        send_me+="0";
                                        this->shortToBytes(8, arr);
                                        this->c_handler.sendBytes(arr, 2);
                                        this->c_handler.sendLine(send_me);

                                    }
                                    else
                                    {//maybe this is bullshit
                                        if(std::equal(command.begin(), command.end(), "NOTIFICATION") | std::equal(command.begin(), command.end(), "notification"))
                                        {
                                            std::string pm_or_public;
                                            std::string username;
                                            std::string content;
                                            while (index <linesize && line.at(index) != ' ') {
                                                pm_or_public += line.at(index);
                                                index++;
                                            }

                                            while(index <linesize && line.at(index)==' ')
                                            {
                                                index++;
                                            }


                                            while (index <linesize && line.at(index) != ' ') {
                                                username += line.at(index);
                                                index++;
                                            }

                                            while(index <linesize && line.at(index)==' ')
                                            {
                                                index++;
                                            }

                                            while (index <linesize && line.at(index) != ' ') {
                                                content += line.at(index);
                                                index++;
                                            }
                                            this->shortToBytes(9, arr);
                                            this->c_handler.sendBytes(arr, 2);
                                            std::string send_me=pm_or_public+username+"0"+content+"0";
                                            this->c_handler.sendLine(send_me);
                                        }
                                        else
                                        {
                                            if(std::equal(command.begin(), command.end(), "ACK") | std::equal(command.begin(), command.end(), "ack"))
                                            {
                                                // i think i should ddelete this case
                                            }
                                            else
                                            {
                                                if(std::equal(command.begin(), command.end(), "ERROR") | std::equal(command.begin(), command.end(), "error"))
                                                {
                                                    // i think i should ddelete this case
                                                }
                                                else
                                                {//it is block

                                                    std::string username;
                                                    while (index <linesize && line.at(index) != ' ') {
                                                        username += line.at(index);
                                                        index++;
                                                    }
                                                    this->shortToBytes(12, arr);
                                                    this->c_handler.sendBytes(arr, 2);
                                                    std::string send_me=username+"0";
                                                    this->c_handler.sendLine(send_me);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!this->c_handler.sendLine(line)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }


    }



}



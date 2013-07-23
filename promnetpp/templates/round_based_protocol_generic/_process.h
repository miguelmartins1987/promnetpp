#ifndef PROCESS_H_
#define PROCESS_H_

#include "process_interface.h"
#include "types.h"
#include "message_m.h"

class Process : public ProcessInterface '{'
private:
    virtual void initialize();
    virtual void handleMessage(cMessage* msg);
    virtual void finish();
    //Generic functions
    void begin_round();
    void end_round();
    void send_to_all(message_t& msg);
    void receive(message_t& msg, byte id);
    //Specific functions
{0}
    //Variables
{1}
    //Extra functions
    void enqueue_message(cMessage* msg);
    //Extra variables
    Message* received_messages[{2}];
    int received_message_count;
'}';

Define_Module(Process)

#endif /* PROCESS_H_ */

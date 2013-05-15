#ifndef PROCESS_H_
#define PROCESS_H_

#include "process_interface.h"
#include "types.h"

class Process : public ProcessInterface '{'
private:
    virtual void initialize();
    virtual void handleMessage(cMessage* msg);
    virtual void finish();
    //Functions
    void begin_round();
    void compute_message();
    void end_round();
    void receive();
    void send_message_to_all_processes();
    void state_transition();
    //Variables
    {0}
    //Extra functions
    void enqueue_message(cMessage* msg);
    //Extra variables
    cQueue received_messages;
'}';

Define_Module(Process)

#endif /* PROCESS_H_ */

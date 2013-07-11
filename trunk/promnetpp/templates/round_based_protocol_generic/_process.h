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
    //Functions
    void begin_round();
    void compute_message();
    void end_round();
    void receive(byte id);
    void send_to_all();
    void state_transition();
    //Variables
{0}
    //Extra functions
    void enqueue_message(cMessage* msg);
    //Extra variables
    Message* received_messages[NUMBER_OF_PROCESSES];
    int received_message_count;
'}';

Define_Module(Process)

#endif /* PROCESS_H_ */

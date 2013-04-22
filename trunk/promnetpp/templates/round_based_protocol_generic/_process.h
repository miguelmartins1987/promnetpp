#ifndef PROCESS_H_
#define PROCESS_H_

#include "process_interface.h"
#include "types.h"

class Process : public ProcessInterface {
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
    void wait_to_receive();
    //Variables
    message_t _message;
};

Define_Module(Process)

#endif /* PROCESS_H_ */

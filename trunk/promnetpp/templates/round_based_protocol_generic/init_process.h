#ifndef INIT_PROCESS_H_
#define INIT_PROCESS_H_

#include "process_interface.h"
#include "types.h"

class InitProcess: public ProcessInterface '{'
private:
    virtual void initialize();
    virtual void handleMessage(cMessage* msg);
    virtual void finish();

    int round_number;
    void do_new_round();
    void run_processes();
    //Functions
    void system_every_round();
    void system_init();
    //Variables
{0}
'}';

Define_Module(InitProcess)

#endif /* INIT_PROCESS_H_ */

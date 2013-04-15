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
    {0}
    //Variables
    {1}
'}';

Define_Module(Process)

#endif /* PROCESS_H_ */

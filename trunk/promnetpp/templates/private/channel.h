#ifndef {0}
#define {0}

#include <omnetpp.h>

class {1} : public cSimpleModule '{'
private:
    cQueue queue;
    cQueue pending_requests;
    virtual void initialize();
    virtual void handleMessage(cMessage* msg);
    virtual void finish();
    int capacity;
public:
    int get_length();
    bool is_empty();
    bool is_not_empty();
    bool is_full();
    bool is_not_full();
'}';

Define_Module({1})

#endif /* {0} */

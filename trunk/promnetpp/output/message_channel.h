#ifndef MESSAGE_CHANNEL_H_
#define MESSAGE_CHANNEL_H_

#include <omnetpp.h>

class MessageChannel : public cSimpleModule {
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
};

Define_Module(MessageChannel)

#endif /* MESSAGE_CHANNEL_H_ */

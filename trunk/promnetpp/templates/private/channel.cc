#include "{0}"
#include "channel_m.h"
#include "global_definitions.h"

void {1}::initialize() '{'
    capacity = NUMBER_OF_PROCESSES;
'}'

void {1}::handleMessage(cMessage* msg) '{'
    ChannelMessage* message = check_and_cast<ChannelMessage*>(msg);

    if (message->getOperationType() == REQUEST_PUT) '{'
        queue.insert(message);
    '}' else if (message->getOperationType() == REQUEST_TAKE) '{'
        if (!queue.isEmpty()) '{'
            ChannelMessage* reply = check_and_cast<ChannelMessage*>
                (queue.pop());
            cGate* reply_gate = gateHalf(msg->getArrivalGate()->getBaseName(),
                    cGate::OUTPUT,msg->getArrivalGate()->getIndex());
            send(reply, reply_gate);
            delete msg;
        '}' else '{'
            pending_requests.insert(msg);
        '}'
    '}'
    cQueue temporary_queue;
    while (!pending_requests.isEmpty()) '{'
        ChannelMessage* request = check_and_cast<ChannelMessage*>
            (pending_requests.pop());
        if (request->getOperationType() == REQUEST_TAKE) '{'
            if (!queue.isEmpty()) '{'
                ChannelMessage* reply = check_and_cast<ChannelMessage*>
                    (queue.pop());
                cGate* reply_gate = gateHalf(
                        request->getArrivalGate()->getBaseName(),
                        cGate::OUTPUT,request->getArrivalGate()->getIndex());
                send(reply, reply_gate);
            '}' else '{'
                temporary_queue.insert(request);
            '}'
        '}'
    '}'
    //Copy from temporary queue
    while (!temporary_queue.isEmpty()) '{'
        pending_requests.insert(temporary_queue.pop());
    '}'
'}'

void {1}::finish() '{'
    //Empty
'}'

int {1}::get_length() '{'
    return queue.length();
'}'

bool {1}::is_empty() '{'
    return (queue.length() == 0);
'}'

bool {1}::is_not_empty() '{'
    return (!is_empty());
'}'

bool {1}::is_full() '{'
    return (queue.length() == capacity);
'}'

bool {1}::is_not_full() '{'
    return (!is_full());
'}'

#include "message_channel.h"
#include "channel_m.h"
#include "global_definitions.h"

void MessageChannel::initialize() {
    capacity = NUMBER_OF_PROCESSES;
}

void MessageChannel::handleMessage(cMessage* msg) {
    ChannelMessage* message = check_and_cast<ChannelMessage*>(msg);

    if (message->getOperationType() == REQUEST_PUT) {
        queue.insert(message);
    } else if (message->getOperationType() == REQUEST_TAKE) {
        if (!queue.isEmpty()) {
            ChannelMessage* reply = check_and_cast<ChannelMessage*>
                (queue.pop());
            cGate* reply_gate = gateHalf(msg->getArrivalGate()->getBaseName(),
                    cGate::OUTPUT,msg->getArrivalGate()->getIndex());
            send(reply, reply_gate);
            delete msg;
        } else {
            pending_requests.insert(msg);
        }
    }
    cQueue temporary_queue;
    while (!pending_requests.isEmpty()) {
        ChannelMessage* request = check_and_cast<ChannelMessage*>
            (pending_requests.pop());
        if (request->getOperationType() == REQUEST_TAKE) {
            if (!queue.isEmpty()) {
                ChannelMessage* reply = check_and_cast<ChannelMessage*>
                    (queue.pop());
                cGate* reply_gate = gateHalf(
                        request->getArrivalGate()->getBaseName(),
                        cGate::OUTPUT,request->getArrivalGate()->getIndex());
                send(reply, reply_gate);
            } else {
                temporary_queue.insert(request);
            }
        }
    }
    //Copy from temporary queue
    while (!temporary_queue.isEmpty()) {
        pending_requests.insert(temporary_queue.pop());
    }
}

void MessageChannel::finish() {
    //Empty
}

int MessageChannel::get_length() {
    return queue.length();
}

bool MessageChannel::is_empty() {
    return (queue.length() == 0);
}

bool MessageChannel::is_not_empty() {
    return (!is_empty());
}

bool MessageChannel::is_full() {
    return (queue.length() == capacity);
}

bool MessageChannel::is_not_full() {
    return (!is_full());
}

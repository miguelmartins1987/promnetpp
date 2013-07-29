#include "_process.h"
#include "types.h"
#include "utilities.h"

#include "message_m.h"
#include <omnetpp.h>

{0}

void Process::initialize() '{'
    ProcessInterface::initialize();
    _pid = getIndex() + 1;
    received_message_count = 0;
    //Variable initialization
{1}
'}'

void Process::handleMessage(cMessage* msg) '{'
    if (msg->isSelfMessage()) '{'
        /*
         * Messages whose class is "Message" are messages that a process sent to
         * itself. If we encounter these, we must queue them, as if any other
         * process sent them.
         */
        if (strcmp(msg->getClassName(), "Message") == 0) '{'
            enqueue_message(msg);
        '}'
        else if (strcmp(current_location, "main") == 0) '{'
            int step = step_map["main"];
            if (step == 0) '{'
                begin_round();
            '}'
            if (step == 1) '{'
                compute_message({2});
                send_to_all({2});
            '}'
            if (step == 2) '{'
                state_transition();
                end_round();
            '}'
        '}'
    '}' else '{'
        const char* sender_name = msg->getSenderModule()->getName();
        //Messages from init process
        if (strcmp(sender_name, "init") == 0) '{'
            //"init" message
            if (strcmp(msg->getName(), "init") == 0) '{'
                //Placeholder
            '}'
            //"new_round" message
            else if (strcmp(msg->getName(), "new_round") == 0) '{'
                step_map["main"] = 0;
                scheduleAt(simTime(), empty_message);
            '}'
            //"begin" message
            else if (strcmp(msg->getName(), "begin") == 0) '{'
                step_map["main"] = 1;
                scheduleAt(simTime(), empty_message);
            '}'
            delete msg;
        '}'
        //Messages from another process
        else if (strcmp(sender_name, "process") == 0) '{'
            enqueue_message(msg);
        '}'
    '}'
'}'

void Process::finish() '{'
    ProcessInterface::finish();
'}'

void Process::enqueue_message(cMessage* msg) '{'
    Message* message = check_and_cast<Message*>(msg);
    Process* sender = check_and_cast<Process*>(message->getSenderModule());
    int sender_pid = sender->_pid;
    received_messages[sender_pid - 1] = message;
    ++received_message_count;
    if (received_message_count == {3}) '{'
        ++step_map["main"];
        scheduleAt(simTime(), empty_message);
    '}'
'}'

//Generic functions
void Process::begin_round() '{'
    send(ready_message->dup(), "init_gate$o");
'}'

void Process::end_round() '{'
    for (int i = 0; i < {3}; ++i) '{'
        delete received_messages[i];
    '}'
    received_message_count = 0;
    send(finished_message->dup(), "init_gate$o");
'}'

void Process::send_to_all(message_t& msg) '{'
    Message* message = new Message();
    message->set_message(msg);
    for (i = 1; i <= {3}; ++i) '{'
        if (i == _pid) '{'
            scheduleAt(simTime(), message->dup());
        '}' else '{'
            send(message->dup(), "process_gate$o", i);
        '}'
    '}'
    delete message;
'}'

void Process::receive(message_t& msg, byte id) '{'
    Message* message = received_messages[id];
    msg = message->get_message();
'}'

//Specific functions
void Process::compute_message({4}) '{'
{5}
'}'

void Process::state_transition() '{'
{6}
'}'

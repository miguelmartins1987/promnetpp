#include "_process.h"
#include "types.h"
#include "utilities.h"

#include "message_m.h"
#include <omnetpp.h>

extern process_state state[];
extern int round_id;

void Process::initialize() '{'
    ProcessInterface::initialize();
    _pid = getIndex() + 1;
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
                compute_message();
                send_message_to_all_processes();
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
                utilities::printf(this, "MSC: P%d has initial value x=%d\n",
                    _pid, my_state.local_value);
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
    received_messages.insert(msg);
    if (received_messages.length() == NUMBER_OF_PROCESSES) '{'
        ++step_map["main"];
        scheduleAt(simTime(), empty_message);
    '}'
'}'

//Generic functions
void Process::begin_round() '{'
    send(ready_message->dup(), "init_gate$o");
'}'

void Process::end_round() '{'
    //Clear the queue before we move on to the next round
    while (!received_messages.empty()) '{'
        Message* message = check_and_cast<Message*>(received_messages.pop());
        delete message;
    '}'
    send(finished_message->dup(), "init_gate$o");
'}'

void Process::send_message_to_all_processes() '{'
    Message* message = new Message();
    message->set_message(_message);
    for (i = 1; i <= NUMBER_OF_PROCESSES; ++i) '{'
        if (i == _pid) '{'
            scheduleAt(simTime(), message->dup());
        '}' else '{'
            send(message->dup(), "process_gate$o", i);
        '}'
    '}'
    delete message;
'}'

void Process::receive() '{'
    Message* message = check_and_cast<Message*>(received_messages.pop());
    _message = message->get_message();
    delete message;
'}'

//Specific functions
void Process::compute_message() '{'
{0}
'}'

void Process::state_transition() '{'
{1}
'}'
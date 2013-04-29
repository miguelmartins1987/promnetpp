#include "channel.h"
#include "channel_m.h"
#include "_process.h"
#include "types.h"
#include "utilities.h"
#include <omnetpp.h>

extern process_state state[];

void Process::initialize() '{'
    ProcessInterface::initialize();
    _pid = getIndex() + 1;
'}'

void Process::handleMessage(cMessage* msg) '{'
    {0}
'}'

void Process::finish() '{'
    ProcessInterface::finish();
'}'

//Generic functions
void Process::begin_round() '{'
    send(ready_message->dup(), "init_gate$o");
'}'

void Process::end_round() '{'
    send(finished_message->dup(), "init_gate$o");
'}'

void Process::send_message_to_all_processes() '{'
    ChannelMessage* message = new ChannelMessage();
    message->setOperationType(REQUEST_PUT);
    message->set_message(_message);
    for (int i = 0; i < NUMBER_OF_PROCESSES; ++i) '{'
        send(message->dup(), "channel_gate$o", i);
    '}'
    delete message;
'}'

void Process::wait_to_receive() '{'
    Channel* channel = check_and_cast<Channel*>(
            this->getParentModule()->getSubmodule("_channel", _pid - 1));
    if (channel->is_full()) '{'
        ++step_map["main"];
        scheduleAt(simTime(), empty_message);
    '}' else '{'
        scheduleAt(simTime() + NUMBER_OF_PROCESSES, empty_message);
    '}'
'}'

void Process::receive() '{'
    ChannelMessage* message = new ChannelMessage();
    message->setOperationType(REQUEST_TAKE);
    send(message, "channel_gate$o", _pid - 1);
'}'

//Specific functions
void Process::compute_message() '{'
{1}
'}'

void Process::state_transition() '{'
{2}
'}'
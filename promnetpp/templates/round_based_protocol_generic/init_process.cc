#include "global_definitions.h"
#include "init_process.h"
#include "utilities.h"

//Template-specific global variables
extern byte number_of_processes_in_current_round;
//Global variables from the PROMELA model
{0}

void InitProcess::initialize() '{'
    ProcessInterface::initialize();
    _pid = 0;
    //Variable initialization
{1}
    memset(state, 0, sizeof(state));
    //Do the actual work
    system_init();
    run_processes();
    scheduleAt(simTime(), empty_message);
'}'

void InitProcess::handleMessage(cMessage* msg) '{'
    if (msg->isSelfMessage()) '{'
        if (number_of_processes_in_current_round == 0) '{'
            system_every_round();
            do_new_round();
        '}'
    '}' else '{'
        if (strcmp(msg->getName(), "ready") == 0) '{'
            ++number_of_processes_in_current_round;
            if (number_of_processes_in_current_round == {2}) '{'
                for (i = 0; i < {2}; ++i) '{'
                    send(new cMessage("begin"), "process_gate$o", i);
                '}'
            '}'
        '}' else if (strcmp(msg->getName(), "finished") == 0) '{'
            --number_of_processes_in_current_round;
            if (number_of_processes_in_current_round == 0) '{'
                scheduleAt(simTime(), empty_message);
            '}'
        '}'
        delete msg;
    '}'
'}'

void InitProcess::finish() '{'
    ProcessInterface::finish();
'}'

void InitProcess::do_new_round() '{'
    for (i = 0; i < {2}; ++i) '{'
        send(new cMessage("new_round"), "process_gate$o", i);
    '}'
'}'

void InitProcess::run_processes() '{'
    for (i = 0; i < {2}; ++i) '{'
        send(new cMessage("init"), "process_gate$o", i);
    '}'
'}'

void InitProcess::system_every_round() '{'
{3}
'}'

void InitProcess::system_init() '{'
{4}
'}'

#include "global_definitions.h"
#include "init_process.h"
#include "utilities.h"

extern process_state state[];
extern byte number_of_processes_in_current_round;

void InitProcess::initialize() {
    IProcess::initialize();
    _pid = 0;
    round_number = 0;
    memset(state, 0, sizeof(process_state) * NUMBER_OF_PROCESSES);
    //Do the actual work
    system_init();
    run_processes();
    scheduleAt(simTime(), empty_message);
}

void InitProcess::handleMessage(cMessage* msg) {
    if (msg->isSelfMessage()) {
        if (number_of_processes_in_current_round == 0) {
            ++round_number;
            system_every_round();
            do_new_round();
        }
    } else {
        if (strcmp(msg->getName(), "ready") == 0) {
            ++number_of_processes_in_current_round;
            if (number_of_processes_in_current_round == NUMBER_OF_PROCESSES) {
                for (i = 0; i < NUMBER_OF_PROCESSES; ++i) {
                    send(new cMessage("begin"), "process_gate$o", i);
                }
            }
        } else if (strcmp(msg->getName(), "finished") == 0) {
            --number_of_processes_in_current_round;
            if (number_of_processes_in_current_round == 0) {
                utilities::printf(this, "Round %d over!\n", round_number);
                scheduleAt(simTime(), empty_message);
            }
        }
        delete msg;
    }
}

void InitProcess::finish() {
    IProcess::finish();
}

void InitProcess::do_new_round() {
    for (i = 0; i < NUMBER_OF_PROCESSES; ++i) {
        send(new cMessage("new_round"), "process_gate$o", i);
    }
}

void InitProcess::run_processes() {
    for (i = 0; i < NUMBER_OF_PROCESSES; ++i) {
        send(new cMessage("init"), "process_gate$o", i);
    }
}

void InitProcess::system_every_round() {
    utilities::printf(this, "Setting up round %d...\n", round_number);
    for (i = 0; i <= (NUMBER_OF_PROCESSES - 1); ++i) {
        for (j = 0; j <= (NUMBER_OF_PROCESSES - 1); ++j) {
            int decision = intrand(2);
            if (decision == 0) {
                state[i].heard_of[j] = false;
            } else {
                state[i].heard_of[j] = true;
            }
        }
    }
}

void InitProcess::system_init() {
    for (i = 1; i <= NUMBER_OF_PROCESSES; ++i) {
        utilities::select(j, 1, NUMBER_OF_PROCESSES);
        state[i - 1].local_value = j;
    }
}
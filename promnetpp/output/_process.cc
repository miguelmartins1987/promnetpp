#include "channel.h"
#include "channel_m.h"
#include "_process.h"
#include "types.h"
#include "utilities.h"
#include <omnetpp.h>

extern process_state state[];

void Process::initialize() {
    ProcessInterface::initialize();
    _pid = getIndex() + 1;
}

void Process::handleMessage(cMessage* msg) {
}

void Process::finish() {
    ProcessInterface::finish();
}

{0}

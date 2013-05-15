#include "process_interface.h"
#include "utilities.h"

void ProcessInterface::initialize() {
    empty_message = new cMessage();
    finished_message = new cMessage("finished");
    ready_message = new cMessage("ready");
    step_map["main"] = 0;
    //Every process starts at "main"
    current_location = "main";
}

void ProcessInterface::finish() {
    cancelAndDelete(empty_message);
    cancelAndDelete(finished_message);
    cancelAndDelete(ready_message);
}

int ProcessInterface::get_pid() {
    return _pid;
}

void ProcessInterface::save_location(const char* location, int step) {
    current_location = location;
    step_map[current_location] = step;
}

void ProcessInterface::return_to(const char* location) {
    //Reset the step for the current location
    step_map[current_location] = 0;
    //Set the new location, and go to the next step
    current_location = location;
    ++step_map[location];
    scheduleAt(simTime(), empty_message);
}

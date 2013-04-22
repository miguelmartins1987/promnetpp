#ifndef PROCESS_INTERFACE_H_
#define PROCESS_INTERFACE_H_

#include <map>
#include <string>
#include <omnetpp.h>

class ProcessInterface : public cSimpleModule {
public:
    virtual ~ProcessInterface() {}
    void initialize();
    virtual void handleMessage(cMessage* msg) = 0;
    void finish();
    int get_pid();
    void save_location(const char* location, int step);
    void return_to(const char* location);
protected:
    cMessage* empty_message;
    cMessage* finished_message;
    cMessage* ready_message;
    int _pid;
    const char* current_location;
    std::map<std::string, int> step_map;
};

#endif /* PROCESS_INTERFACE_H_ */

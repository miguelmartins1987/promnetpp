#ifndef UTILITIES_H_
#define UTILITIES_H_

#include "types.h"
#include <omnetpp.h>

namespace utilities {
    void printf(cSimpleModule* module, const char* format, ...);
    void select(byte& value, int min, int max);
    void select(int& value, int min, int max);
}

#endif /* UTILITIES_H_ */

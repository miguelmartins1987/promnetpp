#ifndef TYPES_H_
#define TYPES_H_

#include "global_definitions.h"

typedef unsigned char byte;

typedef struct {
    byte id;
    byte value;
} message_t;

typedef struct {
    bool received_message[NUMBER_OF_PROCESSES];
    byte received_message_count;
    byte local_value;
    byte decision_value;
    byte values[NUMBER_OF_PROCESSES];
} process_state;



#endif /* TYPES_H */

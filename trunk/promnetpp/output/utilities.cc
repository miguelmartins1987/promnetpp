#include "utilities.h"

void utilities::printf(cSimpleModule* module, const char* format, ...) {
    char buffer [4096];
    va_list args;
    va_start(args, format);
    vsnprintf(buffer, 4096, format, args);
    ev.printf(buffer);
    if (ev.isGUI()) {
        module->bubble(buffer);
    }
    va_end(args);
}

void utilities::select(byte& value, int min, int max) {
    value = intrand(max) + min;
}

void utilities::select(int& value, int min, int max) {
    value = intrand(max) + min;
}

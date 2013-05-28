#include "utilities.h"

void utilities::printf(cSimpleModule* module, const char* format, ...) {
    char buffer [4096];
    va_list args;
    va_start(args, format);
    vsnprintf(buffer, 4096, format, args);
    //Print to the environment's console
    ev.printf(buffer);
    //Print graphically, using speech bubbles (if available)
    if (ev.isGUI()) {
        module->bubble(buffer);
    }
    //Print to the simulation output file
    FILE* simulation_output_file = fopen("simulation-output.txt", "a");
    fprintf(simulation_output_file, buffer);
    fclose(simulation_output_file);
    va_end(args);
}

void utilities::select(byte& value, int min, int max) {
    value = intrand(max) + min;
}

void utilities::select(int& value, int min, int max) {
    value = intrand(max) + min;
}

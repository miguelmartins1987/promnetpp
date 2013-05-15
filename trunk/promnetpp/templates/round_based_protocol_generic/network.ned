simple InitProcess
'{'
    @display("i=promela/init");
    gates:
        inout process_gate[];
'}'

simple Process
'{'
    @display("i=promela/process");
    gates:
        inout process_gate[];
        inout init_gate;
'}'

network Network
'{'
    int NUMBER_OF_PROCESSES = {0};
    submodules:
        init: InitProcess;
        process[NUMBER_OF_PROCESSES]: Process;
    connections:
        for i=0..NUMBER_OF_PROCESSES-1, for j=0..NUMBER_OF_PROCESSES-1, if i != j '{'
            process[i].process_gate++ <--> process[j].process_gate++;
        '}'
        for k=0..NUMBER_OF_PROCESSES-1 '{'
            init.process_gate++ <--> process[k].init_gate;
        '}'
'}'

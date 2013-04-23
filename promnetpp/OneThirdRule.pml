/*
 * Copyright (c) 2013, Raul Barbosa
 * Use is subject to license terms.
 *
 * This source code file is provided under the MIT License. Full licensing
 * terms should be available in the form of text files. The standard source code
 * distribution provides a LICENSE.txt file which can be consulted for licensing
 * details.
 */
 
/*
OneThirdRule in PROMELA
Author: Raul B. Barbosa <rbarbosa@dei.uc.pt>
Modified by Miguel Martins <marm@student.dei.uc.pt>

Modifications were done on the specification provided on February 15 2013:
-More descriptive variable names
-Indentation of 4 spaces per tab
-Annotations for PROMNeT++
*/

/* @USES_TEMPLATE(template_name="round_based_protocol_generic") */
#define NUMBER_OF_PROCESSES 4

typedef message {
    byte id;
    byte value
}

typedef process_state {
    bool received_message[NUMBER_OF_PROCESSES];
    byte received_message_count;
    
    byte local_value;
    byte decision_value;
    byte values[NUMBER_OF_PROCESSES]
}

process_state state[NUMBER_OF_PROCESSES];

#define my_state state[_pid-1]

inline compute_message(_message) {
    _message.id = _pid - 1;
    _message.value = my_state.local_value
}

inline state_transition() {
    d_step {
        my_state.received_message_count = 0;
        do
        :: temp = nempty(channel[(_pid-1)]);
            if
            :: temp -> receive(_message);
                if
                :: my_state.received_message[_message.id] ->
                   my_state.values[_message.id] = _message.value;
                   my_state.received_message_count++
                :: else -> skip
                fi
            :: else -> break
            fi
        od;

        if
        :: my_state.received_message_count > (2 * NUMBER_OF_PROCESSES/3 ) ->
            l = 0;
            for(i : 1..(NUMBER_OF_PROCESSES)) {
                k = 0;
                for(j : 0..(NUMBER_OF_PROCESSES-1)) {
                    if
                    :: my_state.values[j] == i -> k++
                    :: else -> skip
                    fi
                }
                if
                :: k > l -> my_state.local_value = i; l = k
                :: else -> skip
                fi
            }
        :: else -> skip
        fi;

        if
        :: l > (2 * NUMBER_OF_PROCESSES/3) ->
            assert( (my_state.decision_value == my_state.local_value) ||
                (my_state.decision_value == 0) );
            my_state.decision_value = my_state.local_value;
            printf("MSC: P%d decides %d\n", _pid, my_state.decision_value)
        :: else -> skip
        fi
    }

    printf("P%d received messages = %d\n", _pid,
        my_state.received_message_count)
}

inline system_init() {
    for(i : 1..(NUMBER_OF_PROCESSES)) {
        select(j : 1..(NUMBER_OF_PROCESSES));
        state[i-1].local_value = j
	}
}

inline system_every_round() {
    printf("MSC: new round\n");
    for(i : 0..(NUMBER_OF_PROCESSES-1)) {
        for(j : 0..(NUMBER_OF_PROCESSES-1)) {
            if
            :: state[i].received_message[j] = true
            :: state[i].received_message[j] = false
            fi
        }
    }
}

/* @BEGIN_TEMPLATE_BLOCK(block_name="generic_part") */
#define predecessor(process, n) ((process - 1 + n) % n)
#define successor(process, n) ((process + 1) % n)

byte number_of_processes_in_current_round;
chan channel[NUMBER_OF_PROCESSES] = [NUMBER_OF_PROCESSES] of {message};

inline begin_round() {
    (timeout);
    number_of_processes_in_current_round++;
    (number_of_processes_in_current_round == NUMBER_OF_PROCESSES)
}

inline end_round() {
    number_of_processes_in_current_round--
}

inline send_message_to_all_processes(_message) {
    channel[_pid-1] ! _message;
    i = predecessor((_pid-1), NUMBER_OF_PROCESSES);
    do
    :: i != successor((_pid-1), NUMBER_OF_PROCESSES) ->
        channel[i] ! _message;
        i = predecessor(i, NUMBER_OF_PROCESSES)
    :: else -> break
    od;
    i = 0;
    channel[successor((_pid-1), NUMBER_OF_PROCESSES)] ! _message
}

inline wait_to_receive() {
    (full(channel[(_pid-1)]))
}

inline receive(_message) {
    channel[(_pid-1)] ? _message
}

proctype Process() {
    message _message;
    byte i, j, k, l;
    bool temp;
    
    printf("The process with PID %d has initial value %d\n", _pid,
        my_state.local_value);
    do
    ::
        begin_round();
        compute_message(_message);
        send_message_to_all_processes(_message);
        wait_to_receive();
        state_transition();
        end_round()
    od	
}

init {
    byte i, j;
    system_init();

    atomic {
        for(i : 1..(NUMBER_OF_PROCESSES)) {
            run Process()
        }
    }
    
    do
    :: 
        (number_of_processes_in_current_round == 0);
        system_every_round();
        (number_of_processes_in_current_round != 0)
    od
}
/* @END_TEMPLATE_BLOCK */

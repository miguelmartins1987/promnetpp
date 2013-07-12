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
1-of-n in PROMELA
Author: Raul B. Barbosa <rbarbosa@dei.uc.pt>
Modified by Miguel Martins <marm@student.dei.uc.pt>

Modifications were done on the specification provided on June 12 2013:
-More descriptive variable names
-Indentation of 4 spaces per tab
-Annotations for PROMNeT++
*/

/* @UsesTemplate(name="round_based_protocol_generic") */
/* @TemplateParameter(name="numberOfParticipants") */
#define NUMBER_OF_PROCESSES 3
#define NUMBER_OF_ASYNCHRONOUS_ROUNDS 4
#define R 4
#define ABORT 0

/* Random number generation */
int random = 1234;
#define next(r) (r * 16807) % 2147483647
#define boolean(r) ((r >> 30) & 1)

/* End random number generation */

int round_id = 0;

typedef message {
    byte value;
    bool view[NUMBER_OF_PROCESSES]
}

typedef process_state {
    bool received_message[NUMBER_OF_PROCESSES];
    bool view[NUMBER_OF_PROCESSES];
    
    byte local_value;
    byte decision_value;
}

process_state state[NUMBER_OF_PROCESSES];

#define my_state state[_pid-1]

inline compute_message(_message) {
    _message.value = my_state.local_value;
    for(i : 0..(NUMBER_OF_PROCESSES-1)) {
        _message.view[i] = my_state.view[i]
    }
}

inline state_transition() {
    d_step {
        if
        :: round < (R-1) ->
            round++;
            for(i : 0..(NUMBER_OF_PROCESSES-1)) {
                if
                :: my_state.received_message[i] ->
                    receive(_message, i);
                    if
                    :: my_state.local_value < _message.value ->
                        my_state.local_value = _message.value
                    :: else -> skip
                    fi;
                    
                    for(j : 0..(NUMBER_OF_PROCESSES-1)) {
                        my_state.view[j] = my_state.view[j] || _message.view[j]
                    }
                :: else -> skip
                fi
            } 
       
        :: round == (R-1) ->
            round++;
            check = true;
            for(i : 0..(NUMBER_OF_PROCESSES-1)) {
                if
                :: !my_state.view[i] -> check = false
                :: else -> skip
                fi
            }
            
            if
            :: check ->
                for(i : 0..(NUMBER_OF_PROCESSES-1)) {
                    if
                    :: my_state.received_message[i] ->
                        receive(_message, i);
                        for(j : 0..(NUMBER_OF_PROCESSES-1)) {
                            if
                            :: !_message.view[j] -> check = false
                            :: else -> skip
                            fi
                        }
                    :: else -> skip
                    fi
                }
            :: else -> skip
            fi;
       
            if
            :: check ->
                my_state.decision_value = my_state.local_value;
                printf("MSC: P%d decides %d\n", _pid, my_state.decision_value)
            :: !check ->
                printf("MSC: P%d decides ABORT\n", _pid)
            fi
        fi;
        
        for(i : 0..(NUMBER_OF_PROCESSES-1)) {
            assert(my_state.decision_value == state[i].decision_value ||
                my_state.decision_value == ABORT ||
                state[i].decision_value == ABORT)
        }
    }
}

inline system_init() {
    j = 1;
    for(i : 0..(NUMBER_OF_PROCESSES-1)) {
        state[i].local_value = j;
        random = next(random);
        if
        :: boolean(random) -> j++
        :: else -> skip
        fi;
        state[i].view[i] = true
    }
}

inline system_every_round() {
    round_id++;
    printf("MSC: new round, id=%d\n", round_id);

    if
    :: remaining_asynchronous_rounds == 0 -> synchronous = true
    :: else -> remaining_asynchronous_rounds--
    fi;

    for(i : 0..(NUMBER_OF_PROCESSES-1)) {
        for(j : 0..(NUMBER_OF_PROCESSES-1)) {
            if
            :: state[i].received_message[j] = true
            :: !synchronous && i != j -> state[i].received_message[j] = false
            fi
        }
    }
}

inline send_to_all(_message) {
    messages[_pid-1].value = _message.value;
    for(j : 0..(NUMBER_OF_PROCESSES-1)) {
        messages[_pid-1].view[j] = _message.view[j]
    }
}

inline receive(_message, id) {
    _message.value = messages[id].value;
    for(j : 0..(NUMBER_OF_PROCESSES-1)) {
        _message.view[j] = messages[id].view[j]
    }
}

/* @BeginTemplateBlock(name="generic_part") */
message messages[NUMBER_OF_PROCESSES];

byte token;

inline begin_round() {
    (token == _pid)
}

inline end_round() {
    token--
}

inline wait_to_receive() {
    token--;
    (token == _pid)
}

proctype Process() {
    message _message;
    byte i, j;
    byte round;
    bool check;
    printf("MSC: P%d has initial value x=%d\n", _pid, my_state.local_value);
    do
    :: begin_round();
        compute_message(_message);
        send_to_all(_message);
        wait_to_receive();
        state_transition();
        end_round()
    od	
}

init {
    byte i, j, remaining_asynchronous_rounds = NUMBER_OF_ASYNCHRONOUS_ROUNDS;
    bool synchronous = false;
    
    system_init();

    atomic {
        for(i : 1..(NUMBER_OF_PROCESSES)) {
            run Process()
        }
    }

    do
    :: (token == 0);
        system_every_round();
        token = NUMBER_OF_PROCESSES;
        (token == 0);
        token = NUMBER_OF_PROCESSES
    od
}
/* @EndTemplateBlock */
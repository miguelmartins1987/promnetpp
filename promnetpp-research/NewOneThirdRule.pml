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

Modifications were done on the specification provided on April 23 2013:
-More descriptive variable names
-Indentation of 4 spaces per tab
-Annotations for PROMNeT++
*/

/* @UsesTemplate(name="round_based_protocol_generic") */
/* @TemplateParameter(name="numberOfParticipants") */
#define NUMBER_OF_PROCESSES 3
#define NUMBER_OF_ASYNCHRONOUS_ROUNDS 1

/* Random number generation */
int random = <INSERT_SEED_HERE>;
#define next(r) (r * 16807) % 2147483647
#define boolean(r) ((r >> 30) & 1)

/* End random number generation */

int round_id = 0;

typedef message {
    byte value;
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
    _message.value = my_state.local_value
}

inline state_transition() { 
    d_step {
        my_state.received_message_count = 0;
        for(i : 0..(NUMBER_OF_PROCESSES-1)) {
            if
            :: my_state.received_message[i] ->
                receive(_message, i);
                my_state.values[i] = _message.value;
                my_state.received_message_count++
            :: else -> skip
            fi
        }
  
        if
        :: my_state.received_message_count > (2 * NUMBER_OF_PROCESSES/3) ->
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
            assert((my_state.decision_value == my_state.local_value)
                || (my_state.decision_value == 0));
            my_state.decision_value = my_state.local_value;
            printf("MSC: P%d decides %d on round %d\n", _pid,
                my_state.decision_value, round_id)
        :: else -> skip
        fi
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
        fi
    }
}

inline system_every_round() {
    round_id++;
    printf("MSC: new round, id=%d\n", round_id);
    printf("random=%d\n", random);

    if
    :: remaining_asynchronous_rounds == 0 -> synchronous = true
    :: else -> remaining_asynchronous_rounds--
    fi;

    for(i : 0..(NUMBER_OF_PROCESSES-1)) {
        for(j : 0..(NUMBER_OF_PROCESSES-1)) {
            if
            :: synchronous || i == j -> state[i].received_message[j] = true
            :: else ->
                random = next(random);
                state[i].received_message[j] = boolean(random)
            fi
        }
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

inline send_to_all(_message) {
    messages[_pid-1].value = _message.value
}

inline wait_to_receive() {
    token--;
    (token == _pid)
}

inline receive(_message, id) {
    _message.value = messages[id].value
}

proctype Process() {
    message _message;
    byte i, j, k, l;
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
    bool synchronous;

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

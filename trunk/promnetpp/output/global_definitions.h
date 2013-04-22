#ifndef GLOBAL_DEFINITIONS_H_
#define GLOBAL_DEFINITIONS_H_

#define NUMBER_OF_PROCESSES 4
#define my_state state[_pid-1]
#define predecessor(process, n) ((process - 1 + n) % n)
#define successor(process, n) ((process + 1) % n)


#endif /* GLOBAL_DEFINITIONS_H_ */

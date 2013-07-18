#define rand	pan_rand
#if defined(HAS_CODE) && defined(VERBOSE)
	#ifdef BFS_PAR
		bfs_printf("Pr: %d Tr: %d\n", II, t->forw);
	#else
		cpu_printf("Pr: %d Tr: %d\n", II, t->forw);
	#endif
#endif
	switch (t->forw) {
	default: Uerror("bad forward move");
	case 0:	/* if without executable clauses */
		continue;
	case 1: /* generic 'goto' or 'skip' */
		IfNotBlocked
		_m = 3; goto P999;
	case 2: /* generic 'else' */
		IfNotBlocked
		if (trpt->o_pm&1) continue;
		_m = 3; goto P999;

		 /* PROC :init: */
	case 3: /* STATE 1 - temp.pml:131 - [j = 1] (0:16:2 - 1) */
		IfNotBlocked
		reached[1][1] = 1;
		(trpt+1)->bup.ovals = grab_ints(2);
		(trpt+1)->bup.ovals[0] = ((int)((P1 *)this)->j);
		((P1 *)this)->j = 1;
#ifdef VAR_RANGES
		logval(":init::j", ((int)((P1 *)this)->j));
#endif
		;
		/* merge: i = 0(16, 2, 16) */
		reached[1][2] = 1;
		(trpt+1)->bup.ovals[1] = ((int)((P1 *)this)->i);
		((P1 *)this)->i = 0;
#ifdef VAR_RANGES
		logval(":init::i", ((int)((P1 *)this)->i));
#endif
		;
		/* merge: .(goto)(0, 17, 16) */
		reached[1][17] = 1;
		;
		_m = 3; goto P999; /* 2 */
	case 4: /* STATE 3 - temp.pml:132 - [((i<=(3-1)))] (0:0:0 - 1) */
		IfNotBlocked
		reached[1][3] = 1;
		if (!((((int)((P1 *)this)->i)<=(3-1))))
			continue;
		_m = 3; goto P999; /* 0 */
	case 5: /* STATE 4 - temp.pml:133 - [state[i].local_value = j] (0:0:1 - 1) */
		IfNotBlocked
		reached[1][4] = 1;
		(trpt+1)->bup.oval = ((int)now.state[ Index(((int)((P1 *)this)->i), 3) ].local_value);
		now.state[ Index(((P1 *)this)->i, 3) ].local_value = ((int)((P1 *)this)->j);
#ifdef VAR_RANGES
		logval("state[:init::i].local_value", ((int)now.state[ Index(((int)((P1 *)this)->i), 3) ].local_value));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 6: /* STATE 5 - temp.pml:134 - [random = ((random*16807)%2147483647)] (0:0:1 - 1) */
		IfNotBlocked
		reached[1][5] = 1;
		(trpt+1)->bup.oval = now.random;
		now.random = ((now.random*16807)%2147483647);
#ifdef VAR_RANGES
		logval("random", now.random);
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 7: /* STATE 6 - temp.pml:136 - [(((random>>30)&1))] (0:0:0 - 1) */
		IfNotBlocked
		reached[1][6] = 1;
		if (!(((now.random>>30)&1)))
			continue;
		_m = 3; goto P999; /* 0 */
	case 8: /* STATE 7 - temp.pml:136 - [j = (j+1)] (0:0:1 - 1) */
		IfNotBlocked
		reached[1][7] = 1;
		(trpt+1)->bup.oval = ((int)((P1 *)this)->j);
		((P1 *)this)->j = (((int)((P1 *)this)->j)+1);
#ifdef VAR_RANGES
		logval(":init::j", ((int)((P1 *)this)->j));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 9: /* STATE 12 - temp.pml:139 - [state[i].view[i] = 1] (0:0:1 - 3) */
		IfNotBlocked
		reached[1][12] = 1;
		(trpt+1)->bup.oval = ((int)now.state[ Index(((int)((P1 *)this)->i), 3) ].view[ Index(((int)((P1 *)this)->i), 3) ]);
		now.state[ Index(((P1 *)this)->i, 3) ].view[ Index(((P1 *)this)->i, 3) ] = 1;
#ifdef VAR_RANGES
		logval("state[:init::i].view[:init::i]", ((int)now.state[ Index(((int)((P1 *)this)->i), 3) ].view[ Index(((int)((P1 *)this)->i), 3) ]));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 10: /* STATE 13 - temp.pml:132 - [i = (i+1)] (0:0:1 - 1) */
		IfNotBlocked
		reached[1][13] = 1;
		(trpt+1)->bup.oval = ((int)((P1 *)this)->i);
		((P1 *)this)->i = (((int)((P1 *)this)->i)+1);
#ifdef VAR_RANGES
		logval(":init::i", ((int)((P1 *)this)->i));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 11: /* STATE 20 - temp.pml:224 - [i = 1] (0:0:1 - 1) */
		IfNotBlocked
		reached[1][20] = 1;
		(trpt+1)->bup.oval = ((int)((P1 *)this)->i);
		((P1 *)this)->i = 1;
#ifdef VAR_RANGES
		logval(":init::i", ((int)((P1 *)this)->i));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 12: /* STATE 21 - temp.pml:224 - [((i<=3))] (0:0:0 - 1) */
		IfNotBlocked
		reached[1][21] = 1;
		if (!((((int)((P1 *)this)->i)<=3)))
			continue;
		_m = 3; goto P999; /* 0 */
	case 13: /* STATE 22 - temp.pml:225 - [(run Process())] (0:0:0 - 1) */
		IfNotBlocked
		reached[1][22] = 1;
		if (!(addproc(II, 0)))
			continue;
		_m = 3; goto P999; /* 0 */
	case 14: /* STATE 23 - temp.pml:224 - [i = (i+1)] (0:0:1 - 1) */
		IfNotBlocked
		reached[1][23] = 1;
		(trpt+1)->bup.oval = ((int)((P1 *)this)->i);
		((P1 *)this)->i = (((int)((P1 *)this)->i)+1);
#ifdef VAR_RANGES
		logval(":init::i", ((int)((P1 *)this)->i));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 15: /* STATE 30 - temp.pml:230 - [((token==0))] (0:0:0 - 1) */
		IfNotBlocked
		reached[1][30] = 1;
		if (!((((int)now.token)==0)))
			continue;
		_m = 3; goto P999; /* 0 */
	case 16: /* STATE 31 - temp.pml:144 - [round_id = (round_id+1)] (0:0:1 - 1) */
		IfNotBlocked
		reached[1][31] = 1;
		(trpt+1)->bup.oval = round_id;
		round_id = (round_id+1);
#ifdef VAR_RANGES
		logval("round_id", round_id);
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 17: /* STATE 32 - temp.pml:145 - [printf('MSC: new round, id=%d\\n',round_id)] (0:0:0 - 1) */
		IfNotBlocked
		reached[1][32] = 1;
		Printf("MSC: new round, id=%d\n", round_id);
		_m = 3; goto P999; /* 0 */
	case 18: /* STATE 33 - temp.pml:146 - [printf('random=%d\\n',random)] (0:0:0 - 1) */
		IfNotBlocked
		reached[1][33] = 1;
		Printf("random=%d\n", now.random);
		_m = 3; goto P999; /* 0 */
	case 19: /* STATE 34 - temp.pml:149 - [((remaining_asynchronous_rounds==0))] (65:0:2 - 1) */
		IfNotBlocked
		reached[1][34] = 1;
		if (!((((int)((P1 *)this)->remaining_asynchronous_rounds)==0)))
			continue;
		/* merge: synchronous = 1(65, 35, 65) */
		reached[1][35] = 1;
		(trpt+1)->bup.ovals = grab_ints(2);
		(trpt+1)->bup.ovals[0] = ((int)((P1 *)this)->synchronous);
		((P1 *)this)->synchronous = 1;
#ifdef VAR_RANGES
		logval(":init::synchronous", ((int)((P1 *)this)->synchronous));
#endif
		;
		/* merge: .(goto)(65, 39, 65) */
		reached[1][39] = 1;
		;
		/* merge: i = 0(65, 40, 65) */
		reached[1][40] = 1;
		(trpt+1)->bup.ovals[1] = ((int)((P1 *)this)->i);
		((P1 *)this)->i = 0;
#ifdef VAR_RANGES
		logval(":init::i", ((int)((P1 *)this)->i));
#endif
		;
		/* merge: .(goto)(0, 66, 65) */
		reached[1][66] = 1;
		;
		_m = 3; goto P999; /* 4 */
	case 20: /* STATE 37 - temp.pml:150 - [remaining_asynchronous_rounds = (remaining_asynchronous_rounds-1)] (0:65:2 - 1) */
		IfNotBlocked
		reached[1][37] = 1;
		(trpt+1)->bup.ovals = grab_ints(2);
		(trpt+1)->bup.ovals[0] = ((int)((P1 *)this)->remaining_asynchronous_rounds);
		((P1 *)this)->remaining_asynchronous_rounds = (((int)((P1 *)this)->remaining_asynchronous_rounds)-1);
#ifdef VAR_RANGES
		logval(":init::remaining_asynchronous_rounds", ((int)((P1 *)this)->remaining_asynchronous_rounds));
#endif
		;
		/* merge: .(goto)(65, 39, 65) */
		reached[1][39] = 1;
		;
		/* merge: i = 0(65, 40, 65) */
		reached[1][40] = 1;
		(trpt+1)->bup.ovals[1] = ((int)((P1 *)this)->i);
		((P1 *)this)->i = 0;
#ifdef VAR_RANGES
		logval(":init::i", ((int)((P1 *)this)->i));
#endif
		;
		/* merge: .(goto)(0, 66, 65) */
		reached[1][66] = 1;
		;
		_m = 3; goto P999; /* 3 */
	case 21: /* STATE 40 - temp.pml:153 - [i = 0] (0:65:1 - 3) */
		IfNotBlocked
		reached[1][40] = 1;
		(trpt+1)->bup.oval = ((int)((P1 *)this)->i);
		((P1 *)this)->i = 0;
#ifdef VAR_RANGES
		logval(":init::i", ((int)((P1 *)this)->i));
#endif
		;
		/* merge: .(goto)(0, 66, 65) */
		reached[1][66] = 1;
		;
		_m = 3; goto P999; /* 1 */
	case 22: /* STATE 41 - temp.pml:153 - [((i<=(3-1)))] (59:0:1 - 1) */
		IfNotBlocked
		reached[1][41] = 1;
		if (!((((int)((P1 *)this)->i)<=(3-1))))
			continue;
		/* merge: j = 0(0, 42, 59) */
		reached[1][42] = 1;
		(trpt+1)->bup.oval = ((int)((P1 *)this)->j);
		((P1 *)this)->j = 0;
#ifdef VAR_RANGES
		logval(":init::j", ((int)((P1 *)this)->j));
#endif
		;
		/* merge: .(goto)(0, 60, 59) */
		reached[1][60] = 1;
		;
		_m = 3; goto P999; /* 2 */
	case 23: /* STATE 43 - temp.pml:154 - [((j<=(3-1)))] (0:0:0 - 1) */
		IfNotBlocked
		reached[1][43] = 1;
		if (!((((int)((P1 *)this)->j)<=(3-1))))
			continue;
		_m = 3; goto P999; /* 0 */
	case 24: /* STATE 44 - temp.pml:156 - [(synchronous)] (0:0:0 - 1) */
		IfNotBlocked
		reached[1][44] = 1;
		if (!(((int)((P1 *)this)->synchronous)))
			continue;
		_m = 3; goto P999; /* 0 */
	case 25: /* STATE 45 - temp.pml:156 - [state[i].received_message[j] = 1] (0:0:1 - 1) */
		IfNotBlocked
		reached[1][45] = 1;
		(trpt+1)->bup.oval = ((int)now.state[ Index(((int)((P1 *)this)->i), 3) ].received_message[ Index(((int)((P1 *)this)->j), 3) ]);
		now.state[ Index(((P1 *)this)->i, 3) ].received_message[ Index(((P1 *)this)->j, 3) ] = 1;
#ifdef VAR_RANGES
		logval("state[:init::i].received_message[:init::j]", ((int)now.state[ Index(((int)((P1 *)this)->i), 3) ].received_message[ Index(((int)((P1 *)this)->j), 3) ]));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 26: /* STATE 47 - temp.pml:159 - [((i!=j))] (0:0:0 - 1) */
		IfNotBlocked
		reached[1][47] = 1;
		if (!((((int)((P1 *)this)->i)!=((int)((P1 *)this)->j))))
			continue;
		_m = 3; goto P999; /* 0 */
	case 27: /* STATE 48 - temp.pml:160 - [random = ((random*16807)%2147483647)] (0:0:1 - 1) */
		IfNotBlocked
		reached[1][48] = 1;
		(trpt+1)->bup.oval = now.random;
		now.random = ((now.random*16807)%2147483647);
#ifdef VAR_RANGES
		logval("random", now.random);
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 28: /* STATE 49 - temp.pml:161 - [state[i].received_message[j] = ((random>>30)&1)] (0:0:1 - 1) */
		IfNotBlocked
		reached[1][49] = 1;
		(trpt+1)->bup.oval = ((int)now.state[ Index(((int)((P1 *)this)->i), 3) ].received_message[ Index(((int)((P1 *)this)->j), 3) ]);
		now.state[ Index(((P1 *)this)->i, 3) ].received_message[ Index(((P1 *)this)->j, 3) ] = ((now.random>>30)&1);
#ifdef VAR_RANGES
		logval("state[:init::i].received_message[:init::j]", ((int)now.state[ Index(((int)((P1 *)this)->i), 3) ].received_message[ Index(((int)((P1 *)this)->j), 3) ]));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 29: /* STATE 51 - temp.pml:162 - [state[i].received_message[j] = 1] (0:0:1 - 1) */
		IfNotBlocked
		reached[1][51] = 1;
		(trpt+1)->bup.oval = ((int)now.state[ Index(((int)((P1 *)this)->i), 3) ].received_message[ Index(((int)((P1 *)this)->j), 3) ]);
		now.state[ Index(((P1 *)this)->i, 3) ].received_message[ Index(((P1 *)this)->j, 3) ] = 1;
#ifdef VAR_RANGES
		logval("state[:init::i].received_message[:init::j]", ((int)now.state[ Index(((int)((P1 *)this)->i), 3) ].received_message[ Index(((int)((P1 *)this)->j), 3) ]));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 30: /* STATE 56 - temp.pml:154 - [j = (j+1)] (0:59:1 - 5) */
		IfNotBlocked
		reached[1][56] = 1;
		(trpt+1)->bup.oval = ((int)((P1 *)this)->j);
		((P1 *)this)->j = (((int)((P1 *)this)->j)+1);
#ifdef VAR_RANGES
		logval(":init::j", ((int)((P1 *)this)->j));
#endif
		;
		/* merge: .(goto)(0, 60, 59) */
		reached[1][60] = 1;
		;
		_m = 3; goto P999; /* 1 */
	case 31: /* STATE 62 - temp.pml:153 - [i = (i+1)] (0:65:1 - 3) */
		IfNotBlocked
		reached[1][62] = 1;
		(trpt+1)->bup.oval = ((int)((P1 *)this)->i);
		((P1 *)this)->i = (((int)((P1 *)this)->i)+1);
#ifdef VAR_RANGES
		logval(":init::i", ((int)((P1 *)this)->i));
#endif
		;
		/* merge: .(goto)(0, 66, 65) */
		reached[1][66] = 1;
		;
		_m = 3; goto P999; /* 1 */
	case 32: /* STATE 69 - temp.pml:232 - [token = 3] (0:0:1 - 3) */
		IfNotBlocked
		reached[1][69] = 1;
		(trpt+1)->bup.oval = ((int)now.token);
		now.token = 3;
#ifdef VAR_RANGES
		logval("token", ((int)now.token));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 33: /* STATE 70 - temp.pml:233 - [((token==0))] (0:0:0 - 1) */
		IfNotBlocked
		reached[1][70] = 1;
		if (!((((int)now.token)==0)))
			continue;
		_m = 3; goto P999; /* 0 */
	case 34: /* STATE 71 - temp.pml:234 - [token = 3] (0:0:1 - 1) */
		IfNotBlocked
		reached[1][71] = 1;
		(trpt+1)->bup.oval = ((int)now.token);
		now.token = 3;
#ifdef VAR_RANGES
		logval("token", ((int)now.token));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 35: /* STATE 75 - temp.pml:236 - [-end-] (0:0:0 - 1) */
		IfNotBlocked
		reached[1][75] = 1;
		if (!delproc(1, II)) continue;
		_m = 3; goto P999; /* 0 */

		 /* PROC Process */
	case 36: /* STATE 1 - temp.pml:206 - [printf('MSC: P%d has initial value x=%d\\n',_pid,state[(_pid-1)].local_value)] (0:0:0 - 1) */
		IfNotBlocked
		reached[0][1] = 1;
		Printf("MSC: P%d has initial value x=%d\n", ((int)((P0 *)this)->_pid), ((int)now.state[ Index((((int)((P0 *)this)->_pid)-1), 3) ].local_value));
		_m = 3; goto P999; /* 0 */
	case 37: /* STATE 2 - temp.pml:189 - [((token==_pid))] (0:0:0 - 1) */
		IfNotBlocked
		reached[0][2] = 1;
		if (!((((int)now.token)==((int)((P0 *)this)->_pid))))
			continue;
		_m = 3; goto P999; /* 0 */
	case 38: /* STATE 4 - temp.pml:56 - [message.value = state[(_pid-1)].local_value] (0:0:1 - 1) */
		IfNotBlocked
		reached[0][4] = 1;
		(trpt+1)->bup.oval = ((int)((P0 *)this)->_message.value);
		((P0 *)this)->_message.value = ((int)now.state[ Index((((int)((P0 *)this)->_pid)-1), 3) ].local_value);
#ifdef VAR_RANGES
		logval("Process:message.value", ((int)((P0 *)this)->_message.value));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 39: /* STATE 5 - temp.pml:57 - [i = 0] (0:0:1 - 1) */
		IfNotBlocked
		reached[0][5] = 1;
		(trpt+1)->bup.oval = ((int)((P0 *)this)->i);
		((P0 *)this)->i = 0;
#ifdef VAR_RANGES
		logval("Process:i", ((int)((P0 *)this)->i));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 40: /* STATE 6 - temp.pml:57 - [((i<=(3-1)))] (0:0:0 - 1) */
		IfNotBlocked
		reached[0][6] = 1;
		if (!((((int)((P0 *)this)->i)<=(3-1))))
			continue;
		_m = 3; goto P999; /* 0 */
	case 41: /* STATE 7 - temp.pml:58 - [message.view[i] = state[(_pid-1)].view[i]] (0:0:1 - 1) */
		IfNotBlocked
		reached[0][7] = 1;
		(trpt+1)->bup.oval = ((int)((P0 *)this)->_message.view[ Index(((int)((P0 *)this)->i), 3) ]);
		((P0 *)this)->_message.view[ Index(((P0 *)this)->i, 3) ] = ((int)now.state[ Index((((int)((P0 *)this)->_pid)-1), 3) ].view[ Index(((int)((P0 *)this)->i), 3) ]);
#ifdef VAR_RANGES
		logval("Process:message.view[Process:i]", ((int)((P0 *)this)->_message.view[ Index(((int)((P0 *)this)->i), 3) ]));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 42: /* STATE 8 - temp.pml:57 - [i = (i+1)] (0:0:1 - 1) */
		IfNotBlocked
		reached[0][8] = 1;
		(trpt+1)->bup.oval = ((int)((P0 *)this)->i);
		((P0 *)this)->i = (((int)((P0 *)this)->i)+1);
#ifdef VAR_RANGES
		logval("Process:i", ((int)((P0 *)this)->i));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 43: /* STATE 15 - temp.pml:170 - [messages[(_pid-1)].value = message.value] (0:0:1 - 1) */
		IfNotBlocked
		reached[0][15] = 1;
		(trpt+1)->bup.oval = ((int)now.messages[ Index((((int)((P0 *)this)->_pid)-1), 3) ].value);
		now.messages[ Index((((P0 *)this)->_pid-1), 3) ].value = ((int)((P0 *)this)->_message.value);
#ifdef VAR_RANGES
		logval("messages[(_pid-1)].value", ((int)now.messages[ Index((((int)((P0 *)this)->_pid)-1), 3) ].value));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 44: /* STATE 16 - temp.pml:171 - [j = 0] (0:0:1 - 1) */
		IfNotBlocked
		reached[0][16] = 1;
		(trpt+1)->bup.oval = ((int)((P0 *)this)->j);
		((P0 *)this)->j = 0;
#ifdef VAR_RANGES
		logval("Process:j", ((int)((P0 *)this)->j));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 45: /* STATE 17 - temp.pml:171 - [((j<=(3-1)))] (0:0:0 - 1) */
		IfNotBlocked
		reached[0][17] = 1;
		if (!((((int)((P0 *)this)->j)<=(3-1))))
			continue;
		_m = 3; goto P999; /* 0 */
	case 46: /* STATE 18 - temp.pml:172 - [messages[(_pid-1)].view[j] = message.view[j]] (0:0:1 - 1) */
		IfNotBlocked
		reached[0][18] = 1;
		(trpt+1)->bup.oval = ((int)now.messages[ Index((((int)((P0 *)this)->_pid)-1), 3) ].view[ Index(((int)((P0 *)this)->j), 3) ]);
		now.messages[ Index((((P0 *)this)->_pid-1), 3) ].view[ Index(((P0 *)this)->j, 3) ] = ((int)((P0 *)this)->_message.view[ Index(((int)((P0 *)this)->j), 3) ]);
#ifdef VAR_RANGES
		logval("messages[(_pid-1)].view[Process:j]", ((int)now.messages[ Index((((int)((P0 *)this)->_pid)-1), 3) ].view[ Index(((int)((P0 *)this)->j), 3) ]));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 47: /* STATE 19 - temp.pml:171 - [j = (j+1)] (0:0:1 - 1) */
		IfNotBlocked
		reached[0][19] = 1;
		(trpt+1)->bup.oval = ((int)((P0 *)this)->j);
		((P0 *)this)->j = (((int)((P0 *)this)->j)+1);
#ifdef VAR_RANGES
		logval("Process:j", ((int)((P0 *)this)->j));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 48: /* STATE 26 - temp.pml:197 - [token = (token-1)] (0:0:1 - 1) */
		IfNotBlocked
		reached[0][26] = 1;
		(trpt+1)->bup.oval = ((int)now.token);
		now.token = (((int)now.token)-1);
#ifdef VAR_RANGES
		logval("token", ((int)now.token));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 49: /* STATE 27 - temp.pml:198 - [((token==_pid))] (0:0:0 - 1) */
		IfNotBlocked
		reached[0][27] = 1;
		if (!((((int)now.token)==((int)((P0 *)this)->_pid))))
			continue;
		_m = 3; goto P999; /* 0 */
	case 50: /* STATE 148 - temp.pml:63 - [D_STEP] */
		if (!(((boq == -1 && (((int)((P0 *)this)->round)<(4-1)))) || ((boq == -1 && (((int)((P0 *)this)->round)==(4-1))))))
			continue;

		reached[0][148] = 1;
		reached[0][t->st] = 1;
		reached[0][tt] = 1;

		if (TstOnly) return 1;

		sv_save();
		S_136_0: /* 2 */
S_028_0: /* 2 */
		if (!((((int)((P0 *)this)->round)<(4-1))))
			goto S_136_1;
S_029_0: /* 2 */
		((P0 *)this)->round = (((int)((P0 *)this)->round)+1);
#ifdef VAR_RANGES
		logval("Process:round", ((int)((P0 *)this)->round));
#endif
		;
S_030_0: /* 2 */
		((P0 *)this)->i = 0;
#ifdef VAR_RANGES
		logval("Process:i", ((int)((P0 *)this)->i));
#endif
		;
S_067_0: /* 2 */
S_066_0: /* 2 */
S_031_0: /* 2 */
		if (!((((int)((P0 *)this)->i)<=(3-1))))
			goto S_066_1;
S_061_0: /* 2 */
S_032_0: /* 2 */
		if (!(((int)now.state[ Index((((int)((P0 *)this)->_pid)-1), 3) ].received_message[ Index(((int)((P0 *)this)->i), 3) ])))
			goto S_061_1;
S_043_0: /* 2 */
S_033_0: /* 2 */
		((P0 *)this)->_message.value = ((int)now.messages[ Index(((int)((P0 *)this)->i), 3) ].value);
#ifdef VAR_RANGES
		logval("Process:message.value", ((int)((P0 *)this)->_message.value));
#endif
		;
S_034_0: /* 2 */
		((P0 *)this)->j = 0;
#ifdef VAR_RANGES
		logval("Process:j", ((int)((P0 *)this)->j));
#endif
		;
S_041_0: /* 2 */
S_040_0: /* 2 */
S_035_0: /* 2 */
		if (!((((int)((P0 *)this)->j)<=(3-1))))
			goto S_040_1;
S_036_0: /* 2 */
		((P0 *)this)->_message.view[ Index(((P0 *)this)->j, 3) ] = ((int)now.messages[ Index(((int)((P0 *)this)->i), 3) ].view[ Index(((int)((P0 *)this)->j), 3) ]);
#ifdef VAR_RANGES
		logval("Process:message.view[Process:j]", ((int)((P0 *)this)->_message.view[ Index(((int)((P0 *)this)->j), 3) ]));
#endif
		;
S_037_0: /* 2 */
		((P0 *)this)->j = (((int)((P0 *)this)->j)+1);
#ifdef VAR_RANGES
		logval("Process:j", ((int)((P0 *)this)->j));
#endif
		;
		goto S_041_0; /* ';' */
S_040_1: /* 3 */
S_038_0: /* 2 */
		/* else */;
S_039_0: /* 2 */
		goto S_048_0;	/* 'goto' */
S_040_2: /* 3 */
		Uerror("blocking sel in d_step (nr.0, near line 180)");
S_042_0: /* 2 */
		goto S_048_0;	/* 'break' */
		goto S_048_0;
S_048_0: /* 2 */
S_044_0: /* 2 */
		if (!((((int)now.state[ Index((((int)((P0 *)this)->_pid)-1), 3) ].local_value)<((int)((P0 *)this)->_message.value))))
			goto S_048_1;
S_045_0: /* 2 */
		now.state[ Index((((P0 *)this)->_pid-1), 3) ].local_value = ((int)((P0 *)this)->_message.value);
#ifdef VAR_RANGES
		logval("state[(_pid-1)].local_value", ((int)now.state[ Index((((int)((P0 *)this)->_pid)-1), 3) ].local_value));
#endif
		;
		goto S_049_0;
S_048_1: /* 3 */
S_046_0: /* 2 */
		/* else */;
S_047_0: /* 2 */
		if (!(1))
			Uerror("block in d_step seq");
		goto S_049_0;
S_048_2: /* 3 */
		Uerror("blocking sel in d_step (nr.1, near line 71)");
S_049_0: /* 2 */
S_050_0: /* 2 */
		((P0 *)this)->j = 0;
#ifdef VAR_RANGES
		logval("Process:j", ((int)((P0 *)this)->j));
#endif
		;
S_057_0: /* 2 */
S_056_0: /* 2 */
S_051_0: /* 2 */
		if (!((((int)((P0 *)this)->j)<=(3-1))))
			goto S_056_1;
S_052_0: /* 2 */
		now.state[ Index((((P0 *)this)->_pid-1), 3) ].view[ Index(((P0 *)this)->j, 3) ] = (((int)now.state[ Index((((int)((P0 *)this)->_pid)-1), 3) ].view[ Index(((int)((P0 *)this)->j), 3) ])||((int)((P0 *)this)->_message.view[ Index(((int)((P0 *)this)->j), 3) ]));
#ifdef VAR_RANGES
		logval("state[(_pid-1)].view[Process:j]", ((int)now.state[ Index((((int)((P0 *)this)->_pid)-1), 3) ].view[ Index(((int)((P0 *)this)->j), 3) ]));
#endif
		;
S_053_0: /* 2 */
		((P0 *)this)->j = (((int)((P0 *)this)->j)+1);
#ifdef VAR_RANGES
		logval("Process:j", ((int)((P0 *)this)->j));
#endif
		;
		goto S_057_0; /* ';' */
S_056_1: /* 3 */
S_054_0: /* 2 */
		/* else */;
S_055_0: /* 2 */
		goto S_063_0;	/* 'goto' */
S_056_2: /* 3 */
		Uerror("blocking sel in d_step (nr.2, near line 79)");
S_058_0: /* 2 */
		goto S_063_0;	/* 'break' */
		goto S_062_0;
S_061_1: /* 3 */
S_059_0: /* 2 */
		/* else */;
S_060_0: /* 2 */
		if (!(1))
			Uerror("block in d_step seq");
		goto S_062_0;
S_061_2: /* 3 */
		Uerror("blocking sel in d_step (nr.3, near line 68)");
S_062_0: /* 2 */
S_063_0: /* 2 */
		((P0 *)this)->i = (((int)((P0 *)this)->i)+1);
#ifdef VAR_RANGES
		logval("Process:i", ((int)((P0 *)this)->i));
#endif
		;
		goto S_067_0; /* ';' */
S_066_1: /* 3 */
S_064_0: /* 2 */
		/* else */;
S_065_0: /* 2 */
		goto S_138_0;	/* 'goto' */
S_066_2: /* 3 */
		Uerror("blocking sel in d_step (nr.4, near line 82)");
S_068_0: /* 2 */
		goto S_138_0;	/* 'break' */
		goto S_137_0;
S_136_1: /* 3 */
S_069_0: /* 2 */
		if (!((((int)((P0 *)this)->round)==(4-1))))
			goto S_136_2;
S_070_0: /* 2 */
		((P0 *)this)->round = (((int)((P0 *)this)->round)+1);
#ifdef VAR_RANGES
		logval("Process:round", ((int)((P0 *)this)->round));
#endif
		;
S_071_0: /* 2 */
		((P0 *)this)->check = 1;
#ifdef VAR_RANGES
		logval("Process:check", ((int)((P0 *)this)->check));
#endif
		;
S_072_0: /* 2 */
		((P0 *)this)->i = 0;
#ifdef VAR_RANGES
		logval("Process:i", ((int)((P0 *)this)->i));
#endif
		;
S_084_0: /* 2 */
S_083_0: /* 2 */
S_073_0: /* 2 */
		if (!((((int)((P0 *)this)->i)<=(3-1))))
			goto S_083_1;
S_078_0: /* 2 */
S_074_0: /* 2 */
		if (!( !(((int)now.state[ Index((((int)((P0 *)this)->_pid)-1), 3) ].view[ Index(((int)((P0 *)this)->i), 3) ]))))
			goto S_078_1;
S_075_0: /* 2 */
		((P0 *)this)->check = 0;
#ifdef VAR_RANGES
		logval("Process:check", ((int)((P0 *)this)->check));
#endif
		;
		goto S_079_0;
S_078_1: /* 3 */
S_076_0: /* 2 */
		/* else */;
S_077_0: /* 2 */
		if (!(1))
			Uerror("block in d_step seq");
		goto S_079_0;
S_078_2: /* 3 */
		Uerror("blocking sel in d_step (nr.5, near line 88)");
S_079_0: /* 2 */
S_080_0: /* 2 */
		((P0 *)this)->i = (((int)((P0 *)this)->i)+1);
#ifdef VAR_RANGES
		logval("Process:i", ((int)((P0 *)this)->i));
#endif
		;
		goto S_084_0; /* ';' */
S_083_1: /* 3 */
S_081_0: /* 2 */
		/* else */;
S_082_0: /* 2 */
		goto S_127_0;	/* 'goto' */
S_083_2: /* 3 */
		Uerror("blocking sel in d_step (nr.6, near line 92)");
S_085_0: /* 2 */
		goto S_127_0;	/* 'break' */
S_127_0: /* 2 */
S_086_0: /* 2 */
		if (!(((int)((P0 *)this)->check)))
			goto S_127_1;
S_087_0: /* 2 */
		((P0 *)this)->i = 0;
#ifdef VAR_RANGES
		logval("Process:i", ((int)((P0 *)this)->i));
#endif
		;
S_123_0: /* 2 */
S_122_0: /* 2 */
S_088_0: /* 2 */
		if (!((((int)((P0 *)this)->i)<=(3-1))))
			goto S_122_1;
S_117_0: /* 2 */
S_089_0: /* 2 */
		if (!(((int)now.state[ Index((((int)((P0 *)this)->_pid)-1), 3) ].received_message[ Index(((int)((P0 *)this)->i), 3) ])))
			goto S_117_1;
S_100_0: /* 2 */
S_090_0: /* 2 */
		((P0 *)this)->_message.value = ((int)now.messages[ Index(((int)((P0 *)this)->i), 3) ].value);
#ifdef VAR_RANGES
		logval("Process:message.value", ((int)((P0 *)this)->_message.value));
#endif
		;
S_091_0: /* 2 */
		((P0 *)this)->j = 0;
#ifdef VAR_RANGES
		logval("Process:j", ((int)((P0 *)this)->j));
#endif
		;
S_098_0: /* 2 */
S_097_0: /* 2 */
S_092_0: /* 2 */
		if (!((((int)((P0 *)this)->j)<=(3-1))))
			goto S_097_1;
S_093_0: /* 2 */
		((P0 *)this)->_message.view[ Index(((P0 *)this)->j, 3) ] = ((int)now.messages[ Index(((int)((P0 *)this)->i), 3) ].view[ Index(((int)((P0 *)this)->j), 3) ]);
#ifdef VAR_RANGES
		logval("Process:message.view[Process:j]", ((int)((P0 *)this)->_message.view[ Index(((int)((P0 *)this)->j), 3) ]));
#endif
		;
S_094_0: /* 2 */
		((P0 *)this)->j = (((int)((P0 *)this)->j)+1);
#ifdef VAR_RANGES
		logval("Process:j", ((int)((P0 *)this)->j));
#endif
		;
		goto S_098_0; /* ';' */
S_097_1: /* 3 */
S_095_0: /* 2 */
		/* else */;
S_096_0: /* 2 */
		goto S_101_0;	/* 'goto' */
S_097_2: /* 3 */
		Uerror("blocking sel in d_step (nr.7, near line 180)");
S_099_0: /* 2 */
		goto S_101_0;	/* 'break' */
		goto S_101_0;
S_101_0: /* 2 */
		((P0 *)this)->j = 0;
#ifdef VAR_RANGES
		logval("Process:j", ((int)((P0 *)this)->j));
#endif
		;
S_113_0: /* 2 */
S_112_0: /* 2 */
S_102_0: /* 2 */
		if (!((((int)((P0 *)this)->j)<=(3-1))))
			goto S_112_1;
S_107_0: /* 2 */
S_103_0: /* 2 */
		if (!( !(((int)((P0 *)this)->_message.view[ Index(((int)((P0 *)this)->j), 3) ]))))
			goto S_107_1;
S_104_0: /* 2 */
		((P0 *)this)->check = 0;
#ifdef VAR_RANGES
		logval("Process:check", ((int)((P0 *)this)->check));
#endif
		;
		goto S_108_0;
S_107_1: /* 3 */
S_105_0: /* 2 */
		/* else */;
S_106_0: /* 2 */
		if (!(1))
			Uerror("block in d_step seq");
		goto S_108_0;
S_107_2: /* 3 */
		Uerror("blocking sel in d_step (nr.8, near line 101)");
S_108_0: /* 2 */
S_109_0: /* 2 */
		((P0 *)this)->j = (((int)((P0 *)this)->j)+1);
#ifdef VAR_RANGES
		logval("Process:j", ((int)((P0 *)this)->j));
#endif
		;
		goto S_113_0; /* ';' */
S_112_1: /* 3 */
S_110_0: /* 2 */
		/* else */;
S_111_0: /* 2 */
		goto S_119_0;	/* 'goto' */
S_112_2: /* 3 */
		Uerror("blocking sel in d_step (nr.9, near line 105)");
S_114_0: /* 2 */
		goto S_119_0;	/* 'break' */
		goto S_118_0;
S_117_1: /* 3 */
S_115_0: /* 2 */
		/* else */;
S_116_0: /* 2 */
		if (!(1))
			Uerror("block in d_step seq");
		goto S_118_0;
S_117_2: /* 3 */
		Uerror("blocking sel in d_step (nr.10, near line 97)");
S_118_0: /* 2 */
S_119_0: /* 2 */
		((P0 *)this)->i = (((int)((P0 *)this)->i)+1);
#ifdef VAR_RANGES
		logval("Process:i", ((int)((P0 *)this)->i));
#endif
		;
		goto S_123_0; /* ';' */
S_122_1: /* 3 */
S_120_0: /* 2 */
		/* else */;
S_121_0: /* 2 */
		goto S_134_0;	/* 'goto' */
S_122_2: /* 3 */
		Uerror("blocking sel in d_step (nr.11, near line 108)");
S_124_0: /* 2 */
		goto S_134_0;	/* 'break' */
		goto S_128_0;
S_127_1: /* 3 */
S_125_0: /* 2 */
		/* else */;
S_126_0: /* 2 */
		if (!(1))
			Uerror("block in d_step seq");
		goto S_128_0;
S_127_2: /* 3 */
		Uerror("blocking sel in d_step (nr.12, near line 94)");
S_128_0: /* 2 */
S_134_0: /* 2 */
S_129_0: /* 2 */
		if (!(((int)((P0 *)this)->check)))
			goto S_134_1;
S_130_0: /* 2 */
		now.state[ Index((((P0 *)this)->_pid-1), 3) ].decision_value = ((int)now.state[ Index((((int)((P0 *)this)->_pid)-1), 3) ].local_value);
#ifdef VAR_RANGES
		logval("state[(_pid-1)].decision_value", ((int)now.state[ Index((((int)((P0 *)this)->_pid)-1), 3) ].decision_value));
#endif
		;
S_131_0: /* 2 */
		Printf("MSC: P%d decides %d on round %d\n", ((int)((P0 *)this)->_pid), ((int)now.state[ Index((((int)((P0 *)this)->_pid)-1), 3) ].decision_value), round_id);
		goto S_135_0;
S_134_1: /* 3 */
S_132_0: /* 2 */
		if (!( !(((int)((P0 *)this)->check))))
			goto S_134_2;
S_133_0: /* 2 */
		Printf("MSC: P%d decides ABORT on round %d\n", ((int)((P0 *)this)->_pid), round_id);
		goto S_135_0;
S_134_2: /* 3 */
		Uerror("blocking sel in d_step (nr.13, near line 112)");
S_135_0: /* 2 */
		goto S_137_0;
S_136_2: /* 3 */
		Uerror("blocking sel in d_step (nr.14, near line 64)");
S_137_0: /* 2 */
S_138_0: /* 2 */
		((P0 *)this)->i = 0;
#ifdef VAR_RANGES
		logval("Process:i", ((int)((P0 *)this)->i));
#endif
		;
S_145_0: /* 2 */
S_144_0: /* 2 */
S_139_0: /* 2 */
		if (!((((int)((P0 *)this)->i)<=(3-1))))
			goto S_144_1;
S_140_0: /* 2 */
		spin_assert((((((int)now.state[ Index((((int)((P0 *)this)->_pid)-1), 3) ].decision_value)==((int)now.state[ Index(((int)((P0 *)this)->i), 3) ].decision_value))||(((int)now.state[ Index((((int)((P0 *)this)->_pid)-1), 3) ].decision_value)==0))||(((int)now.state[ Index(((int)((P0 *)this)->i), 3) ].decision_value)==0)), "(((state[(_pid-1)].decision_value==state[i].decision_value)||(state[(_pid-1)].decision_value==0))||(state[i].decision_value==0))", II, tt, t);
S_141_0: /* 2 */
		((P0 *)this)->i = (((int)((P0 *)this)->i)+1);
#ifdef VAR_RANGES
		logval("Process:i", ((int)((P0 *)this)->i));
#endif
		;
		goto S_145_0; /* ';' */
S_144_1: /* 3 */
S_142_0: /* 2 */
		/* else */;
S_143_0: /* 2 */
		goto S_146_0;	/* 'goto' */
S_144_2: /* 3 */
		Uerror("blocking sel in d_step (nr.15, near line 126)");
S_146_0: /* 2 */
		goto S_150_0;	/* 'break' */
S_150_0: /* 1 */

#if defined(C_States) && (HAS_TRACK==1)
		c_update((uchar *) &(now.c_state[0]));
#endif
		_m = 3; goto P999;

	case 51: /* STATE 150 - temp.pml:193 - [token = (token-1)] (0:0:1 - 1) */
		IfNotBlocked
		reached[0][150] = 1;
		(trpt+1)->bup.oval = ((int)now.token);
		now.token = (((int)now.token)-1);
#ifdef VAR_RANGES
		logval("token", ((int)now.token));
#endif
		;
		_m = 3; goto P999; /* 0 */
	case 52: /* STATE 155 - temp.pml:215 - [-end-] (0:0:0 - 1) */
		IfNotBlocked
		reached[0][155] = 1;
		if (!delproc(1, II)) continue;
		_m = 3; goto P999; /* 0 */
	case  _T5:	/* np_ */
		if (!((!(trpt->o_pm&4) && !(trpt->tau&128))))
			continue;
		/* else fall through */
	case  _T2:	/* true */
		_m = 3; goto P999;
#undef rand
	}


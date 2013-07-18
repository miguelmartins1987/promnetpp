	switch (t->back) {
	default: Uerror("bad return move");
	case  0: goto R999; /* nothing to undo */

		 /* PROC :init: */

	case 3: /* STATE 2 */
		;
		((P1 *)this)->i = trpt->bup.ovals[1];
		((P1 *)this)->j = trpt->bup.ovals[0];
		;
		ungrab_ints(trpt->bup.ovals, 2);
		goto R999;
;
		;
		
	case 5: /* STATE 4 */
		;
		now.state[ Index(((P1 *)this)->i, 3) ].local_value = trpt->bup.oval;
		;
		goto R999;

	case 6: /* STATE 5 */
		;
		now.random = trpt->bup.oval;
		;
		goto R999;
;
		;
		
	case 8: /* STATE 7 */
		;
		((P1 *)this)->j = trpt->bup.oval;
		;
		goto R999;

	case 9: /* STATE 12 */
		;
		now.state[ Index(((P1 *)this)->i, 3) ].view[ Index(((P1 *)this)->i, 3) ] = trpt->bup.oval;
		;
		goto R999;

	case 10: /* STATE 13 */
		;
		((P1 *)this)->i = trpt->bup.oval;
		;
		goto R999;

	case 11: /* STATE 20 */
		;
		((P1 *)this)->i = trpt->bup.oval;
		;
		goto R999;
;
		;
		
	case 13: /* STATE 22 */
		;
		;
		delproc(0, now._nr_pr-1);
		;
		goto R999;

	case 14: /* STATE 23 */
		;
		((P1 *)this)->i = trpt->bup.oval;
		;
		goto R999;
;
		;
		
	case 16: /* STATE 31 */
		;
		round_id = trpt->bup.oval;
		;
		goto R999;
;
		;
		;
		;
		
	case 19: /* STATE 40 */
		;
		((P1 *)this)->i = trpt->bup.ovals[1];
		((P1 *)this)->synchronous = trpt->bup.ovals[0];
		;
		ungrab_ints(trpt->bup.ovals, 2);
		goto R999;

	case 20: /* STATE 40 */
		;
		((P1 *)this)->i = trpt->bup.ovals[1];
		((P1 *)this)->remaining_asynchronous_rounds = trpt->bup.ovals[0];
		;
		ungrab_ints(trpt->bup.ovals, 2);
		goto R999;

	case 21: /* STATE 40 */
		;
		((P1 *)this)->i = trpt->bup.oval;
		;
		goto R999;

	case 22: /* STATE 42 */
		;
		((P1 *)this)->j = trpt->bup.oval;
		;
		goto R999;
;
		;
		;
		;
		
	case 25: /* STATE 45 */
		;
		now.state[ Index(((P1 *)this)->i, 3) ].received_message[ Index(((P1 *)this)->j, 3) ] = trpt->bup.oval;
		;
		goto R999;
;
		;
		
	case 27: /* STATE 48 */
		;
		now.random = trpt->bup.oval;
		;
		goto R999;

	case 28: /* STATE 49 */
		;
		now.state[ Index(((P1 *)this)->i, 3) ].received_message[ Index(((P1 *)this)->j, 3) ] = trpt->bup.oval;
		;
		goto R999;

	case 29: /* STATE 51 */
		;
		now.state[ Index(((P1 *)this)->i, 3) ].received_message[ Index(((P1 *)this)->j, 3) ] = trpt->bup.oval;
		;
		goto R999;

	case 30: /* STATE 56 */
		;
		((P1 *)this)->j = trpt->bup.oval;
		;
		goto R999;

	case 31: /* STATE 62 */
		;
		((P1 *)this)->i = trpt->bup.oval;
		;
		goto R999;

	case 32: /* STATE 69 */
		;
		now.token = trpt->bup.oval;
		;
		goto R999;
;
		;
		
	case 34: /* STATE 71 */
		;
		now.token = trpt->bup.oval;
		;
		goto R999;

	case 35: /* STATE 75 */
		;
		p_restor(II);
		;
		;
		goto R999;

		 /* PROC Process */
;
		;
		;
		;
		
	case 38: /* STATE 4 */
		;
		((P0 *)this)->_message.value = trpt->bup.oval;
		;
		goto R999;

	case 39: /* STATE 5 */
		;
		((P0 *)this)->i = trpt->bup.oval;
		;
		goto R999;
;
		;
		
	case 41: /* STATE 7 */
		;
		((P0 *)this)->_message.view[ Index(((P0 *)this)->i, 3) ] = trpt->bup.oval;
		;
		goto R999;

	case 42: /* STATE 8 */
		;
		((P0 *)this)->i = trpt->bup.oval;
		;
		goto R999;

	case 43: /* STATE 15 */
		;
		now.messages[ Index((((P0 *)this)->_pid-1), 3) ].value = trpt->bup.oval;
		;
		goto R999;

	case 44: /* STATE 16 */
		;
		((P0 *)this)->j = trpt->bup.oval;
		;
		goto R999;
;
		;
		
	case 46: /* STATE 18 */
		;
		now.messages[ Index((((P0 *)this)->_pid-1), 3) ].view[ Index(((P0 *)this)->j, 3) ] = trpt->bup.oval;
		;
		goto R999;

	case 47: /* STATE 19 */
		;
		((P0 *)this)->j = trpt->bup.oval;
		;
		goto R999;

	case 48: /* STATE 26 */
		;
		now.token = trpt->bup.oval;
		;
		goto R999;
;
		;
			case 50: /* STATE 148 */
		sv_restor();
		goto R999;

	case 51: /* STATE 150 */
		;
		now.token = trpt->bup.oval;
		;
		goto R999;

	case 52: /* STATE 155 */
		;
		p_restor(II);
		;
		;
		goto R999;
	}


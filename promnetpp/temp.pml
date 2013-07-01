int random = 1234;
#define next(r) (r * 16807) % 2147483647
#define boolean(r) ((r >> 30) & 1)

init {
    int soma = 0;
    /* select(random: 1..2147483647); */
    do
    ::
        if
        :: boolean(random) -> soma++
        :: else -> soma--
        fi;
        
        printf("%d ", soma);
        random = next(random);
        //assert(random >= 0)
    od
}
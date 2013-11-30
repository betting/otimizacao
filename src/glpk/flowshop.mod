/*  Tempo de fluxo no flow shop permutacional */

param m, integer, >=1;  # Numero de maquinas
param n, integer, >=1;  # Numero de itens

param P{ i in 1..m, j in 1..n } >=0;
param C{ i in 1..n, j in 1..n } >=0;

# Funcao objetiva e suas restricoes
#minimize C{ i in 1..m, j in 1..n }: sum { j in 1..n } P[i,j];
minimize Time:  sum { j in 1..n } P[m,j];

subject to R1 { i in 1..m } : sum { a in 1..i } P[a,1] = C[i,1];
subject to R2 { j in 1..n } : sum { b in 1..j } P[1,b] = C[1,j];
subject to R3 { i in 2..m, j in 2..n }: max( C[i-1,j], C[i,j-1] ) + P[i,j] = C[i,j];


data;

param m:=5; # the number of machine

param n:=20; # the number of items

/* the times dij required to perform the work on item i by machine j */
param P : 1 2 3 4 5 :=
1 54  79  16  66  58
2 83   3  89  58  56
3 15  11  49  31  20
4 71  99  15  68  85
5 77  56  89  78  53
6 36  70  45  91  35
7 53  99  60  13  53
8 38  60  23  59  41
9 27   5  57  49  69
10 87  56  64  85  13
11 76   3   7  85  86
12 91  61   1   9  72
13 14  73  63  39   8
14 29  75  41  41  49
;

end;

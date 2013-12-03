/*  Tempo de fluxo no flow shop permutacional */

param m, integer, >=1;  # Numero de maquinas
param n, integer, >=1;  # Numero de itens

param B{ j in 1..n, r in 1..m } >=0;
param T{ j in 1..n, r in 1..m } >=0;
param Z{ j in 1..n, i in 1..n } >=0;

# Funcao objetiva e suas restricoes
minimize pfsp: B[n,m] + sum { i in 1..n } (T[i,m]*Z[n,i]);
#minimize pfsp: B[5,20] + sum { i in 1..n } (T[5,i]*Z[i,20]);

subject to R1 { i in 1..n } : sum { j in 1..n } Z[j,i] = 1;
subject to R2 { j in 1..n } : sum { i in 1..n } Z[j,i] = 1;
subject to R3 { j in 1..(n-1) }: B[j,1] + sum { i in 1..n } (T[i,1]*Z[j,i]) = B[j+1,1];
subject to R4 : B[1,1] = 0;
subject to R5 { r in 1..(m-1) } : B[1,r] + sum  { i in 1..n } (T[i,r]*Z[1,i]) = B[1,r+1];
subject to R6 { r in 1..(m-1), j in 2..n} : B[j,r] + sum { i in 1..n } (T[i,r]*Z[j,i]) <= B[j,r+1];
subject to R7 { r in 2..m, j in 1..(n-1)} : B[j,r] + sum { i in 1..n } (T[i,r]*Z[j,i]) <= B[j+1,r];

data;

param m:=5; # the number of machine

param n:=20; # the number of items

/* the times dij required to perform the work on item i by machine j */
param T : 
   1   2   3   4   5 :=
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
15 12  47  63  56  47
16 77  14  47  40  87
17 32  21  26  54  58
18 87  86  75  77  18
19 68   5  77  51  68
20 94  77  40  31  28;


param B :
   1 2 3 4 5 :=
1  0 0 0 0 0
2  0 0 0 0 0
3  0 0 0 0 0
4  0 0 0 0 0
5  0 0 0 0 0
6  0 0 0 0 0
7  0 0 0 0 0
8  0 0 0 0 0
9  0 0 0 0 0
10 0 0 0 0 0
11 0 0 0 0 0
12 0 0 0 0 0
13 0 0 0 0 0
14 0 0 0 0 0
15 0 0 0 0 0
16 0 0 0 0 0
17 0 0 0 0 0
18 0 0 0 0 0
19 0 0 0 0 0
20 0 0 0 0 0;



param Z :
   1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 :=
1  0 0 0 0 0 0 0 0 0  0  0  0  0  0  0  0  0  0  0  0 
2  0 0 0 0 0 0 0 0 0  0  0  0  0  0  0  0  0  0  0  0
3  0 0 0 0 0 0 0 0 0  0  0  0  0  0  0  0  0  0  0  0
4  0 0 0 0 0 0 0 0 0  0  0  0  0  0  0  0  0  0  0  0
5  0 0 0 0 0 0 0 0 0  0  0  0  0  0  0  0  0  0  0  0
6  0 0 0 0 0 0 0 0 0  0  0  0  0  0  0  0  0  0  0  0
7  0 0 0 0 0 0 0 0 0  0  0  0  0  0  0  0  0  0  0  0
8  0 0 0 0 0 0 0 0 0  0  0  0  0  0  0  0  0  0  0  0
9  0 0 0 0 0 0 0 0 0  0  0  0  0  0  0  0  0  0  0  0
10 0 0 0 0 0 0 0 0 0  0  0  0  0  0  0  0  0  0  0  0
11 0 0 0 0 0 0 0 0 0  0  0  0  0  0  0  0  0  0  0  0
12 0 0 0 0 0 0 0 0 0  0  0  0  0  0  0  0  0  0  0  0
13 0 0 0 0 0 0 0 0 0  0  0  0  0  0  0  0  0  0  0  0
14 0 0 0 0 0 0 0 0 0  0  0  0  0  0  0  0  0  0  0  0
15 0 0 0 0 0 0 0 0 0  0  0  0  0  0  0  0  0  0  0  0
16 0 0 0 0 0 0 0 0 0  0  0  0  0  0  0  0  0  0  0  0
17 0 0 0 0 0 0 0 0 0  0  0  0  0  0  0  0  0  0  0  0
18 0 0 0 0 0 0 0 0 0  0  0  0  0  0  0  0  0  0  0  0
19 0 0 0 0 0 0 0 0 0  0  0  0  0  0  0  0  0  0  0  0
20 1 1 1 1 1 1 1 1 1  1  1  1  1  1  1  1  1  1  1  1;


end;

param m, integer, >=1; # Numero de maquinas
set Machines:={1..m};  # Conjunto de maquinas
param n, integer, >=1; # Numero de itens
set Items:={1..n};  # Conjunto de itens

param d{i in Items, j in Machines} >=0;  # Tempo para que uma máquina k leva para processar um item l
param B:=1+sum{i in Items, j in Machines} d[i,j]; 

var t{i in Items, j in Machines} >=0; # Tempo inicial do processamento do item l na maquina k
var makespan >= 0;
var y{j in Machines, i in Items, k in Items: i<k}, binary;

minimize flowshop: makespan;
subject to R1 {i in Items, j in Machines:j<m}: t[i,j+1]>=t[i,j]+d[i,j]; # Sequencia do processamento (Tempo item processado
                                                                        # na máquina 2 é maior que o tempo da maquina 1
subject to R2 {j in Machines, i in Items, k in Items: i<k}: t[i,j]-t[k,j]+B*y[j,i,k]>=d[k,j];     # Apenas um elemento pode ser
subject to R3 {j in Machines, i in Items, k in Items: i<k}: t[k,j]-t[i,j]+B*(1-y[j,i,k])>=d[i,j]; # processado por maquina
subject to R4 {i in Items}: t[i,m]+d[i,m] <= makespan; # Makespan e o calculo do processamento de todos os itens na ultima maquina

end;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {

	private Scanner scan;

	public Main() {
		scan = new Scanner(System.in);
	}

	public static void main(String[] args) {
		new Main().execute();
	}

	private void execute() {
		final double alpha = 0.5;
		// final int num_solucoes_vizinhanca = 2;
		final int num_solucoes = 10;

		// leitura dos valores iniciais
		int maquinas = scan.nextInt();
		int tarefas = scan.nextInt();

		// matriz de solucoes encontradas
		int[][] solucoes = new int[num_solucoes][tarefas];

		// matriz de tarefas x maquinas
		int[][] matrizMaqsTarefas = new int[tarefas][maquinas];

		// vetor com o tempo total de execu��o de cada tarefa
		int[] tempoTotalOp = new int[tarefas];

		// matriz para calcular as medias de resultados
		// i=0 primeiras solu��es, i=1 melhores solu��es
		// i=2 tempos de execu��es
		int[][] resultados = new int[3][num_solucoes];

		// leitura e descarte dos 1s do arquivo de entrada
		for (int i = 0; i < maquinas - 1; i++) {
			scan.nextInt();
		}

		// leitura da matriz de entrada
		for (int i = 0; i < tarefas; i++) {
			tempoTotalOp[i] = 0;
			for (int j = 0; j < maquinas; j++) {
				matrizMaqsTarefas[i][j] = scan.nextInt();
				tempoTotalOp[i] += matrizMaqsTarefas[i][j]; // soma total

			}
		}

		// int[] teste = { 1, 0, 3, 4, 2 };
		// System.out.println("Teste: ");
		// System.out.println(calculaMakespan(matriz, teste));

		// RESOLU��O////////////////////////////
		// ////////////////////////////////////

		// FASE DE CONSTRU��O
		for (int i = 0; i < num_solucoes; i++) {
			// inicia contagem de tempo de execu��o do GRASP
			long duracaoInicio = System.currentTimeMillis();

			int[] tempoTotalOpIterativo = Arrays.copyOf(tempoTotalOp,
					tempoTotalOp.length);

			//
			for (int j = 0; j < tarefas; j++) {
				int range = maximoVetor(tempoTotalOpIterativo)
						- minimoVetor(tempoTotalOpIterativo);
				double width = range * alpha;

				double[] rcl = new double[2];
				double[] probabilidades = new double[tarefas];
				int[] rank = new int[tarefas];

				for (int k = 0; k < rank.length; k++) {
					rank[k] = 0;
					probabilidades[k] = 0;
				}

				rcl[0] = minimoVetor(tempoTotalOpIterativo);
				rcl[1] = minimoVetor(tempoTotalOpIterativo) + width;

				// System.out.println(rcl[0] + " " + rcl[1] + "\n");

				rank = tarefasDentroDoIntervaloRankeadas(tempoTotalOpIterativo,
						rcl[0], rcl[1]);

				probabilidades = calculaProbabilidades(rank);

				// System.out.println("Probabilidades:");
				// imprimeVetor(probabilidades);

				solucoes[i][j] = escolheRandomicoComProbabilidade(probabilidades);

				// System.out.println("Matriz solucoes: ");
				// imprimeMatriz(solucoes);

				// atribui -1 para eliminar a tarefa ja adicionada
				tempoTotalOpIterativo[solucoes[i][j]] = -1;

				// System.out.println("Vetor Total: ");
				// imprimeVetor(totalIterativo);

				// System.out.println();

			}

			// FASE DE BUSCA LOCAL
			// numero de possiveis vizinhos � a PA do tamanho do vetor Solucao
			int[][] vizinhanca = new int[pa(solucoes[i].length - 1)][solucoes[i].length];
			int[] makespans = new int[vizinhanca.length];
			int indiceViz = 0;

			// CRIA VIZINHOS
			for (int v = 0; v <= vizinhanca[0].length - 2; v++) {
				// System.out.println("-------v = " + v);
				for (int x = v + 1; x <= vizinhanca[0].length - 1; x++) {
					// System.out.println("x = " + x);
					vizinhanca[indiceViz] = Arrays.copyOf(solucoes[i],
							solucoes[i].length);
					// System.out.print("S: ");
					// imprimeVetor(solucoes[0]);

					// TROCA
					int temp = vizinhanca[indiceViz][x];
					vizinhanca[indiceViz][x] = vizinhanca[indiceViz][v];
					vizinhanca[indiceViz][v] = temp;

					// System.out.print("V: ");
					// imprimeVetor(vizinhanca[indiceViz]);
					// System.out.println();

					makespans[indiceViz] = calculaMakespan(matrizMaqsTarefas,
							vizinhanca[indiceViz]);

					// PRIMEIRA SOLU��O
					if (indiceViz == 0) {
						// System.out.println("Primeira solucao: "
						// + makespans[indiceViz]);

						// GUARDA PRIMEIRO RESULTADO NA MATRIZ DE RESULTADOS
						resultados[0][i] = makespans[indiceViz];
					}

					indiceViz++;
				}
			}

			// // IMPRIME MELHOR SOLU��O
			// System.out.println("Melhor solucao: " + minimoVetor(makespans));

			// GUARDA MELHOR RESULTADO NA MATRIZ DE RESULTADOS
			resultados[1][i] = minimoVetor(makespans);

			// termina contagem de tempo do GRASP
			long duracaoFim = System.currentTimeMillis();

			// // IMPRIME DURA��O DE UMA EXECU��O DO GRASP
			// System.out.println("Duracao: " + (duracaoFim - duracaoInicio)
			// + "ms");

			// GUARDA DURA��O NA MATRIZ DE RESULTADOS
			resultados[2][i] = (int) (duracaoFim - duracaoInicio);

			// System.out.print("Solucao: ");
			// imprimeVetor(solucoes[i]);
			// System.out.println("Makespan: "
			// + calculaMakespan(matrizMaqsTarefas, solucoes[i]));

			// System.out.println();
		}

		// System.out.println("Resultados: ");
		// imprimeMatriz(resultados);

		System.out.println("Medias");
		System.out.println("Primeira solucao: " + mediaVetor(resultados[0]));
		System.out.println("Melhor solucao: " + minimoVetor(resultados[1]));
		System.out.println("Duracao: " + mediaVetor(resultados[2]) + " ms");

		scan.close();
	}

	// FUN��ES//////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////

	int mediaVetor(int[] vetor) {
		int media = 0;

		for (int i = 0; i < vetor.length; i++)
			media += vetor[i];

		return media / vetor.length;
	}

	int pa(int n) {
		return (n * (n + 1)) / 2;
	}

	int calculaMakespan(int matrizMaqsOps[][], int[] solucao) {
		int[][] shifts = new int[matrizMaqsOps.length][matrizMaqsOps[0].length];

		for (int i = 0; i < shifts.length; i++) {
			for (int j = 0; j < shifts[0].length; j++) {
				if (i == 0) { // se � a primeira opera��o
					if (j == 0) { // se � a primeira tarefa
						shifts[0][0] = matrizMaqsOps[solucao[i]][0];

					} else {// se s�o as demais tarefas
						shifts[i][j] = shifts[i][j - 1]
								+ matrizMaqsOps[solucao[i]][j];

					}
				} else { // se s�o as demais opera��es (>0)
					if (j == 0) { // se � a primeira tarefa
						shifts[i][0] = shifts[i - 1][0]
								+ matrizMaqsOps[solucao[i]][0];

					} else { // se s�o as demais tarefas
						shifts[i][j] = Math.max(shifts[i - 1][j],
								shifts[i][j - 1])
								+ matrizMaqsOps[solucao[i]][j];
					}
				}
			}
			// imprimeVetor(shifts[i]);
		}

		return shifts[matrizMaqsOps.length - 1][matrizMaqsOps[0].length - 1];
	}

	int[] tarefasDentroDoIntervaloRankeadas(int total[], double i1, double i2) {
		int i;
		int j;
		int rank = -1;
		int vetor[] = new int[total.length];

		for (i = 0; i < vetor.length; i++)
			vetor[i] = 0;

		// coloca os valores dentro do intervalo no vetor
		for (i = 0; i < total.length; i++)
			if (i1 <= total[i] && total[i] <= i2) {
				vetor[i] = total[i];
			}

		// imprimeVetor(vetor);

		// rankeia as posi��es opostamente (-1, -2...)
		for (i = 0; i < vetor.length; i++) {
			int min = Integer.MAX_VALUE;
			int indice = -1;

			for (j = 0; j < vetor.length; j++)
				if (0 < vetor[j] && vetor[j] < min) {
					min = vetor[j];
					indice = j;
				}

			if (indice == -1)
				break;

			vetor[indice] = rank;
			rank--;
		}

		// tira do oposto
		for (i = 0; i < vetor.length; i++)
			vetor[i] *= -1;

		return vetor;
	}

	double[] calculaProbabilidades(int rank[]) {
		double[] probabilidades = new double[rank.length];
		double soma = 0;

		// inverte os valores e calcula a soma total
		for (int i = 0; i < rank.length; i++) {
			probabilidades[i] = rank[i];

			if (probabilidades[i] != 0) {
				probabilidades[i] = 1 / probabilidades[i];
				soma += probabilidades[i];
			}

		}

		// calcula a probabilidade (valor/soma)
		for (int i = 0; i < probabilidades.length; i++)
			if (probabilidades[i] != 0)
				probabilidades[i] /= soma;

		return probabilidades;
	}

	int escolheRandomicoComProbabilidade(double probabilidades[]) {
		int indice = -1;
		double[][] intervalos = new double[probabilidades.length][2];
		double[] probs = Arrays.copyOf(probabilidades, probabilidades.length);

		// ordena em rela��o a probabilidade (menor pro maior)
		for (int i = 0; i < intervalos.length; i++) {
			indice = -1;
			double min = 2;

			for (int j = 0; j < probs.length; j++) {
				if (probs[j] != 0 && probs[j] < min) {
					min = probs[j];
					indice = j;
				}
			}

			// se indice==-1 nao tem mais candidatos
			if (indice == -1)
				break;

			// coloca na ordem
			intervalos[i][0] = indice;
			intervalos[i][1] = probs[indice];

			// ajusta a faixa do intervalo
			if (i > 0)
				intervalos[i][1] += intervalos[i - 1][1];

			// retira do vetor probabilidades
			probs[indice] = 0;
		}

		// imprimeMatriz(intervalos);

		// calcula uma probabilidade
		Random r = new Random();
		double entrezeroeum = r.nextDouble();

		// escolhe elemento dentro da faixa
		for (indice = 0; indice < intervalos.length
				&& intervalos[indice][1] != 0
				&& intervalos[indice][1] < entrezeroeum; indice++) {
		}
		// System.out.println("prob: " + entrezeroeum);
		// System.out.println("indice: " + intervalos[indice][0]);

		// retorna indice escolhido (armazenado em intervalos[i][0])
		return (int) intervalos[indice][0];
	}

	void imprimeMatriz(int matriz[][]) {
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[0].length; j++)
				System.out.print(matriz[i][j] + " ");
			System.out.println();
		}
	}

	void imprimeMatriz(double matriz[][]) {
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[0].length; j++)
				System.out.print(matriz[i][j] + " ");
			System.out.println();
		}
	}

	void imprimeVetor(int vetor[]) {
		for (int j = 0; j < vetor.length; j++)
			System.out.print(vetor[j] + " ");
		System.out.println();
	}

	void imprimeVetor(double vetor[]) {
		for (int j = 0; j < vetor.length; j++)
			System.out.print(vetor[j] + " ");
		System.out.println();
	}

	void imprimeVetorVertical(int vetor[]) {
		for (int j = 0; j < vetor.length; j++)
			System.out.println(vetor[j]);
	}

	int maximoVetor(int v[]) {
		int max = 0;
		for (int i = 0; i < v.length; i++) {
			if (v[i] > max && v[i] != -1)
				max = v[i];
		}

		return max;
	}

	int minimoVetor(int v[]) {
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < v.length; i++) {
			if (v[i] < min && v[i] != -1)
				min = v[i];
		}

		return min;
	}

}
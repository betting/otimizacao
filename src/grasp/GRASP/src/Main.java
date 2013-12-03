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
		final int num_solucoes_vizinhanca = 1;
		int num_solucoes = 1;

		// leitura dos valores iniciais
		int maquinas = scan.nextInt();
		int tarefas = scan.nextInt();

		// matriz de solucoes
		int[][] solucoes = new int[num_solucoes_vizinhanca][tarefas];

		// matriz de tarefas x maquinas + tempo total
		int[][] matrizMaqsOps = new int[tarefas][maquinas];

		// vetor com o tempo total de execu��o de cada tarefa
		int[] total = new int[tarefas];

		// leitura e descarte dos 1s
		for (int i = 0; i < maquinas - 1; i++) {
			scan.nextInt();
		}

		// leitura da matriz
		for (int i = 0; i < tarefas; i++) {
			total[i] = 0;
			for (int j = 0; j < maquinas; j++) {
				matrizMaqsOps[i][j] = scan.nextInt();
				total[i] += matrizMaqsOps[i][j]; // soma total

			}
		}

		// int[] teste = { 1, 0, 3, 4, 2 };
		// System.out.println("Teste: ");
		// System.out.println(calculaMakespan(matriz, teste));

		// RESOLU��O////////////////////////////
		//

		// FASE DE CONSTRU��O
		for (int i = 0; i < num_solucoes; i++) {
			int[] totalIterativo = Arrays.copyOf(total, total.length);

			for (int j = 0; j < tarefas; j++) {
				int range = maximoVetor(totalIterativo)
						- minimoVetor(totalIterativo);
				double width = range * alpha;

				double[] rcl = new double[2];
				double[] probabilidades = new double[tarefas];

				int[] rank = new int[tarefas];

				for (int k = 0; k < rank.length; k++) {
					rank[k] = 0;
					probabilidades[k] = 0;
				}

				rcl[0] = minimoVetor(totalIterativo);
				rcl[1] = minimoVetor(totalIterativo) + width;

				// System.out.println(rcl[0] + " " + rcl[1] + "\n");

				rank = tarefasDentroDoIntervaloRankeadas(totalIterativo,
						rcl[0], rcl[1]);
				probabilidades = calculaProbabilidades(rank);

				// solucoes[i][j] =
				solucoes[i][j] = escolheRandomicoComProbabilidade(probabilidades);

				// System.out.println("Matriz solucoes: ");
				// imprimeMatriz(solucoes);

				// atribui -1 para eliminar a tarefa ja adicionada
				totalIterativo[solucoes[i][j]] = -1;

				// System.out.println("Vetor Total: ");
				// imprimeVetor(totalIterativo);

				// System.out.println();

				// FASE DE BUSCA LOCAL

			}

			System.out.print("Solucao: ");
			imprimeVetor(solucoes[i]);
			System.out.println("Makespan: "
					+ calculaMakespan(matrizMaqsOps, solucoes[i]));
		}

		scan.close();
	}

	// FUN��ES//////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////

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
			imprimeVetor(shifts[i]);
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

		// escolhe elemento na faixa na probabilidade
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
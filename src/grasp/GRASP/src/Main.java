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
		int[][] matriz = new int[tarefas][maquinas];

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
				matriz[i][j] = scan.nextInt();
				total[i] += matriz[i][j]; // soma total

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
				solucoes[0][j] = escolheRandomicoComProbabilidade(probabilidades);

				// System.out.println("Matriz solucoes: ");
				// imprimeMatriz(solucoes);

				// atribui -1 para eliminar a tarefa ja adicionada
				totalIterativo[solucoes[0][j]] = -1;

				// System.out.println("Vetor Total: ");
				// imprimeVetor(totalIterativo);

				// System.out.println();

				// FASE DE BUSCA LOCAL

			}
			System.out.println("Makespan: "
					+ calculaMakespan(matriz, solucoes[0]));
		}

		scan.close();
	}

	// FUN��ES//////////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////////////////

	int calculaMakespan(int m[][], int[] solucao) {
		int[] v = new int[m[0].length];
		v[0] = 0;
		int shift = 0;

		// soma os tempos na ordem contida no vetor solucao
		// for (int i = 0; i < solucao.length; i++) {
		// for (int j = 0; j < m[0].length; j++) {
		// if (i == 0 && j==0) {
		// v[j] += m[solucao[i]][j];
		// } else {
		// v[j] += m[solucao[i]][j];
		// }
		//
		// }
		// imprimeVetor(v);
		// }

		for (int i = 0; i < solucao.length - 1; i++)
			shift += m[solucao[i]][0];

		// System.out.println("Shift: " + shift);

		v[0] += m[solucao[solucao.length - 1]][0];
		for (int i = 1; i < v.length; i++) {
			v[i] += m[solucao[solucao.length - 1]][i] + v[i - 1];
		}

		// imprimeVetor(v);

		return v[v.length - 1] + shift;
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
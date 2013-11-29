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
		int maquinas = scan.nextInt();
		int tarefas = scan.nextInt();

		// matriz de tarefas x maquinas + tempo total
		int[][] matriz = new int[tarefas][maquinas + 1];

		// leitura e descarte dos 1s
		for (int i = 0; i < maquinas - 1; i++)
			matriz[0][0] = scan.nextInt();

		// leitura da matriz
		for (int i = 0; i < tarefas; i++) {
			for (int j = 0; j < maquinas; j++) {
				matriz[i][j] = scan.nextInt();
				matriz[i][maquinas] += matriz[i][j]; // soma total
			}
		}

		// imprimeMatriz(matriz);
		//
		// System.out.println(maximoMatriz(matriz));
		// System.out.println(minimoMatriz(matriz));

		double alpha = 0.5;
		int range = maximoMatriz(matriz) - minimoMatriz(matriz);
		double width = range * alpha;

		double[] rcl = new double[2];

		int[] rank = new int[tarefas];
		for (int i = 0; i < rank.length; i++)
			rank[i] = 0;

		rcl[0] = minimoMatriz(matriz);
		rcl[1] = minimoMatriz(matriz) + width;

		System.out.println(rcl[0] + " " + rcl[1] + "\n");

		rank = tarefasDentroDoIntervaloRankeadas(matriz, rcl[0], rcl[1]);
		imprimeVetor(rank);

		scan.close();
	}

	int[] tarefasDentroDoIntervaloRankeadas(int m[][], double i1, double i2) {
		int i;
		int j;
		int rank = -1;
		int vetor[] = new int[m.length];

		for (i = 0; i < vetor.length; i++)
			vetor[i] = 0;

		// coloca os valores dentro do intervalo no vetor
		for (i = 0; i < m.length; i++)
			if (i1 <= m[i][m[0].length - 1] && m[i][m[0].length - 1] <= i2) {
				vetor[i] = m[i][m[0].length - 1];
			}

		// imprimeVetor(vetor);

		// rankeia as posições opostamente (-1, -2...)
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

			imprimeVetor(vetor);
			vetor[indice] = rank;
			rank--;

		}

		// tira do oposto
		for (i = 0; i < vetor.length; i++)
			vetor[i] *= -1;

		return vetor;
	}

	void imprimeMatriz(int matriz[][]) {
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

	int maximoMatriz(int m[][]) {
		int max = 0;
		for (int i = 0; i < m.length; i++) {
			if (m[i][m[0].length - 1] > max)
				max = m[i][m[0].length - 1];
		}

		return max;
	}

	int minimoMatriz(int m[][]) {
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < m.length; i++) {
			if (m[i][m[0].length - 1] < min)
				min = m[i][m[0].length - 1];
		}

		return min;
	}

}

package Algorithm;

import java.util.*;

public class kmpAlgorithm {

	public static int[] getFail(String VirusPattern) { // suffix prefix matching count 
		int j = 0; 
		int LenVirusPattern = VirusPattern.length(); 
		int[] fail = new int[LenVirusPattern]; 
		char[] VP = VirusPattern.toCharArray();
		
		for (int i = 1; i < LenVirusPattern; i++) { 
			while (j > 0 && VP[i] != VP[j]) { 
				j = fail[j - 1]; 
			}
			if (VP[i] == VP[j]) { // suffix == prefix
				fail[i] = ++j; 
			}
		}
		return fail; 
	}

	public static int KMP(String ProgramCode, String VirusPattern) {

		int NumOfMatch = 0, j = 0; 
		int LenProgramCode = ProgramCode.length(); 
		int LenVirusPattern = VirusPattern.length();
		int fail[] = getFail(VirusPattern); 
		char[] VP = VirusPattern.toCharArray();
		char[] PC = ProgramCode.toCharArray();
		
		for (int i = 0; i < LenProgramCode; i++) { // Code의 처음부터
			while (j > 0 && PC[i] != VP[j]) { 
				j = fail[j - 1]; 
			}
			if (PC[i] == VP[j]) { // Same Character
				if (j == LenVirusPattern - 1) { 
					NumOfMatch++;
					j = fail[j]; 
				} else { 
					j++; 
				}
			}
		}
		return NumOfMatch; 
	}
}
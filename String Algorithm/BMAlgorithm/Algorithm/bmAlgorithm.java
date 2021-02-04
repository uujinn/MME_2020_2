package Algorithm;
import java.util.*;

public class bmAlgorithm {
	public static int[] getMatchJump(String VirusPattern) {

		int i = 0, j = 0;
		char[] VP = VirusPattern.toCharArray();
		int LenVirusPattern = VirusPattern.length();
		int[] MatchJump = new int[LenVirusPattern];

		for (i = 0; i < LenVirusPattern; ++i) {
			MatchJump[i] = LenVirusPattern;
		}
		i = LenVirusPattern - 1;

		while (i > 0) {
			if (j >= 0 && VP[i + j] == VP[j]) {
				j--;
			} else {
				if (j < 0) {
					for (j = j; i + j >= 0; --j) {
						MatchJump[i + j] = i;
					}
				} else {
					MatchJump[i + j] = i;
				}
				j = LenVirusPattern - i;
				i--;
			}
		}

		return MatchJump;
	}

	public static int BM(String ProgramCode, String VirusPattern) {
		int NumOfMatch = 0, j = 0, move = 0;
		int LenProgramCode = ProgramCode.length();
		int LenVirusPattern = VirusPattern.length();
		int[] MatchJump = getMatchJump(VirusPattern);
		
		char[] VP = VirusPattern.toCharArray();
		char[] PC = ProgramCode.toCharArray();
		
		while (move <= (LenProgramCode - LenVirusPattern)) {
			j = LenVirusPattern - 1;

			while (j >= 0 && VP[j] == PC[move + j]) {
				j--;
			}
			if (j < 0) {
				NumOfMatch++;
				move += MatchJump[0];
			} else {
				move += MatchJump[j];
			}
		}

		return NumOfMatch;
	}

}

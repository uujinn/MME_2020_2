import java.io.*;
import java.util.*;
import Algorithm.bmAlgorithm;

public class VirusCheckingProgram extends bmAlgorithm{	
	private int NumOfVirusProgram = 0, NumOfVirusFreeProgram = 0, NumOfFile=0;; 
	private String VirusCode;
	private ArrayList<String> Malware = new ArrayList<String>();
	static Scanner sc = new Scanner(System.in);
	private ArrayList<Long> RunningTime = new ArrayList<>();
	private ArrayList<String> VirusProgram = new ArrayList<>();
	private ArrayList<String> VirusFreeProgram = new ArrayList<>();
	
	public void inputVirusCode() {
		while(true) {
			System.out.print("악성 코드를 입력하세요: ");
			VirusCode = sc.nextLine();
			if(VirusCode.length()==0) { 
				System.out.println("[알림]악성 코드가 입력되지 않았습니다.");
			}
			else if(VirusCode.length()<12) { 
				System.out.println("[알림]악성 코드의 길이가 12미만입니다.");
			}
			else {
				break;
			}
		}

		while(true) {
			try{
				int n = 0;
				while(true) {
					System.out.print("바이러스 패턴의 길이를 입력해주세요: ");
					n = sc.nextInt();
					if(n>VirusCode.length()) {
						System.out.println("[알림]패턴 크기 범위를 벗어났습니다.");
					}
					else {
						break;
					}
				}
				System.out.print("바이러스 패턴 = [");
				for(int i = 0;i<VirusCode.length()-n+1;i++) {
					Malware.add(VirusCode.substring(i,i+n));
					if(i==(VirusCode.length()-n)) {
						System.out.print(Malware.get(i)+"]");
					}
					else{
						System.out.print(Malware.get(i)+", ");
					}
				}
				break;
			}catch(InputMismatchException e) {
				System.out.println("[알림]정수를 입력해주세요."); 
				sc.nextLine();
				continue;
			}
		}
	}
	
	public void inputFile() throws IOException {
		System.out.println();
		System.out.println("** 프로그램 분석을 원할 시에는 \"분석\"을 입력해주세요 **");
		while(true) {
			try{
				System.out.println();
				System.out.println();
				System.out.print("프로그램 이름을 입력해주세요: ");
				String filename = sc.next();
				if (filename.equals("분석")) {
					if(NumOfFile<15) { 
						System.out.println("[알림]등록된 파일이 15개 미만입니다.");
						continue;
					}
					break;
				}
				File f = new File(filename);
				FileReader fr = new FileReader(f);
				int cur = 0;
				int lenOfProgramCode = 0;
				while((cur = fr.read()) != -1){
					lenOfProgramCode++;
				}
				if(lenOfProgramCode<27) { 
					System.out.println("[알림]저장된 코드 길이가 27미만입니다.");
					continue;
				}
				NumOfFile++;
				runAlgorithm(f); 
			}
			catch(FileNotFoundException e) {
				System.out.println("[알림]파일이 존재하지 않습니다."); 
			}
		}
	}
	
	public void runAlgorithm(File f) throws IOException { 
		String filepath = f.toString();
		BufferedReader br = new BufferedReader(new FileReader(filepath));
		String ProgramCode = br.readLine();
		int NumOfMatchingProgram=0;
		for(int i=0;i<Malware.size();i++) {
			long start = System.nanoTime();
			BM(ProgramCode,Malware.get(i));
			long end = System.nanoTime();
			int numOfMatching = BM(ProgramCode,Malware.get(i));
			System.out.print(Malware.get(i)+" "+numOfMatching+"번"+" ");
			if(numOfMatching>0) {
				NumOfMatchingProgram++;
			}else {
				// pass
			}

			RunningTime.add(end-start);
		}
        if(NumOfMatchingProgram>0) {
        	VirusProgram.add(f.getName());
        	NumOfVirusProgram++;
        }
        else {
        	VirusFreeProgram.add(f.getName());
        	NumOfVirusFreeProgram++;
        }

	}
	
	public void showResult() {
		System.out.println("==============전체 프로그램 분석 시작==============");
		System.out.println();
		System.out.println("바이러스 패턴이 검출된 프로그램은 "+NumOfVirusProgram+"개 입니다.");
		System.out.print("바이러스 패턴이 검출된 프로그램 이름 : ");
		for(int i=0;i<VirusProgram.size();i++) {
			System.out.print(VirusProgram.get(i)+" ");
		}
		System.out.println();
		System.out.println("바이러스 패턴이 검출되지 않은 프로그램은 "+NumOfVirusFreeProgram+"개 입니다.");
		System.out.print("바이러스 패턴이 검출되지 않은 프로그램 이름 : ");
		for(int i=0;i<VirusFreeProgram.size();i++) {
			System.out.print(VirusFreeProgram.get(i)+" ");
		}
		System.out.println();
		long sum = 0;
		
		for(int i =0;i<RunningTime.size();i++) {
			sum = sum + RunningTime.get(i);
		}
		
		System.out.println("BM 알고리즘 실행시간 : " + ((double)sum)/ 1000000 +"ms");
		System.out.println();
		System.out.println("==============프로그램 분석 종료==============");
		sc.close();
	}
	
	public static void main(String[] args) throws IOException {
		VirusCheckingProgram p = new VirusCheckingProgram();
		p.inputVirusCode();
		p.inputFile();
		p.showResult();

	}
}


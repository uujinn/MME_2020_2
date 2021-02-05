import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class StudentRegister {
	private static final String FILE_NAME = "C:\\Users\\ureal\\Desktop\\StudentRegister\\Register.txt";
	private static final int NUMBER_INDEX = 0;
	private static final int NAME_INDEX = 1;
	private static final int MAJOR_INDEX = 2;
	private ArrayList<Student> students = new ArrayList<>();

	public static void main(String[] args) {
		StudentRegister studentRegister = new StudentRegister();
		studentRegister.run();
	}

	public static void sort(ArrayList<Student> s) { // swap 작업없이 단순히 값들을 shift 시키는 것만으로도 삽입 정렬의 구현이 가능합니다.
													// 앞의 값이 정렬 범위에 추가시킨 값보다 클 경우 앞의 값을 뒤로 밀다가 최초로 작은 값을 만나는 순간 그 뒤에 추가된 값을 꼽으면 됩니다
		for (int end = 1; end < s.size(); end++) {
			Student key = s.get(end);
			int i = end;
			while (i > 0 && (Integer.parseInt(s.get(i - 1).getNumber())) > Integer.parseInt(key.getNumber())) {
				s.set(i, s.get(i - 1));
				i--;
			}
			s.set(i, key);
		}

	}

	public void run() {
		readFile();
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("=================Menu=================");
			System.out.println("1.입력\t2.정렬\t3.삭제\t4.출력\t5.종료");
			int selection = scanner.nextInt();
							
			if (selection == 5) {
				System.out.println("학적부가 종료됩니다.");
				writeToFile();
				break;
			}
			switch (selection) {
			case 1:
				System.out.print("학번 이름 전공 순으로 입력하세요: ");	// 학번, 이름, 전공을 순차적으로 입력받아 학생 목록에 추가

				Student student = new Student(scanner.next(), scanner.next(), scanner.next());
				students.add(student);
				break;
			case 2:
				long start = System.nanoTime();
				sort(students);
				long end = System.nanoTime();
				System.out.println("Insertion 정렬 알고리즘 실행 시간" + (double) (end - start) / 1000000 + "ms");
				break;
			case 3:
				System.out.print("삭제할 학번을 입력하세요: ");
				String number = scanner.next();	// 학번으로 학생을 삭제하여 삭제 성공 여부를 반환받음

				if (delete(number))	// 삭제 성공시
					System.out.println("삭제하였습니다.");

				else	// 삭제 실패시
					System.out.println("해당 학번이 존재하지 않습니다.");
				break;
			case 4:
				for (Student s : students)
					System.out.println(s);
				break;
			default:
				System.out.println("잘못 입력하셨습니다.");
				break;
			}
		}
	}

	private void readFile() {
		File file = new File(FILE_NAME);
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line;
			String[] split;
			
			while ((line = br.readLine()) != null) {	// 라인을 읽어 null이 아닐 때까지 반복
				
				split = line.split(" ");	// 공백으로 나눔
				// Student 객체를 만들어 리스트에 추가
				students.add(new Student(split[NUMBER_INDEX], split[NAME_INDEX], split[MAJOR_INDEX]));
			}
			br.close();
		} catch (IOException e) {
			System.out.println("파일 읽기에 실패하였습니다.");
		}
	}

	private boolean delete(String number) {
		Iterator<Student> iterator = students.iterator();
		while (iterator.hasNext()) {
			Student student = iterator.next();
			
			if (number.equals(student.getNumber())) {	// 입력한 학번과 동일할 경우 삭제 후 true 반환
				iterator.remove();
				return true;
			}
		}
		
		return false;// 입력한 학번을 찾지 못했을 경우 false 반환
	}

	private void writeToFile() {
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter("Register.txt");
			for (Student student : students) {// 파일에 학생 정보와 개행문자 추가
				
				fileWriter.append(student.toString());
				fileWriter.append("\n");
			}
			fileWriter.close();
		} catch (IOException e) {
			System.out.println("파일에 쓰는데 실패하였습니다.");
		}
	}
}

class Student {
	private String number;
	private String name;
	private String major;

	public Student(String number, String name, String major) {
		this.number = number;
		this.name = name;
		this.major = major;
	}

	public String getNumber() {
		return number;
	}

	public String getName() {
		return name;
	}

	public String getMajor() {
		return major;
	}

	public int compareTo(Student s) {
		// 학번을 비교함
		int result = this.number.compareTo(s.number);
		// 학번이 서로 다르면 비교 결과 반환
		if (result != 0)
			return result;
		// 학번이 같으면 이름을 비교함
		result = this.name.compareTo(s.name);
		// 이름이 서로 다르면 결과 반환
		if (result != 0)
			return result;
		// 이름이 같으면 전공 비교 후 결과 반환
		return this.major.compareTo(s.major);
	}

	public String toString() {
		// 학번 이름 전공 순으로 출력
		return String.format("%s %s %s", this.number, this.name, this.major);
	}
}
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EmptyStackException;

public class MyCalculator extends JFrame {
	private static final double PI = 3.141592;
	public static final int NUMBER_MODE = 1;
	public static final int OPERATOR_MODE = 2;
	public static final int DONE = 3;
	private static final Color CUSTOM_GREEN = new Color(0x30B264);
	private static final Color CUSTOM_RED = new Color(0xF33F12);
	private static final Color CUSTOM_BLACK = new Color(0x1B1818);
	private static int status;
	private JLabel resultLabel, infoLabel;
	private JTextField inputTextField;
	private boolean isAbleToAddPoint = true;
	private final Calculator calculator;

	public MyCalculator(Calculator calculator) {
		this.calculator = calculator;
		setTitle("공학용 계산기"); // 프레임 제목 설정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창을 닫으면 애플리케이션 종료
		initMenu(); // 메뉴 생성
		initPanel(); // 페널 생성
		setResizable(false);
		setSize(450, 600);
		setLocationRelativeTo(null); // 프레임을 가운데 오게 함
		setVisible(true);
	}

	private void initPanel() { // panel 설정
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.setBackground(CUSTOM_BLACK);

		CenterPanel cp = new CenterPanel();
		cp.setBorder(new EmptyBorder(5, 5, 5, 5));
		c.add(cp, BorderLayout.CENTER);

		SouthPanel sp = new SouthPanel();
		sp.setBorder(new EmptyBorder(5, 5, 5, 5));
		c.add(sp, BorderLayout.SOUTH);
	}

	class CenterPanel extends JPanel { // infoLabel, inputTextField, resultLabel
		public CenterPanel() {

			setLayout(new GridLayout(3, 1));
			setBackground(CUSTOM_BLACK);

			infoLabel = new JLabel("[사칙 연산]"); // 지금 어떤 계산을 하는지 나타내준다
			resultLabel = new JLabel("정답 표시 ");
			inputTextField = new JTextField(); // 계산창

			infoLabel.setBackground(CUSTOM_BLACK);
			infoLabel.setForeground(Color.WHITE);
			infoLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
			infoLabel.setHorizontalAlignment(SwingConstants.RIGHT);

			resultLabel.setBackground(CUSTOM_BLACK);
			resultLabel.setForeground(Color.WHITE);
			resultLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
			resultLabel.setHorizontalAlignment(SwingConstants.RIGHT);

			inputTextField.setBackground(Color.WHITE);
			inputTextField.setForeground(CUSTOM_BLACK);
			inputTextField.setFont(new Font("Arial", Font.BOLD, 35));
			inputTextField.setHorizontalAlignment(SwingConstants.RIGHT);
			inputTextField.setEditable(false); // 계산창은 편집되면 안되기때문에 false로 설정
			inputTextField.setBorder(null);
			add(infoLabel);
			add(inputTextField);
			add(resultLabel);

		}
	}

	class SouthPanel extends JPanel { // 버튼 넣는 panel
		private final String[] buttonNames = { "(-)", "÷", "7", "8", "9", "×", "4", "5", "6", "-", "1", "2", "3", "+",
				"x^", "0" };

		public SouthPanel() {
			setLayout(new GridLayout(6, 4, 5, 5));
			setBackground(CUSTOM_BLACK);
			JButton[] buttons = new JButton[buttonNames.length];
			Button clearButton = new Button("C");
			// "C" 버튼 생성 및 초기화
			clearButton.setForeground(CUSTOM_RED);
			clearButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					inputTextField.setText("");
					resultLabel.setText("수식을 입력하세요");
					infoLabel.setText("[사칙연산]");
				}
			});
			// "<-" 버튼 생성 및 초기화
			Button backspaceButton = new Button("⟵");
			backspaceButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// 수식이 비어있으면 아무것도 안 함
					String value = inputTextField.getText();
					if (value.isEmpty())
						return;
					// 뒤부터 한 칸을 지움
					inputTextField.setText(value.substring(0, value.length() - 1));
				}
			});
			backspaceButton.setForeground(CUSTOM_GREEN);
			// 버튼을 패널에 추가
			add(clearButton);
			add(backspaceButton);
			// "C", "<-", ".", "="를 제외한 버튼을 모두 추가
			for (int i = 0; i < buttonNames.length; i++) {
				String buttonName = buttonNames[i];
				// 숫자이면 NumberButton, 그렇지 않으면 OperatorButton 생성 및 초기화
				buttons[i] = isNumber(buttonName) ? new NumberButton(buttonName) : new OperatorButton(buttonName);
				// 리스너 추가
				if (i != 0 && i != 14) {
					buttons[i].addActionListener(new ButtonListener());
				}

				// 패널에 버튼 추가
				add(buttons[i]);
			}
			// (-) 버튼 추가
			buttons[0].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					inputTextField.setText(inputTextField.getText() + "(-)");
				}

			});

			// x^ 버튼 색상 변경
			buttons[14].setForeground(Color.DARK_GRAY);
			buttons[14].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					inputTextField.setText(inputTextField.getText() + "x^");
				}

			});

			// "." 버튼 생성 및 초기화, 리스너 추가
			Button pointButton = new Button(".");
			pointButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// 소수점을 더 이상 찍을 수 없을 때 아무것도 안 함
					if (!isAbleToAddPoint)
						return;
					// 소수점을 찍을 수 있을 때 점 찍고 플래그 값을 바꿔줌
					inputTextField.setText(inputTextField.getText() + ".");
					isAbleToAddPoint = false;
				}
			});
			// "=" 버튼 생성 및 초기화
			Button equalsButton = new Button("=");
			equalsButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						double result = calculator.evaluate(inputTextField.getText());
						resultLabel.setText(String.valueOf(result));
						status = DONE;
					} catch (NumberFormatException e1) {
						resultLabel.setText("[잘못된 입력값]");
					} catch (EmptyStackException e2) {
						resultLabel.setText("[잘못된 입력값]");
					}

				}
			});
			equalsButton.setBackground(CUSTOM_GREEN);
			equalsButton.setForeground(Color.WHITE);

			Button sinButton = new Button("sin");
			sinButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					inputTextField.setText(inputTextField.getText() + "sin");
				}
			});
			Button cosButton = new Button("cos");
			cosButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					inputTextField.setText(inputTextField.getText() + "cos");
				}
			});
			Button tanButton = new Button("tan");
			tanButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					inputTextField.setText(inputTextField.getText() + "tan");
				}
			});
			Button commaButton = new Button(",");
			commaButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					inputTextField.setText(inputTextField.getText() + ",");
				}
			});
			commaButton.setForeground(CUSTOM_GREEN);
			// 버튼을 패널에 추가
			add(pointButton);
			add(commaButton);
			add(sinButton);
			add(cosButton);
			add(tanButton);
			add(equalsButton);

		}

		// 숫자인지 판별
		// buttonName : 버튼 이름
		// boolean 숫자면 true, 아니면 false
		private boolean isNumber(String buttonName) {
			try {
				// 숫자로 파싱해보고 에러가 나지 않으면 true
				Integer.parseInt(buttonName);
				return true;
			} catch (NumberFormatException e) {
				// 문자열이라 에러가 나면 false
				return false;
			}
		}
	}

	class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// 이벤트 소스를 버튼으로 형변환
			Button button = (Button) e.getSource();
			// 기존 문자열 획득
			String originalText = inputTextField.getText();
			// 비어있을 경우 숫자만 입력할 수 있음
			if (originalText.isEmpty())
				status = NUMBER_MODE;
			switch (status) {
			case NUMBER_MODE: // 숫자를 입력 중일 때, 숫자/연산자 상관없이 입력 가능
				inputTextField.setText(originalText + button.getValue());
				break;
			case OPERATOR_MODE: // 직전 입력이 연산자 일 때, 숫자는 추가, 연산자 중 "="는 계산, 나머지는 치환
				if (button instanceof NumberButton) {
					inputTextField.setText(originalText + button.getValue());
				} else {
					inputTextField.setText(originalText.substring(0, originalText.length() - 1) + button.getValue());
				}
				isAbleToAddPoint = true;
				break;
			case DONE: // 계산이 끝났을 때("=" 버튼을 누른 이후)
				// 다시 점을 찍을 수 있게 바꿔줌
				isAbleToAddPoint = true;
				// 숫자를 바로 입력하면 다 지워진 상태에서 숫자만 입력됨
				if (button instanceof NumberButton) {
					inputTextField.setText(button.getValue());
				}
				// 연산자를 입력하면 뒤에 추가됨
				else if (button instanceof OperatorButton) {
					inputTextField.setText(inputTextField.getText() + button.getValue());
				}
				break;
			default:
				inputTextField.setText(originalText + button.getValue());

			}
		}

	}

	class Button extends JButton {
		public Button(String value) {
			super(value);
			setFont(new Font("Sanserif", Font.BOLD, 27));
			setOpaque(false);
			setForeground(Color.DARK_GRAY);
			setBackground(Color.LIGHT_GRAY);
		}

		public String getValue() {
			// 입력 모드를 변환하지 않음
			return getText();
		}
	}

	class NumberButton extends Button {
		public NumberButton(String value) {
			super(value);
		}

		@Override
		public String getValue() {
			// 값을 반환하기 전에 숫자 입력 모드로 전환
			status = NUMBER_MODE;
			return super.getValue();
		}
	}

	class OperatorButton extends Button {
		public OperatorButton(String value) {
			super(value);
			setForeground(CUSTOM_GREEN);
		}

		@Override
		public String getValue() {
			// 값을 반환하기 전에 연산자 입력 모드로 전환
			status = OPERATOR_MODE;
			return super.getValue();
		}
	}

	private void initMenu() { // 메뉴 설정
		JMenuBar menuBar = new JMenuBar();

		String[] menuName = { "Function", "Notation", "Matrix" };
		JMenu Menu[] = new JMenu[3];
		for (int i = 0; i < Menu.length; i++) {
			Menu[i] = new JMenu(menuName[i]);
			menuBar.add(Menu[i]);

		}

		String[] FuncName = { "Integral", "Differential", "Trigonometric" }; // 적분, 미분, 삼각함수 계산
		JMenuItem FuncItem[] = new JMenuItem[3];
		for (int i = 0; i < FuncItem.length; i++) {
			FuncItem[i] = new JMenuItem(FuncName[i]);
			Menu[0].add(FuncItem[i]);
			FuncItem[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JMenuItem m = (JMenuItem) e.getSource();
					if (m.getText().equals("Integral")) {
						try {
							infoLabel.setText("[정적분]");
							String polynomial = inputTextField.getText();
							resultLabel.setText(IntegralFunc(polynomial));
						} catch (ArrayIndexOutOfBoundsException e1) {
							resultLabel.setText("[잘못된 입력값]");
						} catch (NumberFormatException e2) {
							resultLabel.setText("[잘못된 입력값]");
						}
					}
					if (m.getText().equals("Differential")) {
						try {
							infoLabel.setText("[미분]");
							String polynomial = inputTextField.getText();
							resultLabel.setText(DifferentialFunc(polynomial));
						} catch (ArrayIndexOutOfBoundsException e1) {
							resultLabel.setText("[잘못된 입력값]");
						} catch (NumberFormatException e2) {
							resultLabel.setText("[잘못된 입력값]");
						}
					}
					if (m.getText().equals("Trigonometric")) {
						infoLabel.setText("[삼각함수 계산]");
						String polynomial = inputTextField.getText();
						resultLabel.setText(Trigonometric(polynomial));

					}
				}
			});
		}

		String[] NotationName = { "Binary", "Hex" }; // 진수 변환
		JMenuItem NotationItem[] = new JMenuItem[2];
		for (int i = 0; i < NotationItem.length; i++) {
			NotationItem[i] = new JMenuItem(NotationName[i]);
			Menu[1].add(NotationItem[i]);
			NotationItem[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JMenuItem m = (JMenuItem) e.getSource();
					if (m.getText().equals("Binary")) {
						try {
							infoLabel.setText("[10진수에서 2진수로 변환]");
							int DecimalNum = Integer.parseInt(inputTextField.getText());
							resultLabel.setText(DecimalToBinary(DecimalNum));
						} catch (NumberFormatException e1) {
							resultLabel.setText("잘못된 입력값");
						}
					}
					if (m.getText().equals("Hex")) {
						try {
							infoLabel.setText("[10진수에서 16진수로 변환]");
							int DecimalNum = Integer.parseInt(inputTextField.getText());
							resultLabel.setText(DecimalToHex(DecimalNum));
						} catch (NumberFormatException e2) {
							resultLabel.setText("잘못된 입력값");
						}
					}
				}
			});
		}

		String[] MatrixName = { "Transposition", "Inverse" }; // 행렬 계산
		JMenuItem MatrixItem[] = new JMenuItem[2];
		for (int i = 0; i < MatrixItem.length; i++) {
			MatrixItem[i] = new JMenuItem(MatrixName[i]);
			Menu[2].add(MatrixItem[i]);
			MatrixItem[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JMenuItem m = (JMenuItem) e.getSource();
					if (m.getText().equals("Transposition")) {
						try {
							infoLabel.setText("[전치행렬 변환]");
							String matrix = inputTextField.getText();
							resultLabel.setText(ToTranspose(matrix));
						} catch (ArrayIndexOutOfBoundsException e1) {
							resultLabel.setText("[잘못된 입력값]");
						}
					}
					if (m.getText().equals("Inverse")) {
						try {
							infoLabel.setText("[역행렬 변환]");
							String matrix = inputTextField.getText();
							resultLabel.setText(ToInverse(matrix));
						} catch (ArrayIndexOutOfBoundsException e1) {
							resultLabel.setText("[잘못된 입력값]");
						}
					}
				}
			});
		}

		setJMenuBar(menuBar);
	}

	private String IntegralFunc(String polynomial) { // 적분 계산

		String[] division = polynomial.split(","); // ","로 구분하여 계산하는 식과 대입할 값을 나눠준다
		int a = Integer.parseInt(division[1]);
		int b = Integer.parseInt(division[2]);

		String[] poly = division[0].split("\\+"); // "+"로 구분하여 항을 나눠준다

		String[][] separation = new String[poly.length][2];
		for (int i = 0; i < poly.length; i++) {
			separation[i] = poly[i].split("x\\^"); // "x^"로 계수와 차수를 구분해준다.
			for (int j = 0; j < 2; j++) {
				if (separation[i][j].contains("-")) {
					separation[i][j] = separation[i][j].replace("(-)", "-"); // 앞에 (-)가 있으면 계수와 차수를 음수로 간주하여 바꾸어준다
				}
			}
		}
		int num[] = new int[poly.length * 2];
		int k = 0;
		for (int i = 0; i < poly.length; i++) {
			for (int j = 0; j < 2; j++) {
				num[k++] = Integer.parseInt(separation[i][j]);
			}
		}

		double sumA = 0; // 첫번째 값 a 대입해서 계산
		for (int i = 0; i < separation.length; i++) { // 정적분 과정 구현
			double n = (num[2 * (i + 1) - 2] / (num[2 * (i + 1) - 1] + 1)) * Math.pow(a, num[2 * (i + 1) - 1] + 1);
			sumA += n;
		}

		double sumB = 0; // 두번재 값 b 대입해서 계산
		for (int i = 0; i < separation.length; i++) { // 정적분 과정 구현
			double n = (num[2 * (i + 1) - 2] / (num[2 * (i + 1) - 1] + 1)) * Math.pow(b, num[2 * (i + 1) - 1] + 1);
			sumB += n;
		}

		String result = "";
		String t[] = new String[separation.length];

		String start = "[";
		result += start;
		String last = "";
		for (int i = 0; i < separation.length; i++) { // 출력을 위해 String으로 정리해줌
			t[i] = ((double) num[2 * (i + 1) - 2] / (double) (num[2 * (i + 1) - 1] + 1)) + "x^"
					+ (num[2 * (i + 1) - 1] + 1);
			if (i == separation.length - 1) {
				last = "]" + a + "," + b + "="; // 마지막
			} else {
				last = "+"; // 중간
			}
			result = result + t[i] + last;

		}

		String value = Double.toString(sumA - sumB); // 정적분 값
		result += value;

		return result;

	}

	private String DifferentialFunc(String polynomial) { // 미분계산
		String[] division = polynomial.split(",");
		int a = Integer.parseInt(division[1]);
		String[] poly = division[0].split("\\+"); // "+"로 구분하여 항을 나눠준다

		String[][] separation = new String[poly.length][2];
		for (int i = 0; i < poly.length; i++) {
			separation[i] = poly[i].split("x\\^"); // "x^"로 계수와 차수를 구분해준다.
			for (int j = 0; j < 2; j++) {
				if (separation[i][j].contains("-")) {
					separation[i][j] = separation[i][j].replace("(-)", "-"); // 앞에 (-)가 있으면 음수로 간주하여 바꾸어준다
				}
			}
		}
		int num[] = new int[poly.length * 2];
		int k = 0;
		for (int i = 0; i < poly.length; i++) {
			for (int j = 0; j < 2; j++) {
				num[k++] = Integer.parseInt(separation[i][j]);
			}
		}

		double sumA = 0; // a 대입해서 미분값 계산
		for (int i = 0; i < separation.length; i++) {
			if (num[2 * (i + 1) - 1] == 0) {

				continue;
			}
			double n = ((double) num[2 * (i + 1) - 2] * (double) (num[2 * (i + 1) - 1]))
					* Math.pow(a, num[2 * (i + 1) - 1] - 1);
			sumA += n;
		}

		String result = "";
		String t[] = new String[separation.length];

		String start = "[";
		result += start;
		String last = "";
		for (int i = 0; i < separation.length; i++) { // 출력을 위해 String으로 정리해줌
			if (num[2 * (i + 1) - 1] - 1 == -1) {
				i = separation.length - 1;
			} else {
				t[i] = ((double) num[2 * (i + 1) - 2] * (double) (num[2 * (i + 1) - 1])) + "x^"
						+ (num[2 * (i + 1) - 1] - 1);
				result += t[i];
			}

			if (i == separation.length - 1) {
				last = "](" + a + ")="; // 마지막
			}

			else if ((num[2 * (i + 1) - 1] - 1 == 0)) { // 상수항은 사라지므로 "+" 대신 ""로 처리
				last = "";
			}

			else {
				last = "+"; // 중간
			}
			result = result + last;

		}

		String value = Double.toString(sumA); // 정적분 값
		result += value;

		return result;
	}

	private String Trigonometric(String polynomial) {

		double result = 0;

		double a = 0;
		double b = 0;

		if (polynomial.contains("+")) {
			String poly[] = new String[2];
			poly = polynomial.split("\\+");

			for (int i = 0; i < poly.length; i++) {
				if (poly[i].contains("sin")) {
					poly[i] = poly[i].replace("sin", "");
					if (i == 0) // degree & radian 변환 포함
						a = (Math.round((Math.sin(Double.parseDouble(poly[i]) / 180.0 * PI)) * 100)) / 100.0;
					else if (i == 1)
						b = (Math.round((Math.sin(Double.parseDouble(poly[i]) / 180.0 * PI)) * 100)) / 100.0;
				}
				if (poly[i].contains("cos")) {
					poly[i] = poly[i].replace("cos", "");
					if (i == 0)
						a = (Math.round((Math.cos(Double.parseDouble(poly[i]) / 180.0 * PI)) * 100)) / 100.0;
					else if (i == 1)
						b = (Math.round((Math.cos(Double.parseDouble(poly[i]) / 180.0 * PI)) * 100)) / 100.0;
				}
				if (poly[i].contains("tan")) {
					poly[i] = poly[i].replace("tan", "");
					if (i == 0)
						a = (Math.round((Math.tan(Double.parseDouble(poly[i]) / 180.0 * PI)) * 100)) / 100.0;
					else if (i == 1)
						b = (Math.round((Math.tan(Double.parseDouble(poly[i]) / 180.0 * PI)) * 100)) / 100.0;
				}
			}

			result = (Math.round((a + b) * 100)) / 100.0;

		} else if (polynomial.contains("-")) {
			String poly[] = new String[2];
			poly = polynomial.split("\\-");

			for (int i = 0; i < poly.length; i++) {
				if (poly[i].contains("sin")) {
					poly[i] = poly[i].replace("sin", "");
					if (i == 0)
						a = (Math.round((Math.sin(Double.parseDouble(poly[i]) / 180.0 * PI)) * 100)) / 100.0;
					else if (i == 1)
						b = (Math.round((Math.sin(Double.parseDouble(poly[i]) / 180.0 * PI)) * 100)) / 100.0;
				}
				if (poly[i].contains("cos")) {
					poly[i] = poly[i].replace("cos", "");
					if (i == 0)
						a = (Math.round((Math.cos(Double.parseDouble(poly[i]) / 180.0 * PI)) * 100)) / 100.0;
					else if (i == 1)
						b = (Math.round((Math.cos(Double.parseDouble(poly[i]) / 180.0 * PI)) * 100)) / 100.0;
				}
				if (poly[i].contains("tan")) {
					poly[i] = poly[i].replace("tan", "");
					if (i == 0)
						a = (Math.round((Math.tan(Double.parseDouble(poly[i]) / 180.0 * PI)) * 100)) / 100.0;
					else if (i == 1)
						b = (Math.round((Math.tan(Double.parseDouble(poly[i]) / 180.0 * PI)) * 100)) / 100.0;
				}
			}

			result = (Math.round((a - b) * 100)) / 100.0;

		} else if (polynomial.contains("×")) {
			String poly[] = new String[2];
			poly = polynomial.split("\\×");

			for (int i = 0; i < poly.length; i++) {
				if (poly[i].contains("sin")) {
					poly[i] = poly[i].replace("sin", "");
					if (i == 0)
						a = (Math.round((Math.sin(Double.parseDouble(poly[i]) / 180.0 * PI)) * 100)) / 100.0;
					else if (i == 1)
						b = (Math.round((Math.sin(Double.parseDouble(poly[i]) / 180.0 * PI)) * 100)) / 100.0;
				}
				if (poly[i].contains("cos")) {
					poly[i] = poly[i].replace("cos", "");
					if (i == 0)
						a = (Math.round((Math.cos(Double.parseDouble(poly[i]) / 180.0 * PI)) * 100)) / 100.0;
					else if (i == 1)
						b = (Math.round((Math.cos(Double.parseDouble(poly[i]) / 180.0 * PI)) * 100)) / 100.0;
				}
				if (poly[i].contains("tan")) {
					poly[i] = poly[i].replace("tan", "");
					if (i == 0)
						a = (Math.round((Math.tan(Double.parseDouble(poly[i]) / 180.0 * PI)) * 100)) / 100.0;
					else if (i == 1)
						b = (Math.round((Math.tan(Double.parseDouble(poly[i]) / 180.0 * PI)) * 100)) / 100.0;
				}
			}

			result = (Math.round((a * b) * 100)) / 100.0;
		} else if (polynomial.contains("÷")) {
			String poly[] = new String[2];
			poly = polynomial.split("\\÷");

			for (int i = 0; i < poly.length; i++) {
				if (poly[i].contains("sin")) {
					poly[i] = poly[i].replace("sin", "");
					if (i == 0)
						a = (Math.round((Math.sin(Double.parseDouble(poly[i]) / 180.0 * PI)) * 100)) / 100.0;
					else if (i == 1)
						b = (Math.round((Math.sin(Double.parseDouble(poly[i]) / 180.0 * PI)) * 100)) / 100.0;
				}
				if (poly[i].contains("cos")) {
					poly[i] = poly[i].replace("cos", "");
					if (i == 0)
						a = (Math.round((Math.cos(Double.parseDouble(poly[i]) / 180.0 * PI)) * 100)) / 100.0;
					else if (i == 1)
						b = (Math.round((Math.cos(Double.parseDouble(poly[i]) / 180.0 * PI)) * 100)) / 100.0;
				}
				if (poly[i].contains("tan")) {
					poly[i] = poly[i].replace("tan", "");
					if (i == 0)
						a = (Math.round((Math.tan(Double.parseDouble(poly[i]) / 180.0 * PI)) * 100)) / 100.0;
					else if (i == 1)
						b = (Math.round((Math.tan(Double.parseDouble(poly[i]) / 180.0 * PI)) * 100)) / 100.0;
				}
			}

			result = (Math.round((a / b) * 100)) / 100.0;

		}
		String resultText = Double.toString(result);

		return resultText;
	}

	private String DecimalToBinary(int DecimalNum) { // 10진수를 2진수로 변환하는 메소드
		int remainder = 0;
		String temp = "";
		String BinaryNum;

		while (true) {
			remainder = DecimalNum % 2; // 2로 나눈 나머지
			DecimalNum = DecimalNum / 2; // 10진수를 계속 2로 나눈다
			temp = remainder + temp;
			if (DecimalNum == 0) {
				break;
			}

		}

		BinaryNum = temp;

		return BinaryNum;
	}

	private String DecimalToHex(int DecimalNum) { // 10진수를 16진수로 변환하는 메소드

		int remainder = 0;
		String temp = "";
		String HexNum;

		while (true) {
			remainder = DecimalNum % 16; // 16으로 나눈 나머지
			DecimalNum = DecimalNum / 16; // 16으로 나눈 몫

			if (remainder == 10) {
				temp = "A" + temp; // 10-> A
			} else if (remainder == 11) {
				temp = "B" + temp; // 11-> B
			} else if (remainder == 12) {
				temp = "C" + temp; // 12-> C
			} else if (remainder == 13) {
				temp = "D" + temp; // 13-> D
			} else if (remainder == 14) {
				temp = "E" + temp; // 14-> E
			} else if (remainder == 15) {
				temp = "F" + temp; // 15-> F
			} else {
				temp = remainder + temp;
			}
			if (DecimalNum == 0) {
				break;
			}

		}

		HexNum = temp;

		return HexNum;
	}

	private static String ToTranspose(String tempStr) { // 전치행렬 구하는 메소드

		String[] entryStr = tempStr.split(",");

		int halfOfentry = entryStr.length / 2;
		int[][] entries = new int[halfOfentry][halfOfentry];
		int k = 0;

		for (int i = 0; i < halfOfentry; i++) {
			for (int j = 0; j < halfOfentry; j++) {
				entries[i][j] = Integer.parseInt(entryStr[k]);
				k++;
			}
		}

		int[][] TransposeMatrix = new int[halfOfentry][halfOfentry];
		for (int i = 0; i < halfOfentry; i++) {
			for (int j = 0; j < halfOfentry; j++) {
				TransposeMatrix[i][j] = entries[j][i]; // 전치행렬의 개념
			}
		}

		String[] uptext = new String[halfOfentry + 2];
		String[] downtext = new String[halfOfentry + 2];

		uptext[0] = "|";
		uptext[halfOfentry + 1] = "|";
		for (int i = 1; i < halfOfentry + 1; i++) {
			uptext[i] = Integer.toString(TransposeMatrix[0][i - 1]);

		}
		downtext[0] = "|";
		downtext[halfOfentry + 1] = "|";
		for (int i = 1; i < halfOfentry + 1; i++) {
			downtext[i] = Integer.toString(TransposeMatrix[1][i - 1]);

		}

		StringBuilder sb1 = new StringBuilder();
		for (String item : uptext) {
			if (sb1.length() > 0) {
				sb1.append(" ");

			}
			sb1.append(item);
		}

		StringBuilder sb2 = new StringBuilder();
		for (String item : downtext) {
			if (sb2.length() > 0) {
				sb2.append(" ");

			}
			sb2.append(item);

		}

		return "<html><body>" + sb1 + "<br>" + sb2 + "</body></html>";

	}

	private static String ToInverse(String tempStr) { // 역행렬 구하는 메소드

		String[] entryStr = tempStr.split(",");
		int halfOfentry = entryStr.length / 2;
		int[][] entries = new int[halfOfentry][halfOfentry];
		int k = 0;
		for (int i = 0; i < halfOfentry; i++) {
			for (int j = 0; j < halfOfentry; j++) {
				entries[i][j] = Integer.parseInt(entryStr[k]);
				k++;
			}
		}

		double[][] InverseMatrix = new double[2][2]; // 역행렬의 개념
		double adbc = (entries[0][0] * entries[1][1]) - (entries[1][0] * entries[0][1]); // ad-bc공식
		InverseMatrix[0][0] = Math.round(entries[1][1] / adbc * 100) / 100.0;
		InverseMatrix[0][1] = Math.round(-(entries[0][1] / adbc * 100)) / 100.0;
		InverseMatrix[1][0] = Math.round(-(entries[1][0] / adbc * 100)) / 100.0;
		InverseMatrix[1][1] = Math.round(entries[0][0] / adbc * 100) / 100.0;

		String[] uptext = new String[halfOfentry + 2];
		String[] downtext = new String[halfOfentry + 2];

		uptext[0] = "|";
		uptext[halfOfentry + 1] = "|";
		for (int i = 1; i < halfOfentry + 1; i++) {
			uptext[i] = Double.toString(InverseMatrix[0][i - 1]);

		}

		downtext[0] = "|";
		downtext[halfOfentry + 1] = "|";
		for (int i = 1; i < halfOfentry + 1; i++) {
			downtext[i] = Double.toString(InverseMatrix[1][i - 1]);

		}

		StringBuilder sb1 = new StringBuilder();
		for (String item : uptext) {
			if (sb1.length() > 0) {
				sb1.append(" ");

			}
			sb1.append(item);
		}

		StringBuilder sb2 = new StringBuilder();
		for (String item : downtext) {
			if (sb2.length() > 0) {
				sb2.append(" ");

			}
			sb2.append(item);

		}

		return "<html><body>" + sb1 + "<br>" + sb2 + "</body></html>";

	}

}
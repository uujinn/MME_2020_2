import javax.swing.*;
import java.util.Stack;

public class Calculator extends JFrame {

	public static void main(String[] args) {
		initTheme();
		Calculator calculator = new Calculator();
		new MyCalculator(calculator);
	}

	private static void initTheme() {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception ignored) {
		}
	}

	public double evaluate(String expression) {
		Stack<Character> operators = new Stack<>();
		Stack<Double> operands = new Stack<>();

		int start = 0;
		for (int i = 0; i < expression.length();) {
			char c = expression.charAt(i);
			if (isOperator(c)) {
				while (!operators.isEmpty() && getPrecedence(operators.peek()) >= getPrecedence(c)) {
					operands.push(apply(operators.pop(), operands.pop(), operands.pop()));
				}
				operators.push(c);
				i++;
			} else {
				StringBuilder sb = new StringBuilder();
				while (!isOperator(c)) {
					sb.append(c);
					i++;
					if (i == expression.length())
						break;
					c = expression.charAt(i);
				}
				operands.push(Double.valueOf(sb.toString()));
			}
		}

		while (!operators.isEmpty()) {
			operands.push(apply(operators.pop(), operands.pop(), operands.pop()));
		}

		return operands.pop();
	}

	private Double apply(Character operator, Double first, Double second) {
		switch (operator) {
		case '+':
			return first + second;
		case '-':
			return second - first;
		case '×':
			return first * second;
		case '÷':
			return second / first;
		}
		throw new IllegalArgumentException("잘못된 입력값");
	}


	// 문자가 연산자인지 여부를 판단 
	private boolean isOperator(char c) {
		return c == '+' || c == '-' || c == '×' || c == '÷';
	}


	// 연산자의 우선순위를 반환
	public int getPrecedence(char operator) {
		switch (operator) {
		case '+':
		case '-':
			return 1;
		case '×':
		case '÷':
			return 2;
		}
		return -1;
	}


}


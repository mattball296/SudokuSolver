import javax.swing.*;
import java.awt.*;
import javax.swing.BorderFactory.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.util.concurrent.*;

public class SudokuGUI{
	
	private int idx;
	JTextField[] elements;
	JFrame window;
	
	public final Font FONT1 = new Font("SansSerif",Font.BOLD,20);
	public final Font FONT2 = new Font("SansSerif",Font.PLAIN,20);
	public final Font FONT3 = new Font("SansSerif",Font.PLAIN,14);
	
	public static int ArrayIndextoBoardIndex(int A){
		return(int)(9*(3*Math.floor((A/27))+Math.floor((A%9)/3))+3*(Math.floor((A%27)/9))+(A%3));
	}
	
	public static int ArrayIndextoRow(int A){
		return (int)Math.floor(A/9);
	}
	
	public static int ArrayIndextoCol(int A){
		return A%9;
	}
	
	public static int RCtoBoardIndex(int R, int C){
		return ArrayIndextoBoardIndex(9*R+C);
	}
	
	public static int BoardIndextoArrayIndex(int B){
		return 9*BoardIndextoRow(B)+BoardIndextoCol(B);
	}
	
	public static int BoardIndextoRow(int B){
		return ((3*((int)Math.floor((int)Math.floor(B/9)/3)))+(int)Math.floor((B%9)/3));
	}
	
	public static int BoardIndextoCol(int B){
		return (3*(int)Math.floor((B%27)/9)+B%3);
	}
	
	public SudokuGUI(){
		this.idx = 0;
		try { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());  // sets look and feel
        } 
        catch (Exception e) { 
            System.err.println(e.getMessage()); 
        } 
		this.window  = new JFrame("Sudoku Solver");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(new Dimension(525,525));
		
		JPanel GUI = new JPanel(new BorderLayout(5,5));
		JPanel board = new JPanel(new GridLayout(3,3));
		
		this.elements = new JTextField[81]; 
		for(int x = 0; x < 81; x++){
			int c = x;
			JTextField JTF = new JTextField();
			JTF.setHorizontalAlignment(SwingConstants.CENTER);
			JTF.setBorder(BorderFactory.createLineBorder(Color.lightGray,1));
			JTF.setFont(FONT1);
			JTF.setEditable(false);
			JTF.setFocusable(true);
			JTF.addFocusListener(new FocusAdapter(){
				public void focusGained(FocusEvent e){
					JTF.setBorder(BorderFactory.createLineBorder(Color.darkGray,2));
					idx = ArrayIndextoBoardIndex(c);
				}
				public void focusLost(FocusEvent e){
					JTF.setBorder(BorderFactory.createLineBorder(Color.lightGray,1));
				}
			});
			
			JTF.addKeyListener(new KeyAdapter(){
				public void keyPressed(KeyEvent e){
					if(e.getKeyCode()==KeyEvent.VK_ENTER){
					SolveSudoku();
					}
					else if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE){
						JTF.setText("");
					}
					else if(e.getKeyCode()==KeyEvent.VK_RIGHT&&c!=80){
						idx++;
						elements[BoardIndextoArrayIndex(idx)].requestFocus();
					}
					else if(e.getKeyCode()==KeyEvent.VK_LEFT&&c!=0){
						idx--;
						elements[BoardIndextoArrayIndex(idx)].requestFocus();
					}
					else if(e.getKeyCode()==KeyEvent.VK_UP&&BoardIndextoRow(c)!=0){
						idx-=9;
						elements[BoardIndextoArrayIndex(idx)].requestFocus();
					}
					else if(e.getKeyCode()==KeyEvent.VK_DOWN&&BoardIndextoRow(c)!=8){
						idx+=9;
						elements[BoardIndextoArrayIndex(idx)].requestFocus();
					}
					else{
						char ch = e.getKeyChar();
						if(ch>='1'&&ch<='9'){
							JTF.setText(""+ch);
						}
					}
				}
			});
			elements[x] = JTF;
		}

		JPanel Buttons = new JPanel(new GridLayout(0,3));
		
		JButton CALC = new JButton("Calculate");
		CALC.setFont(FONT1);
		CALC.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				SolveSudoku();
				elements[0].requestFocus();
			}
		});
		
		JButton RETURN = new JButton("Return");
		RETURN.setFont(FONT1);
		RETURN.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				for(JTextField J : elements){
					J.setFocusable(true);
					if(J.getForeground()==Color.GRAY){
						J.setText("");
						J.setFont(FONT1);
						J.setForeground(Color.BLACK);
					}
				}
				elements[0].requestFocus();
			}
		});
		
		JButton CLEAR = new JButton("Clear");
		CLEAR.setFont(FONT1);
		CLEAR.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				for(JTextField J : elements){
					J.setFocusable(true);
					J.setText("");
					J.setFont(FONT1);
					J.setForeground(Color.BLACK);
				}
				elements[0].requestFocus();
			}
		});
		
		Buttons.add(CLEAR);
		Buttons.add(CALC);
		Buttons.add(RETURN);
		
		
		JPanel[] boxes = new JPanel[9];
		for(int x = 0; x < 9; x++){
			JPanel JP = new JPanel(new GridLayout(3,3));
			JP.setBorder(BorderFactory.createLineBorder(Color.black,2));
			boxes[x] = JP;
		}
		int m = 0;
		for(JPanel P : boxes){
			for(int n = 0; n < 9; n++){
				P.add(elements[9*m+n]);
			}
			m++;
			board.add(P);
		}
		
		// elements[BoardIndextoArrayIndex(idx)].setBorder(BorderFactory.createLineBorder(Color.darkGray,2));
	
		GUI.add(Buttons,BorderLayout.PAGE_END);
		GUI.add(board,BorderLayout.CENTER);
		window.add(GUI);
		window.setUndecorated(false);
		window.setVisible(true);
	}
	
	public static void main(String[] args){
		SudokuGUI SG = new SudokuGUI();
	}
	
	public void SolveSudoku(){
		int[][] GRID = new int[9][9];
		for(int x=0; x<81; x++){
			elements[x].setFocusable(false);
			try{
				GRID[BoardIndextoRow(x)][BoardIndextoCol(x)] = Integer.parseInt(elements[x].getText());
			}
			catch(Exception e){
				elements[x].setFont(FONT2);
				elements[x].setForeground(Color.GRAY);
				GRID[BoardIndextoRow(x)][BoardIndextoCol(x)] = 0; 
			}
		}
		Sudoku S = new Sudoku(GRID);
		if(S.isValid()){
			S.Solve();
			updateBoard(S);
		}
		else{
			Invalid();
		}
	}
	
	public void updateBoard(Sudoku S){
		for(int r=0; r<9; r++){
			for(int c=0; c<9; c++){
				if(S.grid[r][c]==0){
					Invalid();
					return;
				}
				int B_idx = RCtoBoardIndex(r,c);
				elements[B_idx].setText(S.grid[r][c]+"");
			}
		}
	}
	
	public void Invalid(){
		JFrame InvalidFrame = new JFrame("INVALID");
		JPanel InvalidPanel = new JPanel(new GridLayout(0,1));
		JTextField InvalidText = new JTextField(10);
		InvalidText.setEditable(false);
		InvalidText.setText("Invalid Sudoku");
		InvalidText.setFont(FONT1);
		InvalidText.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton InvalidButton = new JButton("OK");
		InvalidButton.setFont(FONT3);
		InvalidButton.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			InvalidFrame.setVisible(false);
			for(JTextField J : elements){
				J.setFont(FONT1);
				J.setForeground(Color.BLACK);
				J.setFocusable(true);
			}
		}
		});
		InvalidButton.setBounds(50,50,50,50);
		InvalidPanel.add(InvalidText);
		InvalidPanel.add(InvalidButton);
		InvalidFrame.add(InvalidPanel);
		InvalidFrame.setUndecorated(true);
		InvalidFrame.setLocationRelativeTo(window);
		InvalidFrame.setAlwaysOnTop(true);
		InvalidFrame.pack();
		InvalidFrame.setVisible(true);
	}
}


	
	
	
	
	

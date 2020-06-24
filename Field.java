import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;

public class Field extends JPanel implements KeyListener, MouseListener
{
	private boolean sizePicked = false;
	private int size;
	private int flagCount;
	private int flagInit = 1;
	private int[][] board;
	private boolean[][] showing;
	private boolean[][] flagged;
	private boolean flagSelected;
	private boolean gameOver = false;
	private boolean gameWon = false;
	private Color[] tileColors = new Color[] {Color.white, Color.blue, Color.green, Color.red, Color.magenta, Color.orange, Color.black, Color.black,Color.black};
	public Field()
	{
		addKeyListener(this);
		setFocusable(true);
		addMouseListener(this);
	}
	public void paint(Graphics g)
	{
		if(flagCount == 0)
		{
			int count = 0;
			for(int i = 0; i < size; i++)
			{
				for(int j = 0; j < size; j++)
				{
					if(flagged[i][j] && board[i][j] == -1)
						count++;
				}
			}
			if(count == flagInit)
			{
				gameWon = true;
				System.out.println("this");
			}
		}

		if(gameOver)
		{
			System.out.println("Boom");
		}
		else if(gameWon)
		{
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, 1000, 1000);
			g.setColor(Color.black);
			g.setFont(new Font("Times Roman", 0, 24));
			g.drawString("You Won!", 175, 250);
		}
		else if(sizePicked)
		{
			g.setColor(Color.white);
			g.fillRect(0, 0, 1000, 1000);
			g.setColor(new Color(128,128,128));
			g.fillRect(0,0,500,500);
			
			for(int i = 0; i < size; i++)
			{
				for(int j = 0; j < size; j++)
				{
					if(showing[i][j])
					{
						if(board[i][j] == 0)
						{
							g.setColor(Color.LIGHT_GRAY);
							g.fillRect(i*(500/size), j*(500/size), (500/size), (500/size));
						}
						else
						{
							g.setColor(Color.LIGHT_GRAY);
							g.fillRect(i*(500/size), j*(500/size), (500/size), (500/size));
							g.setColor(tileColors[board[i][j]]);
							g.setFont(new Font("Times Roman", 0, (500/size) - 5));
							g.drawString("" + board[i][j],(int)((i+ 0.25)*(500/size)), (int)((j + 0.77)*(500/size)));
						}
						
					}
				}
			}
			
			
			g.setColor(Color.red);
			for(int i = 0; i < size; i++)
			{
				for(int j = 0; j < size; j++)
				{
					if(flagged[i][j])
					{
						g.drawPolygon(new int[]{(int)((i+0.25)*(500/size)), (int)((i+0.75)*(500/size)), (int)((i+0.75)*(500/size))}, 
									  new int[]{(int)((j+0.5)*(500/size)), (int)((j+0.25)*(500/size)), (int)((j+0.75)*(500/size))},
									  3);
					}
				}
			}
			
			
			
			g.setColor(Color.DARK_GRAY);
			for(int i = 0; i <= size; i++)
			{
				g.drawLine(0,i*(500/size), 500,i*(500/size));
			}
			for(int i = 0; i <= size; i++)
			{
				g.drawLine(i*(500/size),0,i*(500/size), 500);
			}
			g.setColor(Color.red);
			g.setFont(new Font("Times Roman", 0, 24));
			g.drawString(""+flagCount, 450, 510); 
		}
		else
		{
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0,0,500,500);
			g.setColor(Color.black);
			g.setFont(new Font("Times Roman", 0, 30));
			g.drawString("Mine Sweeper", 225,250);
			g.drawString("Press 1 for 10x10 10 mines",100,300);
			g.drawString("Press 2 for 16x16 40 mines",100,350);
			g.drawString("Press 3 for 20x20 80 mines",100,400);
		}
	}
	
	
	public void generateBoard(int Size, int flags)
	{
		board = new int[Size][Size];
		for(int i = 0; i < Size; i++)
		{
			for(int j = 0; j < Size; j++)
			{
				board[i][j] = 0;
			}
		}
		
		//Puts all of the flags in place
		int dummy = flags;
		int x,y;
		while(dummy != 0)
		{
			x = (int)(Math.random()*Size);
			y = (int)(Math.random()*Size);
			if(board[x][y] != -1)
			{
				board[x][y] = -1;
				dummy--;
			}
		}
		
		//Gives all of the tiles their corresponding numbers
		int count;
		for(int i = 0; i < Size; i++)
		{
			for(int j = 0; j < Size; j++)
			{
				count = 0;
				if(board[i][j] != -1)
				{
					for(int X = -1; X < 2; X++)
					{
						for(int Y = -1; Y < 2; Y++)
						{
							try
							{
								if(board[i+X][j+Y] == -1)
									count++;
							}
							catch(Exception e) {  }
						}
					}
					board[i][j] = count;
				}
			}
		}
		
		flagged = new boolean[Size][Size];
		showing = new boolean[Size][Size];
		for(int i = 0; i < Size; i++)
		{
			for(int j = 0; j < Size; j++)
			{
				flagged[i][j] = false;
				showing[i][j] = false;
			}
		}

		printField();
	}
	
	

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("here");
		if(!sizePicked)
		{
			if(e.getKeyCode() == 49)
			{
				size = 10;
				flagCount = flagInit = 10;
				sizePicked = true;
				update();
				generateBoard(size,flagCount);
			}
			else if(e.getKeyCode() == 50)
			{
				System.out.println("here2");
				size = 16;
				flagCount = flagInit = 40;
				sizePicked = true;
				update();
				generateBoard(size,flagCount);
			}
			else if(e.getKeyCode() == 51)
			{
				size = 20;
				flagCount = flagInit = 80;
				sizePicked = true;
				update();
				generateBoard(size,flagCount);
			}
		}
	}
	public void mouseClicked(MouseEvent e) {
		if(sizePicked)
		{
			int x = e.getX() / (500/size);
			int y = e.getY() / (500/size);
			/*
			if(y == size && x == size - 1)
				flagSelected = !flagSelected;
				*/
			if(SwingUtilities.isRightMouseButton(e))
			{
				try
				{
					if(flagged[x][y])
					{
						flagged[x][y] = false;
						flagCount+=1;
					}
					else
					{
						if(flagCount > 0)
						{
							flagged[x][y] = true;
							flagCount-=1;
						}
					}
				}
				catch(Exception ex) {  }
			}
			else
			{
				try {
					clickBlock(x,y, true);
				} catch (Exception e1) {
					
				}
			}
			update();
			/*
			else
			{
				if(flagSelected)
				{
					try
					{
						if(flagged[x][y])
						{
							flagged[x][y] = false;
							flagCount+=1;
						}
						else
						{
							if(flagCount > 0)
							{
								flagged[x][y] = true;
								flagCount-=1;
							}
						}
					}
					catch(Exception ex) {  }
				}
				else
				{
					try {
						clickBlock(x,y, true);
					} catch (Exception e1) {
						
					}
				}
				update();
			}
			*/
		}
	}
	
	public void update()
	{
		this.setSize(511,510);
		this.setSize(510,510);
		
	}
	public void clickBlock(int x, int y, boolean initial) throws Exception
	{
		if(!showing[x][y])
		{
			if(board[x][y] == -1 && initial)
				gameOver = true;
			else if(board[x][y] > 0)
				showing[x][y] = true;
			else if(board[x][y] == 0)
			{
				showing[x][y] = true;
				for(int i = -1; i < 2; i++)
				{
					for(int j = -1; j < 2; j++)
					{
						try 
						{
							clickBlock(x+i,y+j,false);
						}
						catch(Exception e) {  }
					}
				}
			}
		}
	}
	
	public void printField()
	{
		for(int i = 0; i < size; i++)
		{
			for(int j = 0; j < size; j++)
			{
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	}
	

	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {
		
	}
	
	public void mouseReleased(MouseEvent arg0) {}
}

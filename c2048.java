import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.util.*;
class c2048 extends Frame implements ActionListener
{
Dialog d;
int a[][]=new int[4][4] , temp[][]=new int[4][4];
int x,y,val;
Random rand=new Random();
Button undo=new Button("Undo") , replay=new Button("Replay") , quit=new Button("Quit");
Deque<arr> U = new LinkedList<arr>();
Long score=0L;
boolean norandom=false;

public void refresh_array()
{
	for(int i=0;i<4;i++)
		for(int j=0;j<4;j++)
			a[i][j]=0;
}

c2048(String name)
{
	super(name);
	setSize(650,650);
	setVisible(true);
	setLayout(new FlowLayout(FlowLayout.LEFT));
	addKeyListener(new KeyAdapter() 
	{
		public void keyPressed(KeyEvent ke)
		{
			int k=ke.getKeyCode(),D=0;
			switch(k)
			{
			case KeyEvent.VK_UP:
				D=1;	// up
				break;
			case KeyEvent.VK_DOWN:
				D=2;	//  down 
				break;
			case KeyEvent.VK_LEFT:
				D=3;	// left
				break;
			case KeyEvent.VK_RIGHT :
				D=4;	// right
				break;
			}	
			calculate(D);
			fill();
			norandom=false;
			repaint();
		}		
	});
	addWindowListener(new WindowAdapter()
	{
		public void windowClosing(WindowEvent we)
		{
			setVisible(false);
			dispose();
		}		
	});
	
	undo.setPreferredSize(new Dimension(75,75));
	add(undo);
	
	undo.addActionListener(this);
	refresh_array();	
	fill();
	fill();
	requestFocus();
	
}
Color getcolour(int s)
{
	int r=240,g=240,b=0;
	if(s==0)return Color.gray;
	if(s==2048)return Color.red;
	if(s>2048)
			return new Color(125,0,0);
	for( int v = s ; v > 1; v >>= 1, g -= (s>256)?20:10 );
	return new Color(r,g,b);
}
void calculate(int dir)
{
int i,j,k,ai,aj;
for( i=0;i<4;i++)
    for( j=0;j<4;j++)
        temp[i][j]=0;
switch(dir)
{
case 1:	// up
	for(j=0;j<4;j++)
	{
		ai=0;
		for( i=0;i<4;i++)
			if(a[i][j]!=0)
				temp[ai++][j]=a[i][j];
        for(i=0;i<ai-1;i++)
			if(temp[i][j]!=0&&temp[i][j]==temp[i+1][j])
			{
                temp[i][j]*=2;
				score+=temp[i][j];
                for(k=i+1;k<ai-1;k++)
                    temp[k][j]=temp[k+1][j];
                temp[k][j]=0;
            }
     	for( i=0;i<ai;i++)
			a[i][j]=temp[i][j];
		for(;i<4;i++)
			a[i][j]=0;
	}
	break;
case 2:	// down
	for(j=0;j<4;j++)
	{
		ai=3;
		for( i=3;i>=0;i--)
			if(a[i][j]!=0)
				temp[ai--][j]=a[i][j];
		for(i=3;i>0;i--)
			if(temp[i][j]!=0&&temp[i][j]==temp[i-1][j])
            {
                temp[i][j]*=2;
				score+=temp[i][j];
                for(k=i-1;k>ai+1;k--)
                    temp[k][j]=temp[k-1][j];
                temp[k][j]=0 ;
            }
		for( i=3;i>ai;i--)
			a[i][j]=temp[i][j];
		for(;i>=0;i--)
			a[i][j]=0;
	}
	break;
case 3:	// left
	for(i=0;i<4;i++)
	{
		aj=0;
		for( j=0;j<4;j++)
			if(a[i][j]!=0)
				temp[i][aj++]=a[i][j];
		for(j=0;j<aj-1;j++)
			if(temp[i][j]!=0&&temp[i][j]==temp[i][j+1])
			{
			    temp[i][j]*=2;
				score+=temp[i][j];
                for(k=j+1;k<aj-1;k++)
                    temp[i][k]=temp[i][k+1] ;
                temp[i][k]=0;
            }
		for( j=0;j<aj;j++)
			a[i][j]=temp[i][j];
		for(;j<4;j++)
			a[i][j]=0;
	}
	break;
case 4:	// right
	for(i=0;i<4;i++)
	{
		aj=3;
		for( j=3;j>=0;j--)
			if(a[i][j]!=0)
				temp[i][aj--]=a[i][j];
        for(j=3;j>0;j--)
			if(temp[i][j]!=0&&temp[i][j]==temp[i][j-1])
			{
                temp[i][j]*=2;
				score+=temp[i][j];
                for(k=j-1;k>aj+1;k--)
                    temp[i][k]=temp[i][k-1] ;
                temp[i][k]=0;
            }
		for( j=3;j>aj;j--)
			a[i][j]=temp[i][j];
		for(;j>=0;j--)
			a[i][j]=0;
	}
	break;
}
}
public void actionPerformed(ActionEvent ae)
{
	Object o=ae.getSource();
	if(o==undo)
	{
		if(U.isEmpty())
			return;
		norandom=true;
		a=U.peekFirst().copy();
		score = U.peekFirst().get_prev_score();
		U.pollFirst();
		if(U.isEmpty())
			undo.setVisible(false);
		requestFocus();
	}
	else if(o==replay)
	{
		U=new LinkedList<arr>();
		score=0L;
		undo.setVisible(false);
		refresh_array();
		fill();
		fill();
		requestFocus();
		d.setVisible(false);
		d.dispose();
		norandom=false;
	}
	else if(o==quit)
		System.exit(0);
	repaint();
		
}
boolean no_moves_possible()
{
	int count=0;
	for(int i=0;i<4;i++)
		for(int j=0;j<4;j++)
			if(a[i][j]==0)
				return false;
	return true;
}
void fill()
{
	if(norandom==false)
	{	do{
			
			x=rand.nextInt(4);
			y=rand.nextInt(4);
			
		}while(a[x][y]!=0);
		val=rand.nextInt(10);
		
		a[x][y]=(val==9)?4:2;
		if(U.size()<=2)
			a[x][y]=2;
		System.out.println(""+(x+1)+" "+(y+1));
		U.addFirst(new arr(a,score));	//store last 20 moves
		if(U.size()>20)
			U.pollLast();
		undo.setVisible(true);
	}
}
public void paint(Graphics g)
{
	if(no_moves_possible()==true)	//game over
	{
		norandom=true;
		d=new Dialog(this,"Game Over",true);
		d.setSize(250,250);
		d.setLayout(new FlowLayout(FlowLayout.CENTER));
		Label l=new Label("Score = "+score);
		l.setFont(new Font("Tahoma",Font.BOLD,30));
		
		l.setPreferredSize(new Dimension(200,100));
		replay.setPreferredSize(new Dimension(100,100));
		quit.setPreferredSize(new Dimension(100,100));
		
		d.add(l);
		d.add(replay);
		d.add(quit);
		d.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent we)
			{
					d.setVisible(false);
					d.dispose();
			}
		});
		replay.addActionListener(this);
		quit.addActionListener(this);
		d.setVisible(true);
	}
	
	g.setFont(new Font("Tahoma",Font.BOLD,30));	
	for(int i=0;i<4;i++)
	{
		for(int j=0;j<4;j++)
		{
			g.setColor(getcolour(a[j][i]));
			g.fillRect((i*100)+100,(j*100)+100,100,100);
			g.setColor(Color.black);
			int xx=i*100+140;
			if(a[j][i]>64)xx-=20;
			if(a[j][i]!=0)	g.drawString(""+a[j][i],xx,j*100+160);
		
		}
	}
	g.drawString("Score : "+score,200,550);
}
	public static void main(String args[])
	{
		new c2048("2048");
	}
}
class arr
{
    Long prev_score;
	int ar[][]=new int[4][4];
    arr(int[][] x,Long y)
    {
        for(int i=0;i<4;i++)
            for(int j=0;j<4;j++)
                ar[i][j]=x[i][j];
		prev_score=y;
    }
	int[][] copy()
	{
		return ar;
	}
	Long get_prev_score()
	{
		return prev_score;
	}
}
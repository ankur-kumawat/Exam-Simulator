package exam;

import java.awt.event.*;
import java.awt.*;
import java.io.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.sql.*;

public class ExamConductor implements ActionListener {
	int id[];
	String ans[];
	JFrame f,res;
	JButton strt,review;
	int i=-1;
	JPanel opt;
	JLabel q;
	String str;
	Connection con;
	Statement stmt;
	ResultSet rs;
	JButton fns;
	JRadioButton a,b,c,d;
	JCheckBox aa,bb,cc,dd;
	ButtonGroup bg;
	boolean mode=false;
	boolean flag=false;
	public ExamConductor()
	{
		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			con=DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=data/examination.mdb");
			stmt=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			id=new int[10];
			ans=new String[]{"","","","","","","","","",""};
			f=new JFrame("Exam Simulator");
			strt=new JButton("Start");
			final JPanel gls=(JPanel)f.getGlassPane();
			gls.setLayout(new BorderLayout());
			gls.add(strt);
			gls.setVisible(true);
			f.add(gls);
		
			fns=new JButton("Finish");
			
							
			f.setBounds(new Rectangle(100,100,500,400));
			f.setVisible(true);
			f.addWindowListener(new WindowAdapter(){

				public void windowClosed(WindowEvent e) 
				{
					try
					{
						if(res!=null)
						review.setEnabled(false);
						Window w=e.getWindow();
						w.dispose();
						flag=true;
						stmt.close();
						con.close();
						f=null;
						if(res==null)
							System.exit(0);
					}
					catch(Exception x)
					{
						System.out.println("Problem Closing");
					}
				}		
			});
			strt.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					int a;
					//gls.setVisible(false);
					//f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					x:for(int i=0;i<10;i++)
					{
						a=(int)(Math.random()*100);
						if(a>20||a==0)
						{
							i--;
							continue;
						}
						for(int j=0;j<=i;j++)
						{
							if(id[j]==a||a==0)
							{
								i--;
								continue x;
							}
						}
						id[i]=a;
						System.out.println("ID"+id[i]);
					}
					//f.remove(gls);
					gls.setVisible(false);
					exam();
					f.repaint();
				}
			});
		}
		catch(Exception e)
		{
			System.out.println("Exception");
			e.printStackTrace();
		}
	}
	private void exam()
	{
		q=new JLabel("Hello");
		f.add(q,BorderLayout.NORTH);
		opt=new JPanel(new GridLayout(4,1));
		final JPanel bp=new JPanel();
		final JButton nxt=new JButton("Next");
		final JButton pre=new JButton("Previous");
		
		final JProgressBar jpb=new JProgressBar(0,10);
		final JPanel rp= new JPanel();
		review=new JButton("Review");
		final JButton restrt=new JButton("Try Again");
		final JLabel rt =new JLabel();
		rp.add(review);
		rp.add(restrt);
		bp.add(nxt);
		next();
		bp.add(pre);
		bp.add(fns);
		pre.setEnabled(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		nxt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae)
			{
				pre.setEnabled(true);
				if(i>=8)
					nxt.setEnabled(false);
				next();
			}
		});
		fns.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent at)
			{
				if(fns.getText().equals("Close"))
				{
					if(res!=null)
						review.setEnabled(false);
					f.dispose();
					flag=true;
					f=null;
					if(res==null)
					{
						System.exit(0);
					}
				}
				else
				{
					res=new JFrame("Result");
					System.out.println("Hat");
					int v=0;
					try
					{
						for(int k=0;k<id.length;k++)
						{
							System.out.println("hot"+ans[k]);
							ResultSet r=stmt.executeQuery("select * from exam1 where id="+id[k]);
							r.next();
							String chk=r.getString("answer");
							if(r.getString("type").equals("s"))
							{
								if(chk.equals(ans[k]))
									v+=1;
								System.out.println("in"+v);
							}
							else
							{
								char a[]=ans[k].toCharArray();
								if(chk.length()==ans[k].length())
								{
									for(int i=0;i<a.length;i++)
									{
										if(chk.indexOf(""+a[i])==-1)
											break;
										if(i==chk.length()-1)
											v+=1;
									}
								}
							}
						}
						System.out.println("Outside"+v);
						rt.setText("Result "+(v*10)+"%");
						jpb.setValue(v);
					//	f.removeAll();
					//	f.repaint();
						res.add(rt,BorderLayout.NORTH);
						res.add(jpb);
						res.add(rp,BorderLayout.SOUTH);
						res.repaint();
						f.setFocusable(false);
						res.setBounds(200,200,300,250);
						res.setVisible(true);
						res.addWindowListener(new WindowAdapter(){
							public void windowClosed(WindowEvent we)
							{
								Window w=we.getWindow();
								w.setVisible(false);
								w.dispose();
								res=null;
								if(f==null)
									System.exit(0);
							}
						});
						
						restrt.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent ae)
							{
								if(f!=null)
								f.setVisible(false);
								f=null;
								res.setVisible(false);
								res=null;
								new ExamConductor();
							}
						});
						review.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent ae)
							{
								opt.enableInputMethods(flag);
								if(mode)
								{
									aa.setEnabled(false);
									bb.setEnabled(false);
									cc.setEnabled(false);
									dd.setEnabled(false);
									//char answer[]=ans[i].toCharArray();
									char ao[]=str.toCharArray();
									if(aa.isSelected())
									{
										for(int k=0;k<ao.length;k++)
										if(aa.getName().indexOf(""+ao[k])!=-1)
										{
											aa.setBackground(Color.green);
											break;
										}
										else
											aa.setBackground(Color.red);
									}
									else
										for(int k=0;k<ao.length;k++)
										if(aa.getName().indexOf(""+ao[k])!=-1)
											aa.setBackground(Color.blue);
									if(bb.isSelected())
									{
										for(int k=0;k<ao.length;k++)
										if(bb.getName().indexOf(""+ao[k])!=-1)
										{
											bb.setBackground(Color.green);
											break;
										}
										else
											bb.setBackground(Color.red);
									}
									else
										for(int k=0;k<ao.length;k++)
										if(bb.getName().indexOf(""+ao[k])!=-1)
											bb.setBackground(Color.blue);
									if(cc.isSelected())
									{
										for(int k=0;k<ao.length;k++)
										if(cc.getName().indexOf(""+ao[k])!=-1)
										{
											cc.setBackground(Color.green);
											break;
										}
										else
											cc.setBackground(Color.red);
									}
									else
										for(int k=0;k<ao.length;k++)
										if(cc.getName().indexOf(""+ao[k])!=-1)
											cc.setBackground(Color.blue);
									if(dd.isSelected())
									{
										for(int k=0;k<ao.length;k++)
										if(dd.getName().indexOf(""+ao[k])!=-1)
										{
											dd.setBackground(Color.green);
											break;
										}
										else
											dd.setBackground(Color.red);
									}
									else
										for(int k=0;k<ao.length;k++)
										if(dd.getName().indexOf(""+ao[k])!=-1)
											dd.setBackground(Color.blue);
								}
								else
								{
									a.setEnabled(false);
									b.setEnabled(false);
									c.setEnabled(false);
									d.setEnabled(false);
									f.setFocusable(true);
									JRadioButton z=null;
									if(a.isSelected())
										z=a;
									if(b.isSelected())
										z=b;
									if(c.isSelected())
										z=c;
									if(d.isSelected())
										z=d;
									//System.out.println(z.getName());
									if(z!=null && z.getName().equals(str))
									{
										z.setBackground(Color.green);
									}
									else
									{
										System.out.println("Hhhhhhhhhhhhhhhhhhhhhhh  "+str);
										if(z!=null)
											z.setBackground(Color.red);
										if(a.getName().equals(str))
											a.setBackground(Color.blue);
										if(b.getName().equals(str))
											b.setBackground(Color.blue);
										if(c.getName().equals(str))
											c.setBackground(Color.blue);
										if(d.getName().equals(str))
											d.setBackground(Color.blue);
									}
								}
								res.setFocusable(false);
								fns.setText("Close");
							}
						});
					}
					catch(Exception se)
					{
						System.out.println("exception");
						se.printStackTrace();
					}
				}	
			}
		});
/*		new Thread(new Runnable(){
			public void run()
			{
				while(true)
				{
					if(flag)
						break;
					if(i<=0)
						pre.setEnabled(false);
					else
						pre.setEnabled(true);
					if(i>=9)
					{
						nxt.setEnabled(false);
					}
					else
					{
						nxt.setEnabled(true);
					}
					
				}
			}
		}).start();
	*/
		pre.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae)
			{
				nxt.setEnabled(true);
				if(i==1)
					pre.setEnabled(false);
				try
				{
					i--;
					String s="select * from exam1 where ID="+id[i];
					rs=stmt.executeQuery(s);
					rs.next();
					q.setText("Q."+(i+1)+" "+rs.getString("Question"));
					String mod=rs.getString("type");
					if(mod.equals("m"))
					{
						mode=true;
						opt.removeAll();
						aa=new JCheckBox(rs.getString("a"));
						bb=new JCheckBox(rs.getString("b"));
						cc=new JCheckBox(rs.getString("c"));
						dd=new JCheckBox(rs.getString("d"));
						aa.setName("a");
						bb.setName("b");
						cc.setName("c");
						dd.setName("d");
						opt.add(aa);
						opt.add(bb);
						opt.add(cc);
						opt.add(dd);
						str=rs.getString("answer");
						if(ans[i]!=null)
						{
							char arr[]=ans[i].toCharArray();
							for(int k=0;k<arr.length;k+=2)
							{
								if((arr[k]+"").equals("a"))
									aa.setSelected(true);
								if((arr[k]+"").equals("b"))
									bb.setSelected(true);
								if((arr[k]+"").equals("c"))
									cc.setSelected(true);
								if((arr[k]+"").equals("d"))
									dd.setSelected(true);
							}
						}
						if(fns.getText().equals("Close"))
						{
							System.out.println("hhhhhhhhhhhhhhh       "+ans[i]);
							aa.setEnabled(false);
							bb.setEnabled(false);
							cc.setEnabled(false);
							dd.setEnabled(false);
							//char answer[]=ans[i].toCharArray();
							char ao[]=str.toCharArray();
							if(aa.isSelected())
							{
								for(int k=0;k<ao.length;k++)
								if(aa.getName().indexOf(""+ao[k])!=-1)
								{
									aa.setBackground(Color.green);
									break;
								}
								else
									aa.setBackground(Color.red);
							}
							else
								for(int k=0;k<ao.length;k++)
								if(aa.getName().indexOf(""+ao[k])!=-1)
									aa.setBackground(Color.blue);
							if(bb.isSelected())
							{
								for(int k=0;k<ao.length;k++)
								if(bb.getName().indexOf(""+ao[k])!=-1)
								{
									bb.setBackground(Color.green);
									break;
								}
								else
									bb.setBackground(Color.red);
							}
							else
								for(int k=0;k<ao.length;k++)
								if(bb.getName().indexOf(""+ao[k])!=-1)
									bb.setBackground(Color.blue);
							if(cc.isSelected())
							{
								for(int k=0;k<ao.length;k++)
								if(cc.getName().indexOf(""+ao[k])!=-1)
								{
									cc.setBackground(Color.green);
									break;
								}
								else
									cc.setBackground(Color.red);
							}
							else
								for(int k=0;k<ao.length;k++)
								if(cc.getName().indexOf(""+ao[k])!=-1)
									cc.setBackground(Color.blue);
							if(dd.isSelected())
							{
								for(int k=0;k<ao.length;k++)
								if(dd.getName().indexOf(""+ao[k])!=-1)
								{
									dd.setBackground(Color.green);
									break;
								}
								else
									dd.setBackground(Color.red);
							}
							else
								for(int k=0;k<ao.length;k++)
								if(dd.getName().indexOf(""+ao[k])!=-1)
									dd.setBackground(Color.blue);							
						}
						aa.addActionListener(new ActionListener(){

							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								if(aa.isSelected())
								{
									if(ans[i].equals(""))
										ans[i]="a";
									else
										ans[i]+=",a";
									System.out.println("HIT");
								}
								else
								{
									int ind;
									if(( ind=ans[i].indexOf(",a"))!=-1)
									{
										StringBuffer tmp=new StringBuffer(ans[i]);
										tmp.replace(ind, ind+2, "");
										ans[i]=tmp.toString();
									}
									else
									{
										ind=ans[i].indexOf("a,");
										if(ind!=-1)
										{
											StringBuffer tmp=new StringBuffer(ans[i]);
											tmp.replace(ind, ind+2, "");
											ans[i]=tmp.toString();
										}
									}
									System.out.println(ans[i]);
								}
							}
							
						});
						bb.addActionListener(new ActionListener(){

							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								if(bb.isSelected())
								{
									if(ans[i].equals(""))
										ans[i]="b";
									else
										ans[i]+=",b";
									System.out.println("HIT");
								}
								else
								{
									int ind;
									if(( ind=ans[i].indexOf(",b"))!=-1)
									{
										StringBuffer tmp=new StringBuffer(ans[i]);
										tmp.replace(ind, ind+2, "");
										ans[i]=tmp.toString();
									}
									else
									{
										ind=ans[i].indexOf("b,");
										if(ind!=-1)
										{
											StringBuffer tmp=new StringBuffer(ans[i]);
											tmp.replace(ind, ind+2, "");
											ans[i]=tmp.toString();
										}
									}
								}
							}
						});
						cc.addActionListener(new ActionListener(){

							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								if(cc.isSelected())
								{
									if(ans[i].equals(""))
										ans[i]="c";
									else
										ans[i]+=",c";
									System.out.println("HIT");
								}
								else
								{
									int ind;
									if(( ind=ans[i].indexOf(",c"))!=-1)
									{
										StringBuffer tmp=new StringBuffer(ans[i]);
										tmp.replace(ind, ind+2, "");
										ans[i]=tmp.toString();
									}
									else
									{
										ind=ans[i].indexOf("c,");
										if(ind!=-1)
										{
											StringBuffer tmp=new StringBuffer(ans[i]);
											tmp.replace(ind, ind+2, "");
											ans[i]=tmp.toString();
										}
									}
								}
							}
						});
						dd.addActionListener(new ActionListener(){

							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								if(dd.isSelected())
								{
									if(ans[i].equals(""))
										ans[i]="d";
									else
										ans[i]+=",d";
									System.out.println("HIT");
								}
								else
								{
									int ind;
									if(( ind=ans[i].indexOf(",d"))!=-1)
									{
										StringBuffer tmp=new StringBuffer(ans[i]);
										tmp.replace(ind, ind+2, "");
										ans[i]=tmp.toString();
									}
									else
									{
										ind=ans[i].indexOf("d,");
										if(ind!=-1)
										{
											StringBuffer tmp=new StringBuffer(ans[i]);
											tmp.replace(ind, ind+2, "");
											ans[i]=tmp.toString();
										}
									}
								}
							}
						});
					}
					else
					{
						mode=false;
						opt.removeAll();
						a=new JRadioButton(rs.getString("a"));
						b=new JRadioButton(rs.getString("b"));
						c=new JRadioButton(rs.getString("c"));
						d=new JRadioButton(rs.getString("d"));
						bg=new ButtonGroup();
						bg.add(a);
						bg.add(b);
						bg.add(c);
						bg.add(d);
						a.setName("a");
						b.setName("b");
						c.setName("c");
						d.setName("d");
						str=rs.getString("answer");
						opt.add(a);
						opt.add(b);
						opt.add(c);
						opt.add(d);
						if("a".equals(ans[i]))
							a.setSelected(true);
						if("b".equals(ans[i]))
							b.setSelected(true);
						if("c".equals(ans[i]))
							c.setSelected(true);
						if("d".equals(ans[i]))
							d.setSelected(true);
					
						if(fns.getText().equals("Close"))
						{
							a.setEnabled(false);
							b.setEnabled(false);
							c.setEnabled(false);
							d.setEnabled(false);
							JRadioButton z=null;
							if(a.isSelected())
								z=a;
							if(b.isSelected())
								z=b;
							if(c.isSelected())
								z=c;
							if(d.isSelected())
								z=d;
							//System.out.println(z.getName());
							String str=rs.getString("answer");
							if(z!=null && z.getName().equals(str))
							{
								z.setBackground(Color.green);
							}
							else
							{
								System.out.println("Hhhhhhhhhhhhhhhhhhhhhhh  "+str);
								if(z!=null)
									z.setBackground(Color.red);
								if(a.getName().equals(str))
									a.setBackground(Color.blue);
								if(b.getName().equals(str))
									b.setBackground(Color.blue);
								if(c.getName().equals(str))
									c.setBackground(Color.blue);
								if(d.getName().equals(str))
									d.setBackground(Color.blue);
							}
						}
						a.addActionListener(new ActionListener(){

							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								ans[i]="a";
								System.out.println("HIT");
							}
							
						});
						b.addActionListener(new ActionListener(){

							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								ans[i]="b";
								System.out.println("HIT");
							}
						});
						c.addActionListener(new ActionListener(){

							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								ans[i]="c";
								System.out.println("HIT");
							}
						});
						d.addActionListener(new ActionListener(){

							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								ans[i]="d";
								System.out.println("HIT");
							}
						});
					}
				//	stmt.close();
				//	con.close();
				}
				catch(Exception x)
				{
					System.out.println("SQLException");
					x.printStackTrace();
				}
			}
		});
		f.add(opt);
		f.add(bp,BorderLayout.SOUTH);
	}
	private void next()
	{
		try
		{
			i++;
			String s="select * from exam1 where ID="+id[i];
			System.out.println(id[i]+s);
			rs=stmt.executeQuery(s);
			System.out.println(id[i]+"hey");
			rs.next();
			q.setText("Q."+(i+1)+" "+rs.getString("Question"));
			String mod=rs.getString("type");
			if(mod.equals("m"))
			{
				mode=true;
				opt.removeAll();
				aa=new JCheckBox(rs.getString("a"));
				bb=new JCheckBox(rs.getString("b"));
				cc=new JCheckBox(rs.getString("c"));
				dd=new JCheckBox(rs.getString("d"));
				aa.setName("a");
				bb.setName("b");
				cc.setName("c");
				dd.setName("d");
				opt.add(aa);
				opt.add(bb);
				opt.add(cc);
				opt.add(dd);
				str=rs.getString("answer");
				if(ans[i]!=null)
				{
					char arr[]=ans[i].toCharArray();
					for(int k=0;k<arr.length;k+=2)
					{
						if((arr[k]+"").equals("a"))
							aa.setSelected(true);
						if((arr[k]+"").equals("b"))
							bb.setSelected(true);
						if((arr[k]+"").equals("c"))
							cc.setSelected(true);
						if((arr[k]+"").equals("d"))
							dd.setSelected(true);
					}
				}
				if(fns.getText().equals("Close"))
				{
					System.out.println("hhhhhhhhhhhhhhh       "+ans[i]);
					aa.setEnabled(false);
					bb.setEnabled(false);
					cc.setEnabled(false);
					dd.setEnabled(false);
					//char answer[]=ans[i].toCharArray();
					char ao[]=str.toCharArray();
					if(aa.isSelected())
					{
						for(int k=0;k<ao.length;k++)
						if(aa.getName().indexOf(""+ao[k])!=-1)
						{
							aa.setBackground(Color.green);
							break;
						}
						else
							aa.setBackground(Color.red);
					}
					else
						for(int k=0;k<ao.length;k++)
						if(aa.getName().indexOf(""+ao[k])!=-1)
							aa.setBackground(Color.blue);
					if(bb.isSelected())
					{
						for(int k=0;k<ao.length;k++)
						if(bb.getName().indexOf(""+ao[k])!=-1)
						{
							bb.setBackground(Color.green);
							break;
						}
						else
							bb.setBackground(Color.red);
					}
					else
						for(int k=0;k<ao.length;k++)
						if(bb.getName().indexOf(""+ao[k])!=-1)
							bb.setBackground(Color.blue);
					if(cc.isSelected())
					{
						for(int k=0;k<ao.length;k++)
						if(cc.getName().indexOf(""+ao[k])!=-1)
						{
							cc.setBackground(Color.green);
							break;
						}
						else
							cc.setBackground(Color.red);
					}
					else
						for(int k=0;k<ao.length;k++)
						if(cc.getName().indexOf(""+ao[k])!=-1)
							cc.setBackground(Color.blue);
					if(dd.isSelected())
					{
						for(int k=0;k<ao.length;k++)
						if(dd.getName().indexOf(""+ao[k])!=-1)
						{
							dd.setBackground(Color.green);
							break;
						}
						else
							dd.setBackground(Color.red);
					}
					else
						for(int k=0;k<ao.length;k++)
						if(dd.getName().indexOf(""+ao[k])!=-1)
							dd.setBackground(Color.blue);
				}
				aa.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						if(aa.isSelected())
						{
							if(ans[i].equals(""))
								ans[i]="a";
							else
								ans[i]+=",a";
							System.out.println("HIT");
						}
						else
						{
							int ind;
							if(( ind=ans[i].indexOf(",a"))!=-1)
							{
								StringBuffer tmp=new StringBuffer(ans[i]);
								tmp.replace(ind, ind+2, "");
								ans[i]=tmp.toString();
							}
							else
							{
								ind=ans[i].indexOf("a,");
								if(ind!=-1)
								{	
									StringBuffer tmp=new StringBuffer(ans[i]);
									tmp.replace(ind, ind+2, "");
									ans[i]=tmp.toString();
								}
							}
							System.out.println(ans[i]);
						}
					}
					
				});
				bb.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						if(bb.isSelected())
						{
							if(ans[i].equals(""))
								ans[i]="b";
							else
								ans[i]+=",b";
							System.out.println("HIT");
						}
						else
						{
							int ind;
							if(( ind=ans[i].indexOf(",b"))!=-1)
							{
								StringBuffer tmp=new StringBuffer(ans[i]);
								tmp.replace(ind, ind+2, "");
								ans[i]=tmp.toString();
							}
							else
							{
								ind=ans[i].indexOf("b,");
								if(ind!=-1)
								{
									StringBuffer tmp=new StringBuffer(ans[i]);
									tmp.replace(ind, ind+2, "");
									ans[i]=tmp.toString();
								}
							}
						}
					}
				});
				cc.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						if(cc.isSelected())
						{
							if(ans[i].equals(""))
								ans[i]="c";
							else
								ans[i]+=",c";
							System.out.println("HIT");
						}
						else
						{
							int ind;
							if(( ind=ans[i].indexOf(",c"))!=-1)
							{
								StringBuffer tmp=new StringBuffer(ans[i]);
								tmp.replace(ind, ind+2, "");
								ans[i]=tmp.toString();
							}
							else
							{
								ind=ans[i].indexOf("c,");
								if(ind!=-1)
								{
									StringBuffer tmp=new StringBuffer(ans[i]);
									tmp.replace(ind, ind+2, "");
									ans[i]=tmp.toString();
								}
							}
						}
					}
				});
				dd.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						if(dd.isSelected())
						{
							if(ans[i].equals(""))
								ans[i]="d";
							else
								ans[i]+=",d";
							System.out.println("HIT");
						}
						else
						{
							int ind;
							if(( ind=ans[i].indexOf(",d"))!=-1)
							{
								StringBuffer tmp=new StringBuffer(ans[i]);
								tmp.replace(ind, ind+2, "");
								ans[i]=tmp.toString();
							}
							else
							{
								ind=ans[i].indexOf("d,");
								if(ind!=-1)
								{
									StringBuffer tmp=new StringBuffer(ans[i]);
									tmp.replace(ind, ind+2, "");
									ans[i]=tmp.toString();
								}
							}
						}
					}
				});
			}
			else
			{
				mode=false;
				opt.removeAll();
				a=new JRadioButton(rs.getString("a"));
				b=new JRadioButton(rs.getString("b"));
				c=new JRadioButton(rs.getString("c"));
				d=new JRadioButton(rs.getString("d"));
				a.setName("a");
				b.setName("b");
				c.setName("c");
				d.setName("d");
				bg=new ButtonGroup();
				bg.add(a);
				bg.add(b);
				bg.add(c);
				bg.add(d);
				str=rs.getString("answer");
				opt.add(a);
				opt.add(b);
				opt.add(c);
				opt.add(d);
				if("a".equals(ans[i]))
					a.setSelected(true);
				if("b".equals(ans[i]))
					b.setSelected(true);
				if("c".equals(ans[i]))
					c.setSelected(true);
				if("d".equals(ans[i]))
					d.setSelected(true);
				if(fns.getText().equals("Close"))
				{
					a.setEnabled(false);
					b.setEnabled(false);
					c.setEnabled(false);
					d.setEnabled(false);
					
					JRadioButton z=null;
					if(a.isSelected())
						z=a;
					if(b.isSelected())
						z=b;
					if(c.isSelected())
						z=c;
					if(d.isSelected())
						z=d;
					if(z!=null && z.getName().equals(rs.getString("answer")))
					{
						z.setBackground(Color.green);
					}
					else
					{
						System.out.println("Hhhhhhhhhhhhhhhhhhhhhhh  "+str);
						if(z!=null)
							z.setBackground(Color.red);
						if(a.getName().equals(str))
							a.setBackground(Color.blue);
						if(b.getName().equals(str))
							b.setBackground(Color.blue);
						if(c.getName().equals(str))
							c.setBackground(Color.blue);
						if(d.getName().equals(str))
							d.setBackground(Color.blue);
					}
				}
				a.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						ans[i]="a";
						System.out.println("HIT");
					}
					
				});
				b.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						ans[i]="b";
						System.out.println("HIT");
					}
				});
				c.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						ans[i]="c";
						System.out.println("HIT");
					}
				});
				d.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						ans[i]="d";
						System.out.println("HIT");
					}
				});
			}
		//	stmt.close();
		//	con.close();
			//i++;
		}
		catch(SQLException x)
		{
			System.out.println("Exception N");
			System.out.println(x.getSQLState());
			x.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}

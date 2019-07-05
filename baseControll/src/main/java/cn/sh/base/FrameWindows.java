package cn.sh.base;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Panel;
import java.awt.Toolkit;

import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import java.awt.Component;
import java.io.File;

public class FrameWindows {
    
    private JFrame jFrame = new JFrame("翻译服务");
    private Container c = jFrame.getContentPane();
    private JLabel a1 = new JLabel("11");
    private JTextField username = new JTextField();
    private JLabel a2 = new JLabel("22");
    private JPasswordField password = new JPasswordField();
    private JButton okbtn = new JButton("开启服务");
    private JButton cancelbtn = new JButton("关闭服务");
    private String pid = "init";
    private JLabel titlelab = new JLabel("请点击开启服务");
    private JLabel noticelab = new JLabel("注意:");
    private JLabel noticeMessagelab = new JLabel("运行成功会打开浏览器");
    private final Panel buttenpanel = new Panel();
    private final JPanel imagpanel = new JPanel();
    private final JLabel imageLabel = new JLabel("");
    private final JLabel imageTitleLabel = new JLabel("﻿ε≡٩(๑>₃<)۶ 一心向学 哈哈");
    private final JPanel imageTitlepanel = new JPanel();
    private final JPanel titleTextpanel = new JPanel();
    private final JPanel titleFilepanel = new JPanel();
    private final JButton fileButton = new JButton("选择文件");
    
	public FrameWindows() {
    	
        //设置窗体的位置及大小
        jFrame.setBounds(0, 0, 439, 399);
        jFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(FrameWindows.class.getResource("/wechat.png")));
        //屏幕中间
        jFrame.setLocationRelativeTo(null);
        //设置一层相当于桌布的东西
        c.setLayout(new BorderLayout());//布局管理器
        //设置按下右上角X号后关闭
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //初始化--往窗体里放其他控件
        init();
        //设置窗体可见
        jFrame.setVisible(true);
        listerner();
    }
    public void init() {
        /*标题部分--North*/
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER,10,20));
        titlelab.setFont(new Font("华文行楷", 1, 17));
        titlePanel.add(titlelab);
        c.add(titlePanel, "North");
        
        /*输入部分--Center*/
       /*JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(null);
        a1.setBounds(50, 20, 50, 20);
        a2.setBounds(50, 60, 50, 20);
        fieldPanel.add(a1);
        fieldPanel.add(a2);
        username.setBounds(110, 20, 120, 20);
        password.setBounds(110, 60, 120, 20);
        fieldPanel.add(username);
        fieldPanel.add(password);
        c.add(fieldPanel, "Center");*/
        
        /*按钮部分--Center*/
        JPanel centrePanel = new JPanel();
        c.add(centrePanel, "Center");
        centrePanel.setLayout(new BorderLayout(0, 0));
        
        
        /*Dimension frameSize = new Dimension(100, 100);
        ImageIcon imageIcon = new ImageIcon(this.getClass().getResource(
                "/wechat.png"));
        ImagePanel imagPanel = new ImagePanel(frameSize, imageIcon.getImage());
        imagPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
        FlowLayout flowLayout_1 = (FlowLayout) imagPanel.getLayout();
        centrePanel.add(imagPanel, BorderLayout.CENTER);*/
        
        imageTitlepanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        centrePanel.add(imageTitlepanel, BorderLayout.NORTH);
        imageTitlepanel.setLayout(new BorderLayout(0, 0));
        titleTextpanel.setBorder(new EmptyBorder(0, 0, 5, 0));
        
        imageTitlepanel.add(titleTextpanel, BorderLayout.NORTH);
        titleTextpanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        titleTextpanel.add(imageTitleLabel);
        imageTitlepanel.add(titleFilepanel);
        titleFilepanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        titleFilepanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        final JFileChooser fileDialog = new JFileChooser();
        JpgFileFilter jpgFileFilter = new JpgFileFilter();
        fileDialog.setFileFilter(jpgFileFilter);
        fileDialog.setDialogTitle("请选择文件...");
        fileDialog.setApproveButtonText("选择");
        fileDialog.setApproveButtonToolTipText("选择一个文件");
        fileDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);//仅显示文件
        fileDialog.setMultiSelectionEnabled(false);//设置单文件
        fileButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		int returnVal = fileDialog.showOpenDialog(jFrame);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileDialog.getSelectedFile();
                    System.out.println(file.getName());
                } else {
                	System.out.println("用户取消打开文件");
                }
        	}
        });
        
        titleFilepanel.add(fileButton);
        
        centrePanel.add(imagpanel, BorderLayout.CENTER);
        imagpanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setIcon(new ImageIcon(FrameWindows.class.getResource("/timg.jpg")));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagpanel.add(imageLabel);
        FlowLayout flowLayout = (FlowLayout) buttenpanel.getLayout();
        flowLayout.setVgap(15);
        
        centrePanel.add(buttenpanel, BorderLayout.SOUTH);
        cancelbtn.setVisible(false);
        buttenpanel.add(cancelbtn);
        
        
        /*按钮, 注意部分--South*/
        JPanel noticePanel = new JPanel();
        noticePanel.setBorder(new EmptyBorder(0, 17, 0, 0));
        noticelab.setLabelFor(noticePanel);
        FlowLayout fl_noticePanel = new FlowLayout(FlowLayout.LEFT,5,10);
        noticePanel.setLayout(fl_noticePanel);
        noticeMessagelab.setFont(new Font("华文行楷", 1, 13));
        noticeMessagelab.setForeground(Color.red);
        noticelab.setFont(new Font("华文中宋", Font.PLAIN, 13));
        noticePanel.add(noticelab);
        noticePanel.add(noticeMessagelab);
        c.add(noticePanel, "South");
    }
    //测试
    public static void main(String[] args) {
        new FrameWindows();
    }
    
    public void listerner() {
        buttenpanel.add(okbtn);
        //取消按下去清空
        cancelbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(pid.equals("init")){
            		System.out.println("还未启动服务");
            	}else if(!pid.equals("end") && !pid.equals("start") && !pid.equals("")){
            		Chuangkou.stopService(pid);
            	}else{
            		
            	}
            	
            }
        });
        //确认按下去获取
        okbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	titlelab.setText("启动中。。。。");
            	pid = Chuangkou.startService(new String []{});
            	if(!pid.equals("start")){//启动成功
            		titlelab.setText("启动完成");
            		okbtn.setVisible(false);
            		cancelbtn.setVisible(true);
            	}else{//启动失败
            		titlelab.setText("启动失败");
            	}
            	
            }
        });
    }
	
}

class ImagePanel extends JPanel {
    Dimension d;
    Image image;

    public ImagePanel(Dimension d, Image image) {
        super();
        this.d = d;
        this.image = image;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, d.width, d.height, this);
        //MainFrame.instance().repaint();
    }
}

class JpgFileFilter extends FileFilter {  
    public String getDescription() {  
        return "*.jpg";  
    }  
  
    public boolean accept(File file) {  
        String name = file.getName();  
        return name.toLowerCase().endsWith(".jpg");  
    }  
}  
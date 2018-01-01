

import Client.Register;
import Client.UDPBunissness;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
/**
 * Created by admin on 2017/12/19.
 */
public class RegisterScreen extends JFrame
{
    private JTextField usernameregisterField;
    private JTextField passwordregisterField;
    private JTextField repeatpasswordregisterField;
    Thread registerThread;
    JButton Register;
    public RegisterScreen()
    {
        Container RegisterScreencontainer=getContentPane();
        setLayout(new GridLayout(4,2,10,10));
        RegisterScreencontainer.add(new JLabel("Username",SwingConstants.RIGHT));
        usernameregisterField=new JTextField();
        RegisterScreencontainer.add(usernameregisterField);
        RegisterScreencontainer.add(new JLabel("Password",SwingConstants.RIGHT));
        passwordregisterField=new JTextField();
        RegisterScreencontainer.add(passwordregisterField);
        RegisterScreencontainer.add(new JLabel("RepeatPassword",SwingConstants.RIGHT));
        repeatpasswordregisterField=new JTextField();
        RegisterScreencontainer.add(repeatpasswordregisterField);
        Register=new JButton("Register");
        Register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(passwordregisterField.getText().equals(repeatpasswordregisterField.getText()))
                {
                        registerThread=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try
                                {
                                    UDPBunissness regLink=new UDPBunissness();
                                    regLink.setType("Register");
                                    regLink.setUsername(usernameregisterField.getText());
                                    regLink.setPassword(passwordregisterField.getText());
                                    regLink.sendData();
                                    IDReturn idReturn=new IDReturn(RegisterScreen.this,regLink.receiveOfRegister());
                                    idReturn.setVisible(true);
                                }
                                catch (Exception e1)
                                {
                                    //
                                }
                            }
                        });
                        registerThread.start();
                }
                else
                {
                    new PasswordRepeatWrong(RegisterScreen.this).setVisible(true);
                }
            }
        });
        RegisterScreencontainer.add(Register);
        setSize(300,150);
    }
}

class PasswordRepeatWrong extends JDialog
{
    public PasswordRepeatWrong(RegisterScreen RS)
    {
        super(RS,"",true);
        Container container=getContentPane();
        container.add(new JLabel("两次密码不一致"));
        setBounds(120,120,100,100);
    }
}

class IDReturn extends JDialog //待优化
{
    public IDReturn(RegisterScreen RS,String ID)
    {
        super(RS,"",true);
        Container container=getContentPane();
        container.add(new JLabel("ID为" + ID + "请使用该ID登录"));
        setBounds(120,120,100,100);
    }
}


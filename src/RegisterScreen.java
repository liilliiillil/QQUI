import javax.swing.*;
import java.awt.*;

/**
 * Created by admin on 2017/12/19.
 */
public class RegisterScreen extends JFrame
{
    public RegisterScreen()
    {
        Container RegisterScreencontainer=getContentPane();
        setLayout(new GridLayout(4,2,10,10));
        RegisterScreencontainer.add(new JLabel("Username",SwingConstants.RIGHT));
        JTextField usernameregisterField=new JTextField();
        RegisterScreencontainer.add(usernameregisterField);
        RegisterScreencontainer.add(new JLabel("Password",SwingConstants.RIGHT));
        JTextField passwordregisterField=new JTextField();
        RegisterScreencontainer.add(passwordregisterField);
        RegisterScreencontainer.add(new JLabel("RepeatPassword",SwingConstants.RIGHT));
        JTextField repeatpasswordregisterField=new JTextField();
        RegisterScreencontainer.add(repeatpasswordregisterField);
        JButton Register=new JButton("Register");
        RegisterScreencontainer.add(Register);
        setSize(300,150);
    }
}

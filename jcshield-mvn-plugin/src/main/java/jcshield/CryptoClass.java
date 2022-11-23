package jcshield;


import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo( name = "encrypt")
public class CryptoClass extends AbstractMojo
{
	
	@Parameter
	private String[] classes;
	
	@Parameter
	private String classdir;
	
	@Parameter
	private String secretkey;
	
	 
    public void execute() throws MojoExecutionException
    { 
       if (classes!=null)
       {	   
	       for(String cls : classes)
	       {
	    	   if ((cls==null) || cls.trim().equals(""))
	    	   {
	    		   continue ;
	    	   }
	    	   
	    	   String path = classdir + File.separator + cls.replace(".",File.separator) + ".class";
	    	   byte[] key = hexStringToByteArray(secretkey);
	    	   AES.encryptFile(key,path);
	       }
       }
    }
    
    public static byte[] hexStringToByteArray(String s) 
    {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}

